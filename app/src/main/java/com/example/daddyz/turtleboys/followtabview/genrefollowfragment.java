package com.example.daddyz.turtleboys.followtabview;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.daddyz.turtleboys.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by snow on 8/12/2015.
 */
public class genrefollowfragment extends Fragment {

    private View inflatedView;
    //the over all pager that moves between the tabs
    private ViewPager viewPager;
    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> genreCollection;
    ExpandableListView expListView;
    ExpandableListAdapter expListAdapter;

    public genrefollowfragment() {
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //lock the drawer because we are inception in this bitch
        //main_activity->fragment->fragment

        inflatedView = inflater.inflate(R.layout.genrefollowlist, container, false);

        createGroupList();

        createCollection();

        expListView = (ExpandableListView) inflatedView.findViewById(R.id.genre_list);
        final genrefollowlistadapter expListAdapter;
        expListAdapter = new genrefollowlistadapter(
                getActivity(), groupList, genreCollection);
        expListView.setAdapter(expListAdapter);

        //setGroupIndicatorToRight();

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                final String selected = (String) expListAdapter.getChild(
                        groupPosition, childPosition);
                Toast.makeText(getActivity(), selected, Toast.LENGTH_LONG)
                        .show();

                return true;
            }
        });

        return inflatedView;
    }
    private void createGroupList() {
        //TODO add genres from database
        groupList = new ArrayList<String>();
        groupList.add("music");
        groupList.add("Sports");
        groupList.add("Local Events");

    }

    private void createCollection() {
        // preparing genres collection(child)
        String[] musicModels = { "Classics", "80's & 90's",
                "Rock" };

        String[] LocalEventsModels = { "Limited Time", "City",
                "Parks", "Movies" };
        String[] SportsModels = { "Football", "BasketBall", "Soccer" };

        genreCollection = new LinkedHashMap<String, List<String>>();

        for (String genre : groupList) {
            if (genre.equals("music")) {
                loadChild(musicModels);
            } else if (genre.equals("Sports"))
                loadChild(SportsModels);
            else
                loadChild(LocalEventsModels);

            genreCollection.put(genre, childList);
        }
    }
    private void loadChild(String[] genreModels) {
        childList = new ArrayList<String>();
        for (String model : genreModels)
            childList.add(model);
    }
}
