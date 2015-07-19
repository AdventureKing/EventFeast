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
public class searchResultsAdapter extends ArrayAdapter<searchResultsObject> {

    private Context context;
    private int resource;
    private ArrayList<searchResultsObject> searchResultsObjects;

    public searchResultsAdapter(Context context, int resource, ArrayList<searchResultsObject> searchResultsObjects) {
        super(context, resource,searchResultsObjects);
        this.context = context;
        this.resource = resource;
        this.searchResultsObjects = searchResultsObjects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.searchresultsrow, parent, false);
        //TextView messageBox1 = (TextView) rowView.findViewById(R.id.example);
        TextView description = (TextView) rowView.findViewById(R.id.descLine);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        //messageBox1.setText("This is the Test example message");
        description.setText("The description of the post will go here");


        return rowView;
    }
}