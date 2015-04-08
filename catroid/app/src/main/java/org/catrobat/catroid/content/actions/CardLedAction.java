package org.catrobat.catroid.content.actions;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

import org.catrobat.catroid.ble.BLECard;
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
    private BLECard cardEnum;
    @SuppressLint("NewApi")
    @Override
    protected void update(float v)
    {
        PreStageActivity.bg = null;
        switch(cardEnum){
            case CARD1:
                PreStageActivity.bg = PreStageActivity.card_bgs[0];
                //flag_position = 0;
                break;
            case CARD2:
                PreStageActivity.bg = PreStageActivity.card_bgs[1];
                //flag_position = 1;
                break;
            case CARD3:
                PreStageActivity.bg = PreStageActivity.card_bgs[2];
                //flag_position = 2;
                break;
            case CARD4:
                PreStageActivity.bg = PreStageActivity.card_bgs[3];
                //flag_position = 3;
                break;
            case CARD5:
                PreStageActivity.bg = PreStageActivity.card_bgs[4];
                //flag_position = 4;
                break;
            case CARD6:
                PreStageActivity.bg = PreStageActivity.card_bgs[5];
                //flag_position = 5;
                break;
            case CARD7:
                PreStageActivity.bg = PreStageActivity.card_bgs[6];
                //flag_position = 6;
                break;
            case CARD8:
                PreStageActivity.bg = PreStageActivity.card_bgs[7];
                //flag_position = 7;
                break;
            case CARD9:
                PreStageActivity.bg = PreStageActivity.card_bgs[8];
                //flag_position = 8;
                break;
            case CARD10:
                PreStageActivity.bg = PreStageActivity.card_bgs[9];
                //flag_position = 9;
                break;
            default:
                Log.d("dev", "bg is null");
                break;
        }
        BluetoothGattCharacteristic bgc = PreStageActivity.bg.getService(UUID_SERVICE).getCharacteristic(UUID_LED);
        byte redByte = Byte.parseByte(Integer.toHexString(red), 16);
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
        PreStageActivity.bg.writeCharacteristic(bgc);
    }

    public void setValues(int red, int green, int blue,int time, BLECard card){
        this.red =  red;
        this.green = green;
        this.blue = blue;
        this.time = time*10;
        this.cardEnum = card;
    }
}
