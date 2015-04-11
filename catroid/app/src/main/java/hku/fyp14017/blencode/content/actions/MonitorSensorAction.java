/**
 *  Catroid: An on-device visual programming system for Android devices
 *  Copyright (C) 2010-2013 The Catrobat Team
 *  (<http://developer.fyp14017.hku/credits>)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://developer.fyp14017.hku/license_additional_term
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.hku/licenses/>.
 */
package hku.fyp14017.blencode.content.actions;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

import hku.fyp14017.blencode.content.bricks.MonitorSensorBrick;
import hku.fyp14017.blencode.content.bricks.MonitorSensorBrick.*;
import hku.fyp14017.blencode.stage.PreStageActivity;

import java.util.UUID;

import hku.fyp14017.blencode.stage.PreStageActivity;

@SuppressLint("NewApi")
public class MonitorSensorAction extends TemporalAction {

	private Sensor sensor;
    private boolean pressureFlag = false;
    public static boolean[] tagFlag = new boolean[10];
    private MonitorSensorBrick.SensorTag tag;
    int flag_position;
    public static final UUID
        PRESSURE_SERVICE = UUID.fromString("f000aa40-0451-4000-b000-000000000000"),
        PRESSURE_DATA_CHAR = UUID.fromString("f000aa41-0451-4000-b000-000000000000"),
        PRESSURE_CONFIG_CHAR = UUID.fromString("f000aa42-0451-4000-b000-000000000000"),
        PRESSURE_CAL_CHAR = UUID.fromString("f000aa43-0451-4000-b000-000000000000"),

        IRT_SERVICE = UUID.fromString("f000aa00-0451-4000-b000-000000000000"),
        IRT_DATA_CHAR = UUID.fromString("f000aa01-0451-4000-b000-000000000000"),
        IRT_CONFIG_CHAR = UUID.fromString("f000aa02-0451-4000-b000-000000000000"), // 0: disable, 1: enable

        HUMIDITY_SERVICE = UUID.fromString("f000aa20-0451-4000-b000-000000000000"),
        HUMIDITY_DATA_CHAR = UUID.fromString("f000aa21-0451-4000-b000-000000000000"),
        HUMIDITY_CONFIG_CHAR = UUID.fromString("f000aa22-0451-4000-b000-000000000000"),

        ACC_SERVICE = UUID.fromString("f000aa10-0451-4000-b000-000000000000"),
        ACC_DATA_CHAR = UUID.fromString("f000aa11-0451-4000-b000-000000000000"),
        ACC_CONFIG_CHAR = UUID.fromString("f000aa12-0451-4000-b000-000000000000"),

        MAG_SERVICE = UUID.fromString("f000aa30-0451-4000-b000-000000000000"),
        MAG_DATA_CHAR = UUID.fromString("f000aa31-0451-4000-b000-000000000000"),
        MAG_CONFIG_CHAR = UUID.fromString("f000aa32-0451-4000-b000-000000000000"),

        GYRO_SERVICE = UUID.fromString("f000aa50-0451-4000-b000-000000000000"),
        GYRO_DATA_CHAR = UUID.fromString("f000aa51-0451-4000-b000-000000000000"),
        GYRO_CONFIG_CHAR = UUID.fromString("f000aa52-0451-4000-b000-000000000000"),

        SIMPLE_KEY_SERVICE = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb"),
        SIMPLE_KEY_DATA = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb"),
        /* Client Configuration Descriptor */
        CONFIG_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

	@Override
	protected void update(float percent) {

        PreStageActivity.bg = null;
		switch(tag){
            case TAG1:
                PreStageActivity.bg = PreStageActivity.bgs[0];
                flag_position = 0;
                break;
            case TAG2:
                PreStageActivity.bg = PreStageActivity.bgs[1];
                flag_position = 1;
                break;
            case TAG3:
                PreStageActivity.bg = PreStageActivity.bgs[2];
                flag_position = 2;
                break;
            case TAG4:
                PreStageActivity.bg = PreStageActivity.bgs[3];
                flag_position = 3;
                break;
            case TAG5:
                PreStageActivity.bg = PreStageActivity.bgs[4];
                flag_position = 4;
                break;
            case TAG6:
                PreStageActivity.bg = PreStageActivity.bgs[5];
                flag_position = 5;
                break;
            case TAG7:
                PreStageActivity.bg = PreStageActivity.bgs[6];
                flag_position = 6;
                break;
            case TAG8:
                PreStageActivity.bg = PreStageActivity.bgs[7];
                flag_position = 7;
                break;
            case TAG9:
                PreStageActivity.bg = PreStageActivity.bgs[8];
                flag_position = 8;
                break;
            case TAG10:
                PreStageActivity.bg = PreStageActivity.bgs[9];
                flag_position = 9;
                break;
            default:
                Log.d("dev", "bg is null");
                break;
        }
        if(sensor.equals(Sensor.IR_TEMPERATURE)){
            while(tagFlag[flag_position]){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            tagFlag[flag_position] = true;
            BluetoothGattCharacteristic temp_config = PreStageActivity.bg.getService(IRT_SERVICE).getCharacteristic(IRT_CONFIG_CHAR);
            temp_config.setValue(new byte[] {0x01});
            temp_config.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
            PreStageActivity.bg.writeCharacteristic(temp_config);
        }
		if (sensor.equals(Sensor.AMBIENT_TEMPERATURE)) {
            while(tagFlag[flag_position]){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            tagFlag[flag_position] = true;
            if(!pressureFlag) {
                pressureFlag = true;
                BluetoothGattCharacteristic p_config = PreStageActivity.bg.getService(PRESSURE_SERVICE).getCharacteristic(
                        PRESSURE_CONFIG_CHAR);
                p_config.setValue(new byte[]{0x02});
                PreStageActivity.bg.writeCharacteristic(p_config);
            }
		}
		if (sensor.equals(Sensor.ACCELEROMETER)) {
            while(tagFlag[flag_position]){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            tagFlag[flag_position] = true;
			BluetoothGattCharacteristic acc_config = PreStageActivity.bg.getService(ACC_SERVICE).getCharacteristic(
					ACC_CONFIG_CHAR);
			acc_config.setValue(new byte[] { 0x01 });
			PreStageActivity.bg.writeCharacteristic(acc_config);
		}
		if (sensor.equals(Sensor.PRESSURE)) {
            while(tagFlag[flag_position]){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            tagFlag[flag_position] = true;
            if(!pressureFlag){
                pressureFlag = true;
                BluetoothGattCharacteristic p_config = PreStageActivity.bg.getService(PRESSURE_SERVICE).getCharacteristic(
                        PRESSURE_CONFIG_CHAR);
                p_config.setValue(new byte[] { 0x02 });
                PreStageActivity.bg.writeCharacteristic(p_config);
            }
		}
		if (sensor.equals(Sensor.MAGNETOMETER)) {
            while(tagFlag[flag_position]){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            tagFlag[flag_position] = true;
			BluetoothGattCharacteristic mag_config = PreStageActivity.bg.getService(MAG_SERVICE).getCharacteristic(
					MAG_CONFIG_CHAR);
			mag_config.setValue(new byte[] { 0x01 });
			PreStageActivity.bg.writeCharacteristic(mag_config);
		}
		if (sensor.equals(Sensor.GYROSCOPE)) {
            while(tagFlag[flag_position]){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            tagFlag[flag_position] = true;
            BluetoothGattCharacteristic gyro_config = PreStageActivity.bg.getService(GYRO_SERVICE).getCharacteristic(
                    GYRO_CONFIG_CHAR);
            gyro_config.setValue(new byte[] { 0x07 });
            gyro_config.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
            PreStageActivity.bg.writeCharacteristic(gyro_config);
		}
		if (sensor.equals(Sensor.HUMIDITY)) {
            while(tagFlag[flag_position]){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            tagFlag[flag_position] = true;
            BluetoothGattCharacteristic hum_config = PreStageActivity.bg.getService(HUMIDITY_SERVICE).getCharacteristic(
                    HUMIDITY_CONFIG_CHAR);
            hum_config.setValue(new byte[] { 0x01 });
            PreStageActivity.bg.writeCharacteristic(hum_config);
		}
	}

	public void setEnum(Sensor sensor, MonitorSensorBrick.SensorTag tag) {
		this.sensor = sensor; this.tag = tag;
	}
}
