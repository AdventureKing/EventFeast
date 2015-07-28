package com.example.daddyz.turtleboys.friendFeed;

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

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by richardryangarcia on 7/17/15.
 */
public class userListAdapter extends ArrayAdapter<FollowUser> implements Response.Listener,AbsListView.OnItemClickListener, Response.ErrorListener{


    private Context context;
    private int resource;
    private ArrayList<FollowUser> userObjects;
    private View row = null;
    private LayoutInflater inflater = null;
    private ViewGroup parent = null;
    private RequestQueue mQueue;


    public userListAdapter(Context context, int resource, ArrayList<FollowUser> userObjects) {
        super(context, resource, userObjects);
        this.context = context;
        this.resource = resource;
        this.userObjects = userObjects;
    }

    public void setRow(int num){
        switch(num) {
            case 0:
                row = inflater.inflate(R.layout.user_list_follow_row, parent, false);
                //Log.i("Switched", "to follow button");
                this.notifyDataSetChanged();
                return;
            case 1:
                row = inflater.inflate(R.layout.user_list_unfollow_row, parent, false);
                //Log.i("Switched", "to unfollow button");
                this.notifyDataSetChanged();
                return;
            case 2:
                return;
            default:
                return;
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //get inflator so it will strech the view to fill the row data
        this.inflater = ((Activity) context).getLayoutInflater();
        this.parent = parent;

        switch(userObjects.get(position).getFollowing()){
            case 0 :
                setRow(0);
                Button followBtn = (Button) row.findViewById(R.id.button3);
                followBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("Follow Button", "Clicked");
                        //setRow(1);
                        doFollow(userObjects.get(position).getUserId());
                    }
                });
                break;
            case 1 :
                setRow(1);
                Button unfollowBtn = (Button) this.row.findViewById(R.id.button2);
                unfollowBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("Unfollow Buton", "Clicked");
                        doUnfollow(userObjects.get(position).getUserId());
                    }
                });
                break;
            case 2 :
                break;
            default :
                break;
        }

        TextView description = (TextView) row.findViewById(R.id.descLine);
        TextView venue = (TextView) row.findViewById(R.id.venueLine);

        String placeholderImageUrl = "http://www.grommr.com/Content/Images/placeholder-event-p.png";
        String imageUrl = placeholderImageUrl;
        String imageVenueUrl = placeholderImageUrl;

        venue.setText(userObjects.get(position).getFirstName() + " " + userObjects.get(position).getLastName());
        description.setText(userObjects.get(position).getUsername());

        return row;
    }

    private void doFollow(String userId){
        mQueue = VolleyRequestQueue.getInstance(context)
                .getRequestQueue();
        String url = "http://api.dev.turtleboys.com/v1/friendships/create/" + userId;
        final VolleyJSONObjectRequest jsonRequest = new VolleyJSONObjectRequest(Request.Method
                .POST, url,
                new JSONObject(), this, this);
        jsonRequest.setTag("Follow");
        mQueue.add(jsonRequest);
    }

    private void doUnfollow(String userId){
        mQueue = VolleyRequestQueue.getInstance(context)
                .getRequestQueue();
        String url = "http://api.dev.turtleboys.com/v1/friendships/destroy/" + userId;
        final VolleyJSONObjectRequest jsonRequest = new VolleyJSONObjectRequest(Request.Method
                .DELETE, url,
                new JSONObject(), this, this);
        jsonRequest.setTag("UnFollow");
        mQueue.add(jsonRequest);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Log.i("Volley Error", volleyError.toString());
    }

    @Override
    public void onResponse(Object o) {
        //Log.i("Response", o.toString());
        this.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}

