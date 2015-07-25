package com.example.daddyz.turtleboys.feedTabView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daddyz.turtleboys.R;

/**
 * Created by snow on 7/23/2015.
 */
public class feedtabview extends Fragment {


    private View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //lock the drawer because we are inception in this bitch
        //main_activity->fragment->fragment

        view = inflater.inflate(R.layout.feedtablayout,container,false);

        return view;
    }
}