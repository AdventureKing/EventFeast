package com.example.daddyz.turtleboys.newsfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.daddyz.turtleboys.R;

import java.util.ArrayList;

/**
 * Created by snow on 7/11/2015.
 */
public class newsfeedAdapter extends ArrayAdapter<newsfeedObject> {

    private Context context;
    private int resource;
    private ArrayList<newsfeedObject> newsfeedObjects;

    public newsfeedAdapter(Context context, int resource, ArrayList<newsfeedObject> newsfeedObjects) {
        super(context, resource,newsfeedObjects);
        this.context = context;
        this.resource = resource;
        this.newsfeedObjects = newsfeedObjects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.newsfeedrow, parent, false);
        TextView messageBox1 = (TextView) rowView.findViewById(R.id.example);
        TextView description = (TextView) rowView.findViewById(R.id.description);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        messageBox1.setText("This is the Test example message");
        description.setText("The description of the post will go here");


        return rowView;
    }
}


