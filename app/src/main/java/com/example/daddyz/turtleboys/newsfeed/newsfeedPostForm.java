package com.example.daddyz.turtleboys.newsfeed;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.daddyz.turtleboys.R;

/**
 * Created by snow on 7/13/2015.
 */
public class newsfeedPostForm extends Fragment {

    private View view;
    private newsfeedObject obj;
    private Toolbar toolbar;
    private ActionBar actionbar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //lock the drawer because we are inception in this bitch
        //main_activity->fragment->fragment

        view = inflater.inflate(R.layout.newsfeed_post_form,container,false);

        //toolbar setup

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        actionbar.setTitle("Newsfeed Post Form");

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


        return view;
    }
    public void onBackPressed(FrameLayout frame) {

        if(getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
            frame.setVisibility(View.VISIBLE);

        }
    }
}
