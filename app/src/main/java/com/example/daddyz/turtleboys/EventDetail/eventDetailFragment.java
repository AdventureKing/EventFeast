package com.example.daddyz.turtleboys.EventDetail;


import android.app.Fragment;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daddyz.turtleboys.R;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by snow on 6/25/2015.
 */
public class eventDetailFragment extends Fragment {
    private View view;
    private EventDetailObject obj;
    private Toolbar toolbar;
    private ActionBar actionbar;
    private Button calendarButton;
    private Button shareButton;
    private Button buyButton;

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

        calendarButton = (Button) view.findViewById(R.id.AddToCalendarButton);
        calendarButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // create Intent to take a picture and return control to the calling application
             /*   Intent calIntent = new Intent(Intent.ACTION_INSERT);
                calIntent.setType("vnd.android.cursor.item/event");
                calIntent.putExtra(CalendarContract.Events.TITLE, "My House Party");
                calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, "My Beach House");
                calIntent.putExtra(CalendarContract.Events.DESCRIPTION, "A Pig Roast on the Beach");
                //instaiate with the time to start
                GregorianCalendar calDate = new GregorianCalendar(2015, 7, 15,12,15,10);
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                        calDate.getTimeInMillis());
                calDate.set(2015, 7, 15,17,15,10);
                calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                        calDate.getTimeInMillis());


                startActivity(calIntent);*/

                autocreate();
            }
        });

        //actionlisteners for the buttons

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
    public void autocreate(){
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2015, 7, 20, 7, 30);
        Calendar endTime = Calendar.getInstance();
        endTime.set(2015, 7, 20, 8, 30);


        ContentValues calEvent = new ContentValues();
        calEvent.put(CalendarContract.Events.CALENDAR_ID, 1); // XXX pick)
        calEvent.put(CalendarContract.Events.TITLE, "title is game time");
        calEvent.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
        calEvent.put(CalendarContract.Events.DTEND, endTime.getTimeInMillis());
        calEvent.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        Uri uri = getActivity().getContentResolver().insert(CalendarContract.Events.CONTENT_URI, calEvent);

        // The returned Uri contains the content-retriever URI for
        // the newly-inserted event, including its id
        int id = Integer.parseInt(uri.getLastPathSegment());
        Toast.makeText(getActivity(), "EVENT_TITLE WAS ADDED TO THE CALENDAR", Toast.LENGTH_SHORT).show();
    }
}
