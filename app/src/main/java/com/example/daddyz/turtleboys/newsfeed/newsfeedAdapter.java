package com.example.daddyz.turtleboys.newsfeed;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.daddyz.turtleboys.R;

/**
 * Created by snow on 6/17/2015.
 */
public class newsfeedAdapter extends ArrayAdapter<newsfeedObject> {
    private Context context;
    private int resource;
    private newsfeedObject[] objects;

    public newsfeedAdapter(Context context, int resource, newsfeedObject[] objects) {
        super(context, resource,objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=
                ((Activity) context).getLayoutInflater();
        View row=inflater.inflate(resource, parent, false);

        TextView title= (TextView)
                row.findViewById(R.id.secondLine);
        TextView date =(TextView)row.findViewById(R.id.date);
        TextView number=(TextView)
                row.findViewById(R.id.firstLine);
        title.setText((CharSequence)
                objects[position].description2);
        number.setText(objects[position].description);
        date.setText(objects[position].date);

        return row;
    }
}
