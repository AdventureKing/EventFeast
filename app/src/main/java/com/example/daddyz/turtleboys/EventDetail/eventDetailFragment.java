package com.example.daddyz.turtleboys.EventDetail;


import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daddyz.turtleboys.R;
import com.example.daddyz.turtleboys.eventfeed.gEventObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by snow on 6/25/2015.
 */
public class eventDetailFragment extends Fragment {
    private View view;
    private gEventObject obj;
    private Toolbar toolbar;
    private ActionBar actionbar;
    private Button calendarButton;
    private Button shareButton;
    private Button buyButton;

    private TextView eventName;
    private TextView eventDate;
    private TextView eventLocation;
    private TextView eventDesc;
    private DrawerLayout mDrawer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //lock the drawer because we are inception in this bitch
        //main_activity->fragment->fragment




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
        eventName.setText(obj.getDescription());
        eventDate = (TextView) view.findViewById(R.id.EventDate);
        eventDate.setText(obj.getStart_date_month().get(2) + " " + obj.getStart_date_day().get(0) + ", " + obj.getStart_date_year().get(0));
        eventLocation = (TextView) view.findViewById(R.id.EventLocation);
        eventLocation.setText(obj.getVenue_address());
        eventDesc = (TextView) view.findViewById(R.id.EventDesc);
        eventDesc.setText(obj.getDescription());


        //actionlisteners for the buttons
        calendarButton = (Button) view.findViewById(R.id.AddToCalendarButton);
        calendarButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //createEvent();
                autocreate();
            }
        });

        buyButton = (Button) view.findViewById(R.id.BuyTicketsButton);

        buyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               Intent webBrowser = new Intent(Intent.ACTION_VIEW,Uri.parse(obj.getEvent_external_url()));
                startActivity(webBrowser);
            }
        });

        shareButton = (Button) view.findViewById(R.id.ShareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "SHARE COMMING SOON", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    public void setObj(gEventObject obj) {
        this.obj = obj;
    }
    public gEventObject getObj() {
        return obj;
    }

    public void onBackPressed(FrameLayout frame) {

        if(getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
            frame.setVisibility(View.VISIBLE);

        }
    }

    //auto add event algo
    public void autocreate(){

        //need to set the real times
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2015, 6, 4, 10, 50);
        //need to set the real end time
        Calendar endTime = Calendar.getInstance();
        endTime.set(2015, 6, 4, 12, 30);

        //create content that will go into the calendar
        ContentValues calEvent = new ContentValues();
        //create ability to insert into the calendar
        ContentResolver cr = getActivity().getContentResolver();

        //where/when/id_for_insert/start_time/end_time/time_zone
        //need address/description
        calEvent.put(CalendarContract.Events.CALENDAR_ID, 1); // XXX pick)
        calEvent.put(CalendarContract.Events.TITLE, obj.getDescription());
        calEvent.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
        calEvent.put(CalendarContract.Events.DTEND, endTime.getTimeInMillis());
        calEvent.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        calEvent.put(CalendarContract.Events.EVENT_LOCATION,obj.getVenue_name());

        Uri uri = getActivity().getContentResolver().insert(CalendarContract.Events.CONTENT_URI, calEvent);
        //get id for reminders
        int id = Integer.parseInt(uri.getLastPathSegment());
        //create a reminders value and put a reminder for XX mins
        ContentValues reminders = new ContentValues();
        reminders.put(CalendarContract.Reminders.EVENT_ID,id);
        reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        //reminder could be a setting??????????
        reminders.put(CalendarContract.Reminders.MINUTES, 3);
       //insert into the event they just added
        Uri uri2 = cr.insert(CalendarContract.Reminders.CONTENT_URI, reminders);


        Toast.makeText(getActivity(), obj.getDescription() + " was added to the Calendar", Toast.LENGTH_SHORT).show();
    }
    //manuel add event algo
    public void createEvent(){
        Intent calIntent = new Intent(Intent.ACTION_INSERT);
        calIntent.setType("vnd.android.cursor.item/event");
        calIntent.putExtra(CalendarContract.Events.TITLE, obj.getTitle());
        calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, "My Beach House");
        calIntent.putExtra(CalendarContract.Events.DESCRIPTION, "A Pig Roast on the Beach");
        //instaiate with the time to start
        GregorianCalendar calDate = new GregorianCalendar(2015, 7, 15,12,15,10);
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                calDate.getTimeInMillis());
        calDate.set(2015, 7, 15,17,15,10);
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                calDate.getTimeInMillis());


        startActivity(calIntent);
    }
}
