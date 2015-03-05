/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2014 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.content.bricks;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.WhenScript;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WhenBrick extends ScriptBrick {
    protected WhenScript whenScript;
    private static final long serialVersionUID = 1L;

    public static enum ScriptAction{
        TAPPED, SENSORTAG_BUTTON_PRESSED, CARD_BUTTON_PRESSED
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
    private String action;
    private String key;
    private String tag;
    private String[] arr = {"Tapped" , "SensorTag Button Pressed", "Card Button Pressed"};
    private ArrayList<String> options = new ArrayList<String>(Arrays.asList(arr));
    private ArrayList<String> keys = new ArrayList<String>(Arrays.asList("Left Button","Right Button"));
    public WhenBrick(WhenScript whenScript) {
        this.whenScript = whenScript;
        String temp = whenScript.getAction();
        if(temp.startsWith("SENSORTAG")){
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
    public WhenBrick(ScriptAction scriptAction, Keys keys1, SensorTag tag){

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

        view = View.inflate(context, R.layout.brick_when, null);

        setCheckboxView(R.id.brick_when_checkbox);

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


        final Spinner spinner = (Spinner) view.findViewById(R.id.brick_when_spinner);
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

        final Spinner tagSpinner = (Spinner) view.findViewById(R.id.brick_when_spinner_tag);
        tagSpinner.setFocusable(false);
        tagSpinner.setClickable(true);
        ArrayAdapter<CharSequence> sensorTagAdapter = ArrayAdapter.createFromResource(context, R.array.sensortag_chooser,
                android.R.layout.simple_spinner_item);
        sensorTagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagSpinner.setAdapter(sensorTagAdapter);
        tagSpinner.setSelection(tagEnum.ordinal());
        tagSpinner.setVisibility(View.GONE);

        final Spinner keySpinner = (Spinner) view.findViewById(R.id.brick_when_spinner_key);
        keySpinner.setFocusable(false);
        keySpinner.setClickable(true);
        ArrayAdapter<CharSequence> keySpinnerAdapter = new ArrayAdapter<CharSequence>(context,
                android.R.layout.simple_spinner_item, (List)keys);
        keySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        keySpinner.setAdapter(keySpinnerAdapter);
        keySpinner.setSelection(keyEnum.ordinal());
        keySpinner.setVisibility(View.GONE);


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


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelected(true);
                spinner.setSelection(position);
                actionEnum = ScriptAction.values()[position];
                action = actionEnum.name();
                //action = options.get(position);
                whenScript.setAction(action);
                if(action.startsWith("SENSORTAG")){
                    tagSpinner.setVisibility(View.VISIBLE);
                    keySpinner.setVisibility(View.VISIBLE);
                    whenScript.setAction(action+ " " + tag +" " + key);
                }else{
                    tagSpinner.setVisibility(View.GONE);
                    keySpinner.setVisibility(View.GONE);
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
                whenScript.setAction(action + " " +tag+" "+key);
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
                whenScript.setAction(action + " " +tag+" "+key);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    @Override
    public View getViewWithAlpha(int alphaValue) {

        if (view != null) {

            View layout = view.findViewById(R.id.brick_when_layout);
            Spinner spinner = (Spinner) view.findViewById(R.id.brick_when_spinner);
            Drawable background = layout.getBackground();
            background.setAlpha(alphaValue);
            spinner.getBackground().setAlpha(alphaValue);
            this.alphaValue = (alphaValue);

        }

        return view;
    }

    @Override
    public View getPrototypeView(Context context) {
        View v = View.inflate(context, R.layout.brick_when, null);
        Spinner spinner = (Spinner) v.findViewById(R.id.brick_when_spinner);
        spinner.setFocusable(false);
        spinner.setFocusableInTouchMode(false);
        ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<CharSequence>(context,
                android.R.layout.simple_spinner_item, (List)options);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(actionEnum.ordinal());

        Spinner tagSpinner = (Spinner) v.findViewById(R.id.brick_when_spinner_tag);
        tagSpinner.setFocusable(false);
        tagSpinner.setFocusableInTouchMode(false);
        tagSpinner.setSelection(tagEnum.ordinal());
        tagSpinner.setVisibility(View.GONE);

        Spinner keySpinner = (Spinner) v.findViewById(R.id.brick_when_spinner_key);
        keySpinner.setFocusable(false);
        keySpinner.setFocusableInTouchMode(false);
        keySpinner.setSelection(keyEnum.ordinal());
        keySpinner.setVisibility(View.GONE);
        return v;
    }

    @Override
    public Brick clone() {
        return new WhenBrick(actionEnum, keyEnum, tagEnum);
    }

    @Override
    public Script getScriptSafe() {
        if (whenScript == null) {
            whenScript = new WhenScript();
        }
        if(action !=null){
            if(key !=null) {
                if (tag != null) {
                    whenScript.setAction(action + " "+tag+" " + key);
                } else {
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
