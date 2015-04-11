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

import android.app.AlertDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.content.bricks.Brick;
import hku.fyp14017.blencode.facedetection.FaceDetectionHandler;
import hku.fyp14017.blencode.formulaeditor.Formula;
import hku.fyp14017.blencode.formulaeditor.FormulaElement.ElementType;
import hku.fyp14017.blencode.formulaeditor.SensorHandler;

public class FormulaEditorComputeDialog extends AlertDialog implements SensorEventListener {

	private Formula formulaToCompute = null;
	private Context context;
	private TextView computeTextView;

	public FormulaEditorComputeDialog(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(hku.fyp14017.blencode.R.layout.dialog_formulaeditor_compute);
		computeTextView = (TextView) findViewById(hku.fyp14017.blencode.R.id.formula_editor_compute_dialog_textview);
		showFormulaResult();
	}

	public void setFormula(Formula formula) {
		formulaToCompute = formula;

		if (formula.containsElement(ElementType.SENSOR)) {
			SensorHandler.startSensorListener(context);
			SensorHandler.registerListener(this);
		}
		int resources = formula.getRequiredResources();
		if ((resources & Brick.FACE_DETECTION) > 0) {
			FaceDetectionHandler.startFaceDetection(getContext());
		}
	}

	@Override
	protected void onStop() {
		SensorHandler.unregisterListener(this);
		FaceDetectionHandler.stopFaceDetection();
		super.onStop();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		dismiss();
		return true;
	}

	private void showFormulaResult() {
		if (computeTextView == null) {
			return;
		}

		String result = formulaToCompute.getResultForComputeDialog(context);
		setDialogTextView(result);

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
			case Sensor.TYPE_LINEAR_ACCELERATION:
				showFormulaResult();
				break;
			case Sensor.TYPE_ROTATION_VECTOR:
				showFormulaResult();
				break;
		}
	}

	private void setDialogTextView(final String newString) {
		computeTextView.post(new Runnable() {
			@Override
			public void run() {
				computeTextView.setText(newString);
			}
		});
	}
}
