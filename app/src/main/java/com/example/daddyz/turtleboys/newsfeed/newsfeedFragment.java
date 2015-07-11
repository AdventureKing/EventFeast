package com.example.daddyz.turtleboys.newsfeed;

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

import com.android.volley.RequestQueue;
import com.example.daddyz.turtleboys.R;

import java.util.ArrayList;

/**
 * Created by snow on 7/11/2015.
 */
public class newsfeedFragment extends Fragment{

    public static final String REQUEST_TAG = "MainVolleyActivity";
    private Button mButton;

    private TextView mTextView;
    private RequestQueue mQueue;
    private View rootView;
    private ListView list;
    private newsfeedAdapter adapter;
    private ArrayList<newsfeedObject> newsfeedList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = inflater.inflate(R.layout.newsfeedlistfragment, container, false);
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
        newsfeedList = new ArrayList<newsfeedObject>();
        newsfeedObject obj = new newsfeedObject();
        newsfeedList.add(obj);
        adapter = new newsfeedAdapter(getActivity(), R.layout.eventfeedroweven, newsfeedList);
        list.setAdapter(adapter);
        Log.d("CustomAdapter", "Newsfeed Launched successfully");

        return rootView;
    }
}
