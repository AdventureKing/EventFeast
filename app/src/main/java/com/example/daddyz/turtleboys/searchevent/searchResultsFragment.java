package com.example.daddyz.turtleboys.searchevent;

import android.app.Fragment;
import android.os.Bundle;
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
import com.example.daddyz.turtleboys.R;
import com.example.daddyz.turtleboys.eventfeed.gEventObject;

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
    private ArrayList<gEventObject> searchResultsList;
    //private  FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = inflater.inflate(R.layout.searchresultslistfragment, container, false);
        list = (ListView) rootView.findViewById(R.id.listView);

        mTextView = (TextView) rootView.findViewById(R.id.textView);
        mButton = (Button) rootView.findViewById(R.id.button);
        list.setClickable(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //get object at that position
                //this code is gonna get nocked out monday

                //start the fragment

                //this is where we are gonna

            }
        });

        /*
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Toast.makeText(getActivity(), "User Wants to make a post", Toast.LENGTH_SHORT).show();
            }
        });
        */
        searchResultsList = new ArrayList<gEventObject>();
        gEventObject obj = new gEventObject();
        gEventObject obj3 = new gEventObject();
        gEventObject obj4 = new gEventObject();
        gEventObject obj5 = new gEventObject();
        gEventObject obj6 = new gEventObject();
        gEventObject obj7 = new gEventObject();
        gEventObject obj8 = new gEventObject();
        gEventObject obj9 = new gEventObject();
        gEventObject obj10 = new gEventObject();
        searchResultsList.add(obj);
        searchResultsList.add(obj3);
        searchResultsList.add(obj4);
        searchResultsList.add(obj5);
        searchResultsList.add(obj6);
        searchResultsList.add(obj7);
        searchResultsList.add(obj8);
        searchResultsList.add(obj9);
        searchResultsList.add(obj10);
        adapter = new searchResultsAdapter(getActivity(), R.layout.eventfeedroweven, searchResultsList);
        list.setAdapter(adapter);
        Log.d("CustomAdapter", "Search Results Launched successfully");

        return rootView;
    }
}