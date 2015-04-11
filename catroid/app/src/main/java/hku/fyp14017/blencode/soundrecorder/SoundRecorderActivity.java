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
package hku.fyp14017.blencode.soundrecorder;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Chronometer;
import android.widget.Toast;

import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.common.Constants;
import hku.fyp14017.blencode.ui.BaseActivity;
import hku.fyp14017.blencode.utils.Utils;

import java.io.IOException;

import hku.fyp14017.blencode.common.Constants;

public class SoundRecorderActivity extends BaseActivity implements OnClickListener {

	private static final String TAG = SoundRecorderActivity.class.getSimpleName();
	private SoundRecorder soundRecorder;
	private Chronometer timeRecorderChronometer;
	private RecordButton recordButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(hku.fyp14017.blencode.R.layout.activity_soundrecorder);

		recordButton = (RecordButton) findViewById(hku.fyp14017.blencode.R.id.soundrecorder_record_button);
		timeRecorderChronometer = (Chronometer) findViewById(hku.fyp14017.blencode.R.id.soundrecorder_chronometer_time_recorded);
		recordButton.setOnClickListener(this);
		Utils.checkForExternalStorageAvailableAndDisplayErrorIfNot(this);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == hku.fyp14017.blencode.R.id.soundrecorder_record_button) {
			if (soundRecorder != null && soundRecorder.isRecording()) {
				stopRecording();
				timeRecorderChronometer.stop();
				finish();
			} else {
				startRecording();
				long currentPlayingBase = SystemClock.elapsedRealtime();
				timeRecorderChronometer.setBase(currentPlayingBase);
				timeRecorderChronometer.start();
			}
		}
	}

	@Override
	public void onBackPressed() {
		stopRecording();
		super.onBackPressed();
	}

	private synchronized void startRecording() {
		if (soundRecorder != null && soundRecorder.isRecording()) {
			return;
		}
		try {
			String recordPath = Utils.buildPath(Constants.TMP_PATH, getString(hku.fyp14017.blencode.R.string.soundrecorder_recorded_filename)
					+ SoundRecorder.RECORDING_EXTENSION);
			soundRecorder = new SoundRecorder(recordPath);
			soundRecorder.start();
			setViewsToRecordingState();
		} catch (IOException e) {
			Log.e(TAG, "Error recording sound.", e);
			Toast.makeText(this, hku.fyp14017.blencode.R.string.soundrecorder_error, Toast.LENGTH_SHORT).show();
		} catch (IllegalStateException e) {
			// app would crash if other app uses mic, catch IllegalStateException and display Toast
			Log.e(TAG, "Error recording sound (Other recorder running?).", e);
			Toast.makeText(this, hku.fyp14017.blencode.R.string.soundrecorder_error, Toast.LENGTH_SHORT).show();
		}
	}

	private void setViewsToRecordingState() {
		recordButton.setState(RecordButton.RecordState.RECORD);
		recordButton.setImageResource(hku.fyp14017.blencode.R.drawable.ic_microphone_active);
	}

	private synchronized void stopRecording() {
		if (soundRecorder == null || !soundRecorder.isRecording()) {
			return;
		}
		setViewsToNotRecordingState();
		try {
			soundRecorder.stop();
			Uri uri = soundRecorder.getPath();
			setResult(Activity.RESULT_OK, new Intent(Intent.ACTION_PICK, uri));
		} catch (IOException e) {
			Log.e("CATROID", "Error recording sound.", e);
			Toast.makeText(this, hku.fyp14017.blencode.R.string.soundrecorder_error, Toast.LENGTH_SHORT).show();
			setResult(Activity.RESULT_CANCELED);
		}
	}

	private void setViewsToNotRecordingState() {
		recordButton.setState(RecordButton.RecordState.STOP);
		recordButton.setImageResource(hku.fyp14017.blencode.R.drawable.ic_microphone);
	}

}
