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
package hku.fyp14017.blencode.ui.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;

import hku.fyp14017.blencode.ProjectManager;
import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.common.Constants;
import hku.fyp14017.blencode.common.LookData;
import hku.fyp14017.blencode.common.SoundInfo;
import hku.fyp14017.blencode.content.Sprite;
import hku.fyp14017.blencode.formulaeditor.UserVariablesContainer;
import hku.fyp14017.blencode.io.LoadProjectTask;
import hku.fyp14017.blencode.io.LoadProjectTask.OnLoadProjectCompleteListener;
import hku.fyp14017.blencode.io.StorageHandler;
import hku.fyp14017.blencode.ui.BottomBar;
import hku.fyp14017.blencode.ui.ProgramMenuActivity;
import hku.fyp14017.blencode.ui.ScriptActivity;
import hku.fyp14017.blencode.ui.adapter.SpriteAdapter;
import hku.fyp14017.blencode.ui.adapter.SpriteAdapter.OnSpriteEditListener;
import hku.fyp14017.blencode.ui.dialogs.CustomAlertDialogBuilder;
import hku.fyp14017.blencode.ui.dialogs.RenameSpriteDialog;
import hku.fyp14017.blencode.utils.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import hku.fyp14017.blencode.ProjectManager;
import hku.fyp14017.blencode.common.Constants;
import hku.fyp14017.blencode.common.LookData;
import hku.fyp14017.blencode.common.SoundInfo;
import hku.fyp14017.blencode.formulaeditor.UserVariablesContainer;
import hku.fyp14017.blencode.io.LoadProjectTask;
import hku.fyp14017.blencode.ui.BottomBar;
import hku.fyp14017.blencode.ui.ProgramMenuActivity;
import hku.fyp14017.blencode.ui.ScriptActivity;
import hku.fyp14017.blencode.ui.dialogs.CustomAlertDialogBuilder;

public class SpritesListFragment extends SherlockListFragment implements OnSpriteEditListener,
        LoadProjectTask.OnLoadProjectCompleteListener {

	private static final String BUNDLE_ARGUMENTS_SPRITE_TO_EDIT = "sprite_to_edit";
	private static final String SHARED_PREFERENCE_NAME = "showDetailsProjects";

	private static String multiSelectActionModeTitle;
	private static String singleItemAppendixMultiSelectActionMode;
	private static String multipleItemAppendixMultiSelectActionMode;

	private SpriteAdapter spriteAdapter;
	private ArrayList<Sprite> spriteList;
	private Sprite spriteToEdit;

	private SpriteRenamedReceiver spriteRenamedReceiver;
	private SpritesListChangedReceiver spritesListChangedReceiver;
	private SpritesListInitReceiver spritesListInitReceiver;

	private ActionMode actionMode;
	private View selectAllActionModeButton;

	private boolean actionModeActive = false;
	private boolean isRenameActionMode;
	private String programName;

	private LoadProjectTask loadProjectTask;
	public boolean isLoading = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

		programName = getActivity().getIntent().getStringExtra(Constants.PROJECTNAME_TO_LOAD);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(hku.fyp14017.blencode.R.layout.fragment_sprites_list, container);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		registerForContextMenu(getListView());
		if (savedInstanceState != null) {
			spriteToEdit = (Sprite) savedInstanceState.get(BUNDLE_ARGUMENTS_SPRITE_TO_EDIT);
		}

		try {
			Utils.loadProjectIfNeeded(getActivity());
		} catch (ClassCastException exception) {
			Log.e("CATROID", getActivity().toString() + " does not implement ErrorListenerInterface", exception);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(BUNDLE_ARGUMENTS_SPRITE_TO_EDIT, spriteToEdit);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStart() {
		super.onStart();
		initListeners();

		ProjectManager projectManager = ProjectManager.getInstance();
		if (programName != null
				&& ((projectManager.getCurrentProject() != null && !projectManager.getCurrentProject().getName()
						.equals(programName)) || projectManager.getCurrentProject() == null)) {

			getActivity().findViewById(hku.fyp14017.blencode.R.id.progress_circle).setVisibility(View.VISIBLE);
			getActivity().findViewById(hku.fyp14017.blencode.R.id.progress_circle).bringToFront();
			getActivity().findViewById(hku.fyp14017.blencode.R.id.fragment_sprites_list).setVisibility(View.GONE);
			getActivity().findViewById(hku.fyp14017.blencode.R.id.bottom_bar).setVisibility(View.GONE);

			isLoading = true;
			this.getSherlockActivity().supportInvalidateOptionsMenu();

			loadProjectTask = new LoadProjectTask(getActivity(), programName, true, true);
			loadProjectTask.setOnLoadProjectCompleteListener(this);
			loadProjectTask.execute();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (actionMode != null) {
			actionMode.finish();
			actionMode = null;
		}
		if (!Utils.checkForExternalStorageAvailableAndDisplayErrorIfNot(getActivity())) {
			return;
		}

		StorageHandler.getInstance().fillChecksumContainer();

		if (spriteRenamedReceiver == null) {
			spriteRenamedReceiver = new SpriteRenamedReceiver();
		}

		if (spritesListChangedReceiver == null) {
			spritesListChangedReceiver = new SpritesListChangedReceiver();
		}

		if (spritesListInitReceiver == null) {
			spritesListInitReceiver = new SpritesListInitReceiver();
		}

		IntentFilter intentFilterSpriteRenamed = new IntentFilter(ScriptActivity.ACTION_SPRITE_RENAMED);
		getActivity().registerReceiver(spriteRenamedReceiver, intentFilterSpriteRenamed);

		IntentFilter intentFilterSpriteListChanged = new IntentFilter(ScriptActivity.ACTION_SPRITES_LIST_CHANGED);
		getActivity().registerReceiver(spritesListChangedReceiver, intentFilterSpriteListChanged);

		IntentFilter intentFilterSpriteListInit = new IntentFilter(ScriptActivity.ACTION_SPRITES_LIST_INIT);
		getActivity().registerReceiver(spritesListInitReceiver, intentFilterSpriteListInit);

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity()
				.getApplicationContext());

		setShowDetails(settings.getBoolean(SHARED_PREFERENCE_NAME, false));
	}

	@Override
	public void onPause() {
		super.onPause();

		getActivity().getIntent().removeExtra(Constants.PROJECTNAME_TO_LOAD);
		if (loadProjectTask != null) {
			loadProjectTask.cancel(true);
			ProjectManager.getInstance().cancelLoadProject();
		}

		ProjectManager projectManager = ProjectManager.getInstance();
		if (projectManager.getCurrentProject() != null) {
			projectManager.saveProject();
		}

		if (spriteRenamedReceiver != null) {
			getActivity().unregisterReceiver(spriteRenamedReceiver);
		}

		if (spritesListChangedReceiver != null) {
			getActivity().unregisterReceiver(spritesListChangedReceiver);
		}

		if (spritesListInitReceiver != null) {
			getActivity().unregisterReceiver(spritesListInitReceiver);
		}

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity()
				.getApplicationContext());
		SharedPreferences.Editor editor = settings.edit();

		editor.putBoolean(SHARED_PREFERENCE_NAME, getShowDetails());
		editor.commit();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, view, menuInfo);

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;

		spriteToEdit = spriteAdapter.getItem(info.position);
		spriteAdapter.addCheckedSprite(info.position);

		if (ProjectManager.getInstance().getCurrentProject().getSpriteList().indexOf(spriteToEdit) == 0) {
			return;
		}

		menu.setHeaderTitle(spriteToEdit.getName());

		getSherlockActivity().getMenuInflater().inflate(hku.fyp14017.blencode.R.menu.context_menu_default, menu);
		menu.findItem(hku.fyp14017.blencode.R.id.context_menu_copy).setVisible(true);
		if (!false) {
			menu.findItem(hku.fyp14017.blencode.R.id.context_menu_backpack).setVisible(false);
			menu.findItem(hku.fyp14017.blencode.R.id.context_menu_unpacking).setVisible(false);
		}

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case hku.fyp14017.blencode.R.id.context_menu_copy:
				copySprite();
				break;

			case hku.fyp14017.blencode.R.id.context_menu_cut:
				break;

			case hku.fyp14017.blencode.R.id.context_menu_insert_below:
				break;

			case hku.fyp14017.blencode.R.id.context_menu_move:
				break;

			case hku.fyp14017.blencode.R.id.context_menu_rename:
				showRenameDialog();
				break;

			case hku.fyp14017.blencode.R.id.context_menu_delete:
				showConfirmDeleteDialog();
				break;

		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onSpriteChecked() {
		if (isRenameActionMode || actionMode == null) {
			return;
		}

		updateActionModeTitle();
		Utils.setSelectAllActionModeButtonVisibility(selectAllActionModeButton, spriteAdapter.getCount() > 1
				&& spriteAdapter.getAmountOfCheckedSprites() != spriteAdapter.getCount() - 1);
	}

	private void updateActionModeTitle() {
		int numberOfSelectedItems = spriteAdapter.getAmountOfCheckedSprites();

		if (numberOfSelectedItems == 0) {
			actionMode.setTitle(multiSelectActionModeTitle);
		} else {
			String appendix = multipleItemAppendixMultiSelectActionMode;

			if (numberOfSelectedItems == 1) {
				appendix = singleItemAppendixMultiSelectActionMode;
			}

			String numberOfItems = Integer.toString(numberOfSelectedItems);
			String completeTitle = multiSelectActionModeTitle + " " + numberOfItems + " " + appendix;

			int titleLength = multiSelectActionModeTitle.length();

			Spannable completeSpannedTitle = new SpannableString(completeTitle);
			completeSpannedTitle.setSpan(
					new ForegroundColorSpan(getResources().getColor(hku.fyp14017.blencode.R.color.actionbar_title_color)), titleLength + 1,
					titleLength + (1 + numberOfItems.length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			actionMode.setTitle(completeSpannedTitle);
		}
	}

	@Override
	public void onSpriteEdit(int position) {
		ProjectManager.getInstance().setCurrentSprite(spriteAdapter.getItem(position));
		Intent intent = new Intent(getActivity(), ProgramMenuActivity.class);
		startActivity(intent);
	}

	public void startRenameActionMode() {
		if (actionMode == null) {
			actionMode = getSherlockActivity().startActionMode(renameModeCallBack);
			BottomBar.hideBottomBar(getActivity());
			isRenameActionMode = true;
		}
	}

	public void startDeleteActionMode() {
		if (actionMode == null) {
			actionMode = getSherlockActivity().startActionMode(deleteModeCallBack);
			BottomBar.hideBottomBar(getActivity());
			isRenameActionMode = false;
		}
	}

	public void startCopyActionMode() {
		if (actionMode == null) {
			actionMode = getSherlockActivity().startActionMode(copyModeCallBack);
			BottomBar.hideBottomBar(getActivity());
			isRenameActionMode = false;
		}
	}

	public void handleCheckBoxClick(View view) {
		int position = getListView().getPositionForView(view);
		getListView().setItemChecked(position, ((CheckBox) view.findViewById(hku.fyp14017.blencode.R.id.sprite_checkbox)).isChecked());
	}

	public void copySprite() {
		Sprite copiedSprite = spriteToEdit.clone();
		copiedSprite.setName(getSpriteName(spriteToEdit.getName().concat(getString(hku.fyp14017.blencode.R.string.copy_sprite_name_suffix)),
				0));

		ProjectManager projectManager = ProjectManager.getInstance();

		projectManager.addSprite(copiedSprite);
		projectManager.setCurrentSprite(copiedSprite);

		getActivity().sendBroadcast(new Intent(ScriptActivity.ACTION_SPRITES_LIST_CHANGED));

		Toast.makeText(
				getActivity(),
				this.getString(hku.fyp14017.blencode.R.string.copy_sprite_prefix).concat(" ").concat(spriteToEdit.getName()).concat(" ")
						.concat(this.getString(hku.fyp14017.blencode.R.string.copy_sprite_finished)), Toast.LENGTH_LONG).show();

		Log.d("Sprite copied", copiedSprite.toString());
	}

	private static String getSpriteName(String name, int nextNumber) {
		String newName;
		if (nextNumber == 0) {
			newName = name;
		} else {
			newName = name + nextNumber;
		}
		for (Sprite sprite : ProjectManager.getInstance().getCurrentProject().getSpriteList()) {
			if (sprite.getName().equals(newName)) {
				return getSpriteName(name, ++nextNumber);
			}
		}
		return newName;
	}

	public void showRenameDialog() {
		RenameSpriteDialog dialog = RenameSpriteDialog.newInstance(spriteToEdit.getName());
		dialog.show(getFragmentManager(), RenameSpriteDialog.DIALOG_FRAGMENT_TAG);
	}

	private void showConfirmDeleteDialog() {
		int titleId;
		if (spriteAdapter.getAmountOfCheckedSprites() == 1) {
			titleId = hku.fyp14017.blencode.R.string.dialog_confirm_delete_object_title;
		} else {
			titleId = hku.fyp14017.blencode.R.string.dialog_confirm_delete_multiple_objects_title;
		}

		AlertDialog.Builder builder = new CustomAlertDialogBuilder(getActivity());
		builder.setTitle(titleId);
		builder.setMessage(hku.fyp14017.blencode.R.string.dialog_confirm_delete_object_message);
		builder.setPositiveButton(hku.fyp14017.blencode.R.string.yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				deleteCheckedSprites();
				clearCheckedSpritesAndEnableButtons();
			}
		});
		builder.setNegativeButton(hku.fyp14017.blencode.R.string.no, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				clearCheckedSpritesAndEnableButtons();
			}
		});

		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

	public void deleteSprite() {
		ProjectManager projectManager = ProjectManager.getInstance();
		UserVariablesContainer userVariablesContainer = projectManager.getCurrentProject().getUserVariables();

		deleteSpriteFiles();
		userVariablesContainer.cleanVariableListForSprite(spriteToEdit);

		if (projectManager.getCurrentSprite() != null && projectManager.getCurrentSprite().equals(spriteToEdit)) {
			projectManager.setCurrentSprite(null);
		}
		projectManager.getCurrentProject().getSpriteList().remove(spriteToEdit);
	}

	private void deleteCheckedSprites() {
		int numDeleted = 0;
		for (int position : spriteAdapter.getCheckedSprites()) {
			spriteToEdit = (Sprite) getListView().getItemAtPosition(position - numDeleted);
			deleteSprite();
			numDeleted++;
		}
	}

	private void clearCheckedSpritesAndEnableButtons() {
		setSelectMode(ListView.CHOICE_MODE_NONE);
		spriteAdapter.clearCheckedSprites();

		actionMode = null;
		actionModeActive = false;

		BottomBar.showBottomBar(getActivity());
	}

	public void setSelectMode(int selectMode) {
		spriteAdapter.setSelectMode(selectMode);
		spriteAdapter.notifyDataSetChanged();
	}

	public int getSelectMode() {
		return spriteAdapter.getSelectMode();
	}

	public void setShowDetails(boolean showDetails) {
		spriteAdapter.setShowDetails(showDetails);
		spriteAdapter.notifyDataSetChanged();
	}

	public boolean getShowDetails() {
		return spriteAdapter.getShowDetails();
	}

	public boolean getActionModeActive() {
		return actionModeActive;
	}

	private void addSelectAllActionModeButton(ActionMode mode, Menu menu) {
		selectAllActionModeButton = Utils.addSelectAllActionModeButton(getLayoutInflater(null), mode, menu);
		selectAllActionModeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				for (int position = 1; position < spriteList.size(); position++) {
					spriteAdapter.addCheckedSprite(position);
				}
				spriteAdapter.notifyDataSetChanged();
				onSpriteChecked();
			}

		});
	}

	private class SpriteRenamedReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ScriptActivity.ACTION_SPRITE_RENAMED)) {
				String newSpriteName = intent.getExtras().getString(RenameSpriteDialog.EXTRA_NEW_SPRITE_NAME);
				spriteToEdit.setName(newSpriteName);
			}
		}
	}

	private class SpritesListChangedReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ScriptActivity.ACTION_SPRITES_LIST_CHANGED)) {
				spriteAdapter.notifyDataSetChanged();
				final ListView listView = getListView();
				listView.post(new Runnable() {
					@Override
					public void run() {
						listView.setSelection(listView.getCount() - 1);
					}
				});
			}
		}
	}

	private class SpritesListInitReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ScriptActivity.ACTION_SPRITES_LIST_INIT)) {
				spriteAdapter.notifyDataSetChanged();
			}
		}
	}

	private ActionMode.Callback deleteModeCallBack = new ActionMode.Callback() {
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			setSelectMode(ListView.CHOICE_MODE_MULTIPLE);

			actionModeActive = true;

			multiSelectActionModeTitle = getString(hku.fyp14017.blencode.R.string.delete);
			singleItemAppendixMultiSelectActionMode = getString(hku.fyp14017.blencode.R.string.sprite);
			multipleItemAppendixMultiSelectActionMode = getString(hku.fyp14017.blencode.R.string.sprites);

			mode.setTitle(multiSelectActionModeTitle);
			addSelectAllActionModeButton(mode, menu);

			return true;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, com.actionbarsherlock.view.MenuItem item) {
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			if (spriteAdapter.getAmountOfCheckedSprites() == 0) {
				clearCheckedSpritesAndEnableButtons();
			} else {
				showConfirmDeleteDialog();
			}
		}
	};

	private ActionMode.Callback renameModeCallBack = new ActionMode.Callback() {

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			setSelectMode(ListView.CHOICE_MODE_SINGLE);
			mode.setTitle(hku.fyp14017.blencode.R.string.rename);

			actionModeActive = true;

			return true;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, com.actionbarsherlock.view.MenuItem item) {
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			Set<Integer> checkedSprites = spriteAdapter.getCheckedSprites();
			Iterator<Integer> iterator = checkedSprites.iterator();
			if (iterator.hasNext()) {
				int position = iterator.next();
				spriteToEdit = (Sprite) getListView().getItemAtPosition(position);
				showRenameDialog();
			}
			clearCheckedSpritesAndEnableButtons();
		}
	};

	private ActionMode.Callback copyModeCallBack = new ActionMode.Callback() {
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			setSelectMode(ListView.CHOICE_MODE_MULTIPLE);

			actionModeActive = true;

			multiSelectActionModeTitle = getString(hku.fyp14017.blencode.R.string.copy);
			singleItemAppendixMultiSelectActionMode = getString(hku.fyp14017.blencode.R.string.sprite);
			multipleItemAppendixMultiSelectActionMode = getString(hku.fyp14017.blencode.R.string.sprites);

			mode.setTitle(multiSelectActionModeTitle);
			addSelectAllActionModeButton(mode, menu);

			return true;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, com.actionbarsherlock.view.MenuItem item) {
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			for (int position : spriteAdapter.getCheckedSprites()) {
				spriteToEdit = (Sprite) getListView().getItemAtPosition(position);
				copySprite();
			}
			clearCheckedSpritesAndEnableButtons();
		}
	};

	private void initListeners() {
		spriteList = (ArrayList<Sprite>) ProjectManager.getInstance().getCurrentProject().getSpriteList();
		spriteAdapter = new SpriteAdapter(getActivity(), hku.fyp14017.blencode.R.layout.activity_project_spritelist_item,
				hku.fyp14017.blencode.R.id.project_activity_sprite_title, spriteList);

		spriteAdapter.setOnSpriteEditListener(this);
		setListAdapter(spriteAdapter);
		getListView().setTextFilterEnabled(true);
		getListView().setDivider(null);
		getListView().setDividerHeight(0);
	}

	private void deleteSpriteFiles() {
		for (LookData currentLookData : spriteToEdit.getLookDataList()) {
			StorageHandler.getInstance().deleteFile(currentLookData.getAbsolutePath());
		}

		for (SoundInfo currentSoundInfo : spriteToEdit.getSoundList()) {
			StorageHandler.getInstance().deleteFile(currentSoundInfo.getAbsolutePath());
		}
	}

	@Override
	public void onLoadProjectSuccess(boolean startProjectActivity) {
		initListeners();
		spriteAdapter.notifyDataSetChanged();
		isLoading = false;
		this.getSherlockActivity().supportInvalidateOptionsMenu();
		getActivity().findViewById(hku.fyp14017.blencode.R.id.progress_circle).setVisibility(View.GONE);
		getActivity().findViewById(hku.fyp14017.blencode.R.id.fragment_sprites_list).setVisibility(View.VISIBLE);
		getActivity().findViewById(hku.fyp14017.blencode.R.id.bottom_bar).setVisibility(View.VISIBLE);
	}

	@Override
	public void onLoadProjectFailure() {
		getActivity().onBackPressed();
	}

}
