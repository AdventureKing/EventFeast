package com.example.daddyz.turtleboys.newsfeed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.daddyz.turtleboys.R;

import java.util.ArrayList;

/**
 * Created by Admin on 04-06-2015.
 */
public class NewsFeedFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.listfragment, container, false);

        ListView list = (ListView) rootView.findViewById(R.id.listView);

        // create newsfeed array from newsfeed request
        // newsfeedRequest dataRequest= new newsfeedRequest();

        // newsfeedRequest myDataArray[]= dataRequest.getTableData();
        //String myDataArray[] = {"hello", "hi", "hi again"};
        //newsfeedObject[] myDataArray = new newsfeedObject[1];
        //myDataArray[0].setEventDesc("Event Description");
        //myDataArray[0].setEventCity("San Antonio");
        //myDataArray[0].setEventDate("07/07/2015");

        newsfeedObject obj1 = new newsfeedObject();
        obj1.setEventDesc("Event Description");
        obj1.setEventCity("San Antonio");
        obj1.setEventDate("07/07/2015");
        ArrayList<newsfeedObject> myDataArray = new ArrayList<>();

        myDataArray.add(obj1);

        newsfeedAdapter adapter = new newsfeedAdapter(getActivity(), R.layout.newsfeedroweven, myDataArray);
        list.setAdapter(adapter);

        Log.d("CustomAdapter", "MusicFragment onCreateView successful");

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}