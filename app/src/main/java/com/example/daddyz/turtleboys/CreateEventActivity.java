package com.example.daddyz.turtleboys;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.daddyz.turtleboys.eventfeed.gEventObject;
import com.example.daddyz.turtleboys.searchevent.searchResultsFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreateEventActivity extends Activity {

    private EditText eventName;
    private EditText eventDescription;
    private DialogFragment eventDateSelector;
    private Date eventDate;
    private EditText eventDateText;
    private EditText start_date_month;
    private EditText start_date_day;
    private EditText start_date_year;
    private EditText start_date_time;
    private EditText eventAddress;
    private Button saveButton;


    private TextView tvDisplayTime;
    private TimePicker timePicker1;
    private Button btnChangeTime;
    private int hour;
    private int minute;
    static final int TIME_DIALOG_ID = 999;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);


        eventName = (EditText) findViewById(R.id.eventName);
        eventName.setHint(R.string.eventname_text);
        eventDescription = (EditText) findViewById(R.id.eventDescription);
        eventDescription.setHint(R.string.eventDescription_text);
        eventAddress = (EditText) findViewById(R.id.eventAddress);
        eventAddress.setHint(R.string.eventAddress_text);


        eventDateSelector = new registration_activity.DatePickerFragment();
        eventDateText = (EditText) findViewById(R.id.eventDate);
        eventDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventDateSelector.registerForContextMenu(eventDateText);
                eventDateSelector.show(getFragmentManager(), "Date Picker");
            }
        });

        String[] dates = eventDateText.getText().toString().split("/");
       if ( dates.length < 3 ) {
            //Toast.makeText(getApplicationContext(), "Select Event Date" , Toast.LENGTH_SHORT).show();
            return;
        }

        int tempMonth=Integer.parseInt(dates[0]);
        tempMonth -= 1;
        int tempYear =Integer.parseInt(dates[2]);
        tempYear -= 1900;
        int tempDay = Integer.parseInt(dates[1]);



        eventDate = new Date(tempYear,tempMonth,tempDay);
        saveButton = (Button) findViewById(R.id.sButton);
       // setCurrentTimeOnView();
       // addListenerOnButton();


    }

    public void doSaveFunction(View view){
        ArrayList<String> datetime = new ArrayList<>();

        //Do a search
        gEventObject fragment = new gEventObject();

        fragment.setTitle(eventName.getText().toString());
        fragment.setDescription(eventDescription.getText().toString());
        fragment.setVenue_address(eventAddress.getText().toString());
        datetime.add(eventDateText.getText().toString());
        fragment.setStart_date_time(datetime);

        Log.i("name", eventName.getText().toString());
        Log.i("desc", eventDescription.getText().toString());
        Log.i("address",eventAddress.getText().toString());
        Log.i("date",eventDateText.getText().toString());

        //getFragmentManager().beginTransaction().replace(R.id.frame,fragment,"searchResultsFragment").addToBackStack("searchResultsFragment").commit();
        Toast.makeText(getApplicationContext(), "Submitted new event", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }








    // display current time
    public void setCurrentTimeOnView() {

       // tvDisplayTime = (TextView) findViewById(R.id.tvTime);
        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);

        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        // set current time into textview
       /* tvDisplayTime.setText(
                new StringBuilder().append(pad(hour))
                        .append(":").append(pad(minute)));

        // set current time into timepicker*/
        timePicker1.setCurrentHour(hour);
        timePicker1.setCurrentMinute(minute);

    }

    public void addListenerOnButton() {

        btnChangeTime = (Button) findViewById(R.id.btnChangeTime);

        btnChangeTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                showDialog(TIME_DIALOG_ID);

            }

        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                // set time picker as current time
                return new TimePickerDialog(this,
                        timePickerListener, hour, minute,false);

        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {
                    hour = selectedHour;
                    minute = selectedMinute;

                    // set current time into textview
                    tvDisplayTime.setText(new StringBuilder().append(pad(hour))
                            .append(":").append(pad(minute)));

                    // set current time into timepicker
                    timePicker1.setCurrentHour(hour);
                    timePicker1.setCurrentMinute(minute);

                }
            };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }


}
