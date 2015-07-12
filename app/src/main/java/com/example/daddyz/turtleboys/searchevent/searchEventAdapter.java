package com.example.daddyz.turtleboys.searchevent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.daddyz.turtleboys.R;
import com.example.daddyz.turtleboys.eventfeed.gEventObject;

import java.util.ArrayList;

/**
 * Created by Jinbir on 7/12/2015.
 */
public class searchEventAdapter extends ArrayAdapter<gEventObject> {
    private Context context;
    private int resource;
    private ArrayList<gEventObject> searchEventObjects;

    public searchEventAdapter(Context context, int resource, ArrayList<gEventObject> searchEventObjects) {
        super(context, resource,searchEventObjects);
        this.context = context;
        this.resource = resource;
        this.searchEventObjects = searchEventObjects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.eventfeedroweven, parent, false);

        ImageView eventImage = (ImageView) rowView.findViewById(R.id.icon);
        TextView description = (TextView) rowView.findViewById(R.id.descLine);
        TextView citystate = (TextView) rowView.findViewById(R.id.citystateLine);
        TextView date = (TextView)rowView.findViewById(R.id.dateLine);
        TextView time = (TextView) rowView.findViewById(R.id.timeLine);
        TextView venue = (TextView) rowView.findViewById(R.id.venueLine);

        description.setText("The description of the post will go here");


        return rowView;
    }
}