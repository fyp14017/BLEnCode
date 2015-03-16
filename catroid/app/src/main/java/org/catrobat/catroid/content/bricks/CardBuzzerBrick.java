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

public class CardBuzzerBrick extends BrickBaseType implements OnItemSelectedListener
{
    private static final long serialVersionUID = 1L;

    //public static enum frequencyDivider, dutyCycleDivider, timeLast;

    public CardBuzzerBrick(Sprite sprite)
    {
        this.sprite = sprite;
    }

    @Override
    public Brick clone() {
        return new CardBuzzerBrick(getSprite());
    }
    @Override
    public Brick copyBrickForSprite(Sprite sprite) {
        CardBuzzerBrick copyBrick = (CardBuzzerBrick) clone();
        copyBrick.sprite = sprite;
        return copyBrick;
    }

    @Override
    public View getView(Context context, int brickId, final BaseAdapter baseAdapter)
    {
        if (animationState) return view;

        view = View.inflate(context, R.layout.brick_ble_card_buzzer, null);
        setCheckboxView(R.id.brick_ble_cardbuzzer_checkbox);

        return view;
    }

    @Override
    public View getViewWithAlpha(int alphaValue) {
        if (view != null) {
            View layout = view.findViewById(R.id.brick_ble_cardbuzzer_layout);
            Drawable background = layout.getBackground();
            background.setAlpha(alphaValue);
            this.alphaValue = (alphaValue);
        }

        return view;
    }

    @Override
    public String brickTutorial(){
        return "Activates the buzzer on the MIDBot eCard device.\n";
    }

    @Override
    public View getPrototypeView(Context context) {
        View prototypeView = View.inflate(context, R.layout.brick_ble_card_buzzer, null);
        return prototypeView;
    }

    @Override
    public int getRequiredResources()
    {
        return BLUETOOTH_BLE_SENSORS;
    }

    @Override
    public List<SequenceAction> addActionToSequence(Sprite s, SequenceAction sequence)
    {
        sequence.addAction(ExtendedActions.cardBuzzerAction());
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
