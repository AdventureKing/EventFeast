package com.example.daddyz.turtleboys.searchevent;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.daddyz.turtleboys.EventDetail.eventDetailFragment;
import com.example.daddyz.turtleboys.R;
import com.example.daddyz.turtleboys.eventfeed.gEventObject;
import com.example.daddyz.turtleboys.maindrawer;

import java.util.ArrayList;

/**
 * Created by Jinbir on 7/12/2015.
 */
public class searchResultsFragment extends Fragment {

    public static final String REQUEST_TAG = "MainVolleyActivity";
    private Button mButton;

    private TextView mTextView;
    private RequestQueue mQueue;
    private View rootView;
    private ListView list;
    private searchResultsAdapter adapter;
    private ArrayList<searchResultsObject> searchResultsList;
    private ActionBar actionBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                getFragmentManager().beginTransaction().replace(R.id.drawer,fragment,"searchResultDetailFragment").addToBackStack("searchResultDetailFragment").commit();
            }
        });

        searchResultsList = new ArrayList<searchResultsObject>();
        searchResultsObject obj = new searchResultsObject();
        searchResultsObject obj3 = new searchResultsObject();
        searchResultsObject obj4 = new searchResultsObject();
        searchResultsObject obj5 = new searchResultsObject();
        searchResultsObject obj6 = new searchResultsObject();
        searchResultsObject obj7 = new searchResultsObject();
        searchResultsObject obj8 = new searchResultsObject();
        searchResultsObject obj9 = new searchResultsObject();
        searchResultsObject obj10 = new searchResultsObject();
        searchResultsList.add(obj);
        searchResultsList.add(obj3);
        searchResultsList.add(obj4);
        searchResultsList.add(obj5);
        searchResultsList.add(obj6);
        searchResultsList.add(obj7);
        searchResultsList.add(obj8);
        searchResultsList.add(obj9);
        searchResultsList.add(obj10);
        adapter = new searchResultsAdapter(getActivity(), R.layout.searchresultsrow, searchResultsList);
        list.setAdapter(adapter);
        Log.d("CustomAdapter", "Search Results Launched successfully");

        return rootView;
    }

    public void onBackPressed() {
        Log.d("Test","This is being called in searchResults");
        if(getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        }
    }
}