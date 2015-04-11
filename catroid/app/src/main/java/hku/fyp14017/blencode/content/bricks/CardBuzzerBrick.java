package hku.fyp14017.blencode.content.bricks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.ble.BLECard;
import hku.fyp14017.blencode.content.Sprite;
import hku.fyp14017.blencode.content.actions.ExtendedActions;

import java.util.List;

import hku.fyp14017.blencode.ble.BLECard;

public class CardBuzzerBrick extends BrickBaseType implements OnItemSelectedListener
{
    private static final long serialVersionUID = 1L;
    private int timeLast;
    private String card;
    private BLECard cardEnum;


    public CardBuzzerBrick(Sprite sprite, int timeLast, BLECard cardEnum)
    {
        this.cardEnum = cardEnum;
        this.card = cardEnum.name();
        this.sprite = sprite;
        this.timeLast = timeLast;
    }

    protected Object readResolve(){
        if(card!=null){
            cardEnum = BLECard.valueOf(card);
        }
        return this;
    }

    @Override
    public Brick clone() {
        return new CardBuzzerBrick(getSprite(), timeLast, cardEnum);
    }
    @Override
    public Brick copyBrickForSprite(Sprite sprite) {
        CardBuzzerBrick copyBrick = (CardBuzzerBrick) clone();
        copyBrick.sprite = sprite;
        return copyBrick;
    }

    @Override
    public View getView(final Context context, int brickId, final BaseAdapter baseAdapter)
    {
        if (animationState) return view;

        view = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_ble_card_buzzer, null);
        setCheckboxView(hku.fyp14017.blencode.R.id.brick_ble_cardbuzzer_checkbox);

        ArrayAdapter<CharSequence> cardAdapter = ArrayAdapter.createFromResource(context, hku.fyp14017.blencode.R.array.card_chooser,
                android.R.layout.simple_spinner_item);
        cardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner cardSpinner = (Spinner) view.findViewById(hku.fyp14017.blencode.R.id.card_buzzer_spinner);
        cardSpinner.setOnItemSelectedListener(this);
        cardSpinner.setFocusable(true);
        cardSpinner.setClickable(true);
        cardSpinner.setAdapter(cardAdapter);
        cardSpinner.setSelection(cardEnum.ordinal());


        final TextView time = (TextView) view.findViewById(hku.fyp14017.blencode.R.id.buzzer_time_last);
        time.setClickable(true);
        time.setFocusable(true);
        time.setText(Integer.toString(timeLast));
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adb = new AlertDialog.Builder(context);
                LayoutInflater li = LayoutInflater.from(context);
                View buzzerView = li.inflate(hku.fyp14017.blencode.R.layout.buzzer_time, null);
                final EditText input = (EditText) buzzerView.findViewById(hku.fyp14017.blencode.R.id.editText);
                input.setText(Integer.toString(timeLast));
                adb.setTitle("Enter buzzer duration")
                        .setView(buzzerView)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(Integer.parseInt(input.getText().toString()) > 127){
                                    Toast.makeText(context,"Time can only be less than 128",Toast.LENGTH_SHORT).show();
                                }else {
                                    timeLast = Integer.parseInt(input.getText().toString());
                                    time.setText(Integer.toString(timeLast));
                                    dialogInterface.dismiss();
                                }
                            }
                        }).create().show();
            }
        });
        return view;
    }

    @Override
    public View getViewWithAlpha(int alphaValue) {
        if (view != null) {
            View layout = view.findViewById(hku.fyp14017.blencode.R.id.brick_ble_cardbuzzer_layout);
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
        View prototypeView = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_ble_card_buzzer, null);
        TextView time = (TextView) prototypeView.findViewById(hku.fyp14017.blencode.R.id.buzzer_time_last);
        time.setClickable(false);
        time.setFocusable(false);
        time.setFocusableInTouchMode(false);
        Spinner cardSpinner = (Spinner) prototypeView.findViewById(hku.fyp14017.blencode.R.id.card_buzzer_spinner);
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
        return BLUETOOTH_BLE_SENSORS;
    }

    @Override
    public List<SequenceAction> addActionToSequence(Sprite s, SequenceAction sequence)
    {
        sequence.addAction(ExtendedActions.cardBuzzerAction(timeLast, cardEnum));
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
