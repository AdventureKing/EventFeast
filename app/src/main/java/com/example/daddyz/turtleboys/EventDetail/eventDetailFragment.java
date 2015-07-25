package com.example.daddyz.turtleboys.EventDetail;


import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.daddyz.turtleboys.R;
import com.example.daddyz.turtleboys.eventfeed.gEventImageObject;
import com.example.daddyz.turtleboys.eventfeed.gEventObject;
import com.example.daddyz.turtleboys.maps.MapsActivity;
import com.squareup.picasso.Picasso;
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
    private ImageView eventImage;
    private DrawerLayout mDrawer;
    private boolean AutoAddFlag;
    private SharedPreferences preferences;
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
        eventImage = (ImageView) view.findViewById(R.id.eventImage);
        eventName = (TextView) view.findViewById(R.id.EventTitle);
        eventName.setText(obj.getTitle());
        eventDate = (TextView) view.findViewById(R.id.EventDate);
        eventDate.setText(obj.getStart_date_month().get(2) + " " + obj.getStart_date_day().get(0) + ", " + obj.getStart_date_year().get(0));


        ImageView locationBtn = (ImageView) view.findViewById((R.id.eventStockImage));
        locationBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(getActivity().getApplicationContext(), MapsActivity.class);
                mapIntent.putExtra("desc", obj.getDescription());
                mapIntent.putExtra("addr", obj.getVenue_address());
                mapIntent.putExtra("lat", obj.getLatitude());
                mapIntent.putExtra("lon", obj.getLongitude());
                startActivity(mapIntent);
            }

        });

        eventLocation = (TextView) view.findViewById(R.id.EventLocation);
        eventLocation.setText(obj.getVenue_address());
        eventDesc = (TextView) view.findViewById(R.id.EventDesc);
        eventDesc.setText(obj.getNotes());

        String placeholderImageUrl = "http://www.grommr.com/Content/Images/placeholder-event-p.png";
        String imageUrl = placeholderImageUrl;
        String imageVenueUrl = placeholderImageUrl;

        //Loop through available image objects to populate image url
        for(gEventImageObject image : obj.getImages()){
            if(null != image.getImage_external_url() && image.getImage_category().equals("attraction")){
                imageUrl = image.getImage_external_url();
                break;
            }
            if(null != image.getImage_external_url() && image.getImage_category().equals("venue")){
                imageVenueUrl = image.getImage_external_url();
                break;
            }
        }


        //If no attraction image url was picked up, set to venue URL.
        //Else it uses default placeholder image I placed above.
        if(imageUrl.equals(placeholderImageUrl) && !imageVenueUrl.equals(placeholderImageUrl)){
            imageUrl = imageVenueUrl;
        }
        Picasso.with(view.getContext()).load(imageUrl).into(eventImage);


        //actionlisteners for the buttons
        calendarButton = (Button) view.findViewById(R.id.AddToCalendarButton);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        AutoAddFlag = preferences.getBoolean("auto_calendar_preference", false);
        calendarButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(AutoAddFlag)
                autocreate();
                else
                createEvent();
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
/*
        //need to set the real times
        Calendar beginTime = Calendar.getInstance();

        beginTime.set(Integer.parseInt(obj.getStart_date_year().get(0)),Integer.parseInt(obj.getStart_date_month().get(0)), Integer.parseInt(obj.getStart_date_day().get(0)), 10, 50);
        //need to set the real end time
        Calendar endTime = Calendar.getInstance();
        endTime.set(7,7, 30, 1, 30);

        //create content that will go into the calendar
        ContentValues calEvent = new ContentValues();
        //create ability to insert into the calendar
        ContentResolver cr = getActivity().getContentResolver();

        //where/when/id_for_insert/start_time/end_time/time_zone
        //need address/description
        calEvent.put(CalendarContract.Events.CALENDAR_ID,1); // XXX pick)
        calEvent.put(CalendarContract.Events.TITLE, obj.getDescription());
        calEvent.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
        calEvent.put(CalendarContract.Events.DTEND, endTime.getTimeInMillis());
        calEvent.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        calEvent.put(CalendarContract.Events.EVENT_LOCATION,obj.getVenue_name());

        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, calEvent);
        //get id for reminders
        int id = Integer.parseInt(uri.getLastPathSegment());
        //create a reminders value and put a reminder for XX mins
        ContentValues reminders = new ContentValues();
        reminders.put(CalendarContract.Reminders.EVENT_ID,id);
        reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        //reminder could be a setting??????????
        reminders.put(CalendarContract.Reminders.MINUTES, 30);
       //insert into the event they just added
        Uri uri2 = cr.insert(CalendarContract.Reminders.CONTENT_URI, reminders);

        Toast.makeText(getActivity(), Integer.parseInt(obj.getStart_date_day().get(0)) + " was added to the Calendar", Toast.LENGTH_SHORT).show();
        //Toast.makeText(getActivity(), obj.getDescription() + " was added to the Calendar", Toast.LENGTH_SHORT).show();

        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.EN_US);
         cal.setTime(sdf.parse("Mon Mar 14 16:02:37 GMT 2011"));
        */
        //need to set the real times
        Calendar beginTime = Calendar.getInstance();
        String date = obj.getStart_date_day().get(0);
        int date_int = Integer.parseInt(obj.getStart_date_day().get(0));
        beginTime.set(Integer.parseInt(obj.getStart_date_year().get(0)), 7, date_int, 10, 50);
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


        Toast.makeText(getActivity(), obj.getStart_date_month().get(0) + " was added to the Calendar", Toast.LENGTH_SHORT).show();
    }
    //manuel add event algo
    public void createEvent(){
        Intent calIntent = new Intent(Intent.ACTION_INSERT);
        calIntent.setType("vnd.android.cursor.item/event");
        calIntent.putExtra(CalendarContract.Events.TITLE, obj.getTitle());
        calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, obj.getVenue_name());
        calIntent.putExtra(CalendarContract.Events.DESCRIPTION, obj.getNotes());
        //instaiate with the time to start
        GregorianCalendar calDate = new GregorianCalendar(Integer.parseInt(obj.getStart_date_year().get(0)), Integer.parseInt(obj.getStart_date_month().get(0)), Integer.parseInt(obj.getStart_date_day().get(0)), 10, 50);
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                calDate.getTimeInMillis());
        calDate.set(Integer.parseInt(obj.getStart_date_year().get(1)),Integer.parseInt(obj.getStart_date_month().get(0)), Integer.parseInt(obj.getStart_date_day().get(0)), 1, 30);
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                calDate.getTimeInMillis());


        startActivity(calIntent);
    }

}
