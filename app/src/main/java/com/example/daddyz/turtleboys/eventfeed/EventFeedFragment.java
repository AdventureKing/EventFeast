package com.example.daddyz.turtleboys.eventfeed;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.daddyz.turtleboys.EventDetail.eventDetailFragment;
import com.example.daddyz.turtleboys.R;
import com.example.daddyz.turtleboys.VolleyJSONObjectRequest;
import com.example.daddyz.turtleboys.VolleyRequestQueue;
import com.example.daddyz.turtleboys.maindrawer;

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
    private ArrayList<gEventObject> eventfeedList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = inflater.inflate(R.layout.eventfeedlistfragment, container, false);
        list = (ListView) rootView.findViewById(R.id.listView);

        mTextView = (TextView) rootView.findViewById(R.id.textView);
        mButton = (Button) rootView.findViewById(R.id.button);
        list.setClickable(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //get object at that position
                //this code is gonna get nocked out monday
                gEventObject obj = (gEventObject) list.getItemAtPosition(position);
                eventDetailFragment fragment = new eventDetailFragment();
                fragment.setObj(obj);

                //start the fragment
                ((maindrawer) getActivity()).getFragmentManager().beginTransaction().replace(R.id.drawer,fragment,"EventDetailFragment").addToBackStack("EventDetailFragment").commit();
                //this is where we are gonna

            }
        });

        Log.d("CustomAdapter", "MusicFragment onCreateView successful");

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        mQueue = VolleyRequestQueue.getInstance(this.getActivity().getApplicationContext())
                .getRequestQueue();
        String url = "http://45.55.142.106/prod/ws/rest/findEvents/San%20Antonio?city=San%20Antonio";
        final VolleyJSONObjectRequest jsonRequest = new VolleyJSONObjectRequest(Request.Method
                .GET, url,
                new JSONObject(), this, this);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);

       /* mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQueue.add(jsonRequest);
            }
        });*/
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
      //  mTextView.setText(error.getMessage());
    }

    @Override
    public void onResponse(Object response) {
        loadEvents(response);
    }

    public void loadEvents(Object response){
            eventfeedList = creategEventObjectsFromResponse(response);

            adapter = new eventfeedAdapter(getActivity(), R.layout.eventfeedroweven, eventfeedList);
            list.setAdapter(adapter);
            ((BaseAdapter)list.getAdapter()).notifyDataSetChanged();

            rootView.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    }

    public ArrayList<gEventObject> creategEventObjectsFromResponse(Object response){
        try{
            eventfeedList = new ArrayList<>();
            JSONObject jObject = ((JSONObject) response);
            Iterator<?> keys = jObject.keys();

            while( keys.hasNext() ) {
                String key = (String)keys.next();
                if ( jObject.get(key) instanceof JSONObject ) {
                    //Log.i("Ind Object JSON", ((JSONObject) jObject.get(key)).toString());
                    gEventObject obj = new gEventObject();

                    obj.setInternal_id(((JSONObject) jObject.get(key)).getString("internal_id"));
                    obj.setExternal_id(((JSONObject) jObject.get(key)).getString("external_id"));
                    obj.setDatasource(((JSONObject) jObject.get(key)).getString("datasource"));
                    obj.setEvent_external_url(((JSONObject) jObject.get(key)).getString("event_external_url").replaceAll("\\\\", ""));
                    obj.setTitle(((JSONObject) jObject.get(key)).getString("title"));
                    obj.setDescription(((JSONObject) jObject.get(key)).getString("description"));
                    obj.setNotes(((JSONObject) jObject.get(key)).getString("notes"));
                    obj.setTimezone(((JSONObject) jObject.get(key)).getString("timezone"));
                    obj.setTimezone_abbr(((JSONObject) jObject.get(key)).getString("timezone_abbr"));
                    obj.setStart_time(((JSONObject) jObject.get(key)).getString("start_time"));
                    obj.setEnd_time(((JSONObject) jObject.get(key)).getString("end_time"));
                    obj.setStart_date_month(convertJsonStringArrayToArrayList(((JSONObject) jObject.get(key)).getJSONObject("start_date_month")));
                    obj.setStart_date_day(convertJsonStringArrayToArrayList(((JSONObject) jObject.get(key)).getJSONObject("start_date_day")));
                    obj.setStart_date_year(convertJsonStringArrayToArrayList(((JSONObject) jObject.get(key)).getJSONObject("start_date_year")));
                    obj.setStart_date_time(convertJsonStringArrayToArrayList(((JSONObject) jObject.get(key)).getJSONObject("start_date_time")));
                    obj.setEnd_date_month(convertJsonStringArrayToArrayList(((JSONObject) jObject.get(key)).getJSONObject("end_date_month")));
                    obj.setEnd_date_day(convertJsonStringArrayToArrayList(((JSONObject) jObject.get(key)).getJSONObject("end_date_day")));
                    obj.setEnd_date_year(convertJsonStringArrayToArrayList(((JSONObject) jObject.get(key)).getJSONObject("end_date_year")));
                    obj.setEnd_date_time(convertJsonStringArrayToArrayList(((JSONObject) jObject.get(key)).getJSONObject("end_date_time")));
                    obj.setVenue_external_id(((JSONObject) jObject.get(key)).getString("venue_external_id"));
                    obj.setVenue_external_url(((JSONObject) jObject.get(key)).getString("venue_external_url").replaceAll("\\\\", ""));
                    obj.setVenue_name(((JSONObject) jObject.get(key)).getString("venue_name"));
                    obj.setVenue_display(((JSONObject) jObject.get(key)).getString("venue_display"));
                    obj.setVenue_address(((JSONObject) jObject.get(key)).getString("venue_address"));
                    obj.setState_name(((JSONObject) jObject.get(key)).getString("state_name"));
                    obj.setCity_name(((JSONObject) jObject.get(key)).getString("city_name"));
                    obj.setPostal_code(((JSONObject) jObject.get(key)).getString("postal_code"));
                    obj.setCountry_name(((JSONObject) jObject.get(key)).getString("country_name"));
                    obj.setAll_day(((JSONObject) jObject.get(key)).getBoolean("all_day"));
                    obj.setPrice_range(((JSONObject) jObject.get(key)).getString("price_range"));
                    obj.setIs_free(((JSONObject) jObject.get(key)).getString("is_free"));
                    obj.setMajor_genre(convertJsonStringArrayToArrayList(((JSONObject) jObject.get(key)).getJSONObject("major_genre")));
                    obj.setMinor_genre(convertJsonStringArrayToArrayList(((JSONObject) jObject.get(key)).getJSONObject("minor_genre")));
                    obj.setLatitude(((JSONObject) jObject.get(key)).getDouble("latitude"));
                    obj.setLongitude(((JSONObject) jObject.get(key)).getDouble("longitude"));
                    obj.setPerformers(convertJsonPerformerArrayToArrayList(((JSONObject) jObject.get(key)).getJSONObject("performers")));
                    obj.setImages(convertJsonImageArrayToArrayList(((JSONObject) jObject.get(key)).getJSONObject("images")));

                    eventfeedList.add(obj);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return eventfeedList;
    }

    public ArrayList<String> convertJsonStringArrayToArrayList(JSONObject jsonObj){
        ArrayList<String> retValue = new ArrayList<>();

        Iterator<String> iter = jsonObj.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                Object value = jsonObj.get(key);
                retValue.add(value.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return retValue;
    }

    public ArrayList<gEventPerformerObject> convertJsonPerformerArrayToArrayList(JSONObject jsonObj) {
        ArrayList<gEventPerformerObject> retValue = new ArrayList<>();

        Iterator<String> iter = jsonObj.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                gEventPerformerObject obj = new gEventPerformerObject();

                obj.setPerformer_external_id(((JSONObject) jsonObj.get(key)).getString("performer_external_id"));
                obj.setPerformer_external_url(((JSONObject) jsonObj.get(key)).getString("performer_external_url").replaceAll("\\\\", ""));
                obj.setPerformer_external_image_url(((JSONObject) jsonObj.get(key)).getString("performer_external_image_url").replaceAll("\\\\", ""));
                obj.setPerformer_name(((JSONObject) jsonObj.get(key)).getString("performer_name"));
                obj.setPerformer_short_bio(((JSONObject) jsonObj.get(key)).getString("performer_short_bio"));

                retValue.add(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return retValue;
    }

    public ArrayList<gEventImageObject> convertJsonImageArrayToArrayList(JSONObject jsonObj){
        ArrayList<gEventImageObject> retValue = new ArrayList<>();

        Iterator<String> iter = jsonObj.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                gEventImageObject obj = new gEventImageObject();

                obj.setImage_external_url(((JSONObject) jsonObj.get(key)).getString("image_external_url").replaceAll("\\\\", ""));
                obj.setImage_category(((JSONObject) jsonObj.get(key)).getString("image_category"));
                obj.setImage_height(((JSONObject) jsonObj.get(key)).getString("image_height"));
                obj.setImage_width(((JSONObject) jsonObj.get(key)).getString("image_width"));

                retValue.add(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return retValue;
    }
}