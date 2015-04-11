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
package hku.fyp14017.blencode.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;

import hku.fyp14017.blencode.R;

public class FormulaEditorListFragment extends SherlockListFragment implements Dialog.OnKeyListener {

	public static final String OBJECT_TAG = "objectFragment";
	public static final String FUNCTION_TAG = "functionFragment";
	public static final String LOGIC_TAG = "logicFragment";
	public static final String SENSOR_TAG = "sensorFragment";

	public static final String ACTION_BAR_TITLE_BUNDLE_ARGUMENT = "actionBarTitle";
	public static final String FRAGMENT_TAG_BUNDLE_ARGUMENT = "fragmentTag";

	public static final String[] TAGS = { OBJECT_TAG, FUNCTION_TAG, LOGIC_TAG, SENSOR_TAG };

    public static int numOfSensorTags=10;

	private static final int[] OBJECT_ITEMS = { hku.fyp14017.blencode.R.string.formula_editor_object_x, hku.fyp14017.blencode.R.string.formula_editor_object_y,
			hku.fyp14017.blencode.R.string.formula_editor_object_ghosteffect, hku.fyp14017.blencode.R.string.formula_editor_object_brightness,
			hku.fyp14017.blencode.R.string.formula_editor_object_size, hku.fyp14017.blencode.R.string.formula_editor_object_rotation,
			hku.fyp14017.blencode.R.string.formula_editor_object_layer };

	private static final int[] LOGIC_ITEMS = { hku.fyp14017.blencode.R.string.formula_editor_logic_equal,
			hku.fyp14017.blencode.R.string.formula_editor_logic_notequal, hku.fyp14017.blencode.R.string.formula_editor_logic_lesserthan,
			hku.fyp14017.blencode.R.string.formula_editor_logic_leserequal, hku.fyp14017.blencode.R.string.formula_editor_logic_greaterthan,
			hku.fyp14017.blencode.R.string.formula_editor_logic_greaterequal, hku.fyp14017.blencode.R.string.formula_editor_logic_and,
			hku.fyp14017.blencode.R.string.formula_editor_logic_or, hku.fyp14017.blencode.R.string.formula_editor_logic_not, hku.fyp14017.blencode.R.string.formula_editor_function_true,
			hku.fyp14017.blencode.R.string.formula_editor_function_false };

	private static final int[] FUNCTIONS_ITEMS = { hku.fyp14017.blencode.R.string.formula_editor_function_sin,
			hku.fyp14017.blencode.R.string.formula_editor_function_cos, hku.fyp14017.blencode.R.string.formula_editor_function_tan,
			hku.fyp14017.blencode.R.string.formula_editor_function_ln, hku.fyp14017.blencode.R.string.formula_editor_function_log,
			hku.fyp14017.blencode.R.string.formula_editor_function_pi, hku.fyp14017.blencode.R.string.formula_editor_function_sqrt,
			hku.fyp14017.blencode.R.string.formula_editor_function_rand, hku.fyp14017.blencode.R.string.formula_editor_function_abs,
			hku.fyp14017.blencode.R.string.formula_editor_function_round, hku.fyp14017.blencode.R.string.formula_editor_function_mod,
			hku.fyp14017.blencode.R.string.formula_editor_function_arcsin, hku.fyp14017.blencode.R.string.formula_editor_function_arccos,
			hku.fyp14017.blencode.R.string.formula_editor_function_arctan, hku.fyp14017.blencode.R.string.formula_editor_function_exp,
			hku.fyp14017.blencode.R.string.formula_editor_function_max, hku.fyp14017.blencode.R.string.formula_editor_function_min,
			hku.fyp14017.blencode.R.string.formula_editor_function_length, hku.fyp14017.blencode.R.string.formula_editor_function_letter,
			hku.fyp14017.blencode.R.string.formula_editor_function_join };

	private static final int[] FUNCTIONS_PARAMETERS = { hku.fyp14017.blencode.R.string.formula_editor_function_sin_parameter,
			hku.fyp14017.blencode.R.string.formula_editor_function_cos_parameter, hku.fyp14017.blencode.R.string.formula_editor_function_tan_parameter,
			hku.fyp14017.blencode.R.string.formula_editor_function_ln_parameter, hku.fyp14017.blencode.R.string.formula_editor_function_log_parameter,
			hku.fyp14017.blencode.R.string.formula_editor_function_pi_parameter, hku.fyp14017.blencode.R.string.formula_editor_function_sqrt_parameter,
			hku.fyp14017.blencode.R.string.formula_editor_function_rand_parameter, hku.fyp14017.blencode.R.string.formula_editor_function_abs_parameter,
			hku.fyp14017.blencode.R.string.formula_editor_function_round_parameter, hku.fyp14017.blencode.R.string.formula_editor_function_mod_parameter,
			hku.fyp14017.blencode.R.string.formula_editor_function_arcsin_parameter, hku.fyp14017.blencode.R.string.formula_editor_function_arccos_parameter,
			hku.fyp14017.blencode.R.string.formula_editor_function_arctan_parameter, hku.fyp14017.blencode.R.string.formula_editor_function_exp_parameter,
			hku.fyp14017.blencode.R.string.formula_editor_function_max_parameter, hku.fyp14017.blencode.R.string.formula_editor_function_min_parameter,
			hku.fyp14017.blencode.R.string.formula_editor_function_length_parameter, hku.fyp14017.blencode.R.string.formula_editor_function_letter_parameter,
			hku.fyp14017.blencode.R.string.formula_editor_function_join_parameter };

	private static int[] SENSOR_ITEMS = { hku.fyp14017.blencode.R.string.formula_editor_sensor_x_acceleration,
			hku.fyp14017.blencode.R.string.formula_editor_sensor_y_acceleration, hku.fyp14017.blencode.R.string.formula_editor_sensor_z_acceleration,
			hku.fyp14017.blencode.R.string.formula_editor_sensor_compass_direction, hku.fyp14017.blencode.R.string.formula_editor_sensor_x_inclination,
			hku.fyp14017.blencode.R.string.formula_editor_sensor_y_inclination, hku.fyp14017.blencode.R.string.formula_editor_sensor_loudness,
			hku.fyp14017.blencode.R.string.formula_editor_sensor_face_detected, hku.fyp14017.blencode.R.string.formula_editor_sensor_face_size,
			hku.fyp14017.blencode.R.string.formula_editor_sensor_face_x_position, hku.fyp14017.blencode.R.string.formula_editor_sensor_face_y_position,
    };



	private String tag;
	private String[] items;
	private String actionBarTitle;
	private int[] itemsIds;

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
        if (position >= itemsIds.length){
            SensorTagFragment sensorTagFragment;
            FragmentManager fm = getSherlockActivity().getSupportFragmentManager();
            sensorTagFragment=(SensorTagFragment)fm.findFragmentByTag(SensorTagFragment.SENSOR_TAG_FRAGMENT_TAG);
            if(sensorTagFragment == null){
                sensorTagFragment = new SensorTagFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("Tag number", position - (itemsIds.length-1));
                sensorTagFragment.setArguments(bundle);
                FragmentTransaction ft=fm.beginTransaction();
                ft.add(hku.fyp14017.blencode.R.id.script_fragment_container, sensorTagFragment,SensorTagFragment.SENSOR_TAG_FRAGMENT_TAG);
                ft.hide(this);
                ft.commit();
            }else{
                /*Bundle bundle = new Bundle();
                bundle.putInt("Tag number", position - (itemsIds.length-1));
                sensorTagFragment.setArguments(bundle);*/
                FragmentTransaction ft=fm.beginTransaction();
                ft.hide(this).show(sensorTagFragment).commit();
            }
        }else {
            FormulaEditorFragment formulaEditor = (FormulaEditorFragment) getSherlockActivity().getSupportFragmentManager()
                    .findFragmentByTag(FormulaEditorFragment.FORMULA_EDITOR_FRAGMENT_TAG);
            if (formulaEditor != null) {
                formulaEditor.addResourceToActiveFormula(itemsIds[position]);
                formulaEditor.updateButtonViewOnKeyboard();
                KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
                onKey(null, keyEvent.getKeyCode(), keyEvent);
            }
        }

	}

	public FormulaEditorListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		this.actionBarTitle = getArguments().getString(ACTION_BAR_TITLE_BUNDLE_ARGUMENT);
		this.tag = getArguments().getString(FRAGMENT_TAG_BUNDLE_ARGUMENT);

		itemsIds = new int[] {};

		if (tag == OBJECT_TAG) {
			itemsIds = OBJECT_ITEMS;
		} else if (tag == FUNCTION_TAG) {
			itemsIds = FUNCTIONS_ITEMS;
		} else if (tag == LOGIC_TAG) {
			itemsIds = LOGIC_ITEMS;
		} else if (tag == SENSOR_TAG) {
			itemsIds = SENSOR_ITEMS;
		}

        if(tag == SENSOR_TAG){
            items = new String[itemsIds.length+numOfSensorTags];
            int index=0;
            for (index = 0; index < items.length - numOfSensorTags; index++) {
                items[index] = tag == FUNCTION_TAG ? getString(itemsIds[index]) + getString(FUNCTIONS_PARAMETERS[index])
                        : getString(itemsIds[index]);
            }
            int sensorTagCounter=1;
            for(int i=index; i<items.length;i++){
                items[i] = "Sensor Tag "+ Integer.valueOf(sensorTagCounter);
                sensorTagCounter++;
            }
        }else{
            items = new String[itemsIds.length];
            for (int index = 0; index < items.length; index++) {
                items[index] = tag == FUNCTION_TAG ? getString(itemsIds[index]) + getString(FUNCTIONS_PARAMETERS[index])
                        : getString(itemsIds[index]);
            }
        }

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
				hku.fyp14017.blencode.R.layout.fragment_formula_editor_list_item, items);
		setListAdapter(arrayAdapter);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		for (int index = 0; index < menu.size(); index++) {
			menu.getItem(index).setVisible(false);
		}

		getSherlockActivity().getSupportActionBar().setDisplayShowTitleEnabled(true);
		getSherlockActivity().getSupportActionBar().setTitle(actionBarTitle);
		getSherlockActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);

		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(hku.fyp14017.blencode.R.layout.fragment_formula_editor_list, container, false);
		return fragmentView;
	}

	public void showFragment(Context context) {
		SherlockFragmentActivity activity = (SherlockFragmentActivity) context;
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction fragTransaction = fragmentManager.beginTransaction();
		Fragment formulaEditorFragment = fragmentManager
				.findFragmentByTag(FormulaEditorFragment.FORMULA_EDITOR_FRAGMENT_TAG);
		fragTransaction.hide(formulaEditorFragment);

		fragTransaction.show(this);
		fragTransaction.commit();
	}

    public void showFragmentFromSensorFragment(Context context) {
        SherlockFragmentActivity activity = (SherlockFragmentActivity) context;
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragTransaction = fragmentManager.beginTransaction();
        Fragment sensorFragment = fragmentManager
                .findFragmentByTag(SensorTagFragment.SENSOR_TAG_FRAGMENT_TAG);

        fragTransaction.hide(sensorFragment);

        fragTransaction.show(this);
        fragTransaction.commit();
    }

	@Override
	public boolean onKey(DialogInterface d, int keyCode, KeyEvent event) {
		Log.i("info", "onKey() in FE-ListFragment! keyCode: " + keyCode);
		boolean returnValue = false;
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				Log.i("info", "KEYCODE_BACK pressed in FE-ListFragment!");
				FragmentTransaction fragTransaction = getSherlockActivity().getSupportFragmentManager()
						.beginTransaction();
				fragTransaction.hide(this);
				fragTransaction.show(getSherlockActivity().getSupportFragmentManager().findFragmentByTag(
						FormulaEditorFragment.FORMULA_EDITOR_FRAGMENT_TAG));
				fragTransaction.commit();
				returnValue = true;
				break;
		}
		return returnValue;
	}

}
