package com.example.daddyz.turtleboys.newsfeed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.daddyz.turtleboys.R;

/**
 * Created by Admin on 04-06-2015.
 */
public class NewsFeedFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.listfragment, container, false);

        ListView list = (ListView) rootView.findViewById(R.id.listView);
        newsfeedObject myDataArray[]=new newsfeedObject[]{
                new newsfeedObject("item1","10","tbd"),
                new newsfeedObject("item2","20","tbd"),
                new newsfeedObject("item3","30","tbd"),
                new newsfeedObject("item1","10","tbd"),
                new newsfeedObject("item2","20","tbd"),
                new newsfeedObject("item3","30","tbd")
        };

        newsfeedAdapter adapter = new newsfeedAdapter(getActivity() , R.layout.newsfeedrow, myDataArray);
        list.setAdapter(adapter);

        Log.d("CustomAdapter", "MusicFragment onCreateView successful");

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}