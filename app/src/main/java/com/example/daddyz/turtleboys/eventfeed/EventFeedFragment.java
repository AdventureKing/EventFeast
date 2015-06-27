package com.example.daddyz.turtleboys.eventfeed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private View rootView;
    private ListView list;
    private eventfeedAdapter adapter;
    private ArrayList<eventfeedObject> eventfeedList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = inflater.inflate(R.layout.listfragment, container, false);
        list = (ListView) rootView.findViewById(R.id.listView);

        mTextView = (TextView) rootView.findViewById(R.id.textView);
        mButton = (Button) rootView.findViewById(R.id.button);
        list.setClickable(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //get object at that position
                Object obj = list.getItemAtPosition(position);
               eventfeedObject news = (eventfeedObject) obj;

                //this is where we are gonna
                Toast.makeText(getActivity(), "Clicked arow", Toast.LENGTH_SHORT).show();
            }
        });

        Log.d("CustomAdapter", "MusicFragment onCreateView successful");

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        mQueue = CustomVolleyRequestQueue.getInstance(this.getActivity().getApplicationContext())
                .getRequestQueue();
        String url = "http://45.55.142.106/dev/ws/rest/findEvents/San%20Antonio?city=San%20Antonio";
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method
                .GET, url,
                new JSONObject(), this, this);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);

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
        loadEvents(response);
    }

    public void loadEvents(Object response){
        try{
            JSONObject jObject = ((JSONObject) response);
            Iterator<?> keys = jObject.keys();
            eventfeedList = new ArrayList<>();

            while( keys.hasNext() ) {
                String key = (String)keys.next();
                if ( jObject.get(key) instanceof JSONObject ) {
                    Log.i("Ind Object", ((JSONObject) jObject.get(key)).toString());
                    eventfeedObject obj = new eventfeedObject();

                    obj.setEventSource(((JSONObject) jObject.get(key)).getString("source"));
                    obj.setEventDesc(((JSONObject) jObject.get(key)).getString("desc"));
                    obj.setEventCity(((JSONObject) jObject.get(key)).getString("city"));
                    obj.setEventState(((JSONObject) jObject.get(key)).getString("state"));
                    obj.setEventDate(((JSONObject) jObject.get(key)).getString("date"));
                    obj.setEventTime(((JSONObject) jObject.get(key)).getString("time"));
                    obj.setEventVenue(((JSONObject) jObject.get(key)).getString("venue"));
                    obj.setEventURL(((JSONObject) jObject.get(key)).getString("urlpath"));

                    eventfeedList.add(obj);
                }
            }

            adapter = new eventfeedAdapter(getActivity(), R.layout.eventfeedroweven, eventfeedList);
            list.setAdapter(adapter);
            ((BaseAdapter)list.getAdapter()).notifyDataSetChanged();

            rootView.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}