package com.example.daddyz.turtleboys.searchevent;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.daddyz.turtleboys.R;

/**
 * Created by Jinbir on 6/26/2015.
 */
public class searchEvent extends Fragment {

    private EditText location;
    private EditText keyword;
    private TextView searchRadiusText;
    private long searchRadius_miles;
    private double searchRadius_kilometers;
    private View rootView;
    private SeekBar searchRadiusSeekBar;
    private SeekBar.OnSeekBarChangeListener searchRadiusSeekbarListener;

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
        location = (EditText) rootView.findViewById(R.id.location);
        location.setHint(R.string.searchLocation);

        //Search keyword for events
        keyword = (EditText) rootView.findViewById(R.id.keyword);
        keyword.setHint(R.string.searchKeyword);

        searchRadiusText = (TextView) rootView.findViewById(R.id.searchRadiusText);

        //SearchRadius Seekbar
        searchRadiusSeekBar = (SeekBar) rootView.findViewById(R.id.searchRadius);
        searchRadiusSeekBar.setOnSeekBarChangeListener(searchRadiusSeekbarListener);


        searchRadiusSeekbarListener =
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        searchRadius_miles = progress;
                        searchRadius_kilometers = searchRadius_miles / MILESINAKILOMETER;
                        searchRadiusText.setText( String.format("Set Search Radius\t %f kms / %d miles"
                                , searchRadius_kilometers, searchRadius_miles));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                };


    }

    @Override
    public void onStop() {
        super.onStop();
    }


}
