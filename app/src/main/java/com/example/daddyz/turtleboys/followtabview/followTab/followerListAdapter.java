package com.example.daddyz.turtleboys.followtabview.followTab;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.daddyz.turtleboys.R;
import com.example.daddyz.turtleboys.VolleyJSONObjectRequest;
import com.example.daddyz.turtleboys.VolleyRequestQueue;
import com.example.daddyz.turtleboys.subclasses.FollowUser;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by richardryangarcia on 7/17/15.
 */
public class followerListAdapter extends ArrayAdapter<FollowUser> implements Response.Listener,AbsListView.OnItemClickListener, Response.ErrorListener{


    private Context context;
    private int resource;
    private ArrayList<FollowUser> followerObjects;
    private View row;
    private LayoutInflater inflater = null;
    private ViewGroup parent = null;
    private RequestQueue mQueue;
    private followerListFragment fragment;
    private int position = 0;
    public static final String REQUEST_TAG = "Follower List Adapter";


    public followerListAdapter(Context context, int resource, ArrayList<FollowUser> followerObjects, followerListFragment fragment) {
        super(context, resource, followerObjects);
        this.context = context;
        this.resource = resource;
        this.followerObjects = followerObjects;
        this.fragment = fragment;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        this.inflater = ((Activity) context).getLayoutInflater();
        this.parent = parent;
        this.position = position;

        this.row = inflater.inflate(resource, parent, false);

        switch(followerObjects.get(position).getFollowing()){
            case 0 :
                row.findViewById(R.id.followBtn).setVisibility(View.VISIBLE);
                Button followBtn = (Button) row.findViewById(R.id.followBtn);
                followBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("Follow Button", "Clicked");
                        followerObjects.get(position).setFollowing(1);
                        doFollow(followerObjects.get(position).getUserId(), view);
                    }
                });
                break;
            case 1 :
                row.findViewById(R.id.unfollowBtn).setVisibility(View.VISIBLE);
                Button unfollowBtn = (Button) this.row.findViewById(R.id.unfollowBtn);
                unfollowBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("Unfollow Button", "Clicked");
                        followerObjects.get(position).setFollowing(0);
                        doUnfollow(followerObjects.get(position).getUserId(), view);
                    }
                });
                break;
            case 2 :
                break;
            default :
                break;
        }

        TextView description = (TextView) row.findViewById(R.id.userName);
        TextView venue = (TextView) row.findViewById(R.id.firstLastName);

        String placeholderImageUrl = "http://www.grommr.com/Content/Images/placeholder-event-p.png";
        String imageUrl = placeholderImageUrl;
        String imageVenueUrl = placeholderImageUrl;

        venue.setText(followerObjects.get(position).getFirstName() + " " + followerObjects.get(position).getLastName());
        description.setText(followerObjects.get(position).getUsername());

        return row;
    }

    private void doFollow(String userId, View v){
        v.findViewById(R.id.followBtn).setVisibility(View.GONE);

        mQueue = VolleyRequestQueue.getInstance(context)
                .getRequestQueue();
        String url = "http://api.dev.turtleboys.com/v1/friendships/create/" + userId;
        final VolleyJSONObjectRequest jsonRequest = new VolleyJSONObjectRequest(Request.Method
                .POST, url,
                new JSONObject(), this, this);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);
    }

    private void doUnfollow(String userId, View v){
        v.findViewById(R.id.unfollowBtn).setVisibility(View.GONE);

        mQueue = VolleyRequestQueue.getInstance(context)
                .getRequestQueue();
        String url = "http://api.dev.turtleboys.com/v1/friendships/destroy/" + userId;
        final VolleyJSONObjectRequest jsonRequest = new VolleyJSONObjectRequest(Request.Method
                .DELETE, url,
                new JSONObject(), this, this);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        try{
            Log.i("Volley Error", volleyError.toString());
        }catch(NullPointerException err){
            Log.i("Volley Error", err.toString());
            err.printStackTrace();
        }
    }

    @Override
    public void onResponse(Object response) {
        String result = null;
        String message = null;

        try{
            JSONObject mainObject = ((JSONObject) response);
            result = mainObject.getString("result");
            message = mainObject.getString("message");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(null != result && result.equals("success")){
            Log.i("Response", "Success: " + message);

            fragment.updateItemAtPosition(position, followerObjects);

        } else if(null != result && result.equals("error")){
            Log.i("Response", "Error: " + message);
        } else{
            Log.i("ERROR", "No Response Retrieved from Request");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


}