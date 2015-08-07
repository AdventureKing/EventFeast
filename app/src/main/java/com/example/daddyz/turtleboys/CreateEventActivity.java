package com.example.daddyz.turtleboys;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);




       /* View view = findViewById(R.id.create_event);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
              //  InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                //        INPUT_METHOD_SERVICE);
                //imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });*/


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
            Toast.makeText(getApplicationContext(), "Select Event Date" , Toast.LENGTH_SHORT).show();
            return;
        }

        int tempMonth=Integer.parseInt(dates[0]);
        tempMonth -= 1;
        int tempYear =Integer.parseInt(dates[2]);
        tempYear -= 1900;
        int tempDay = Integer.parseInt(dates[1]);



        eventDate = new Date(tempYear,tempMonth,tempDay);








        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getApplicationContext(), CreateEventActivity.class);
                //startActivity(intent);

                Toast.makeText(getApplicationContext(), "Save Button Pressed", Toast.LENGTH_SHORT).show();

            }
        });





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
}
