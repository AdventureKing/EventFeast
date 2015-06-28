package com.example.daddyz.turtleboys.EventDetail;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daddyz.turtleboys.R;

/**
 * Created by snow on 6/25/2015.
 */
public class eventDetailFragment extends Fragment {
    View view;
    eventDetailObject obj;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.eventdetailfragment,container,false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    public void setObj(eventDetailObject obj) {
        this.obj = obj;
    }
    public eventDetailObject getObj() {
        return obj;
    }

}
