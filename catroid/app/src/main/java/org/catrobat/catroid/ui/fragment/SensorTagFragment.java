package org.catrobat.catroid.ui.fragment;

import android.app.Dialog;
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

import org.catrobat.catroid.R;

/**
 * Created by User HP on 8/2/2015.
 */
public class SensorTagFragment extends SherlockListFragment implements Dialog.OnKeyListener{

    public static final String SENSOR_TAG_FRAGMENT_TAG = "sensor_tag_fragment";
    private int number;

    private static int[] SensorTagElements = {
            R.string.sensor_temperature, R.string.sensor_accelerometer_abs, R.string.sensor_accelerometer_x,
            R.string.sensor_accelerometer_y, R.string.sensor_accelerometer_z, R.string.sensor_gyroscope_x,
            R.string.sensor_gyroscope_y, R.string.sensor_gyroscope_z, R.string.sensor_magnetometer_abs,
            R.string.sensor_magnetometer_x, R.string.sensor_magnetometer_y, R.string.sensor_magnetometer_z
    };
    private String [] items;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        number = getArguments().getInt("Tag number");
        getSherlockActivity().getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSherlockActivity().getSupportActionBar().setTitle("Sensor Tag " + Integer.toString(number));
        items = new String [SensorTagElements.length];
        for (int index = 0; index < items.length; index++) {
            items[index] = getString(SensorTagElements[index]);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_sensor_tag, container, false);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.fragment_formula_editor_list_item, items);
        setListAdapter(arrayAdapter);
        return fragmentView;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        FormulaEditorFragment formulaEditor = (FormulaEditorFragment) getSherlockActivity().getSupportFragmentManager()
                .findFragmentByTag(FormulaEditorFragment.FORMULA_EDITOR_FRAGMENT_TAG);
        if (formulaEditor != null) {
            formulaEditor.addResourceToActiveFormula(SensorTagElements[position], "SensorTag "+ Integer.toString(number)+"."+ getActivity().getBaseContext().getString(SensorTagElements[position]));
            formulaEditor.updateButtonViewOnKeyboard();
        }
        /*KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK);
        onKey(null, keyEvent.getKeyCode(), keyEvent);*/
        FragmentTransaction fragTransaction = getSherlockActivity().getSupportFragmentManager()
                .beginTransaction();
        fragTransaction.hide(this);
        fragTransaction.show(getSherlockActivity().getSupportFragmentManager().findFragmentByTag(
                FormulaEditorFragment.FORMULA_EDITOR_FRAGMENT_TAG));
        fragTransaction.commit();
    }

    @Override
    public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
        Log.d("dev", "Back pressed");
        if( keyCode == KeyEvent.KEYCODE_BACK )
        {
            Log.d("dev", "Back code entered");
            FragmentManager fragmentManager = getSherlockActivity().getSupportFragmentManager();
            Fragment fragment = fragmentManager.findFragmentByTag(FormulaEditorListFragment.SENSOR_TAG);

            if (fragment == null) {
                Log.d("dev", "fragment is null");
                fragment = new FormulaEditorListFragment();
                Bundle bundle = new Bundle();
                bundle.putString(FormulaEditorListFragment.ACTION_BAR_TITLE_BUNDLE_ARGUMENT,
                        getActivity().getString(R.string.formula_editor_sensors));
                bundle.putString(FormulaEditorListFragment.FRAGMENT_TAG_BUNDLE_ARGUMENT, FormulaEditorListFragment.SENSOR_TAG);
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().hide(this).add(R.id.script_fragment_container, fragment, FormulaEditorListFragment.SENSOR_TAG).commit();
            }else {
                Log.d("dev", "fragment is not null");
                ((FormulaEditorListFragment) fragment).showFragmentFromSensorFragment(getActivity());
            }
            return true;
        }
        return false;
    }

   /*@Override
    public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
        *//*boolean returnValue = false;
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                FragmentManager fragmentManager = ((SherlockFragmentActivity) getActivity()).getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentByTag(FormulaEditorListFragment.SENSOR_TAG);

                if (fragment == null) {
                    fragment = new FormulaEditorListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(FormulaEditorListFragment.ACTION_BAR_TITLE_BUNDLE_ARGUMENT,
                            getActivity().getString(R.string.formula_editor_sensors));
                    bundle.putString(FormulaEditorListFragment.FRAGMENT_TAG_BUNDLE_ARGUMENT, FormulaEditorListFragment.SENSOR_TAG);
                    fragment.setArguments(bundle);
                    fragmentManager.beginTransaction().add(R.id.script_fragment_container, fragment, FormulaEditorListFragment.SENSOR_TAG).commit();
                }
                ((FormulaEditorListFragment) fragment).showFragment(getActivity());
                returnValue = true;
                break;
        }
        return returnValue;*//*
       boolean returnValue = false;
       Log.d("dev", "Back pressed");
       switch (keyCode) {
           case KeyEvent.KEYCODE_BACK:
               Log.d("dev", "Back code entered");
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
    }*/
}
