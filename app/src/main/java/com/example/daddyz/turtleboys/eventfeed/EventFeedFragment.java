package com.example.daddyz.turtleboys.eventfeed;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.daddyz.turtleboys.EventDetail.eventDetailFragment;
import com.example.daddyz.turtleboys.R;
import com.example.daddyz.turtleboys.VolleyJSONObjectRequest;
import com.example.daddyz.turtleboys.VolleyRequestQueue;
import com.example.daddyz.turtleboys.maindrawer;
import com.example.daddyz.turtleboys.maps.MapsActivity;
import com.example.daddyz.turtleboys.subclasses.GigUser;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Admin on 04-06-2015.
 */
//had to change fragment class to android.app.Fragment
public class EventFeedFragment extends Fragment implements Response.Listener,
        Response.ErrorListener{

    public static final String REQUEST_TAG = "MainVolleyActivity";

    private ArrayList<String> jsonStringArray;
    private TextView mTextView;
    private RequestQueue mQueue;
    private View rootView;
    private ListView list;
    private eventfeedAdapter adapter;
    private ArrayList<gEventObject> eventfeedList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private final GigUser currentUser = ParseUser.createWithoutData(GigUser.class, ParseUser.getCurrentUser().getObjectId());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = inflater.inflate(R.layout.eventfeedlistfragment, container, false);
        list = (ListView) rootView.findViewById(R.id.listView);

        //swipe down to refresh
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);
        //pull down to refresh the list and set the colors of the loading icon
        mSwipeRefreshLayout.setColorSchemeResources(R.color.loadingorange, R.color.loadinggreen, R.color.loadingblue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();

            }
        });
        mTextView = (TextView) rootView.findViewById(R.id.textView);
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
                ((maindrawer) getActivity()).getFragmentManager().beginTransaction().replace(R.id.drawer, fragment, "EventDetailFragment").addToBackStack("EventDetailFragment").commit();
                //this is where we are gonna

            }
        });

        Button mapBtn = (Button) rootView.findViewById((R.id.viewInMap));
        mapBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(getActivity().getApplicationContext(), MapsActivity.class);
                System.out.println("list size before intent is " + getJsonStringArray().size());

                mapIntent.putStringArrayListExtra("eventArrayOfStrings", getJsonStringArray());
                // Create a Bundle and Put Bundle in to it
                //for(int i = 0; i < eventfeedList.size(); i++){
                    //mapIntent.putExtra("event1", eventfeedList.get(0));
                //}
               // Bundle bundleObject = new Bundle();
                System.out.println("im here \n\n\n\n\n\n");
                System.out.println(eventfeedList.get(0).getCity_name());
                //bundleObject.putSerializable("eventfeedList", eventfeedList);


                // Put Bundle in to Intent and call start Activity
                //mapIntent.putExtras(bundleObject);
                startActivity(mapIntent);
            }

        });

        return rootView;
    }

    private void refreshContent() {
        onStart();
    }

    @Override
    public void onStart() {
        super.onStart();

        double userParseLat = currentUser.getUserHome().getLatitude();
        double userParseLong = currentUser.getUserHome().getLongitude();

        //App_Application mApp = (App_Application)getActivity().getApplicationContext();
        //String userAddress;

        // Get current users address via network or gps
        //userAddress = mApp.getCurrentAddress();

        // If the user has location services turned off, grab their last location from parse db
        /*if(null == userAddress){
            double userParseLat = currentUser.getUserHome().getLatitude();
            double userParseLong = currentUser.getUserHome().getLongitude();

            LocationFinder locFinder = new LocationFinder();
            Location loc = new Location("");
            loc.setLatitude(userParseLat);
            loc.setLongitude(userParseLong);

            userAddress = locFinder.getAddressFromLocation(getActivity().getApplicationContext(), loc);
            Log.i("Location: ", "Getting from Parse!");
        }

        try{
            userAddress = URLEncoder.encode(userAddress, "utf-8");
        } catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
    */
        mQueue = VolleyRequestQueue.getInstance(this.getActivity().getApplicationContext())
                .getRequestQueue();

        StringBuilder url = new StringBuilder(2048);
        url.append("http://api.dev.turtleboys.com/v1/events/find/");
        url.append("San%20Antonio");
        url.append("?userLat="+userParseLat);
        url.append("&userLng="+userParseLong);
        url.append("&city=San%20Antonio");

        final VolleyJSONObjectRequest jsonRequest = new VolleyJSONObjectRequest(Request.Method
                .GET, url.toString(),
                new JSONObject(), this, this);
        jsonRequest.setTag(REQUEST_TAG);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
            setEventListInfo();


            adapter = new eventfeedAdapter(getActivity(), R.layout.eventfeedroweven, eventfeedList);
            list.setAdapter(adapter);
            ((BaseAdapter)list.getAdapter()).notifyDataSetChanged();

            rootView.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
    }

    public ArrayList<gEventObject> creategEventObjectsFromResponse(Object response){
        try{
            eventfeedList = new ArrayList<>();
            JSONObject mainObject = ((JSONObject) response);
            JSONObject itemsObject = mainObject.getJSONObject("items");
            Iterator<?> keys = itemsObject.keys();

            while( keys.hasNext() ) {
                String key = (String)keys.next();
                if ( itemsObject.get(key) instanceof JSONObject ) {
                    //Log.i("Ind Object JSON", ((JSONObject) itemsObject.get(key)).toString());
                    gEventObject obj = new gEventObject();

                    obj.setInternal_id(((JSONObject) itemsObject.get(key)).getString("internal_id"));
                    obj.setExternal_id(((JSONObject) itemsObject.get(key)).getString("external_id"));
                    obj.setDatasource(((JSONObject) itemsObject.get(key)).getString("datasource"));
                    obj.setEvent_external_url(((JSONObject) itemsObject.get(key)).getString("event_external_url").replaceAll("\\\\", ""));
                    obj.setTitle(((JSONObject) itemsObject.get(key)).getString("title"));
                    obj.setDescription(((JSONObject) itemsObject.get(key)).getString("description"));
                    obj.setNotes(((JSONObject) itemsObject.get(key)).getString("notes"));
                    obj.setTimezone(((JSONObject) itemsObject.get(key)).getString("timezone"));
                    obj.setTimezone_abbr(((JSONObject) itemsObject.get(key)).getString("timezone_abbr"));
                    obj.setStart_time(((JSONObject) itemsObject.get(key)).getString("start_time"));
                    obj.setEnd_time(((JSONObject) itemsObject.get(key)).getString("end_time"));
                    obj.setStart_date_month(convertJsonStringArrayToArrayList(((JSONObject) itemsObject.get(key)).getJSONObject("start_date_month")));
                    obj.setStart_date_day(convertJsonStringArrayToArrayList(((JSONObject) itemsObject.get(key)).getJSONObject("start_date_day")));
                    obj.setStart_date_year(convertJsonStringArrayToArrayList(((JSONObject) itemsObject.get(key)).getJSONObject("start_date_year")));
                    obj.setStart_date_time(convertJsonStringArrayToArrayList(((JSONObject) itemsObject.get(key)).getJSONObject("start_date_time")));
                    obj.setEnd_date_month(convertJsonStringArrayToArrayList(((JSONObject) itemsObject.get(key)).getJSONObject("end_date_month")));
                    obj.setEnd_date_day(convertJsonStringArrayToArrayList(((JSONObject) itemsObject.get(key)).getJSONObject("end_date_day")));
                    obj.setEnd_date_year(convertJsonStringArrayToArrayList(((JSONObject) itemsObject.get(key)).getJSONObject("end_date_year")));
                    obj.setEnd_date_time(convertJsonStringArrayToArrayList(((JSONObject) itemsObject.get(key)).getJSONObject("end_date_time")));
                    obj.setVenue_external_id(((JSONObject) itemsObject.get(key)).getString("venue_external_id"));
                    obj.setVenue_external_url(((JSONObject) itemsObject.get(key)).getString("venue_external_url").replaceAll("\\\\", ""));
                    obj.setVenue_name(((JSONObject) itemsObject.get(key)).getString("venue_name"));
                    obj.setVenue_display(((JSONObject) itemsObject.get(key)).getString("venue_display"));
                    obj.setVenue_address(((JSONObject) itemsObject.get(key)).getString("venue_address"));
                    obj.setState_name(((JSONObject) itemsObject.get(key)).getString("state_name"));
                    obj.setCity_name(((JSONObject) itemsObject.get(key)).getString("city_name"));
                    obj.setPostal_code(((JSONObject) itemsObject.get(key)).getString("postal_code"));
                    obj.setCountry_name(((JSONObject) itemsObject.get(key)).getString("country_name"));
                    obj.setAll_day(((JSONObject) itemsObject.get(key)).getBoolean("all_day"));
                    obj.setPrice_range(((JSONObject) itemsObject.get(key)).getString("price_range"));
                    obj.setIs_free(((JSONObject) itemsObject.get(key)).getString("is_free"));
                    obj.setMajor_genre(convertJsonStringArrayToArrayList(((JSONObject) itemsObject.get(key)).getJSONObject("major_genre")));
                    obj.setMinor_genre(convertJsonStringArrayToArrayList(((JSONObject) itemsObject.get(key)).getJSONObject("minor_genre")));
                    obj.setLatitude(((JSONObject) itemsObject.get(key)).getDouble("latitude"));
                    obj.setLongitude(((JSONObject) itemsObject.get(key)).getDouble("longitude"));
                    obj.setDistance(((JSONObject) itemsObject.get(key)).getDouble("distance"));
                    obj.setPerformers(convertJsonPerformerArrayToArrayList(((JSONObject) itemsObject.get(key)).getJSONObject("performers")));
                    obj.setImages(convertJsonImageArrayToArrayList(((JSONObject) itemsObject.get(key)).getJSONObject("images")));

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

    /*
    get marker info
     */

    public void setEventListInfo(){
        //System.out.println(" The Length of the event list is " + eventfeedList.size());
        jsonStringArray = new ArrayList<>();
        for(int i=0; i<eventfeedList.size();i++){



            jsonStringArray.add(listToString(eventfeedList.get(i).getTitle(),
                    eventfeedList.get(i).getLatitude(),
                    eventfeedList.get(i).getLongitude(),
                    eventfeedList.get(i).getVenue_name()));
        }
    }

    public String listToString(String title, Double lat, Double lng, String venue){
        return title + " /// " + Double.toString(lat) + " /// " + Double.toString(lng) + " /// " + venue;
    }

    public ArrayList<String> getJsonStringArray(){
        return jsonStringArray;
    }
}