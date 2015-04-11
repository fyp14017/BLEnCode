package hku.fyp14017.blencode.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import hku.fyp14017.blencode.R;

import java.util.ArrayList;

public class ProximitySensorActivity extends Activity {

    ArrayList<String> sensors = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(hku.fyp14017.blencode.R.layout.activity_proximity_sensor);

        sensors.clear();

        //TODO: parse query

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1, sensors){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                String[] temp = sensors.get(position).split("oCISxD");
                text1.setText(temp[0]);
                text2.setText(temp[1]);
                return view;
            }
        };

        ListView sensorList = (ListView) findViewById(hku.fyp14017.blencode.R.id.sensorView);
        sensorList.setAdapter(adapter);

        Button addSensor = (Button) findViewById(hku.fyp14017.blencode.R.id.button2);
        addSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder sensorAddDialog = new AlertDialog.Builder(ProximitySensorActivity.this);
                LayoutInflater li = LayoutInflater.from(ProximitySensorActivity.this);
                View proximityView = li.inflate(hku.fyp14017.blencode.R.layout.proximity_sensor, null);
                final EditText name = (EditText) proximityView.findViewById(hku.fyp14017.blencode.R.id.editText);
                final EditText mac = (EditText) proximityView.findViewById(hku.fyp14017.blencode.R.id.editText2);
                sensorAddDialog.setView(proximityView)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sensors.add(name.getText().toString()+"oCISxD"+mac.getText().toString());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }).create().show();
            }
        });
    }

}
