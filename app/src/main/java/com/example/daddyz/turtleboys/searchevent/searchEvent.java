package com.example.daddyz.turtleboys.searchevent;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.daddyz.turtleboys.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jinbir on 6/26/2015.
 */
public class searchEvent extends Fragment {

    private EditText city;
    private EditText keyword;
    private TextView searchRadiusText;
    private long searchRadius_miles;
    private double searchRadius_kilometers;
    private View rootView;
    private SeekBar searchRadiusSeekBar;
    private SeekBar.OnSeekBarChangeListener searchRadiusSeekbarListener;

    private Button search;
    private Button reset;

    private EditText toDateText;
    private EditText fromDateText;
    private DialogFragment fromDateSelector;
    private DialogFragment toDateSelector;
    private Date toDate;
    private Date fromDate;

    private RadioGroup radioSortbyGroup;
    private RadioButton sortbyButton;
    private RadioButton defaultButton;

    private EditText toTimeText;
    private EditText fromTimeText;
    private DialogFragment fromTimeSelector;
    private DialogFragment toTimeSelector;
    private Time toTime;
    private Time fromTime;

    private double MILESINAKILOMETER = 0.621;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = inflater.inflate(R.layout.searchevent, container, false);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        //Might use Google geolocation api to autocomplete
        //TODO Add Google's city autocomplete
        city = (EditText) rootView.findViewById(R.id.location);
        city.setHint(R.string.searchLocation);

        //Search keyword for events
        keyword = (EditText) rootView.findViewById(R.id.keyword);
        keyword.setHint(R.string.searchKeyword);

        searchRadiusText = (TextView) rootView.findViewById(R.id.searchRadiusText);

        //Sortby RadioGroup
        radioSortbyGroup = (RadioGroup) rootView.findViewById(R.id.sortGroup);
        sortbyButton = (RadioButton) rootView.findViewById(radioSortbyGroup.getCheckedRadioButtonId());
        defaultButton = (RadioButton) rootView.findViewById(R.id.sortRelevance);

        //SearchRadius Seekbar
        searchRadiusSeekBar = (SeekBar) rootView.findViewById(R.id.searchRadius);
        //Inital Seekbar values
        searchRadius_miles = searchRadiusSeekBar.getProgress()+1;
        searchRadius_kilometers = searchRadius_miles / MILESINAKILOMETER;
        searchRadiusText.setText(String.format("Set Search Radius\t %d miles / %.02f km"
                , searchRadius_miles, searchRadius_kilometers));

        searchRadiusSeekbarListener =
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        searchRadius_miles = progress+1;
                        searchRadius_kilometers = searchRadius_miles / MILESINAKILOMETER;
                        searchRadiusText.setText(String.format("Set Search Radius\t %d miles / %.02f km"
                                , searchRadius_miles, searchRadius_kilometers));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                };
        searchRadiusSeekBar.setOnSeekBarChangeListener(searchRadiusSeekbarListener);

        //fromDate date picker
        fromDateSelector = new DatePickerFragment();
        fromDateText = (EditText) rootView.findViewById(R.id.fromDate);
        fromDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromDateSelector.registerForContextMenu(fromDateText);
                fromDateSelector.show(getFragmentManager(), "Date Picker");
            }
        });

        //toDate date picker
        toDateSelector = new DatePickerFragment();
        toDateText = (EditText) rootView.findViewById(R.id.toDate);
        toDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toDateSelector.registerForContextMenu(toDateText);
                toDateSelector.show(getFragmentManager(), "Date Picker");
            }
        });

        //fromTime time picker
        fromTimeSelector = new TimePickerFragment();
        fromTimeText = (EditText) rootView.findViewById(R.id.fromTime);
        fromTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromTimeSelector.registerForContextMenu(fromTimeText);
                fromTimeSelector.show(getFragmentManager(), "Time Picker");
            }
        });

        //toTime time picker
        toTimeSelector = new TimePickerFragment();
        toTimeText = (EditText) rootView.findViewById(R.id.toTime);
        toTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toTimeSelector.registerForContextMenu(toTimeText);
                toTimeSelector.show(getFragmentManager(), "Time Picker");
            }
        });

        //Search Button
        search = (Button) rootView.findViewById(R.id.searchButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Do a search
                searchResultsFragment fragment = new searchResultsFragment();
                fragment.setSearchQuery(keyword.getText().toString());
                fragment.setFilterCity(city.getText().toString());

                getFragmentManager().beginTransaction().replace(R.id.frame,fragment,"searchResultsFragment").addToBackStack("searchResultsFragment").commit();
                //Toast.makeText(getActivity().getApplicationContext(), "Search Event", Toast.LENGTH_SHORT).show();
            }
        });

        //Reset all search fields; Nuke it button
        reset = (Button) rootView.findViewById(R.id.resetButton);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                city.setText("");
                keyword.setText("");
                radioSortbyGroup.clearCheck();
                defaultButton.toggle();
                fromTimeText.setText(R.string.anyTime);
                toTimeText.setText(R.string.anyTime);
                fromDateText.setText(R.string.anyDay);
                toDateText.setText(R.string.anyDay);
                searchRadiusSeekBar.setProgress(9);
                //Toast.makeText(getActivity().getApplicationContext(), "Reset fields", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onStop() {
        super.onStop();
    }





    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        @Override
        public void registerForContextMenu(View v) {
            dateset = (EditText) v;
        }

        private EditText dateset;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            Calendar time = Calendar.getInstance();
            String am_pm, timeformat;

            time.set(Calendar.MONTH, month);
            time.set(Calendar.DATE, day);
            time.set(Calendar.YEAR, year);

            timeformat = (String) android.text.format.DateFormat.format("M/dd/yyyy", time);
            dateset.setText(timeformat);
        }

    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {


        @Override
        public void registerForContextMenu(View v) {
            timeset = (EditText) v;
        }

        private EditText timeset;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default time in the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute, false);
        }

        public void onTimeSet(TimePicker view, int hour, int minute) {
            // Do something with the time chosen by the user
            String am_pm, minuteString;

            if ( hour > 12) {
                am_pm = "PM";
                hour -= 12;
            } else if ( hour == 12 ) {
                am_pm = "PM";
            } else if (hour == 0 ) {
                am_pm = "AM";
                hour = 12;
            } else{
                am_pm = "AM";
            }

            if ( minute < 10) {
                minuteString = "0"+Integer.toString(minute);
            } else {
                minuteString = Integer.toString(minute);
            }

            timeset.setText(hour + ":" + minuteString + " " + am_pm);
        }

    }
    public void onBackPressed() {
        Log.d("Test", "This is being called in maindrawer");
        if(getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        }
    }

}
