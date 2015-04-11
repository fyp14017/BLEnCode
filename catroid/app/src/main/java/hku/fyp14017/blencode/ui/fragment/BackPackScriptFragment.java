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

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.view.Menu;

import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.ui.adapter.BrickAdapter;

public class BackPackScriptFragment extends BackPackActivityFragment implements Dialog.OnKeyListener {

	public static final String TAG = BackPackScriptFragment.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(hku.fyp14017.blencode.R.layout.fragment_sounds, container, false);
		return rootView;
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		menu.findItem(hku.fyp14017.blencode.R.id.copy).setVisible(true);
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean getShowDetails() {
		return false;
	}

	@Override
	public void setShowDetails(boolean showDetails) {
	}

	@Override
	public void setSelectMode(int selectMode) {
	}

	@Override
	public int getSelectMode() {
		return 0;
	}

	@Override
	public void startDeleteActionMode() {
	}

	@Override
	protected void showDeleteDialog() {
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		return false;
	}

	public BrickAdapter getAdapter() {
		return null;
	}

	@Override
	public void startUnPackingActionMode() {
	}
}
