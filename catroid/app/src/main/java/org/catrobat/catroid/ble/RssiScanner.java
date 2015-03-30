package org.catrobat.catroid.ble;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.util.Log;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.content.Sprite;

import java.util.List;

/**
 * Created by User HP on 30/3/2015.
 */

@SuppressLint("NewApi")
public class RssiScanner {
    private Handler mHandler;

    public RssiScanner(){
        mHandler = new Handler();
    }


    public void startScanning(final BluetoothAdapter mBluetoothAdapter){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBluetoothAdapter.stopLeScan(callback);
                startScanning(mBluetoothAdapter);
            }
        }, 3000);
        mBluetoothAdapter.startLeScan(callback);
    }


     private BluetoothAdapter.LeScanCallback callback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int rssi, byte[] bytes) {
            Log.d("dev", bluetoothDevice.getName() + " " + rssi);
            if(rssi > -70) {
                List<Sprite> sprites = ProjectManager.getInstance().getCurrentProject().getSpriteList();
                for (Sprite s : sprites) {
                    s.look.onKeyfobPressed("BLE_PROXIMITY " + bluetoothDevice.getName());
                }
            }
        }
    };
}
