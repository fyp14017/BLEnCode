/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2014 The Catrobat Team
 * (<http://developer.fyp14017.hku/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.fyp14017.hku/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.hku/licenses/>.
 */
package hku.fyp14017.blencode.content.bricks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import hku.fyp14017.blencode.R;
import hku.fyp14017.blencode.content.Script;
import hku.fyp14017.blencode.content.Sprite;
import hku.fyp14017.blencode.content.WhenScript;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hku.fyp14017.blencode.content.Script;

public class WhenBrick extends ScriptBrick {
    protected WhenScript whenScript;
    private static final long serialVersionUID = 1L;

    public static enum ScriptAction{
        TAPPED, SENSORTAG_BUTTON_PRESSED, BLE_PROXIMITY
    }
    public static enum Keys{
        LEFT_BUTTON, RIGHT_BUTTON
    }
    public static enum SensorTag{
        TAG1, TAG2, TAG3, TAG4, TAG5, TAG6, TAG7, TAG8, TAG9, TAG10
    }
    private transient ScriptAction actionEnum;
    private transient Keys keyEnum;
    private transient SensorTag tagEnum;
    private String mac;
    private String action;
    private String key;
    private String tag;
    private String[] arr = {"Tapped" , "SensorTag Button Pressed", "BLE Device in proximity"};
    private ArrayList<String> options = new ArrayList<String>(Arrays.asList(arr));
    private ArrayList<String> keys = new ArrayList<String>(Arrays.asList("Left Button","Right Button"));
    public WhenBrick(WhenScript whenScript) {
        this.whenScript = whenScript;
        String temp = whenScript.getAction();
        Log.d("dev", "temp starts with "+ temp);
        if(temp.startsWith("BLE_PROXIMITY")){
            this.action = temp.split(" ")[0];
            this.mac = temp.split(" ")[1];
            this.key = Keys.LEFT_BUTTON.name();
            this.tag = SensorTag.TAG1.name();
        }else if(temp.startsWith("SENSORTAG")){
            this.action = temp.split(" ")[0];
            this.tag = temp.split(" ")[1];
            this.key = temp.split(" ")[2];
        }else {
            this.action = temp;
            this.key = Keys.LEFT_BUTTON.name();
            this.tag = SensorTag.TAG1.name();
        }
        this.keyEnum = Keys.valueOf(key);
        this.tagEnum = SensorTag.valueOf(tag);
        this.actionEnum = ScriptAction.valueOf(action);
    }
    public WhenBrick(ScriptAction scriptAction, Keys keys1, SensorTag tag, String mac){
        this.mac = mac;
        this.actionEnum = scriptAction;
        this.action = scriptAction.name();
        this.keyEnum = keys1;
        this.key = keys1.name();
        this.tagEnum = tag;
        this.tag = tag.name();
    }
    public WhenBrick() {

    }

    protected Object readResolve(){
        if(action != null){
            actionEnum = ScriptAction.valueOf(action);
            Log.d("dev", "wtf " + action);
        }
        if(key!=null){
            keyEnum = Keys.valueOf(key);
        }
        if(tag!=null){
            tagEnum = SensorTag.valueOf(tag);
        }
        return this;
    }

    @Override
    public int getRequiredResources() {
        return NO_RESOURCES;
    }

    @Override
    public Brick copyBrickForSprite(Sprite sprite) {
        WhenBrick copyBrick = (WhenBrick) clone();
        copyBrick.whenScript = whenScript;
        return copyBrick;
    }

    @Override
    public View getView(final Context context, int brickId, final BaseAdapter baseAdapter) {
        if (animationState) {
            return view;
        }

        view = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_when, null);

        setCheckboxView(hku.fyp14017.blencode.R.id.brick_when_checkbox);

        //method moved to to DragAndDropListView since it is not working on 2.x
		/*
		 * checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		 * 
		 * @Override
		 * public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		 * 
		 * checked = isChecked;
		 * if (!checked) {
		 * for (Brick currentBrick : adapter.getCheckedBricks()) {
		 * currentBrick.setCheckedBoolean(false);
		 * }
		 * }
		 * adapter.handleCheck(brickInstance, checked);
		 * }
		 * });
		 */


        final Spinner spinner = (Spinner) view.findViewById(hku.fyp14017.blencode.R.id.brick_when_spinner);
        spinner.setFocusable(false);
        spinner.setClickable(true);
        ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<CharSequence>(context,
                android.R.layout.simple_spinner_item, (List)options);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        /*if(actionEnum == null){
            actionEnum = ScriptAction.valueOf(action);l
        }*/
        spinner.setSelection(actionEnum.ordinal());

        final Spinner tagSpinner = (Spinner) view.findViewById(hku.fyp14017.blencode.R.id.brick_when_spinner_tag);
        tagSpinner.setFocusable(false);
        tagSpinner.setClickable(true);
        ArrayAdapter<CharSequence> sensorTagAdapter = ArrayAdapter.createFromResource(context, hku.fyp14017.blencode.R.array.sensortag_chooser,
                android.R.layout.simple_spinner_item);
        sensorTagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagSpinner.setAdapter(sensorTagAdapter);
        tagSpinner.setSelection(tagEnum.ordinal());
        tagSpinner.setVisibility(View.INVISIBLE);

        final Spinner keySpinner = (Spinner) view.findViewById(hku.fyp14017.blencode.R.id.brick_when_spinner_key);
        keySpinner.setFocusable(false);
        keySpinner.setClickable(true);
        ArrayAdapter<CharSequence> keySpinnerAdapter = new ArrayAdapter<CharSequence>(context,
                android.R.layout.simple_spinner_item, (List)keys);
        keySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        keySpinner.setAdapter(keySpinnerAdapter);
        keySpinner.setSelection(keyEnum.ordinal());
        keySpinner.setVisibility(View.INVISIBLE);


        if(whenScript == null){
            whenScript = new WhenScript(this);
        }
        /*if (whenScript.getAction() != null) {
            spinner.setSelection(whenScript.getPosition(), true);
        }*/
        /*if(action!=null){
            spinner.setSelection(options.indexOf(action));
        }
        if(key!=null){
            keySpinner.setSelection(keys.indexOf(key));
        }*/



        /*String temp = options.get(spinner.getSelectedItemPosition());
        if(temp.startsWith("SensorTag")){
            whenScript.setAction(temp + " " + keys.get(keySpinner.getSelectedItemPosition()));
        }else{
            whenScript.setAction(temp);
        }*/
        final TextView textView = (TextView)view.findViewById(hku.fyp14017.blencode.R.id.MAC_label);
        textView.setVisibility(View.GONE);

        final TextView textView1 = (TextView)view.findViewById(hku.fyp14017.blencode.R.id.MAC_value);
        textView1.setText(this.mac);
        textView1.setVisibility(View.GONE);

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder MACbuilder = new AlertDialog.Builder(context);
                LayoutInflater li = LayoutInflater.from(context);
                View proximityView = li.inflate(hku.fyp14017.blencode.R.layout.proximity_sensor_2, null);
                final EditText Mac = (EditText) proximityView.findViewById(hku.fyp14017.blencode.R.id.editText2);
                Mac.setText(mac);
                MACbuilder.setTitle("MAC Address")
                        .setView(proximityView)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mac = Mac.getText().toString();
                                textView1.setText(mac);
                                whenScript.setAction(action+" "+mac);
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelected(true);
                spinner.setSelection(position);
                actionEnum = ScriptAction.values()[position];
                action = actionEnum.name();
                //action = options.get(position);
                whenScript.setAction(action);
                if(action.startsWith("BLE_PROXIMITY")){
                    textView.setVisibility(View.VISIBLE);
                    textView1.setVisibility(View.VISIBLE);
                    textView1.setClickable(true);
                    tagSpinner.setVisibility(View.GONE);
                    whenScript.setAction(action + " " + mac);

                }else if(action.startsWith("SENSORTAG")){
                    tagSpinner.setVisibility(View.VISIBLE);
                    keySpinner.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    textView1.setVisibility(View.GONE);
                    whenScript.setAction(action+ " " + tag +" " + key);
                }else{
                    tagSpinner.setVisibility(View.INVISIBLE);
                    keySpinner.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.GONE);
                    textView1.setVisibility(View.GONE);
                    whenScript.setAction(action);
                }
                //adapter.notifyDataSetChanged();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        tagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tagSpinner.setSelected(true);
                tagSpinner.setSelection(i);
                tagEnum = SensorTag.values()[i];
                tag = tagEnum.name();
                if(action.startsWith("SENSORTAG")) {
                    whenScript.setAction(action + " " + tag + " " + key);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        keySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                keySpinner.setSelected(true);
                keySpinner.setSelection(i);
                keyEnum = Keys.values()[i];
                key = keyEnum.name();
                if(action.startsWith("SENSORTAG")) {
                    whenScript.setAction(action + " " + tag + " " + key);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    @Override
    public String brickTutorial(){
        return "Allows user to set controls on what the app should do if the user performs an action like tap or " +
                "press the buttons on SensorTag devices ";
    }

    @Override
    public View getViewWithAlpha(int alphaValue) {

        if (view != null) {

            View layout = view.findViewById(hku.fyp14017.blencode.R.id.brick_when_layout);
            Spinner spinner = (Spinner) view.findViewById(hku.fyp14017.blencode.R.id.brick_when_spinner);
            Drawable background = layout.getBackground();
            background.setAlpha(alphaValue);
            spinner.getBackground().setAlpha(alphaValue);
            this.alphaValue = (alphaValue);

        }

        return view;
    }

    @Override
    public View getPrototypeView(Context context) {
        View v = View.inflate(context, hku.fyp14017.blencode.R.layout.brick_when, null);
        Spinner spinner = (Spinner) v.findViewById(hku.fyp14017.blencode.R.id.brick_when_spinner);
        spinner.setFocusable(false);
        spinner.setFocusableInTouchMode(false);
        ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<CharSequence>(context,
                android.R.layout.simple_spinner_item, (List)options);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(actionEnum.ordinal());

        Spinner tagSpinner = (Spinner) v.findViewById(hku.fyp14017.blencode.R.id.brick_when_spinner_tag);
        tagSpinner.setFocusable(false);
        tagSpinner.setFocusableInTouchMode(false);
        tagSpinner.setSelection(tagEnum.ordinal());
        tagSpinner.setVisibility(View.INVISIBLE);

        TextView textView = (TextView) v.findViewById(hku.fyp14017.blencode.R.id.MAC_label);
        textView.setFocusable(false);
        textView.setFocusableInTouchMode(false);
        textView.setVisibility(View.GONE);

        TextView textView1 = (TextView) v.findViewById(hku.fyp14017.blencode.R.id.MAC_value);
        textView1.setFocusable(false);
        textView1.setFocusableInTouchMode(false);
        textView1.setVisibility(View.GONE);

        Spinner keySpinner = (Spinner) v.findViewById(hku.fyp14017.blencode.R.id.brick_when_spinner_key);
        keySpinner.setFocusable(false);
        keySpinner.setFocusableInTouchMode(false);
        keySpinner.setSelection(keyEnum.ordinal());
        keySpinner.setVisibility(View.INVISIBLE);
        return v;
    }

    @Override
    public Brick clone() {
        return new WhenBrick(actionEnum, keyEnum, tagEnum, mac);
    }

    @Override
    public Script getScriptSafe() {
        if (whenScript == null) {
            whenScript = new WhenScript();
        }
        if(action !=null){
            if(key !=null) {
                if (tag != null) {
                    if(action.startsWith("SENSORTAG")) {
                        whenScript.setAction(action + " " + tag + " " + key);
                    }else if(action.startsWith("BLE_PROXIMITY")) {
                        if (mac != null) {
                            whenScript.setAction(action + " " + mac);
                        }
                    }
                }else{
                    whenScript.setAction(action);
                }
            }
        }
        return whenScript;
    }

    @Override
    public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
        return null;
    }
}
