package hku.fyp14017.blencode.ble;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.util.Log;

import hku.fyp14017.blencode.ProjectManager;
import hku.fyp14017.blencode.content.Sprite;
import hku.fyp14017.blencode.stage.PreStageActivity;

import java.util.List;

import hku.fyp14017.blencode.ProjectManager;
import hku.fyp14017.blencode.stage.PreStageActivity;

/**
 * Created by User HP on 30/3/2015.
 */

@SuppressLint("NewApi")
public class RssiScanner {
    private Handler mHandler;
    private int number;

    public RssiScanner(int number){
        this.number = number;
        mHandler = new Handler();
    }


    public void startScanning(final BluetoothAdapter mBluetoothAdapter){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i< PreStageActivity.bgs.length;i++){
                    if(PreStageActivity.bgs[i] != null){
                        PreStageActivity.bgs[i].readRemoteRssi();
                    }
                }
                for(int i=0;i< PreStageActivity.card_bgs.length;i++){
                    if(PreStageActivity.card_bgs[i] != null){
                        PreStageActivity.card_bgs[i].readRemoteRssi();
                    }
                }
                mBluetoothAdapter.stopLeScan(callback);
                startScanning(mBluetoothAdapter);
            }
        }, 4000);
        mBluetoothAdapter.startLeScan(callback);
    }


     private BluetoothAdapter.LeScanCallback callback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int rssi, byte[] bytes) {
            Log.d("dev", bluetoothDevice.getName() + " " + rssi);
            /*double exp = (-rssi - 80) / (10*2.09);
            double distance = Math.pow(10.0, exp);*/
            if(rssi > ((-1)*number)) {
                List<Sprite> sprites = ProjectManager.getInstance().getCurrentProject().getSpriteList();
                for (Sprite s : sprites) {
                    s.look.onKeyfobPressed("BLE_PROXIMITY " + bluetoothDevice.getAddress());
                }
            }
        }
    };
}
