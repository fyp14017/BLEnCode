package hku.fyp14017.blencode.content.bricks;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.ble.BLECard;
import hku.fyp14017.blencode.content.Sprite;
import hku.fyp14017.blencode.content.actions.ExtendedActions;
import hku.fyp14017.blencode.stage.PreStageActivity;

import java.util.List;
import java.util.UUID;

import hku.fyp14017.blencode.ble.BLECard;
import hku.fyp14017.blencode.stage.PreStageActivity;

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

    private String card;
    private BLECard cardEnum;

    public ConnectCardBrick(Sprite sprite, BLECard cardEnum)
    {
        this.cardEnum = cardEnum;
        this.card = cardEnum.name();
        this.sprite = sprite;
    }

    protected Object readResolve(){
        if(card!=null){
            cardEnum = BLECard.valueOf(card);
        }
        return this;
    }

    @Override
    public Brick clone() {
        return new ConnectCardBrick(getSprite(), cardEnum);
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

        view = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_ble_connect_card, null);
        setCheckboxView(hku.fyp14017.blencode.R.id.brick_ble_connect_card_checkbox);
        ArrayAdapter<CharSequence> cardAdapter = ArrayAdapter.createFromResource(context, hku.fyp14017.blencode.R.array.card_chooser,
                android.R.layout.simple_spinner_item);
        cardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner cardSpinner = (Spinner) view.findViewById(hku.fyp14017.blencode.R.id.connect_card_spinner);
        cardSpinner.setFocusable(true);
        cardSpinner.setClickable(true);
        cardSpinner.setOnItemSelectedListener(this);

        cardSpinner.setAdapter(cardAdapter);
        cardSpinner.setSelection(cardEnum.ordinal());
        return view;
    }

    @Override
    public View getViewWithAlpha(int alphaValue) {
        if (view != null) {
            View layout = view.findViewById(hku.fyp14017.blencode.R.id.brick_ble_connect_card_layout);
            Drawable background = layout.getBackground();
            background.setAlpha(alphaValue);
            this.alphaValue = (alphaValue);
        }

        return view;
    }

    @Override
    public View getPrototypeView(Context context) {
        View prototypeView = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_ble_connect_card, null);
        Spinner cardSpinner = (Spinner) prototypeView.findViewById(hku.fyp14017.blencode.R.id.connect_card_spinner);
        cardSpinner.setFocusableInTouchMode(false);
        cardSpinner.setFocusable(false);
        ArrayAdapter<CharSequence> cardAdapter = ArrayAdapter.createFromResource(context, hku.fyp14017.blencode.R.array.card_chooser,
                android.R.layout.simple_spinner_item);
        cardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cardSpinner.setAdapter(cardAdapter);
        cardSpinner.setSelection(cardEnum.ordinal());
        return prototypeView;
    }

    @Override
    public int getRequiredResources()
    {
        PreStageActivity.CardCounter++;
        return BLUETOOTH_BLE_SENSORS;
    }

    @Override
    public String brickTutorial(){
        return "Allows apps to connect to the MIDBot eCard and discover all services on the device.\n\n"
                + "Uses Bluetooth 4.0 and Bluetooth Low-Energy frameworks to connect to MIDBot eCard devices.";
    }

    @Override
    public List<SequenceAction> addActionToSequence(Sprite s, SequenceAction sequence)
    {
        sequence.addAction(ExtendedActions.connectCardAction());
        return null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        cardEnum = BLECard.values()[position];
        card = cardEnum.name();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Nothing
    }
}
