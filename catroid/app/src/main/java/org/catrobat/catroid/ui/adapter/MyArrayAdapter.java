package org.catrobat.catroid.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.catrobat.catroid.R;

import java.util.List;

/**
 * Created by User HP on 6/2/2015.
 */
public class MyArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> data;
    private final List<Bitmap> screenshots;
    public MyArrayAdapter(Context context, List<String> objects, List<Bitmap> images) {
        super(context, R.layout.list, objects);
        this.context = context;
        this.data = objects;
        this.screenshots = images;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list, parent, false);
        TextView projectName = (TextView) rowView.findViewById(R.id.project_user);
        TextView description = (TextView) rowView.findViewById(R.id.project_description);
        ImageView screenshot = (ImageView) rowView.findViewById(R.id.screenshot);
        screenshot.setImageBitmap(screenshots.get(position));

        String[] temp = data.get(position).split("oCISxD");
        projectName.setText(temp[0]);
        projectName.setFocusable(false);
        description.setText(temp[1]);
        description.setFocusable(false);
        return rowView;
    }
}
