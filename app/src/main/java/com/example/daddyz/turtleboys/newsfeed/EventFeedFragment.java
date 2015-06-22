package com.example.daddyz.turtleboys.newsfeed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.daddyz.turtleboys.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 04-06-2015.
 */
public class EventFeedFragment extends Fragment implements Response.Listener,
        Response.ErrorListener{

    public static final String REQUEST_TAG = "MainVolleyActivity";
    private Button mButton;
    private TextView mTextView;
    private RequestQueue mQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main_volley);
        View rootView = inflater.inflate(R.layout.listfragment, container, false);
        ListView list = (ListView) rootView.findViewById(R.id.listView);

        mTextView = (TextView) rootView.findViewById(R.id.textView);
        mButton = (Button) rootView.findViewById(R.id.button);

        eventfeedObject obj1 = new eventfeedObject();
        obj1.setEventDesc("Event Description");
        obj1.setEventCity("San Antonio");
        obj1.setEventDate("07/07/2015");
        ArrayList<eventfeedObject> myDataArray = new ArrayList<>();

        myDataArray.add(obj1);

        eventfeedAdapter adapter = new eventfeedAdapter(getActivity(), R.layout.newsfeedroweven, myDataArray);
        list.setAdapter(adapter);

        Log.d("CustomAdapter", "MusicFragment onCreateView successful");

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mQueue = CustomVolleyRequestQueue.getInstance(this.getActivity().getApplicationContext())
                .getRequestQueue();
        String url = "http://45.55.142.106/prod/ws/rest/findEvents/jazz";
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method
                .GET, url,
                new JSONObject(), this, this);
        jsonRequest.setTag(REQUEST_TAG);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQueue.add(jsonRequest);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mTextView.setText(error.getMessage());
    }

    @Override
    public void onResponse(Object response) {
        mTextView.setText("Response is: " + response);
        try {
            mTextView.setText(mTextView.getText() + "\n\n" + ((JSONObject) response).getString
                    ("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}