package org.catrobat.catroid.content.actions;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

import org.catrobat.catroid.stage.PreStageActivity;

import static org.catrobat.catroid.content.bricks.ConnectCardBrick.UUID_BUZZER;
import static org.catrobat.catroid.content.bricks.ConnectCardBrick.UUID_LED;
import static org.catrobat.catroid.content.bricks.ConnectCardBrick.UUID_SERVICE;

/**
 * Created by Yatharth on 09-03-2015.
 */

public class CardLedAction extends TemporalAction
{
    private int red, green, blue, time;
    @SuppressLint("NewApi")
    @Override
    protected void update(float v)
    {
        BluetoothGattCharacteristic bgc = PreStageActivity.card_bgs[0].getService(UUID_SERVICE).getCharacteristic(UUID_LED);
        byte redByte = Byte.parseByte(Integer.toHexString(red),16);
        byte greenByte = Byte.parseByte(Integer.toHexString(green),16);
        byte blueByte = Byte.parseByte(Integer.toHexString(blue),16);

        if(time < 256){
            byte timeLast = Byte.parseByte(Integer.toHexString(time),16);
            bgc.setValue(new byte[] {redByte, greenByte, blueByte, 0x00, timeLast, 0x00});
        }else if(time >= 256 && time <=2550){
            String hex1 = Integer.toHexString(time).substring(0,1), hex2 = Integer.toHexString(time).substring(1,3);
            byte timeLast1 = Byte.parseByte(hex1,16), timeLast2 = Byte.parseByte(hex2,16);
            Log.d("dev", "timeLast1 & 2 = " + timeLast1 + " & " +timeLast2);
            bgc.setValue(new byte[] {redByte, greenByte, blueByte, timeLast1, timeLast2, 0x00});
        }else{
            Log.d("dev","Problem in time");
        }
        PreStageActivity.card_bgs[0].writeCharacteristic(bgc);
    }

    public void setValues(int red, int green, int blue,int time){
        this.red =  red;
        this.green = green;
        this.blue = blue;
        this.time = time*10;
    }
}
