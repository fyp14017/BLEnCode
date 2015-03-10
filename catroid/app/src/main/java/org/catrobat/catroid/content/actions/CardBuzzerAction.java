package org.catrobat.catroid.content.actions;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGattCharacteristic;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

import org.catrobat.catroid.stage.PreStageActivity;

import static org.catrobat.catroid.content.bricks.ConnectCardBrick.*;

/**
 * Created by Yatharth on 09-03-2015.
 */

public class CardBuzzerAction extends TemporalAction
{
    @SuppressLint("NewApi")
    @Override
    protected void update(float v)
    {
        BluetoothGattCharacteristic bgc = PreStageActivity.card_bgs[0].getService(UUID_SERVICE).getCharacteristic(UUID_BUZZER);
        bgc.setValue(new byte[] {0x64, 0x50, 0x00, 0x14});
        PreStageActivity.card_bgs[0].writeCharacteristic(bgc);
    }
}
