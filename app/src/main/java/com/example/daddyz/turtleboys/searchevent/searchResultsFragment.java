package com.example.daddyz.turtleboys.searchevent;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.daddyz.turtleboys.EventDetail.eventDetailFragment;
import com.example.daddyz.turtleboys.R;
import com.example.daddyz.turtleboys.VolleyJSONObjectRequest;
import com.example.daddyz.turtleboys.VolleyRequestQueue;
import com.example.daddyz.turtleboys.eventfeed.gEventImageObject;
import com.example.daddyz.turtleboys.eventfeed.gEventObject;
import com.example.daddyz.turtleboys.eventfeed.gEventPerformerObject;
import com.example.daddyz.turtleboys.maindrawer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Jinbir on 7/12/2015.
 */
public class searchResultsFragment extends Fragment implements Response.Listener,
        Response.ErrorListener{

    public static final String REQUEST_TAG = "Search Event";

    private TextView mTextView;
    private RequestQueue mQueue;
    private View rootView;
    private ListView list;
    private searchResultsAdapter adapter;
    private ArrayList<gEventObject> searchResultsList;
    private ActionBar actionBar;

    private String searchQuery = "";
    private String userAddress = "";
    private String filterDate;
    private String filterCity = "";
    private String filterState = "";
    private String filterSources;
    private Long filterRadius;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        rootView = inflater.inflate(R.layout.searchresultslistfragment, container, false);
        list = (ListView) rootView.findViewById(R.id.listView);

        //actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        //actionBar.setTitle("Search Results");

        mTextView = (TextView) rootView.findViewById(R.id.textView);
        mButton = (Button) rootView.findViewById(R.id.button);
        list.setClickable(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                searchResultsObject obj = (searchResultsObject) list.getItemAtPosition(position);

                //Hard coding obj attributes until backend is ready
                ArrayList<String> month = new ArrayList<String>();
                month.add("0");
                month.add("1");
                month.add("Jul");
                obj.setStart_date_month(new ArrayList<String>(month));
                ArrayList<String> day = new ArrayList<String>();
                day.add("19");
                obj.setStart_date_day(new ArrayList<String>(day));
                ArrayList<String> year = new ArrayList<String>();
                year.add("2015");
                obj.setStart_date_year(new ArrayList<String>(year));
                ArrayList<String> time = new ArrayList<String>();
                time.add("0");
                time.add("1");
                time.add("1:12AM");
                obj.setStart_date_time(new ArrayList<String>(time));

                eventDetailFragment fragment = new eventDetailFragment();
                fragment.setObj(obj);

                //Toast.makeText(getActivity().getApplicationContext(), fragment.getObj().toString(), Toast.LENGTH_SHORT).show();
                //Pain in the arse trouble maker.
                getFragmentManager().beginTransaction().replace(R.id.drawer,fragment,"EventDetailFragment").addToBackStack("EventDetailFragment").commit();
            }
        });

        searchResultsList = new ArrayList<gEventObject>();
        searchResultsObject obj = new searchResultsObject();
        searchResultsList.add(obj);
        adapter = new searchResultsAdapter(getActivity(), R.layout.searchresultsrow, searchResultsList);
        list.setAdapter(adapter);
        Log.d("CustomAdapter", "Search Results Launched successfully");
        */


        rootView = inflater.inflate(R.layout.searchresultslistfragment, container, false);
        list = (ListView) rootView.findViewById(R.id.listView);

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

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        try{
            userAddress = URLEncoder.encode(this.getUserAddress(), "utf-8");
            searchQuery = URLEncoder.encode(this.getSearchQuery(), "utf-8");
            filterCity = URLEncoder.encode(this.getFilterCity(), "utf-8");
            filterState = URLEncoder.encode(this.getFilterState(), "utf-8");
            filterRadius = this.getFilterRadius();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        searchQuery = (searchQuery.length() == 0 && filterCity.length() > 0) ? filterCity : searchQuery;
        searchQuery = (searchQuery.length() == 0 && filterState.length() > 0) ? filterState : searchQuery;

        mQueue = VolleyRequestQueue.getInstance(this.getActivity().getApplicationContext()).getRequestQueue();
        StringBuilder url = new StringBuilder(2048);
        url.append("http://api.dev.turtleboys.com/v1/events/find/");
        url.append(searchQuery);
        url.append("?address="+userAddress);
        if(filterCity.length() > 0){
            url.append("&city="+filterCity);
        }
        if(filterState.length() > 0){
            url.append("&state="+filterState);
        }
        if(filterRadius != 0){
            url.append("&radius="+filterRadius);
        }

        final VolleyJSONObjectRequest jsonRequest = new VolleyJSONObjectRequest(Request.Method
                .GET, url.toString(),
                new JSONObject(), this, this);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);

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
        searchResultsList = creategEventObjectsFromResponse(response);

        adapter = new searchResultsAdapter(getActivity(), R.layout.searchresultsrow, searchResultsList);
        list.setAdapter(adapter);
        ((BaseAdapter)list.getAdapter()).notifyDataSetChanged();

        rootView.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    }

    public ArrayList<gEventObject> creategEventObjectsFromResponse(Object response){
        try{
            searchResultsList = new ArrayList<>();

            JSONObject mainObject = ((JSONObject) response);
            JSONObject itemsObject = mainObject.getJSONObject("items");
            Iterator<?> keys = itemsObject.keys();

            String numFound = mainObject.getString("numFound");

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

                    searchResultsList.add(obj);
                }
            }

            Toast.makeText(getActivity().getApplicationContext(), numFound + " Events Found", Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return searchResultsList;
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

    public void onBackPressed() {
        Log.d("Test","This is being called in searchResults");
        if(getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        }
    }

    public String getFilterSources() {
        return filterSources;
    }

    public void setFilterSources(String filterSources) {
        this.filterSources = filterSources;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public Long getFilterRadius(){ return filterRadius;}

    public void setFilterRadius(Long filterRadius ){ this.filterRadius = filterRadius;}

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public String getFilterDate() {
        return filterDate;
    }

    public void setFilterDate(String filterDate) {
        this.filterDate = filterDate;
    }

    public String getFilterCity() {
        return filterCity;
    }

    public void setFilterCity(String filterCity) {
        this.filterCity = filterCity;
    }

    public String getFilterState() {
        return filterState;
    }

    public void setFilterState(String filterState) {
        this.filterState = filterState;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }
}