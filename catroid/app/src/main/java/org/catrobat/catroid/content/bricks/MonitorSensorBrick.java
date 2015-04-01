/**
 *  Catroid: An on-device visual programming system for Android devices
 *  Copyright (C) 2010-2013 The Catrobat Team
 *  (<http://developer.catrobat.org/credits>)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://developer.catrobat.org/license_additional_term
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.content.bricks;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.actions.ExtendedActions;

import java.util.List;

public class MonitorSensorBrick extends BrickBaseType implements OnItemSelectedListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static enum Sensor {
		IR_TEMPERATURE, AMBIENT_TEMPERATURE, PRESSURE, HUMIDITY, ACCELEROMETER, GYROSCOPE, MAGNETOMETER
	}

    public static enum SensorTag{
        TAG1, TAG2, TAG3, TAG4, TAG5, TAG6, TAG7, TAG8, TAG9, TAG10
    }

	private transient Sensor SensorEnum;
    private transient SensorTag tagEnum;
	private String sensor;
    private String tag;
	private transient AdapterView<?> adapterView;

	public MonitorSensorBrick(Sprite sprite, Sensor sensor, SensorTag tag) {
		this.sprite = sprite;
		this.SensorEnum = sensor;
        this.tagEnum = tag;
        this.tag = tagEnum.name();
		this.sensor = SensorEnum.name();
	}

	protected Object readResolve() {
		if (sensor != null) {
			SensorEnum = Sensor.valueOf(sensor);
		}
        if (tag!=null){
            tagEnum = SensorTag.valueOf(tag);
        }
		return this;
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}
		if (view == null) {
			alphaValue = 255;
		}
		view = View.inflate(context, R.layout.brick_ble_monitor_sensor, null);
		view = getViewWithAlpha(alphaValue);

		setCheckboxView(R.id.brick_ble_monitor_checkbox);
		final Brick brickInstance = this;
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checked = isChecked;
				adapter.handleCheck(brickInstance, isChecked);
			}
		});
		ArrayAdapter<CharSequence> motorAdapter = ArrayAdapter.createFromResource(context, R.array.sensor_chooser,
				android.R.layout.simple_spinner_item);
		motorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		Spinner motorSpinner = (Spinner) view.findViewById(R.id.sensor_spinner);
		motorSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> sensorTagAdapter = ArrayAdapter.createFromResource(context, R.array.sensortag_chooser,
                android.R.layout.simple_spinner_item);
        sensorTagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sensorTagSpinner = (Spinner) view.findViewById(R.id.tag_spinner);
        sensorTagSpinner.setOnItemSelectedListener(new OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                tagEnum = SensorTag.values()[i];
                tag = tagEnum.name();
                adapterView = parent;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

		if (!(checkbox.getVisibility() == View.VISIBLE)) {
			motorSpinner.setClickable(true);
			motorSpinner.setEnabled(true);
		} else {
			motorSpinner.setClickable(false);
			motorSpinner.setEnabled(false);
		}

		motorSpinner.setAdapter(motorAdapter);
		motorSpinner.setSelection(SensorEnum.ordinal());

        if (!(checkbox.getVisibility() == View.VISIBLE)) {
            sensorTagSpinner.setClickable(true);
            sensorTagSpinner.setEnabled(true);
        } else {
            sensorTagSpinner.setClickable(false);
            sensorTagSpinner.setEnabled(false);
        }

        sensorTagSpinner.setAdapter(sensorTagAdapter);
        sensorTagSpinner.setSelection(tagEnum.ordinal());
		return view;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		SensorEnum = Sensor.values()[position];
		sensor = SensorEnum.name();
		adapterView = parent;
	}

	@Override
	public Brick clone() {
		return new MonitorSensorBrick(getSprite(), SensorEnum, tagEnum);
	}

	@Override
	public View getViewWithAlpha(int alphaValue) {

		if (view != null) {

			View layout = view.findViewById(R.id.brick_ble_monitor_layout);
			Drawable background = layout.getBackground();
			background.setAlpha(alphaValue);

			TextView TempLabel = (TextView) view.findViewById(R.id.ble_monitor_label);
			TempLabel.setTextColor(TempLabel.getTextColors().withAlpha(alphaValue));
			Spinner motorSpinner = (Spinner) view.findViewById(R.id.sensor_spinner);
            Spinner sensorTagSpinner = (Spinner) view.findViewById(R.id.tag_spinner);
			ColorStateList color = TempLabel.getTextColors().withAlpha(alphaValue);
			motorSpinner.getBackground().setAlpha(alphaValue);
            sensorTagSpinner.getBackground().setAlpha(alphaValue);
			if (adapterView != null) {
				((TextView) adapterView.getChildAt(0)).setTextColor(color);
			}
			this.alphaValue = (alphaValue);

		}

		return view;
	}

	@Override
	public Brick copyBrickForSprite(Sprite sprite) {
		MonitorSensorBrick copyBrick = (MonitorSensorBrick) clone();
		copyBrick.sprite = sprite;
		return copyBrick;
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public View getPrototypeView(Context context) {
		View prototypeView = View.inflate(context, R.layout.brick_ble_monitor_sensor, null);
		Spinner bleSpinner = (Spinner) prototypeView.findViewById(R.id.sensor_spinner);
		bleSpinner.setFocusableInTouchMode(false);
		bleSpinner.setFocusable(false);
		ArrayAdapter<CharSequence> SensorAdapter = ArrayAdapter.createFromResource(context, R.array.sensor_chooser,
				android.R.layout.simple_spinner_item);
		SensorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		bleSpinner.setAdapter(SensorAdapter);
		bleSpinner.setSelection(SensorEnum.ordinal());

        Spinner sensorTagSpinner = (Spinner) prototypeView.findViewById(R.id.tag_spinner);
        sensorTagSpinner.setFocusable(false);
        sensorTagSpinner.setFocusableInTouchMode(false);

        ArrayAdapter<CharSequence> tagAdapter = ArrayAdapter.createFromResource(context, R.array.sensortag_chooser,
                android.R.layout.simple_spinner_item);
        SensorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sensorTagSpinner.setAdapter(tagAdapter);
        sensorTagSpinner.setSelection(tagEnum.ordinal());

		return prototypeView;
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite s, SequenceAction sequence) {
		sequence.addAction(ExtendedActions.monitorSensorAction(SensorEnum, tagEnum));
		return null;
	}

	@Override
	public String brickTutorial() {
		return "Activates and reads data from the chosen sensor of the SensorTag.\n\n"
				+ "Receives data whenever the values of the sensors change or every 1 second.";

	}
}
