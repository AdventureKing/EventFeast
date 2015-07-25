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

    //need this to replace the frame in the main activity
    private View rootView;

    //just a blanket on create view
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = inflater.inflate(R.layout.feedtablayout, container, false);


        return rootView;
    }
}