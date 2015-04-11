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
import android.view.KeyEvent;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.ui.adapter.ProjectAdapter;
import hku.fyp14017.blencode.ui.dialogs.NewProjectDialog;
import hku.fyp14017.blencode.ui.fragment.ProjectsListFragment;

import java.util.concurrent.locks.Lock;

import hku.fyp14017.blencode.ui.adapter.ProjectAdapter;
import hku.fyp14017.blencode.ui.dialogs.NewProjectDialog;
import hku.fyp14017.blencode.ui.fragment.ProjectsListFragment;

public class MyProjectsActivity extends BaseActivity {

	public static final String ACTION_PROJECT_LIST_INIT = "hku.fyp14017.blencode.PROJECT_LIST_INIT";

	private Lock viewSwitchLock = new ViewSwitchLock();
	private ProjectsListFragment projectsListFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(hku.fyp14017.blencode.R.layout.activity_my_projects);
		setUpActionBar();

		BottomBar.hidePlayButton(this);

		projectsListFragment = (ProjectsListFragment) getSupportFragmentManager().findFragmentById(
				hku.fyp14017.blencode.R.id.fragment_projects_list);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			sendBroadcast(new Intent(ACTION_PROJECT_LIST_INIT));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(hku.fyp14017.blencode.R.menu.menu_myprojects, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		handleShowDetails(projectsListFragment.getShowDetails(), menu.findItem(hku.fyp14017.blencode.R.id.show_details));
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case hku.fyp14017.blencode.R.id.copy:
				projectsListFragment.startCopyActionMode();
				break;

			case hku.fyp14017.blencode.R.id.delete:
				projectsListFragment.startDeleteActionMode();
				break;

			case hku.fyp14017.blencode.R.id.rename:
				projectsListFragment.startRenameActionMode();
				break;

			case hku.fyp14017.blencode.R.id.show_details:
				handleShowDetails(!projectsListFragment.getShowDetails(), item);
				break;

		}
		return super.onOptionsItemSelected(item);
	}

	private void setUpActionBar() {
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(hku.fyp14017.blencode.R.string.my_projects_activity_title);
		actionBar.setHomeButtonEnabled(true);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (projectsListFragment.getActionModeActive() && event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_UP) {
			ProjectAdapter adapter = (ProjectAdapter) projectsListFragment.getListAdapter();
			adapter.clearCheckedProjects();
		}

		return super.dispatchKeyEvent(event);
	}

	public void handleAddButton(View view) {
		if (!viewSwitchLock.tryLock()) {
			return;
		}
		NewProjectDialog dialog = new NewProjectDialog();
		dialog.setOpenendFromProjectList(true);
		dialog.show(getSupportFragmentManager(), NewProjectDialog.DIALOG_FRAGMENT_TAG);
	}

	private void handleShowDetails(boolean showDetails, MenuItem item) {
		projectsListFragment.setShowDetails(showDetails);

		item.setTitle(showDetails ? hku.fyp14017.blencode.R.string.hide_details : hku.fyp14017.blencode.R.string.show_details);
	}
}
