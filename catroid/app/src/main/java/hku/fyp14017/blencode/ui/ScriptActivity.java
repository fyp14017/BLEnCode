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
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import hku.fyp14017.blencode.ProjectManager;
import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.content.BroadcastHandler;
import hku.fyp14017.blencode.drone.DroneInitializer;
import hku.fyp14017.blencode.stage.PreStageActivity;
import hku.fyp14017.blencode.stage.StageActivity;
import hku.fyp14017.blencode.ui.adapter.BrickAdapter;
import hku.fyp14017.blencode.ui.adapter.ScriptActivityAdapterInterface;
import hku.fyp14017.blencode.ui.dragndrop.DragAndDropListView;
import hku.fyp14017.blencode.ui.fragment.FormulaEditorFragment;
import hku.fyp14017.blencode.ui.fragment.FormulaEditorListFragment;
import hku.fyp14017.blencode.ui.fragment.FormulaEditorVariableListFragment;
import hku.fyp14017.blencode.ui.fragment.LookFragment;
import hku.fyp14017.blencode.ui.fragment.ScriptActivityFragment;
import hku.fyp14017.blencode.ui.fragment.ScriptFragment;
import hku.fyp14017.blencode.ui.fragment.SensorTagFragment;
import hku.fyp14017.blencode.ui.fragment.SoundFragment;
import hku.fyp14017.blencode.ui.fragment.UserBrickDataEditorFragment;

import java.util.concurrent.locks.Lock;

import hku.fyp14017.blencode.ProjectManager;

public class ScriptActivity extends BaseActivity {
	public static final int FRAGMENT_SCRIPTS = 0;
	public static final int FRAGMENT_LOOKS = 1;
	public static final int FRAGMENT_SOUNDS = 2;

	public static final String EXTRA_FRAGMENT_POSITION = "hku.fyp14017.blencode.ui.fragmentPosition";

	public static final String ACTION_SPRITE_RENAMED = "hku.fyp14017.blencode.SPRITE_RENAMED";
	public static final String ACTION_SPRITES_LIST_INIT = "hku.fyp14017.blencode.SPRITES_LIST_INIT";
	public static final String ACTION_SPRITES_LIST_CHANGED = "hku.fyp14017.blencode.SPRITES_LIST_CHANGED";
	public static final String ACTION_BRICK_LIST_CHANGED = "hku.fyp14017.blencode.BRICK_LIST_CHANGED";
	public static final String ACTION_LOOK_DELETED = "hku.fyp14017.blencode.LOOK_DELETED";
	public static final String ACTION_LOOK_RENAMED = "hku.fyp14017.blencode.LOOK_RENAMED";
	public static final String ACTION_LOOKS_LIST_INIT = "hku.fyp14017.blencode.LOOKS_LIST_INIT";
	public static final String ACTION_SOUND_DELETED = "hku.fyp14017.blencode.SOUND_DELETED";
	public static final String ACTION_SOUND_COPIED = "hku.fyp14017.blencode.SOUND_COPIED";
	public static final String ACTION_SOUND_RENAMED = "hku.fyp14017.blencode.SOUND_RENAMED";
	public static final String ACTION_SOUNDS_LIST_INIT = "hku.fyp14017.blencode.SOUNDS_LIST_INIT";
	public static final String ACTION_VARIABLE_DELETED = "hku.fyp14017.blencode.VARIABLE_DELETED";
	private static int currentFragmentPosition;
	private FragmentManager fragmentManager = getSupportFragmentManager();
	private ScriptFragment scriptFragment = null;
	private LookFragment lookFragment = null;
	private SoundFragment soundFragment = null;
	private ScriptActivityFragment currentFragment = null;
	private DeleteModeListener deleteModeListener;
	private String currentFragmentTag;

	private Lock viewSwitchLock = new ViewSwitchLock();

	private boolean isSoundFragmentFromPlaySoundBrickNew = false;
	private boolean isSoundFragmentHandleAddButtonHandled = false;
	private boolean isLookFragmentFromSetLookBrickNew = false;
	private boolean isLookFragmentHandleAddButtonHandled = false;

	private ImageButton buttonAdd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(hku.fyp14017.blencode.R.layout.activity_script);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		currentFragmentPosition = FRAGMENT_SCRIPTS;

		if (savedInstanceState == null) {
			Bundle bundle = this.getIntent().getExtras();

			if (bundle != null) {
				currentFragmentPosition = bundle.getInt(EXTRA_FRAGMENT_POSITION, FRAGMENT_SCRIPTS);
			}
		}

		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		updateCurrentFragment(currentFragmentPosition, fragmentTransaction);
		fragmentTransaction.commit();

		setupActionBar();
		setupBottomBar();

		buttonAdd = (ImageButton) findViewById(hku.fyp14017.blencode.R.id.button_add);
		updateHandleAddButtonClickListener();
	}

	private void setupBottomBar() {
		BottomBar.showBottomBar(this);
		BottomBar.showAddButton(this);
		BottomBar.showPlayButton(this);
		updateHandleAddButtonClickListener();

	}

	public void setupActionBar() {
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		String currentSprite = ProjectManager.getInstance().getCurrentSprite().getName();
		actionBar.setTitle(currentSprite);
	}

	@Override
	public void onResume() {
		super.onResume();
		setupActionBar();
		setupBottomBar();
	}

	public void updateHandleAddButtonClickListener() {
		if (buttonAdd == null) {
			buttonAdd = (ImageButton) findViewById(hku.fyp14017.blencode.R.id.button_add);
		}
		buttonAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				handleAddButton(view);
			}
		});
	}

	private void updateCurrentFragment(int fragmentPosition, FragmentTransaction fragmentTransaction) {
		boolean fragmentExists = true;
		currentFragmentPosition = fragmentPosition;

		switch (currentFragmentPosition) {
			case FRAGMENT_SCRIPTS:
				if (scriptFragment == null) {
					scriptFragment = new ScriptFragment();
					fragmentExists = false;
					currentFragmentTag = ScriptFragment.TAG;
				}
				currentFragment = scriptFragment;
				break;
			case FRAGMENT_LOOKS:
				if (lookFragment == null) {
					lookFragment = new LookFragment();
					fragmentExists = false;
					currentFragmentTag = LookFragment.TAG;
				}
				currentFragment = lookFragment;
				break;
			case FRAGMENT_SOUNDS:
				if (soundFragment == null) {
					soundFragment = new SoundFragment();
					fragmentExists = false;
					currentFragmentTag = SoundFragment.TAG;
				}
				currentFragment = soundFragment;
				break;
		}

		updateHandleAddButtonClickListener();

		if (fragmentExists) {
			fragmentTransaction.show(currentFragment);
		} else {
			fragmentTransaction.add(hku.fyp14017.blencode.R.id.script_fragment_container, currentFragment, currentFragmentTag);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		setVolumeControlStream(AudioManager.STREAM_RING);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (currentFragment != null) {
			handleShowDetails(currentFragment.getShowDetails(), menu.findItem(hku.fyp14017.blencode.R.id.show_details));
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(hku.fyp14017.blencode.R.menu.menu_script_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (isHoveringActive()) {
			scriptFragment.getListView().animateHoveringBrick();
			return super.onOptionsItemSelected(item);
		}

		FormulaEditorVariableListFragment formulaEditorVariableListFragment = (FormulaEditorVariableListFragment) getSupportFragmentManager()
				.findFragmentByTag(FormulaEditorVariableListFragment.VARIABLE_TAG);

		if (formulaEditorVariableListFragment != null && formulaEditorVariableListFragment.isVisible()) {
			return super.onOptionsItemSelected(item);
		}

		switch (item.getItemId()) {
			case hku.fyp14017.blencode.R.id.backpack:
				currentFragment.startBackPackActionMode();
				break;

			case hku.fyp14017.blencode.R.id.show_details:
				handleShowDetails(!currentFragment.getShowDetails(), item);
				break;

			case hku.fyp14017.blencode.R.id.copy:
				currentFragment.startCopyActionMode();
				break;

			case hku.fyp14017.blencode.R.id.cut:
				break;

			case hku.fyp14017.blencode.R.id.unpacking:
				Intent intent = new Intent(currentFragment.getActivity(), BackPackActivity.class);
				intent.putExtra(BackPackActivity.EXTRA_FRAGMENT_POSITION, FRAGMENT_SOUNDS);
				startActivity(intent);
				break;

			case hku.fyp14017.blencode.R.id.insert_below:
				break;

			case hku.fyp14017.blencode.R.id.move:
				break;

			case hku.fyp14017.blencode.R.id.rename:
				if (currentFragmentPosition != FRAGMENT_SCRIPTS) {
					currentFragment.startRenameActionMode();
				}
				break;

			case hku.fyp14017.blencode.R.id.delete:
				if (deleteModeListener != null) {
					deleteModeListener.startDeleteActionMode();
				} else {
					currentFragment.startDeleteActionMode();
				}
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		updateHandleAddButtonClickListener();

		if (requestCode == PreStageActivity.REQUEST_RESOURCES_INIT && resultCode == RESULT_OK) {
			Intent intent = new Intent(ScriptActivity.this, StageActivity.class);
			DroneInitializer.addDroneSupportExtraToNewIntentIfPresentInOldIntent(data, intent);
			startActivity(intent);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		FragmentManager fragmentManager = getSupportFragmentManager();

		for (String tag : FormulaEditorListFragment.TAGS) {
			FormulaEditorListFragment fragment = (FormulaEditorListFragment) fragmentManager.findFragmentByTag(tag);
			if (fragment != null && fragment.isVisible()) {
				return fragment.onKey(null, keyCode, event);
			}

		}

		String tag1 = UserBrickDataEditorFragment.BRICK_DATA_EDITOR_FRAGMENT_TAG;
		UserBrickDataEditorFragment fragment = (UserBrickDataEditorFragment) fragmentManager.findFragmentByTag(tag1);
		if (fragment != null && fragment.isVisible()) {
				return fragment.onKey(null, keyCode, event);
		}

		FormulaEditorVariableListFragment formulaEditorVariableListFragment = (FormulaEditorVariableListFragment) getSupportFragmentManager()
				.findFragmentByTag(FormulaEditorVariableListFragment.VARIABLE_TAG);

		if (formulaEditorVariableListFragment != null && formulaEditorVariableListFragment.isVisible()) {
			return formulaEditorVariableListFragment.onKey(null, keyCode, event);
		}

        SensorTagFragment sensorTagFragment = (SensorTagFragment) getSupportFragmentManager().findFragmentByTag(SensorTagFragment.SENSOR_TAG_FRAGMENT_TAG);

        if(sensorTagFragment != null && sensorTagFragment.isVisible()){
            return sensorTagFragment.onKey(null, keyCode, event);
        }

		FormulaEditorFragment formulaEditor = (FormulaEditorFragment) getSupportFragmentManager().findFragmentByTag(
				FormulaEditorFragment.FORMULA_EDITOR_FRAGMENT_TAG);

		if (formulaEditor != null && formulaEditor.isVisible()) {
			scriptFragment.getAdapter().updateProjectBrickList();
			return formulaEditor.onKey(null, keyCode, event);
		}

		if (soundFragment != null && soundFragment.isVisible() && soundFragment.onKey(null, keyCode, event)) {
			return true;
		}

		if (lookFragment != null && lookFragment.isVisible() && lookFragment.onKey(null, keyCode, event)) {
			return true;
		}

		int backStackEntryCount = fragmentManager.getBackStackEntryCount();
		for (int i = backStackEntryCount; i > 0; --i) {
			String backStackEntryName = fragmentManager.getBackStackEntryAt(i - 1).getName();
			if (backStackEntryName != null
					&& (backStackEntryName.equals(LookFragment.TAG) || backStackEntryName.equals(SoundFragment.TAG))) {
				fragmentManager.popBackStack();
			} else {
				break;
			}
		}

		if (keyCode == KeyEvent.KEYCODE_BACK && currentFragmentPosition == FRAGMENT_SCRIPTS) {
			DragAndDropListView listView = scriptFragment.getListView();
			if (listView.isCurrentlyDragging()) {
				listView.resetDraggingScreen();

				BrickAdapter adapter = scriptFragment.getAdapter();
				adapter.removeDraggedBrick();
				return true;
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			if (soundFragment != null && soundFragment.isVisible()) {
				sendBroadcast(new Intent(ScriptActivity.ACTION_SOUNDS_LIST_INIT));

			}

			if (lookFragment != null && lookFragment.isVisible()) {
				sendBroadcast(new Intent(ScriptActivity.ACTION_LOOKS_LIST_INIT));
			}

		}
	}

	public void handleAddButton(View view) {
		if (!viewSwitchLock.tryLock()) {
			return;
		}
		currentFragment.handleAddButton();
	}

	public void handlePlayButton(View view) {
		updateHandleAddButtonClickListener();

		Fragment formulaEditorFragment = fragmentManager
				.findFragmentByTag(FormulaEditorFragment.FORMULA_EDITOR_FRAGMENT_TAG);
		if (formulaEditorFragment != null) {
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			fragmentTransaction.remove(formulaEditorFragment);
			fragmentTransaction.commit();
		}
		BroadcastHandler.clearActionMaps();
		if (isHoveringActive()) {
			scriptFragment.getListView().animateHoveringBrick();
		} else {
			if (!viewSwitchLock.tryLock()) {
				return;
			}
			ProjectManager.getInstance().getCurrentProject().getUserVariables().resetAllUserVariables();
			Intent intent = new Intent(this, PreStageActivity.class);
			startActivityForResult(intent, PreStageActivity.REQUEST_RESOURCES_INIT);
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		//Dismiss ActionMode without effecting checked items

		FormulaEditorVariableListFragment formulaEditorVariableListFragment = (FormulaEditorVariableListFragment) getSupportFragmentManager()
				.findFragmentByTag(FormulaEditorVariableListFragment.VARIABLE_TAG);

		if (formulaEditorVariableListFragment != null && formulaEditorVariableListFragment.isVisible()) {
			ListAdapter adapter = formulaEditorVariableListFragment.getListAdapter();
			((ScriptActivityAdapterInterface) adapter).clearCheckedItems();
			return super.dispatchKeyEvent(event);
		}

		if (currentFragment != null && currentFragment.getActionModeActive()
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
			ListAdapter adapter = null;
			if (currentFragment instanceof ScriptFragment) {
				adapter = ((ScriptFragment) currentFragment).getAdapter();
			} else {
				adapter = currentFragment.getListAdapter();
			}
			((ScriptActivityAdapterInterface) adapter).clearCheckedItems();
		}

		return super.dispatchKeyEvent(event);
	}

	public boolean isHoveringActive() {
		if (currentFragmentPosition == FRAGMENT_SCRIPTS && scriptFragment.getListView().isCurrentlyDragging()) {
			return true;
		}
		return false;
	}

	public void handleShowDetails(boolean showDetails, MenuItem item) {
		currentFragment.setShowDetails(showDetails);

		item.setTitle(showDetails ? hku.fyp14017.blencode.R.string.hide_details : hku.fyp14017.blencode.R.string.show_details);
	}

	public void setDeleteModeListener(DeleteModeListener listener) {
		deleteModeListener = listener;
	}

	public ScriptActivityFragment getFragment(int fragmentPosition) {
		ScriptActivityFragment fragment = null;

		switch (fragmentPosition) {
			case FRAGMENT_SCRIPTS:
				fragment = scriptFragment;
				break;
			case FRAGMENT_LOOKS:
				fragment = lookFragment;
				break;
			case FRAGMENT_SOUNDS:
				fragment = soundFragment;
				break;
		}
		return fragment;
	}

	public void setCurrentFragment(int fragmentPosition) {

		switch (fragmentPosition) {
			case FRAGMENT_SCRIPTS:
				currentFragment = scriptFragment;
				currentFragmentPosition = FRAGMENT_SCRIPTS;
				currentFragmentTag = ScriptFragment.TAG;
				break;
			case FRAGMENT_LOOKS:
				currentFragment = lookFragment;
				currentFragmentPosition = FRAGMENT_LOOKS;
				currentFragmentTag = LookFragment.TAG;
				break;
			case FRAGMENT_SOUNDS:
				currentFragment = soundFragment;
				currentFragmentPosition = FRAGMENT_SOUNDS;
				currentFragmentTag = SoundFragment.TAG;
				break;
		}
	}

	public boolean getIsSoundFragmentFromPlaySoundBrickNew() {
		return this.isSoundFragmentFromPlaySoundBrickNew;
	}

	public void setIsSoundFragmentFromPlaySoundBrickNewFalse() {
		this.isSoundFragmentFromPlaySoundBrickNew = false;
		// TODO quickfix for issue #521 - refactor design (activity and fragment interaction)
		updateHandleAddButtonClickListener();
	}

	public boolean getIsSoundFragmentHandleAddButtonHandled() {
		return this.isSoundFragmentHandleAddButtonHandled;
	}

	public void setIsSoundFragmentHandleAddButtonHandled(boolean isSoundFragmentHandleAddButtonHandled) {
		this.isSoundFragmentHandleAddButtonHandled = isSoundFragmentHandleAddButtonHandled;
	}

	public boolean getIsLookFragmentFromSetLookBrickNew() {
		return this.isLookFragmentFromSetLookBrickNew;
	}

	public void setIsLookFragmentFromSetLookBrickNewFalse() {
		this.isLookFragmentFromSetLookBrickNew = false;
		// TODO quickfix for issue #521 - refactor design (activity and fragment interaction)
		updateHandleAddButtonClickListener();
	}

	public boolean getIsLookFragmentHandleAddButtonHandled() {
		return this.isLookFragmentHandleAddButtonHandled;
	}

	public void setIsLookFragmentHandleAddButtonHandled(boolean isLookFragmentHandleAddButtonHandled) {
		this.isLookFragmentHandleAddButtonHandled = isLookFragmentHandleAddButtonHandled;
	}

	public void setupBrickAdapter(BrickAdapter adapter) {
	}

	public ScriptFragment getScriptFragment() {
		return scriptFragment;
	}

	public void setScriptFragment(ScriptFragment scriptFragment) {
		this.scriptFragment = scriptFragment;
	}

	public void redrawBricks() {
		scriptFragment.getAdapter().notifyDataSetInvalidated();
	}

	public void switchToFragmentFromScriptFragment(int fragmentPosition) {

		ScriptActivityFragment scriptFragment = getFragment(ScriptActivity.FRAGMENT_SCRIPTS);
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (scriptFragment.isVisible()) {
			fragmentTransaction.hide(scriptFragment);
		}

		switch (fragmentPosition) {
			case FRAGMENT_LOOKS:
				isLookFragmentFromSetLookBrickNew = true;

				fragmentTransaction.addToBackStack(LookFragment.TAG);
				if (lookFragment == null) {
					lookFragment = new LookFragment();
					fragmentTransaction.add(hku.fyp14017.blencode.R.id.script_fragment_container, lookFragment, LookFragment.TAG);
				} else {
					fragmentTransaction.show(lookFragment);
				}
				setCurrentFragment(FRAGMENT_LOOKS);
				break;

			case FRAGMENT_SOUNDS:
				isSoundFragmentFromPlaySoundBrickNew = true;

				fragmentTransaction.addToBackStack(SoundFragment.TAG);
				if (soundFragment == null) {
					soundFragment = new SoundFragment();
					fragmentTransaction.add(hku.fyp14017.blencode.R.id.script_fragment_container, soundFragment, SoundFragment.TAG);
				} else {
					fragmentTransaction.show(soundFragment);
				}
				setCurrentFragment(FRAGMENT_SOUNDS);
				break;
		}

		updateHandleAddButtonClickListener();
		fragmentTransaction.commit();
	}
}
