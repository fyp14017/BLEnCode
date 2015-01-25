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
package org.catrobat.catroid.ble;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGattCharacteristic;

/**
 * @author User HP
 * 
 */
@SuppressLint("NewApi")
public class SensorInfo {

	public static float Temp;
	public static float Acc_x;
	public static float Acc_y;
	public static float Acc_z;
	public static float Gyro_x;
	public static float Gyro_y;
	public static float Gyro_z;
	public static float Mag_x;
	public static float Mag_y;
	public static float Mag_z;

	public static float[] extractGyroInfo(BluetoothGattCharacteristic c) {
		float y = shortSignedAtOffset(c, 0) * (500f / 65536f) * -1;
		float x = shortSignedAtOffset(c, 2) * (500f / 65536f);
		float z = shortSignedAtOffset(c, 4) * (500f / 65536f);

		return new float[] { x, y, z };
	}

	public static float[] extractMagInfo(BluetoothGattCharacteristic c) {
		float x = shortSignedAtOffset(c, 0) * (2000f / 65536f) * -1;
		float y = shortSignedAtOffset(c, 2) * (2000f / 65536f) * -1;
		float z = shortSignedAtOffset(c, 4) * (2000f / 65536f);

		return new float[] { x, y, z };
	}

	public static float[] extractAccInfo(BluetoothGattCharacteristic characteristic) {
		byte[] value = characteristic.getValue();
		Integer x = (int) value[0];
		Integer y = (int) value[1];
		Integer z = value[2] * -1;
		final float SCALE = (float) 64.0;
		float[] acc = { x / SCALE, y / SCALE, z / SCALE };
		return acc;
	}

	public static double extractBarTemperature(BluetoothGattCharacteristic characteristic, final int[] c) {
		// c holds the calibration coefficients

		int t_r; // Temperature raw value from sensor
		double t_a; // Temperature actual value in unit centi degrees celsius

		t_r = shortSignedAtOffset(characteristic, 0);

		t_a = (100 * (c[0] * t_r / Math.pow(2, 8) + c[1] * Math.pow(2, 6))) / Math.pow(2, 16);

		return t_a / 100;
	}

	public static double extractBarometer(BluetoothGattCharacteristic characteristic, final int[] c) {
		// c holds the calibration coefficients

		int t_r; // Temperature raw value from sensor
		int p_r; // Pressure raw value from sensor
		double S; // Interim value in calculation
		double O; // Interim value in calculation
		double p_a; // Pressure actual value in unit Pascal.

		t_r = shortSignedAtOffset(characteristic, 0);
		p_r = shortUnsignedAtOffset(characteristic, 2);

		S = c[2] + c[3] * t_r / Math.pow(2, 17) + ((c[4] * t_r / Math.pow(2, 15)) * t_r) / Math.pow(2, 19);
		O = c[5] * Math.pow(2, 14) + c[6] * t_r / Math.pow(2, 3) + ((c[7] * t_r / Math.pow(2, 15)) * t_r)
				/ Math.pow(2, 4);
		p_a = (S * p_r + O) / Math.pow(2, 14);

		//Convert pascal to in. Hg
		double p_hg = p_a * 0.000296;

		return p_hg;
	}

	public static int[] extractCalibrationCoefficients(BluetoothGattCharacteristic c) {
		int[] coefficients = new int[8];

		coefficients[0] = shortUnsignedAtOffset(c, 0);
		coefficients[1] = shortUnsignedAtOffset(c, 2);
		coefficients[2] = shortUnsignedAtOffset(c, 4);
		coefficients[3] = shortUnsignedAtOffset(c, 6);
		coefficients[4] = shortSignedAtOffset(c, 8);
		coefficients[5] = shortSignedAtOffset(c, 10);
		coefficients[6] = shortSignedAtOffset(c, 12);
		coefficients[7] = shortSignedAtOffset(c, 14);

		return coefficients;
	}

	private static Integer shortSignedAtOffset(BluetoothGattCharacteristic c, int offset) {
		Integer lowerByte = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset);
		Integer upperByte = c.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT8, offset + 1); // Note: interpret MSB as signed.

		return (upperByte << 8) + lowerByte;
	}

	private static Integer shortUnsignedAtOffset(BluetoothGattCharacteristic c, int offset) {
		Integer lowerByte = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset);
		Integer upperByte = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset + 1); // Note: interpret MSB as unsigned.

		return (upperByte << 8) + lowerByte;
	}
}
