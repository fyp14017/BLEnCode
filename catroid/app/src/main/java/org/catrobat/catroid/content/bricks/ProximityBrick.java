package org.catrobat.catroid.content.bricks;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.actions.ExtendedActions;

import java.util.List;

public class ProximityBrick extends BrickBaseType
{
    private static final long serialVersionUID = 1L;

    //public static enum frequencyDivider, dutyCycleDivider, timeLast;

    public ProximityBrick(Sprite sprite)
    {
        this.sprite = sprite;
    }

    @Override
    public Brick clone() {
        return new ProximityBrick(getSprite());
    }
    @Override
    public Brick copyBrickForSprite(Sprite sprite) {
        ProximityBrick copyBrick = (ProximityBrick) clone();
        copyBrick.sprite = sprite;
        return copyBrick;
    }

    @Override
    public View getView(Context context, int brickId, final BaseAdapter baseAdapter)
    {
        if (animationState) return view;

        view = View.inflate(context, R.layout.brick_ble_proximity, null);
        setCheckboxView(R.id.brick_ble_proximity_checkbox);

        return view;
    }

    @Override
    public View getViewWithAlpha(int alphaValue) {
        if (view != null) {
            View layout = view.findViewById(R.id.brick_ble_proximity_layout);
            Drawable background = layout.getBackground();
            background.setAlpha(alphaValue);
            this.alphaValue = (alphaValue);
        }

        return view;
    }

    @Override
    public String brickTutorial(){
        return "Scans for BLE devices in a 2 metre range.\n";
    }

    @Override
    public View getPrototypeView(Context context) {
        View prototypeView = View.inflate(context, R.layout.brick_ble_proximity, null);
        return prototypeView;
    }

    @Override
    public int getRequiredResources()
    {
        return BLUETOOTH_BLE_PROXIMITY;
    }

    @Override
    public List<SequenceAction> addActionToSequence(Sprite s, SequenceAction sequence)
    {
        sequence.addAction(ExtendedActions.connectSensorTagAction());
        return null;
    }
}
