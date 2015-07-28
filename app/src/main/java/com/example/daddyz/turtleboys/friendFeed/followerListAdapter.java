package com.example.daddyz.turtleboys.friendFeed;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.daddyz.turtleboys.R;
import com.example.daddyz.turtleboys.VolleyJSONObjectRequest;
import com.example.daddyz.turtleboys.VolleyRequestQueue;
import com.example.daddyz.turtleboys.friendFeed.dummy.DummyContent;
import com.example.daddyz.turtleboys.subclasses.GigUser;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by richardryangarcia on 7/17/15.
 */
public class followerListAdapter extends ArrayAdapter<GigUser> {


    private Context context;
    private int resource;
    private ArrayList<GigUser> followerObjects;


    public followerListAdapter(Context context, int resource, ArrayList<GigUser> followerObjects) {
        super(context, resource, followerObjects);
        this.context = context;
        this.resource = resource;
        this.followerObjects = followerObjects;
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

        venue.setText(followerObjects.get(position).getFirstName() + " " + followerObjects.get(position).getLastName());
        description.setText(followerObjects.get(position).getUsername());

        return row;
    }
}