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

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.ui.controller.BackPackListManager;
import hku.fyp14017.blencode.ui.dialogs.AboutDialogFragment;
import hku.fyp14017.blencode.ui.dialogs.TermsOfUseDialogFragment;
import hku.fyp14017.blencode.ui.controller.BackPackListManager;
import hku.fyp14017.blencode.ui.dialogs.AboutDialogFragment;
import hku.fyp14017.blencode.ui.dialogs.TermsOfUseDialogFragment;

public class BaseActivity extends SherlockFragmentActivity {

	private boolean returnToProjectsList;
	private String titleActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		titleActionBar = null;
		returnToProjectsList = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Partly from http://stackoverflow.com/a/5069354
		unbindDrawables(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0));
		System.gc();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (getTitleActionBar() != null) {
			getSupportActionBar().setTitle(getTitleActionBar());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
			getSupportMenuInflater().inflate(hku.fyp14017.blencode.R.menu.menu_main_menu, menu);
			return super.onCreateOptionsMenu(menu);
		}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if (returnToProjectsList) {
					Intent intent = new Intent(this, MyProjectsActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					BackPackListManager.setBackPackFlag(true);
					startActivity(intent);
				} else {
					Intent intent = new Intent(this, MainMenuActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					BackPackListManager.setBackPackFlag(true);
					startActivity(intent);
				}
				break;
			case hku.fyp14017.blencode.R.id.settings:
				Intent settingsIntent = new Intent(this, SettingsActivity.class);
				startActivity(settingsIntent);
				break;
			case hku.fyp14017.blencode.R.id.menu_rate_app:
				launchMarket();
				return true;
			case hku.fyp14017.blencode.R.id.menu_terms_of_use:
				TermsOfUseDialogFragment termsOfUseDialog = new TermsOfUseDialogFragment();
				termsOfUseDialog.show(getSupportFragmentManager(), TermsOfUseDialogFragment.DIALOG_FRAGMENT_TAG);
				return true;
			case hku.fyp14017.blencode.R.id.menu_about:
				AboutDialogFragment aboutDialog = new AboutDialogFragment();
				aboutDialog.show(getSupportFragmentManager(), AboutDialogFragment.DIALOG_FRAGMENT_TAG);
				return true;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	// Taken from http://stackoverflow.com/a/11270668
	private void launchMarket() {
		Uri uri = Uri.parse("market://details?id=" + getPackageName());
		Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
		try {
			startActivity(myAppLinkToMarket);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, hku.fyp14017.blencode.R.string.main_menu_play_store_not_installed, Toast.LENGTH_SHORT).show();
		}
	}

	// Code from Stackoverflow to reduce memory problems
	// onDestroy() and unbindDrawables() methods taken from
	// http://stackoverflow.com/a/6779067
	protected void unbindDrawables(View view) {
		if (view.getBackground() != null) {
			view.getBackground().setCallback(null);
		}
		if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}
			((ViewGroup) view).removeAllViews();
		}
	}

	public boolean isReturnToProjectsList() {
		return returnToProjectsList;
	}

	public void setReturnToProjectsList(boolean returnToProjectsList) {
		this.returnToProjectsList = returnToProjectsList;
	}

	public String getTitleActionBar() {
		return titleActionBar;
	}

	public void setTitleActionBar(String titleActionBar) {
		this.titleActionBar = titleActionBar;
	}
}
