package com.example.daddyz.turtleboys.newsfeed;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daddyz.turtleboys.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by snow on 7/11/2015.
 */
public class newsfeedPostDetail extends Fragment {



    //global vars
    private View view;
    private newsfeedObject obj;
    private Toolbar toolbar;
    private ActionBar actionbar;
    private ImageView eventImage;
    private TextView eventDesc;
    private TextView eventTitle;
    private TextView likeCount;
    private TextView username;

    private FloatingActionButton likeButton;

    private ListView userCommentsList;

    private newsfeedCommentAdapter adapter1;

    ArrayList<newsfeedCommentObjects> userComments1;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //lock the drawer because we are inception in this bitch
        //main_activity->fragment->fragment

        view = inflater.inflate(R.layout.newsfeedpostdetail,container,false);

        //toolbar setup

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setTitle("Newsfeed Post Detail");
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
                getActivity().onBackPressed();
            }
        });

        eventDesc = (TextView) view.findViewById(R.id.EventDesc);
        eventDesc.setText(obj.getTitle() + "\r\n " + obj.getDescription());

        username = (TextView) view.findViewById(R.id.userNameTag);
        username.setText(obj.getUsername());

        likeCount = (TextView) view.findViewById(R.id.numberOfLikes);
        likeCount.setText(Integer.toString(obj.getLikeCount()) + " likes");

        eventTitle = (TextView) view.findViewById(R.id.example);

        eventImage = (ImageView) view.findViewById(R.id.eventImage);

        eventImage = (ImageView) view.findViewById(R.id.eventImage);

        if(null != obj.getPostImageUrl()){
            Picasso.with(this.getActivity().getApplicationContext()).load(obj.getPostImageUrl()).placeholder(R.drawable.events_calendar_icon).into(eventImage);
        } else{
            Picasso.with(this.getActivity().getApplicationContext()).load(R.drawable.events_calendar_icon).into(eventImage);
        }

        eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "User wants to view image closely", Toast.LENGTH_SHORT).show();
            }
        });
        //set the stuff on the page

        //like fab button
        likeButton = (FloatingActionButton) view.findViewById(R.id.fabLIKE);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "User LIKES THIS POST", Toast.LENGTH_SHORT).show();
            }
        });

        //comments on a users post
        userCommentsList = (ListView) view.findViewById(R.id.userCommentsList);

        ///fill comment list from webservice
        userComments1 = new ArrayList<newsfeedCommentObjects>();
        newsfeedCommentObjects fakeComment = new newsfeedCommentObjects();
        //fake data
        fakeComment.setUserComment("I AM THE SMARTEST MAN ALIVE");
        fakeComment.setUserName("BILLY MADISON");
        //fill comment table with fake data
        userComments1.add(fakeComment);
        userComments1.add(fakeComment);
        userComments1.add(fakeComment);
        userComments1.add(fakeComment);
        userComments1.add(fakeComment);
        userComments1.add(fakeComment);
        userComments1.add(fakeComment);
        userComments1.add(fakeComment);
        userComments1.add(fakeComment);
        userComments1.add(fakeComment);
        userComments1.add(fakeComment);
        //webservice call to fill arrayList Full of bullshit

        //create adapter
        adapter1 = new newsfeedCommentAdapter(getActivity(), R.layout.eventfeedroweven,userComments1);
        //set adapter for comments to a post
        userCommentsList.setAdapter(adapter1);

        //allow user to scroll through the user comment listlist
        userCommentsList.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
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
//gettters and setters for the newsfeedobject
    public newsfeedObject getObj() {
        return obj;
    }

    public void setObj(newsfeedObject obj) {
        this.obj = obj;
    }

    public TextView getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(TextView eventDesc) {
        this.eventDesc = eventDesc;
    }

    public TextView getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(TextView eventTitle) {
        this.eventTitle = eventTitle;
    }

    public TextView getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(TextView likeCount) {
        this.likeCount = likeCount;
    }

    public TextView getUsername() {
        return username;
    }

    public void setUsername(TextView username) {
        this.username = username;
    }
}
