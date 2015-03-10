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
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.actions.ExtendedActions;
import org.catrobat.catroid.stage.PreStageActivity;

import java.util.List;
import java.util.UUID;

public class ConnectCardBrick extends BrickBaseType implements OnItemSelectedListener
{
    private static final long serialVersionUID = 1L;

    public static final UUID UUID_SERVICE = UUID.fromString("d88e0800-0820-11e4-9191-0800200c9a66");
    public static final UUID UUID_LED = UUID.fromString("d88e0801-0820-11e4-9191-0800200c9a66");
    public static final UUID UUID_BUZZER = UUID.fromString("d88e0802-0820-11e4-9191-0800200c9a66");
    public static final UUID UUID_BUTTON = UUID.fromString("d88e0803-0820-11e4-9191-0800200c9a66");
    public static final UUID UUID_NAME = UUID.fromString("d88e0804-0820-11e4-9191-0800200c9a66");
    public static final UUID UUID_CLASS = UUID.fromString("d88e0805-0820-11e4-9191-0800200c9a66");
    public static final UUID UUID_SCHOOL = UUID.fromString("d88e0806-0820-11e4-9191-0800200c9a66");
    public static final UUID UUID_CARDNAME = UUID.fromString("d88e0807-0820-11e4-9191-0800200c9a66");
    public static final UUID UUID_ADINFO = UUID.fromString("d88e0808-0820-11e4-9191-0800200c9a66");
    public static final UUID UUID_OAD = UUID.fromString("d88e0809-0820-11e4-9191-0800200c9a66");

    private transient AdapterView<?> adapterView;

    public ConnectCardBrick(Sprite sprite)
    {
        this.sprite = sprite;
    }

    @Override
    public Brick clone() {
        return new ConnectCardBrick(getSprite());
    }
    @Override
    public Brick copyBrickForSprite(Sprite sprite) {
        ConnectCardBrick copyBrick = (ConnectCardBrick) clone();
        copyBrick.sprite = sprite;
        return copyBrick;
    }

    @Override
    public View getView(Context context, int brickId, final BaseAdapter baseAdapter)
    {
        if (animationState) return view;

        view = View.inflate(context, R.layout.brick_ble_connect_card, null);
        setCheckboxView(R.id.brick_ble_connect_card_checkbox);

        return view;
    }

    @Override
    public View getViewWithAlpha(int alphaValue) {
        if (view != null) {
            View layout = view.findViewById(R.id.brick_ble_connect_card_layout);
            Drawable background = layout.getBackground();
            background.setAlpha(alphaValue);
            this.alphaValue = (alphaValue);
        }

        return view;
    }

    @Override
    public View getPrototypeView(Context context) {
        View prototypeView = View.inflate(context, R.layout.brick_ble_connect_card, null);
        return prototypeView;
    }

    @Override
    public int getRequiredResources()
    {
        PreStageActivity.CardCounter++;
        return BLUETOOTH_BLE_SENSORS;
    }

    @Override
    public List<SequenceAction> addActionToSequence(Sprite s, SequenceAction sequence)
    {
        sequence.addAction(ExtendedActions.connectCardAction());
        return null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Nothing
    }
}
