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

import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.ListAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.common.SoundInfo;
import hku.fyp14017.blencode.ui.adapter.ScriptActivityAdapterInterface;
import hku.fyp14017.blencode.ui.controller.BackPackListManager;
import hku.fyp14017.blencode.ui.controller.SoundController;
import hku.fyp14017.blencode.ui.fragment.BackPackActivityFragment;
import hku.fyp14017.blencode.ui.fragment.BackPackLookFragment;
import hku.fyp14017.blencode.ui.fragment.BackPackScriptFragment;
import hku.fyp14017.blencode.ui.fragment.BackPackSoundFragment;

import java.util.Iterator;

public class BackPackActivity extends BaseActivity {
	public static final int FRAGMENT_BACKPACK_SCRIPTS = 0;
	public static final int FRAGMENT_BACKPACK_LOOKS = 1;
	public static final int FRAGMENT_BACKPACK_SOUNDS = 2;

	public static final String EXTRA_FRAGMENT_POSITION = "hku.fyp14017.blencode.ui.fragmentPosition";
	public static final String BACKPACK_ITEM = "backpackItem";
	public static final String ACTION_SOUND_DELETED = "hku.fyp14017.blencode.SOUND_DELETED";
	public static final String ACTION_LOOK_DELETED = "hku.fyp14017.blencode.LOOK_DELETED";
	public static final String ACTION_SCRIPT_DELETED = "hku.fyp14017.blencode.SCRIPT_DELETED";
	private static int currentFragmentPosition;
	private boolean backpackItem = false;
	private FragmentManager fragmentManager = getSupportFragmentManager();
	private BackPackSoundFragment backPackSoundFragment = null;
	private BackPackLookFragment backPackLookFragment = null;
	private BackPackScriptFragment backPackScriptFragment = null;
	private BackPackActivityFragment currentFragment = null;
	private String currentFragmentTag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(hku.fyp14017.blencode.R.string.backpack_title);
		setContentView(hku.fyp14017.blencode.R.layout.activity_script);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		currentFragmentPosition = FRAGMENT_BACKPACK_SCRIPTS;

		if (savedInstanceState == null) {
			Bundle bundle = this.getIntent().getExtras();

			if (bundle != null) {
				currentFragmentPosition = bundle.getInt(EXTRA_FRAGMENT_POSITION, FRAGMENT_BACKPACK_SCRIPTS);
				backpackItem = bundle.getBoolean(BACKPACK_ITEM);
			}
		}

		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		setCurrentFragment(currentFragmentPosition);
		fragmentTransaction.commit();
		fragmentTransaction.add(hku.fyp14017.blencode.R.id.script_fragment_container, currentFragment, currentFragmentTag);

		final ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (backpackItem) {
			Iterator<SoundInfo> iterator = BackPackListManager.getActionBarSoundInfoArrayList().iterator();

			while (iterator.hasNext()) {
				SoundInfo soundInfo = iterator.next();
				BackPackListManager.setCurrentSoundInfo(soundInfo);
				SoundController.getInstance().backPackSound(BackPackListManager.getCurrentSoundInfo(),
						backPackSoundFragment, BackPackListManager.getInstance().getSoundInfoArrayList(),
						backPackSoundFragment.getAdapter());
			}
			BackPackListManager.getActionBarSoundInfoArrayList().clear();
			backpackItem = false;
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
			menu.findItem(hku.fyp14017.blencode.R.id.unpacking).setVisible(false);
			menu.findItem(hku.fyp14017.blencode.R.id.backpack).setVisible(false);
			menu.findItem(hku.fyp14017.blencode.R.id.cut).setVisible(false);
			menu.findItem(hku.fyp14017.blencode.R.id.rename).setVisible(false);

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

		switch (item.getItemId()) {
			case hku.fyp14017.blencode.R.id.show_details:
				handleShowDetails(!currentFragment.getShowDetails(), item);
				break;

			case hku.fyp14017.blencode.R.id.unpacking:
				currentFragment.startUnPackingActionMode();
				break;

			case hku.fyp14017.blencode.R.id.delete:
				currentFragment.startDeleteActionMode();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		//Dismiss ActionMode without effecting checked items

		if (currentFragment != null && currentFragment.getActionModeActive()
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
			ListAdapter adapter = null;
			if (currentFragment instanceof BackPackScriptFragment) {
				adapter = ((BackPackScriptFragment) currentFragment).getAdapter();
			} else {
				adapter = currentFragment.getListAdapter();
			}
			((ScriptActivityAdapterInterface) adapter).clearCheckedItems();
		}

		return super.dispatchKeyEvent(event);
	}

	public void handleShowDetails(boolean showDetails, MenuItem item) {
		currentFragment.setShowDetails(showDetails);

		item.setTitle(showDetails ? hku.fyp14017.blencode.R.string.hide_details : hku.fyp14017.blencode.R.string.show_details);
	}

	public BackPackActivityFragment getFragment(int fragmentPosition) {
		BackPackActivityFragment fragment = null;

		switch (fragmentPosition) {
			case FRAGMENT_BACKPACK_SCRIPTS:
				fragment = backPackScriptFragment;
				break;
			case FRAGMENT_BACKPACK_LOOKS:
				fragment = backPackLookFragment;
				break;
			case FRAGMENT_BACKPACK_SOUNDS:
				fragment = backPackSoundFragment;
				break;
		}
		return fragment;
	}

	public void setCurrentFragment(int fragmentPosition) {

		switch (fragmentPosition) {
			case FRAGMENT_BACKPACK_SCRIPTS:
				currentFragment = backPackScriptFragment;
				currentFragmentPosition = FRAGMENT_BACKPACK_SCRIPTS;
				currentFragmentTag = BackPackScriptFragment.TAG;
				break;
			case FRAGMENT_BACKPACK_LOOKS:
				currentFragment = backPackLookFragment;
				currentFragmentPosition = FRAGMENT_BACKPACK_LOOKS;
				currentFragmentTag = BackPackLookFragment.TAG;
				break;
			case FRAGMENT_BACKPACK_SOUNDS:
				if (backPackSoundFragment == null) {
					backPackSoundFragment = new BackPackSoundFragment();
				}
				currentFragment = backPackSoundFragment;
				currentFragmentPosition = FRAGMENT_BACKPACK_SOUNDS;
				currentFragmentTag = BackPackSoundFragment.TAG;

				break;
		}
	}
}
