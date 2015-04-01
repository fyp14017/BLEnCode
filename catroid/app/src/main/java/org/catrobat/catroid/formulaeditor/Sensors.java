/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2014 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.formulaeditor;

import android.util.Log;

import org.catrobat.catroid.content.bricks.MonitorSensorBrick;

import java.util.HashMap;

public enum Sensors {
	X_ACCELERATION, Y_ACCELERATION, Z_ACCELERATION, COMPASS_DIRECTION, X_INCLINATION, Y_INCLINATION, LOUDNESS, FACE_DETECTED, FACE_SIZE, FACE_X_POSITION, FACE_Y_POSITION, OBJECT_X(
			true), OBJECT_Y(true), OBJECT_GHOSTEFFECT(true), OBJECT_BRIGHTNESS(true), OBJECT_SIZE(true), OBJECT_ROTATION(
			true), OBJECT_LAYER(true), SENSOR_TAG_TEMPERATURE, IR_TEMPERATURE, ACCELEROMETER_ABS, ACCELEROMETER_X, ACCELEROMETER_Y, ACCELEROMETER_Z,
            GYROSCOPE_X, GYROSCOPE_Y, GYROSCOPE_Z, MAGNETOMETER_ABS, MAGNETOMETER_X, MAGNETOMETER_Y, MAGNETOMETER_Z, PRESSURE, HUMIDITY;

	public final boolean isObjectSensor;
	public static final String TAG = Sensors.class.getSimpleName();

    public static HashMap<String, Sensors> sensorEnumMap;

	Sensors(boolean isObjectSensor) {
		this.isObjectSensor = true;
	}

	Sensors() {
		this.isObjectSensor = false;
	}

	public static boolean isSensor(String value) {
		if (getSensorByValue(value) == null) {
			return false;
		}
		return true;
	}

	public static Sensors getSensorByValue(String value) {
		try {
			return valueOf(value);
		} catch (IllegalArgumentException illegalArgumentException) {
            initSensorEnumMapping();
            return sensorEnumMap.get(value);
			//Log.e(TAG, Log.getStackTraceString(illegalArgumentException));
		}
	}

    private static void initSensorEnumMapping(){
        sensorEnumMap = new HashMap<String, Sensors>();
        sensorEnumMap.put("Ambient_Temperature", SENSOR_TAG_TEMPERATURE);
        sensorEnumMap.put("IR_Temperature", IR_TEMPERATURE);
        sensorEnumMap.put("Accelerometer_absolute", ACCELEROMETER_ABS);
        sensorEnumMap.put("Accelerometer_x", ACCELEROMETER_X);
        sensorEnumMap.put("Accelerometer_y", ACCELEROMETER_Y);
        sensorEnumMap.put("Accelerometer_z", ACCELEROMETER_Z);
        sensorEnumMap.put("Gyroscope_x", GYROSCOPE_X);
        sensorEnumMap.put("Gyroscope_y", GYROSCOPE_Y);
        sensorEnumMap.put("Gyroscope_z", GYROSCOPE_Z);
        sensorEnumMap.put("Magnetometer_absolute", MAGNETOMETER_ABS);
        sensorEnumMap.put("Magnetometer_x", MAGNETOMETER_X);
        sensorEnumMap.put("Magnetometer_y", MAGNETOMETER_Y);
        sensorEnumMap.put("Magnetometer_z", MAGNETOMETER_Z);
        sensorEnumMap.put("Pressure", PRESSURE);
        sensorEnumMap.put("Humidity", HUMIDITY);
    }

}
