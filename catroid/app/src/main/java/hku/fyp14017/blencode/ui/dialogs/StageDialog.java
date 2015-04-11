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
package hku.fyp14017.blencode.ui.dialogs;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.common.BroadcastSequenceMap;
import hku.fyp14017.blencode.common.BroadcastWaitSequenceMap;
import hku.fyp14017.blencode.stage.StageActivity;
import hku.fyp14017.blencode.stage.StageListener;

import hku.fyp14017.blencode.common.BroadcastSequenceMap;
import hku.fyp14017.blencode.common.BroadcastWaitSequenceMap;
import hku.fyp14017.blencode.stage.StageActivity;
import hku.fyp14017.blencode.stage.StageListener;

import static android.widget.Toast.LENGTH_SHORT;

public class StageDialog extends Dialog implements View.OnClickListener {
	private StageActivity stageActivity;
	private StageListener stageListener;

	public StageDialog(StageActivity stageActivity, StageListener stageListener, int theme) {
		super(stageActivity, theme);
		this.stageActivity = stageActivity;
		this.stageListener = stageListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(hku.fyp14017.blencode.R.layout.dialog_stage);
		getWindow().getAttributes();

		getWindow().getAttributes();

		int width = LayoutParams.MATCH_PARENT;
		int height = LayoutParams.WRAP_CONTENT;

		getWindow().setLayout(width, height);

		getWindow().setBackgroundDrawableResource(hku.fyp14017.blencode.R.color.transparent);

		((Button) findViewById(hku.fyp14017.blencode.R.id.stage_dialog_button_back)).setOnClickListener(this);
		((Button) findViewById(hku.fyp14017.blencode.R.id.stage_dialog_button_continue)).setOnClickListener(this);
		((Button) findViewById(hku.fyp14017.blencode.R.id.stage_dialog_button_restart)).setOnClickListener(this);
		((Button) findViewById(hku.fyp14017.blencode.R.id.stage_dialog_button_toggle_axes)).setOnClickListener(this);
		((Button) findViewById(hku.fyp14017.blencode.R.id.stage_dialog_button_screenshot)).setOnClickListener(this);
		if (stageActivity.getResizePossible()) {
			((ImageButton) findViewById(hku.fyp14017.blencode.R.id.stage_dialog_button_maximize)).setOnClickListener(this);
		} else {
			((ImageButton) findViewById(hku.fyp14017.blencode.R.id.stage_dialog_button_maximize)).setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case hku.fyp14017.blencode.R.id.stage_dialog_button_back:
				onBackPressed();
				break;
			case hku.fyp14017.blencode.R.id.stage_dialog_button_continue:
				dismiss();
				stageActivity.resume();
				break;
			case hku.fyp14017.blencode.R.id.stage_dialog_button_restart:
				clearBroadcastMaps();
				dismiss();
				restartProject();
				break;
			case hku.fyp14017.blencode.R.id.stage_dialog_button_toggle_axes:
				toggleAxes();
				break;
			case hku.fyp14017.blencode.R.id.stage_dialog_button_maximize:
				stageListener.toggleScreenMode();
				break;
			case hku.fyp14017.blencode.R.id.stage_dialog_button_screenshot:
				makeScreenshot();
				break;
			default:
				Log.w("CATROID", "Unimplemented button clicked! This shouldn't happen!");
				break;
		}
	}

	@Override
	public void onBackPressed() {
		clearBroadcastMaps();
		dismiss();
		stageActivity.exit();
		new FinishThreadAndDisposeTexturesTask().execute(null, null, null);
	}

	private void makeScreenshot() {
		if (stageListener.makeManualScreenshot()) {
			Toast.makeText(stageActivity, hku.fyp14017.blencode.R.string.notification_screenshot_ok, LENGTH_SHORT).show();
		} else {
			Toast.makeText(stageActivity, hku.fyp14017.blencode.R.string.error_screenshot_failed, LENGTH_SHORT).show();
		}
	}

	private void restartProject() {
		stageListener.reloadProject(stageActivity, this);
		synchronized (this) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				Log.e("CATROID", "Thread activated too early!", e);
			}
		}
		stageActivity.resume();
	}

	private void toggleAxes() {
		Button axesToggleButton = (Button) findViewById(hku.fyp14017.blencode.R.id.stage_dialog_button_toggle_axes);
		if (stageListener.axesOn) {
			stageListener.axesOn = false;
			axesToggleButton.setText(hku.fyp14017.blencode.R.string.stage_dialog_axes_on);
		} else {
			stageListener.axesOn = true;
			axesToggleButton.setText(hku.fyp14017.blencode.R.string.stage_dialog_axes_off);
		}
	}

	private void clearBroadcastMaps() {
		BroadcastSequenceMap.clear();
		BroadcastWaitSequenceMap.clear();
		BroadcastWaitSequenceMap.clearCurrentBroadcastEvent();
	}

	private class FinishThreadAndDisposeTexturesTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			stageActivity.manageLoadAndFinish();
			return null;
		}
	}
}
