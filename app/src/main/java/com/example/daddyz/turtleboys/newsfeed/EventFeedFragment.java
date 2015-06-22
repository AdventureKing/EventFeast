package com.example.daddyz.turtleboys.newsfeed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import java.util.Iterator;

/**
 * Created by Admin on 04-06-2015.
 */
public class EventFeedFragment extends Fragment implements Response.Listener,
        Response.ErrorListener{

    public static final String REQUEST_TAG = "MainVolleyActivity";
    private Button mButton;
    private TextView mTextView;
    private RequestQueue mQueue;
    private ListView list;
    private eventfeedAdapter adapter;
    private ArrayList<eventfeedObject> eventfeedList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main_volley);
        View rootView = inflater.inflate(R.layout.listfragment, container, false);
        list = (ListView) rootView.findViewById(R.id.listView);

        mTextView = (TextView) rootView.findViewById(R.id.textView);
        mButton = (Button) rootView.findViewById(R.id.button);

        eventfeedObject obj = new eventfeedObject();
        obj.setEventDesc("Event Description");
        obj.setEventCity("San Antonio");
        obj.setEventDate("07/07/2015");
        eventfeedList = new ArrayList<>();

        eventfeedList.add(obj);

        adapter = new eventfeedAdapter(getActivity(), R.layout.newsfeedroweven, eventfeedList);
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

        //mTextView.setText("Response is: " + response);
        try {
            JSONObject jObject = ((JSONObject) response);
            Iterator<?> keys = jObject.keys();
            eventfeedList = new ArrayList<>();

            while( keys.hasNext() ) {
                String key = (String)keys.next();
                if ( jObject.get(key) instanceof JSONObject ) {
                    Log.i("Ind Object", ((JSONObject) jObject.get(key)).toString());
                    eventfeedObject obj = new eventfeedObject();
                    obj.setEventDesc(((JSONObject) jObject.get(key)).getString("desc"));
                    obj.setEventCity(((JSONObject) jObject.get(key)).getString("state"));
                    obj.setEventDate(((JSONObject) jObject.get(key)).getString("date"));

                    eventfeedList.add(obj);
                }
            }

            adapter = new eventfeedAdapter(getActivity(), R.layout.newsfeedroweven, eventfeedList);
            list.setAdapter(adapter);
            ((BaseAdapter)list.getAdapter()).notifyDataSetChanged();

            //mTextView.setText(mTextView.getText() + "\n\n" + ((JSONObject) response).getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}