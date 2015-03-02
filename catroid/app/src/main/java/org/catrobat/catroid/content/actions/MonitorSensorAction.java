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
package org.catrobat.catroid.content.actions;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

import org.catrobat.catroid.ble.SensorInfo;
import org.catrobat.catroid.content.bricks.MonitorSensorBrick;
import org.catrobat.catroid.content.bricks.MonitorSensorBrick.*;
import org.catrobat.catroid.stage.PreStageActivity;

import java.util.UUID;

@SuppressLint("NewApi")
public class MonitorSensorAction extends TemporalAction {

	private Sensor sensor;
    private MonitorSensorBrick.SensorTag tag;

	public static final UUID PRESSURE_SERVICE = UUID.fromString("f000aa40-0451-4000-b000-000000000000");
	public static final UUID PRESSURE_DATA_CHAR = UUID.fromString("f000aa41-0451-4000-b000-000000000000");
	public static final UUID PRESSURE_CONFIG_CHAR = UUID.fromString("f000aa42-0451-4000-b000-000000000000");
	public static final UUID PRESSURE_CAL_CHAR = UUID.fromString("f000aa43-0451-4000-b000-000000000000");

	public static final UUID HUMIDITY_SERVICE = UUID.fromString("f000aa20-0451-4000-b000-000000000000");
	public static final UUID HUMIDITY_DATA_CHAR = UUID.fromString("f000aa21-0451-4000-b000-000000000000");
	public static final UUID HUMIDITY_CONFIG_CHAR = UUID.fromString("f000aa22-0451-4000-b000-000000000000");

	public static final UUID ACC_SERVICE = UUID.fromString("f000aa10-0451-4000-b000-000000000000");
	public static final UUID ACC_DATA_CHAR = UUID.fromString("f000aa11-0451-4000-b000-000000000000");
	public static final UUID ACC_CONFIG_CHAR = UUID.fromString("f000aa12-0451-4000-b000-000000000000");

	public static final UUID MAG_SERVICE = UUID.fromString("f000aa30-0451-4000-b000-000000000000");
	public static final UUID MAG_DATA_CHAR = UUID.fromString("f000aa31-0451-4000-b000-000000000000");
	public static final UUID MAG_CONFIG_CHAR = UUID.fromString("f000aa32-0451-4000-b000-000000000000");

	public static final UUID GYRO_SERVICE = UUID.fromString("f000aa50-0451-4000-b000-000000000000");
	public static final UUID GYRO_DATA_CHAR = UUID.fromString("f000aa51-0451-4000-b000-000000000000");
	public static final UUID GYRO_CONFIG_CHAR = UUID.fromString("f000aa52-0451-4000-b000-000000000000");

	public static final UUID SIMPLE_KEY_SERVICE = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
	public static final UUID SIMPLE_KEY_DATA = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
	/* Client Configuration Descriptor */
	public static final UUID CONFIG_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

	@Override
	protected void update(float percent) {
        PreStageActivity.bg = null;
		switch(tag){
            case TAG1:
                PreStageActivity.bg = PreStageActivity.bgs[0];
                break;
            case TAG2:
                PreStageActivity.bg = PreStageActivity.bgs[1];
                break;
            case TAG3:
                PreStageActivity.bg = PreStageActivity.bgs[2];
                break;
            case TAG4:
                PreStageActivity.bg = PreStageActivity.bgs[3];
                break;
            case TAG5:
                PreStageActivity.bg = PreStageActivity.bgs[4];
                break;
            case TAG6:
                PreStageActivity.bg = PreStageActivity.bgs[5];
                break;
            case TAG7:
                PreStageActivity.bg = PreStageActivity.bgs[6];
                break;
            case TAG8:
                PreStageActivity.bg = PreStageActivity.bgs[7];
                break;
            case TAG9:
                PreStageActivity.bg = PreStageActivity.bgs[8];
                break;
            case TAG10:
                PreStageActivity.bg = PreStageActivity.bgs[9];
                break;
            default:
                Log.d("dev", "bg is null");
                break;
        }
		if (sensor.equals(Sensor.TEMPERATURE)) {
			//SensorInfo.Temp = 0f;
			BluetoothGattCharacteristic p_config = PreStageActivity.bg.getService(PRESSURE_SERVICE).getCharacteristic(
					PRESSURE_CONFIG_CHAR);
			p_config.setValue(new byte[] { 0x02 });
			PreStageActivity.bg.writeCharacteristic(p_config);
		}
		if (sensor.equals(Sensor.ACCELEROMETER)) {
			BluetoothGattCharacteristic acc_config = PreStageActivity.bg.getService(ACC_SERVICE).getCharacteristic(
					ACC_CONFIG_CHAR);
			acc_config.setValue(new byte[] { 0x01 });
			PreStageActivity.bg.writeCharacteristic(acc_config);
		}
		if (sensor.equals(Sensor.PRESSURE)) {
			//TODO: check if temp has already enabled pressure service, maybe better not to enable twice
		}
		if (sensor.equals(Sensor.MAGNETOMETER)) {
			BluetoothGattCharacteristic mag_config = PreStageActivity.bg.getService(MAG_SERVICE).getCharacteristic(
					MAG_CONFIG_CHAR);
			mag_config.setValue(new byte[] { 0x01 });
			PreStageActivity.bg.writeCharacteristic(mag_config);
		}
		if (sensor.equals(Sensor.GYROSCOPE)) {
			BluetoothGattCharacteristic gyro_config = PreStageActivity.bg.getService(GYRO_SERVICE).getCharacteristic(
					GYRO_CONFIG_CHAR);
			gyro_config.setValue(new byte[] { 0x07 });
			PreStageActivity.bg.writeCharacteristic(gyro_config);
		}
		if (sensor.equals(Sensor.HUMIDITY)) {

		}
	}

	public void setEnum(Sensor sensor, MonitorSensorBrick.SensorTag tag) {
		this.sensor = sensor; this.tag = tag;
	}
}
