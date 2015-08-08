package com.example.daddyz.turtleboys.my_experiences;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daddyz.turtleboys.R;
import com.example.daddyz.turtleboys.eventfeed.gEventObject;
import com.example.daddyz.turtleboys.subclasses.GigUser;
import com.example.daddyz.turtleboys.subclasses.User_Icon_Animation;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by snow on 7/29/2015.
 */
public class statspagefragment extends Fragment {

    private View view;
    private boolean AnimationFlag;
    private SharedPreferences preferences;
    private GigUser currentUser;
    private TextView userFirstName;
    private TextView userLastName;
    private TextView userPosts;
    private TextView userNumberAttended;
    private TextView userLevel;
    private RelativeLayout userStatistics;
    private final Animation User_Icon = new User_Icon_Animation(com.example.daddyz.turtleboys.subclasses.User_Icon_Animation.Rotate.RIGHT, User_Icon_Animation.Angle.TO_DEGREES_0, 500, false);
    private final Animation user_statistics_animation = new User_Icon_Animation(com.example.daddyz.turtleboys.subclasses.User_Icon_Animation.Rotate.DOWN, User_Icon_Animation.Angle.TO_DEGREES_0, 500, false);
    private ListView list;
    private historyAdapter historyadapter;
    private ArrayList<gEventObject> historyList;
    private ParseImageView userAvatar;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentUser = ParseUser.createWithoutData(GigUser.class, ParseUser.getCurrentUser().getObjectId());
        view = inflater.inflate(R.layout.statspage, container, false);
// Initializing Toolbar and setting it as the actionbar


        //Retrieve preferences and prepare fragment listener to allow dynamic changes to preferences while in fragments
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        AnimationFlag = preferences.getBoolean("animation_preference", false);

        ParseFile image = (ParseFile) currentUser.getUserImage();
        userAvatar = (ParseImageView) view.findViewById(R.id.userImage);
        userAvatar.setParseFile(image);



        userAvatar.loadInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, com.parse.ParseException e) {
                // The image is loaded and displayed!
                int oldHeight = userAvatar.getHeight();
                int oldWidth = userAvatar.getWidth();
                Log.v("LOG!!!!!!", "imageView height = " + oldHeight);      // DISPLAYS 90 px
                Log.v("LOG!!!!!!", "imageView width = " + oldWidth);        // DISPLAYS 90 px

            }
        });

        //get FrameLayout of grey area of screen for animation purposes
        userStatistics = (RelativeLayout) view.findViewById(R.id.userStatistics);

        userFirstName = (TextView) view.findViewById(R.id.userFirstName);
        userFirstName.setText(currentUser.getFirstName());

        userLastName = (TextView) view.findViewById(R.id.userLastName);
        userLastName.setText(currentUser.getLastName());

        userPosts = (TextView) view.findViewById(R.id.numberOfPosts);
        if (currentUser.getUserTotalPost() != null)
            userPosts.setText(currentUser.getUserTotalPost().toString());
        else {
            userPosts.setText("0");
            currentUser.setUserTotalPost(0);
        }

        userNumberAttended = (TextView) view.findViewById(R.id.numberOfMeals);
        if (currentUser.getUserEventsAttended() != null)
            userNumberAttended.setText(currentUser.getUserEventsAttended().toString());
        else {
            userNumberAttended.setText("0");
            currentUser.setUserEventsAttended(0);
        }

        userLevel = (TextView) view.findViewById(R.id.userRank);
        if (currentUser.getUserLevel() != null)
            userLevel.setText(currentUser.getUserLevel().toString());
        else {
            userLevel.setText("0");
            currentUser.setUserLevel(0);
        }

        list = (ListView) view.findViewById(R.id.historyListView);
        list.setClickable(false);

        //Get History and load up the listview
        historyList = getTimeCapsule();
        historyadapter = new historyAdapter(getActivity(), R.layout.eventfeedroweven, historyList);
        list.setAdapter(historyadapter);

        if (AnimationFlag){
            userStatistics.startAnimation(user_statistics_animation);
        }


        return view;
    }
    //builds the list put cloud code here
    public ArrayList<gEventObject> getTimeCapsule(){
        historyList = new ArrayList<>();

        //The following is to hard code some history items.  This will be replaced when the backend is ready
        gEventObject obj = new gEventObject();
        obj.setTitle("You posted \"Dude!  I just saw this cool wumpa tree at Brackenridge!\"");
        ArrayList<String> month = new ArrayList<>();
        month.add("0");
        month.add("1");
        month.add("Jul");
        obj.setStart_date_month(new ArrayList(month));
        ArrayList<String> day = new ArrayList<>();
        day.add("18");
        obj.setStart_date_day(new ArrayList(day));
        ArrayList<String> year = new ArrayList<>();
        year.add("2015");
        obj.setStart_date_year(new ArrayList(year));
        ArrayList<String> time = new ArrayList<>();
        time.add("0");
        time.add("1");
        time.add("2:45PM");
        obj.setStart_date_time(new ArrayList(time));
        obj.setState_name("TX");
        obj.setCity_name("San Antonio");
        historyList.add(obj);

        for(int i = 0; i < 16; i++) {
            obj = new gEventObject();
            obj.setTitle("You joined GigIT!");
            obj.setStart_date_month(new ArrayList(month));
            day.set(0, "16");
            obj.setStart_date_day(new ArrayList(day));
            obj.setStart_date_year(new ArrayList(year));
            time.set(2, "1:16AM");
            obj.setStart_date_time(new ArrayList(time));
            obj.setState_name("TX");
            obj.setCity_name("San Antonio");
            historyList.add(obj);
        }

        return historyList;
    }
}
