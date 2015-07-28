package com.example.daddyz.turtleboys.friendFeed;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.daddyz.turtleboys.R;
import com.example.daddyz.turtleboys.subclasses.FollowUser;
import com.example.daddyz.turtleboys.subclasses.GigUser;

import java.util.ArrayList;

/**
 * Created by richardryangarcia on 7/17/15.
 */

public class followListAdapter extends ArrayAdapter<GigUser> {


    private Context context;
    private int resource;
    private ArrayList<GigUser> followObjects;


    public followListAdapter(Context context, int resource, ArrayList<GigUser> followObjects) {
        super(context, resource, followObjects);
        this.context = context;
        this.resource = resource;
        this.followObjects = followObjects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get inflator so it will strech the view to fill the row data
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View row=inflater.inflate(R.layout.user_list_follow_row,parent,false);

        TextView description = (TextView) row.findViewById(R.id.descLine);
        TextView venue = (TextView) row.findViewById(R.id.venueLine);

        String placeholderImageUrl = "http://www.grommr.com/Content/Images/placeholder-event-p.png";
        String imageUrl = placeholderImageUrl;
        String imageVenueUrl = placeholderImageUrl;

        venue.setText(followObjects.get(position).getFirstName() + " " + followObjects.get(position).getLastName());
        description.setText(followObjects.get(position).getUsername());

        return row;
    }
}






