package com.example.daddyz.turtleboys.EventDetail;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.daddyz.turtleboys.R;

/**
 * Created by snow on 6/25/2015.
 */
public class eventDetailFragment extends Fragment {
    private View view;
    private EventDetailObject obj;
    private Toolbar toolbar;
    private ActionBar actionbar;


    private TextView eventName;
    private TextView eventDate;
    private TextView eventLocation;
    private TextView eventDesc;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.eventdetailfragment,container,false);

        //toolbar setup

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setTitle("Event Detail");
        final FrameLayout frame = (FrameLayout) container.findViewById(R.id.frame);
        frame.setVisibility(View.INVISIBLE);



        //Set up back arrow icon on toolbar
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(frame);
            }
        });


        //set the stuff on the page
        eventName = (TextView) view.findViewById(R.id.EventTitle);
        eventName.setText(obj.getEventDesc());
        eventDate = (TextView) view.findViewById(R.id.EventDate);
        eventDate.setText(obj.getEventDate());
        eventLocation = (TextView) view.findViewById(R.id.EventLocation);
        eventLocation.setText(obj.getEventAddress());
        eventDesc = (TextView) view.findViewById(R.id.EventDesc);
        eventDesc.setText(obj.getEventDesc());

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    public void setObj(EventDetailObject obj) {
        this.obj = obj;
    }
    public EventDetailObject getObj() {
        return obj;
    }

    public void onBackPressed(FrameLayout frame) {

        if(getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
            frame.setVisibility(View.VISIBLE);

        }
    }
}
