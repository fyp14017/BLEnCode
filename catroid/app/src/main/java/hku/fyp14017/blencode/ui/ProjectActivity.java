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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import hku.fyp14017.blencode.ProjectManager;
import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.common.Constants;
import hku.fyp14017.blencode.drone.DroneInitializer;
import hku.fyp14017.blencode.facedetection.FaceDetectionHandler;

import hku.fyp14017.blencode.formulaeditor.SensorHandler;

import hku.fyp14017.blencode.stage.PreStageActivity;
import hku.fyp14017.blencode.stage.StageActivity;
import hku.fyp14017.blencode.ui.adapter.SpriteAdapter;
import hku.fyp14017.blencode.ui.dialogs.NewSpriteDialog;
import hku.fyp14017.blencode.ui.fragment.SpritesListFragment;
import hku.fyp14017.blencode.utils.Utils;

import java.util.concurrent.locks.Lock;

import hku.fyp14017.blencode.ProjectManager;
import hku.fyp14017.blencode.common.Constants;
import hku.fyp14017.blencode.drone.DroneInitializer;
import hku.fyp14017.blencode.facedetection.FaceDetectionHandler;
import hku.fyp14017.blencode.formulaeditor.SensorHandler;
import hku.fyp14017.blencode.stage.PreStageActivity;
import hku.fyp14017.blencode.ui.adapter.SpriteAdapter;
import hku.fyp14017.blencode.ui.dialogs.NewSpriteDialog;
import hku.fyp14017.blencode.ui.fragment.SpritesListFragment;
import hku.fyp14017.blencode.utils.Utils;

public class ProjectActivity extends BaseActivity {

	private SpritesListFragment spritesListFragment;
	private Lock viewSwitchLock = new ViewSwitchLock();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(hku.fyp14017.blencode.R.layout.activity_project);

		if (getIntent() != null && getIntent().hasExtra(Constants.PROJECT_OPENED_FROM_PROJECTS_LIST)) {
			setReturnToProjectsList(true);
		}

	}

	@Override
	protected void onStart() {
		super.onStart();

		String programName;
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			programName = bundle.getString(Constants.PROJECTNAME_TO_LOAD);
		} else {
			programName = ProjectManager.getInstance().getCurrentProject().getName();
		}

		final ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		setTitleActionBar(programName);

		spritesListFragment = (SpritesListFragment) getSupportFragmentManager().findFragmentById(
				hku.fyp14017.blencode.R.id.fragment_sprites_list);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (spritesListFragment != null && spritesListFragment.isLoading == false) {
			handleShowDetails(spritesListFragment.getShowDetails(), menu.findItem(hku.fyp14017.blencode.R.id.show_details));
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (spritesListFragment != null && spritesListFragment.isLoading == false) {
			getSupportMenuInflater().inflate(hku.fyp14017.blencode.R.menu.menu_current_project, menu);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case hku.fyp14017.blencode.R.id.show_details:
				handleShowDetails(!spritesListFragment.getShowDetails(), item);
				break;

			case hku.fyp14017.blencode.R.id.copy:
				spritesListFragment.startCopyActionMode();
				break;

			case hku.fyp14017.blencode.R.id.cut:
				break;

			case hku.fyp14017.blencode.R.id.insert_below:
				break;

			case hku.fyp14017.blencode.R.id.move:
				break;

			case hku.fyp14017.blencode.R.id.rename:
				spritesListFragment.startRenameActionMode();
				break;

			case hku.fyp14017.blencode.R.id.delete:
				spritesListFragment.startDeleteActionMode();
				break;

			case hku.fyp14017.blencode.R.id.upload:
				ProjectManager.getInstance().uploadProject(Utils.getCurrentProjectName(this), this);
				break;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == PreStageActivity.REQUEST_RESOURCES_INIT && resultCode == RESULT_OK) {
			Intent intent = new Intent(ProjectActivity.this, StageActivity.class);
			DroneInitializer.addDroneSupportExtraToNewIntentIfPresentInOldIntent(data, intent);
			startActivity(intent);
		}
		if (requestCode == StageActivity.STAGE_ACTIVITY_FINISH) {
			SensorHandler.stopSensorListeners();
			FaceDetectionHandler.stopFaceDetection();
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			sendBroadcast(new Intent(ScriptActivity.ACTION_SPRITES_LIST_INIT));
		}
	}

	public void handleCheckBoxClick(View view) {
		spritesListFragment.handleCheckBoxClick(view);
	}

	public void handleAddButton(View view) {
		if (!viewSwitchLock.tryLock()) {
			return;
		}
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		Fragment previousFragment = getSupportFragmentManager().findFragmentByTag(NewSpriteDialog.DIALOG_FRAGMENT_TAG);
		if (previousFragment != null) {
			fragmentTransaction.remove(previousFragment);
		}

		DialogFragment newFragment = new NewSpriteDialog();
		newFragment.show(fragmentTransaction, NewSpriteDialog.DIALOG_FRAGMENT_TAG);
	}

	public void handlePlayButton(View view) {
		if (!viewSwitchLock.tryLock()) {
			return;
		}
		ProjectManager.getInstance().getCurrentProject().getUserVariables().resetAllUserVariables();
		Intent intent = new Intent(this, PreStageActivity.class);
		startActivityForResult(intent, PreStageActivity.REQUEST_RESOURCES_INIT);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// Dismiss ActionMode without effecting sounds
		if (spritesListFragment.getActionModeActive() && event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_UP) {
			SpriteAdapter adapter = (SpriteAdapter) spritesListFragment.getListAdapter();
			adapter.clearCheckedSprites();
		}

		return super.dispatchKeyEvent(event);
	}

	public void handleShowDetails(boolean showDetails, MenuItem item) {
		spritesListFragment.setShowDetails(showDetails);

		item.setTitle(showDetails ? hku.fyp14017.blencode.R.string.hide_details : hku.fyp14017.blencode.R.string.show_details);
	}
}
