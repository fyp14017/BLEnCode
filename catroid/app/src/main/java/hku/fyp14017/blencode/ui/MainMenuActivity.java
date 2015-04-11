/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2014 The Catrobat Team
 * (<http://developer.fyp14017.hku/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.fyp14017.hku/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.hku/licenses/>.
 */
package hku.fyp14017.blencode.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.actionbarsherlock.app.ActionBar;

import hku.fyp14017.blencode.ProjectManager;
import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.common.Constants;
import hku.fyp14017.blencode.content.Project;
import hku.fyp14017.blencode.io.LoadProjectTask;
import hku.fyp14017.blencode.io.LoadProjectTask.OnLoadProjectCompleteListener;
import hku.fyp14017.blencode.stage.PreStageActivity;
import hku.fyp14017.blencode.ui.controller.BackPackListManager;
import hku.fyp14017.blencode.ui.dialogs.NewProjectDialog;
import hku.fyp14017.blencode.utils.DownloadUtil;
import hku.fyp14017.blencode.utils.StatusBarNotificationManager;
import hku.fyp14017.blencode.utils.UtilFile;
import hku.fyp14017.blencode.utils.UtilZip;
import hku.fyp14017.blencode.utils.Utils;
import hku.fyp14017.blencode.web.ParseProjectUploadManager;

import java.util.concurrent.locks.Lock;

import hku.fyp14017.blencode.ProjectManager;
import hku.fyp14017.blencode.common.Constants;
import hku.fyp14017.blencode.io.LoadProjectTask;
import hku.fyp14017.blencode.stage.PreStageActivity;
import hku.fyp14017.blencode.ui.controller.BackPackListManager;
import hku.fyp14017.blencode.ui.dialogs.NewProjectDialog;
import hku.fyp14017.blencode.utils.DownloadUtil;
import hku.fyp14017.blencode.utils.StatusBarNotificationManager;
import hku.fyp14017.blencode.utils.UtilZip;

public class MainMenuActivity extends BaseActivity implements LoadProjectTask.OnLoadProjectCompleteListener {

	public static final String SHARED_PREFERENCES_SHOW_BROWSER_WARNING = "shared_preferences_browser_warning";

	private static final String TYPE_FILE = "file";
	private static final String TYPE_HTTP = "http";

	private Lock viewSwitchLock = new ViewSwitchLock();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!Utils.checkForExternalStorageAvailableAndDisplayErrorIfNot(this)) {
			return;
		}
		Utils.updateScreenWidthAndHeight(this);
		setContentView(hku.fyp14017.blencode.R.layout.activity_main_menu);

		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayUseLogoEnabled(true);
		actionBar.setTitle(hku.fyp14017.blencode.R.string.app_name);

		findViewById(hku.fyp14017.blencode.R.id.main_menu_button_continue).setEnabled(false);

		// Load external project from URL or local file system.
		Uri loadExternalProjectUri = getIntent().getData();
		getIntent().setData(null);

		if (loadExternalProjectUri != null) {
			loadProgramFromExternalSource(loadExternalProjectUri);
		}

		if (!BackPackListManager.isBackpackFlag()) {
			BackPackListManager.getInstance().setSoundInfoArrayListEmpty();
		}

		//TODO Drone do not create project for now
		//if (BuildConfig.FEATURE_PARROT_AR_DRONE_ENABLED && DroneUtils.isDroneSharedPreferenceEnabled(getApplication(), false)) {
		//	UtilFile.loadExistingOrCreateStandardDroneProject(this);
		//}
		//SettingsActivity.setTermsOfServiceAgreedPermanently(this, false);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (!Utils.checkForExternalStorageAvailableAndDisplayErrorIfNot(this)) {
			return;
		}

		findViewById(hku.fyp14017.blencode.R.id.progress_circle).setVisibility(View.GONE);

		UtilFile.createStandardProjectIfRootDirectoryIsEmpty(this);

		PreStageActivity.shutdownPersistentResources();
		setMainMenuButtonContinueText();
		findViewById(hku.fyp14017.blencode.R.id.main_menu_button_continue).setEnabled(true);
		String projectName = getIntent().getStringExtra(StatusBarNotificationManager.EXTRA_PROJECT_NAME);
		if (projectName != null) {
			loadProjectInBackground(projectName);
		}
		getIntent().removeExtra(StatusBarNotificationManager.EXTRA_PROJECT_NAME);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (!Utils.externalStorageAvailable()) {
			return;
		}

		Project currentProject = ProjectManager.getInstance().getCurrentProject();
		if (currentProject != null) {
			ProjectManager.getInstance().saveProject();
			Utils.saveToPreferences(this, Constants.PREF_PROJECTNAME_KEY, currentProject.getName());
		}
	}

	// needed because of android:onClick in activity_main_menu.xml
	public void handleContinueButton(View view) {
		handleContinueButton();
	}

	public void handleContinueButton() {
		Intent intent = new Intent(this, ProjectActivity.class);
		intent.putExtra(Constants.PROJECTNAME_TO_LOAD, Utils.getCurrentProjectName(this));
		startActivity(intent);
	}

	private void loadProjectInBackground(String projectName) {
		if (!viewSwitchLock.tryLock()) {
			return;
		}
		LoadProjectTask loadProjectTask = new LoadProjectTask(this, projectName, false, true);
		loadProjectTask.setOnLoadProjectCompleteListener(this);
		loadProjectTask.execute();
	}

	@Override
	public void onLoadProjectSuccess(boolean startProjectActivity) {
		if (ProjectManager.getInstance().getCurrentProject() != null && startProjectActivity) {
			Intent intent = new Intent(MainMenuActivity.this, ProjectActivity.class);
			startActivity(intent);
		}
	}

	public void handleNewButton(View view) {
		if (!viewSwitchLock.tryLock()) {
			return;
		}
		NewProjectDialog dialog = new NewProjectDialog();
		dialog.show(getSupportFragmentManager(), NewProjectDialog.DIALOG_FRAGMENT_TAG);
	}

	public void handleProgramsButton(View view) {
		findViewById(hku.fyp14017.blencode.R.id.progress_circle).setVisibility(View.VISIBLE);
		if (!viewSwitchLock.tryLock()) {
			return;
		}
		Intent intent = new Intent(MainMenuActivity.this, MyProjectsActivity.class);
		startActivity(intent);
	}

	public void handleHelpButton(View view) {
		if (!viewSwitchLock.tryLock()) {
			return;
		}

		startWebViewActivity(Constants.CATROBAT_HELP_URL);
	}

	public void handleWebButton(View view) {
		if (!viewSwitchLock.tryLock()) {
			return;
		}

		startWebViewActivity(Constants.BASE_URL_HTTPS);

	}

	public void startWebViewActivity(String url) {
		// TODO just a quick fix for not properly working webview on old devices
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
			final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			boolean showBrowserWarning = preferences.getBoolean(SHARED_PREFERENCES_SHOW_BROWSER_WARNING, true);
			if (showBrowserWarning) {
				showWebWarningDialog();
			} else {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.BASE_URL_HTTPS));
				startActivity(browserIntent);
			}
		} else {
			/*Intent intent = new Intent(MainMenuActivity.this, WebViewActivity.class);
			intent.putExtra(WebViewActivity.INTENT_PARAMETER_URL, url);
			startActivity(intent);*/
            Intent intent = new Intent(MainMenuActivity.this, ExploreProjectsActivity.class);
            startActivity(intent);
		}

	}

	private void showWebWarningDialog() {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		final View checkboxView = View.inflate(this, hku.fyp14017.blencode.R.layout.dialog_web_warning, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getText(hku.fyp14017.blencode.R.string.main_menu_web_dialog_title));
		builder.setView(checkboxView);

		builder.setPositiveButton(hku.fyp14017.blencode.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				CheckBox dontShowAgainCheckBox = (CheckBox) checkboxView
						.findViewById(hku.fyp14017.blencode.R.id.main_menu_web_dialog_dont_show_checkbox);
				if (dontShowAgainCheckBox != null && dontShowAgainCheckBox.isChecked()) {
					preferences.edit().putBoolean(SHARED_PREFERENCES_SHOW_BROWSER_WARNING, false).commit();
				}

				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.BASE_URL_HTTPS));
				startActivity(browserIntent);
			}
		});
		builder.setNegativeButton(hku.fyp14017.blencode.R.string.cancel_button, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		AlertDialog alertDialog = builder.create();
		alertDialog.setCanceledOnTouchOutside(true);
		alertDialog.show();
	}

	public void handleUploadButton(View view) {
		if (!viewSwitchLock.tryLock()) {
			return;
		}
        ParseProjectUploadManager p = new ParseProjectUploadManager(this);
        p.uploadProject();
		//ProjectManager.getInstance().uploadProject(Utils.getCurrentProjectName(this), this);
	}

	private void loadProgramFromExternalSource(Uri loadExternalProjectUri) {
		String scheme = loadExternalProjectUri.getScheme();
		if (scheme.startsWith((TYPE_HTTP))) {
			String url = loadExternalProjectUri.toString();
			DownloadUtil.getInstance().prepareDownloadAndStartIfPossible(this, url);
		} else if (scheme.equals(TYPE_FILE)) {

			String path = loadExternalProjectUri.getPath();
			int a = path.lastIndexOf('/') + 1;
			int b = path.lastIndexOf('.');
			String projectName = path.substring(a, b);
			if (!UtilZip.unZipFile(path, Utils.buildProjectPath(projectName))) {
				Utils.showErrorDialog(this, hku.fyp14017.blencode.R.string.error_load_project);
			}
		}
	}

	private void setMainMenuButtonContinueText() {
		Button mainMenuButtonContinue = (Button) this.findViewById(hku.fyp14017.blencode.R.id.main_menu_button_continue);
		TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(this, hku.fyp14017.blencode.R.style.MainMenuButtonTextSecondLine);
		SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
		String mainMenuContinue = this.getString(hku.fyp14017.blencode.R.string.main_menu_continue);

		spannableStringBuilder.append(mainMenuContinue);
		spannableStringBuilder.append("\n");
		spannableStringBuilder.append(Utils.getCurrentProjectName(this));

		spannableStringBuilder.setSpan(textAppearanceSpan, mainMenuContinue.length() + 1,
				spannableStringBuilder.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

		mainMenuButtonContinue.setText(spannableStringBuilder);
	}

	@Override
	public void onLoadProjectFailure() {

	}
}
