package com.example.daddyz.turtleboys;

/**
 * Created by snow on 6/13/2015.
 */

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.daddyz.turtleboys.EventDetail.eventDetailFragment;
import com.example.daddyz.turtleboys.feedTabView.feedtabview;
import com.example.daddyz.turtleboys.friendFeed.userListFragment;
import com.example.daddyz.turtleboys.maps.MapsActivity;
import com.example.daddyz.turtleboys.newsfeed.newsfeedPostDetail;
import com.example.daddyz.turtleboys.newsfeed.newsfeedPostForm;
import com.example.daddyz.turtleboys.searchevent.searchEvent;
import com.example.daddyz.turtleboys.settings.SettingsFragment;
import com.example.daddyz.turtleboys.subclasses.GigUser;
import com.example.daddyz.turtleboys.subclasses.User_Icon_Animation;
import com.parse.GetDataCallback;
import com.parse.LogOutCallback;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;

public class maindrawer extends AppCompatActivity {

    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView userName;
    private TextView userEmail;
    private ListView myList;
    private ProgressBar mProgressBar;
    private SharedPreferences preferences;
    private boolean AnimationFlag;
    private ParseImageView imageView;
    private FragmentManager fragManager;
    private GigUser currentUser;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Boolean refreshTabViewFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //Initialize all preferences to their default values if device does not have them previously saved
        PreferenceManager.setDefaultValues(this, R.xml.settings_preference_fragment, true);

        if (GigUser.getCurrentUser() == null){
            Intent intent = new Intent(getApplicationContext(), login_activity.class);
            startActivity(intent);
            finish();
        }else {

            currentUser = ParseUser.createWithoutData(GigUser.class,ParseUser.getCurrentUser().getObjectId());
            // Initializing Toolbar and setting it as the actionbar
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            //Setup the Fragment Manager
            fragManager = getFragmentManager();

            fragManager.addOnBackStackChangedListener(getListener());

           //create eventfeed fragment and launch it to fill the main screen
            //we get the current fragment manager and start a replacement transaction and we add this transaction to a stack
            //so if we need to move through the stack we pop one off

            //create tab view
            feedtabview fragment = new feedtabview();
            refreshTabViewFlag = true;
            fragManager.beginTransaction().replace(R.id.frame, fragment,"EventFeedFragment").addToBackStack("EventFeedFragment").commit();

            //set username and email in the header and user image
            userName = (TextView) findViewById(R.id.username);
            userName.setText(currentUser.getUsername().toString());
            userEmail = (TextView) findViewById(R.id.email);
            userEmail.setText(currentUser.getEmail().toString());

            //get ParseImageView from xml
            ParseFile image = (ParseFile) currentUser.getUserImage();
            imageView = (ParseImageView) findViewById(R.id.profile_image);
            imageView.setParseFile(image);

            //load the image from the parse database
            imageView.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, com.parse.ParseException e) {
                    // The image is loaded and displayed!
                    int oldHeight = imageView.getHeight();
                    int oldWidth = imageView.getWidth();
                    Log.v("LOG!!!!!!", "imageView height = " + oldHeight);      // DISPLAYS 90 px
                    Log.v("LOG!!!!!!", "imageView width = " + oldWidth);        // DISPLAYS 90 px

                }
            });
            //pulled from experience drawer thanks greg 1337 ha
            //Animate the imageview if preferences permit
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            AnimationFlag = preferences.getBoolean("animation_preference", false);
            final Animation User_Icon = new User_Icon_Animation(com.example.daddyz.turtleboys.subclasses.User_Icon_Animation.Rotate.RIGHT, User_Icon_Animation.Angle.TO_DEGREES_0, 500, false);
            if(AnimationFlag) {
                //hide user image until drawer is fully open if animations are enabled
                imageView.setVisibility(View.INVISIBLE);
            }



            //this is the drawer

            //Initializing NavigationView
            navigationView = (NavigationView) findViewById(R.id.navigation_view);

            //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

                // This method will trigger on item Click of navigation menu
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {


                    //Checking if the item is in checked state or not, if not make it in checked state
                    if (menuItem.isChecked()) menuItem.setChecked(false);
                    else menuItem.setChecked(true);

                    //Closing drawer on item click
                    drawerLayout.closeDrawers();

                    //Check to see which item was being clicked and perform appropriate action
                    switch (menuItem.getItemId()) {


                        //Replacing the main content with ContentFragment Which is our Inbox View;
                        case R.id.messaging:
                            Toast.makeText(getApplicationContext(), "Messaging", Toast.LENGTH_SHORT).show();
                            return true;
                        // For rest of the options we just show a toast on click

                        case R.id.my_experiences:
                            //Toast.makeText(getApplicationContext(), "Stared Selected", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), experience_activity.class);
                            startActivity(intent);
                            return true;
                        case R.id.users:
                            userListFragment fragment4 = new userListFragment();
                            fragManager.beginTransaction().replace(R.id.frame, fragment4,"userListFragment").addToBackStack("userListFragment").commit();
                            return true;
                        case R.id.search_event:
                            searchEvent searchFragment = new searchEvent();
                            fragManager.beginTransaction().replace(R.id.frame, searchFragment,"SearchEventFragment").addToBackStack("SearchEventFragment").commit();
                            return true;
                        case R.id.newsfeed:
                            //create tab view
                            feedtabview fragment = new feedtabview();
                            fragManager.beginTransaction().replace(R.id.frame, fragment, "EventFeedFragment").addToBackStack("EventFeedFragment").commit();
                            Toast.makeText(getApplicationContext(), "newsfeed Selected", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.connect:
                            Toast.makeText(getApplicationContext(), "User wants to connect to other Users", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.logoutDrawer:
                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(maindrawer.this);
                            alertDialog.setTitle("Logout");
                            alertDialog.setMessage("Are you sure you want to logout?");
                            alertDialog.setCancelable(false);
                            alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    mProgressBar.setVisibility(View.VISIBLE);
                                    GigUser.logOutInBackground(new LogOutCallback() {
                                        @Override
                                        public void done(com.parse.ParseException e) {
                                            finish();
                                            Intent intent = new Intent(getApplicationContext(), maindrawer.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            Toast.makeText(getApplicationContext(), "Logout Successful!", Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                            mProgressBar.setVisibility(View.GONE);
                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                        }
                                    });
                                }
                            });
                            //alertDialog.setIcon(R.drawable.icon);
                            AlertDialog alert = alertDialog.create();
                            alert.show();
                            return true;

                        case R.id.activity_map:
                            //Toast.makeText(getApplicationContext(), "Stared Selected", Toast.LENGTH_SHORT).show();
                            Intent mapIntent = new Intent(getApplicationContext(), MapsActivity.class);
                            startActivity(mapIntent);
                            return true;



                        default:
                            Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                            return true;

                    }
                }
            });

            // Initializing Drawer Layout and ActionBarToggle
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

                @Override
                public void onDrawerClosed(View drawerView) {
                    // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                    if(AnimationFlag)
                        imageView.setVisibility(View.INVISIBLE);
                    super.onDrawerClosed(drawerView);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                    if(AnimationFlag) {
                        imageView.setVisibility(View.VISIBLE);
                        imageView.startAnimation(User_Icon);
                    }
                    super.onDrawerOpened(drawerView);
                }
            };

            //Setting the actionbarToggle to drawer layout
            drawerLayout.setDrawerListener(actionBarDrawerToggle);

            //calling sync state is necessay or else your hamburger icon wont show up
            actionBarDrawerToggle.syncState();

            //Setup Progress Bar
            mProgressBar = (ProgressBar)findViewById(R.id.login_progress_bar);
            mProgressBar.setVisibility(View.GONE);

        }

    }

        //logout menu and settings menu probably need to take out

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Fragment fragment = new SettingsFragment();
            fragManager.beginTransaction().replace(R.id.drawer, fragment,"SettingsFragment").addToBackStack("SettingsFragment").commit();
            Toast.makeText(getApplicationContext(), "Settings Selected", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (id == R.id.action_gallery) {
            Intent intent = new Intent(getApplicationContext(), gallery1.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //this code allows user to hit backbutton and if you register your fragments correctly it will segway between the fragments they
    //were at previously
    @Override
    public void onBackPressed() {


        FrameLayout frame =(FrameLayout) findViewById(R.id.frame);
        if(frame.getVisibility() == View.INVISIBLE){
            frame.setVisibility(View.VISIBLE);

        }

        //This grabs the current fragment, you can use this to set up actions specific to leaving a certain fragment
        Fragment currFrag = fragManager.findFragmentById(R.id.drawer);
        if (currFrag instanceof SettingsFragment || currFrag instanceof eventDetailFragment || currFrag instanceof newsfeedPostDetail){
            refreshTabViewFlag = false;
        }

        //changed this line
        if(fragManager.getBackStackEntryCount() >= 2 ) {
            fragManager.popBackStack();
        } else {
            super.onBackPressed();
        }

    }

    private FragmentManager.OnBackStackChangedListener getListener(){
        //This is when a fragment is added or popped off the stack, use this section to update anything when a fragment is changed,
        //such as updating preferences on a fragment view after the settings fragment is finished
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                //Update Views based on preferences
                AnimationFlag = preferences.getBoolean("animation_preference", false);
                if(AnimationFlag){
                    imageView.setVisibility(View.INVISIBLE);
                }else{
                    imageView.setVisibility(View.VISIBLE);
                }

                //if its a subview with a arrow check if thats visible if its visible lock the drawer cause they have
                //a back button to segway back to the parent view
                eventDetailFragment myFragment = (eventDetailFragment)fragManager.findFragmentByTag("EventDetailFragment");
                SettingsFragment myFragment2 = (SettingsFragment) fragManager.findFragmentByTag("SettingsFragment");
                newsfeedPostDetail myFragment3 = (newsfeedPostDetail)fragManager.findFragmentByTag("NewsFeedPostDetail");
                newsfeedPostForm myFragment4 = (newsfeedPostForm)fragManager.findFragmentByTag("NewsFeedPostForm");
                searchEvent myFragment5 = (searchEvent) fragManager.findFragmentByTag("SearchEventFragment");
                feedtabview tabsFragment = (feedtabview) fragManager.findFragmentByTag("EventFeedFragment");

                //This is where the refresh of the tabsview occurs, it should only occur with fragments that replace the frame
                if (tabsFragment != null && tabsFragment.isVisible() && myFragment2 == null && myFragment == null && myFragment3 == null && refreshTabViewFlag){
                    tabsFragment = new feedtabview();
                    fragManager.beginTransaction().replace(R.id.frame, tabsFragment, "EventFeedFragment").commit();
                }
                refreshTabViewFlag = true;
                if (myFragment5 != null && myFragment5.isVisible()){
                    actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
                    toolbar.setTitle("Search Event");
                } else {
                    actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
                    toolbar.setTitle("GigIT");
                }
                if ((myFragment != null && myFragment.isVisible()) || (myFragment2 != null && myFragment2.isVisible()) || (myFragment3 != null && myFragment3.isVisible())|| (myFragment4 != null && myFragment4.isVisible()) || (myFragment5 != null && myFragment5.isVisible())) {
                    // add your code here
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }else{
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

                    //Update User Information
                    imageView.setParseFile(currentUser.getUserImage());
                    //load the image from the parse database
                    imageView.loadInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, com.parse.ParseException e) {
                            // The image is loaded and displayed!
                            int oldHeight = imageView.getHeight();
                            int oldWidth = imageView.getWidth();
                            Log.v("LOG!!!!!!", "imageView height = " + oldHeight);      // DISPLAYS 90 px
                            Log.v("LOG!!!!!!", "imageView width = " + oldWidth);        // DISPLAYS 90 px

                        }
                    });
                    userName.setText(currentUser.getUsername().toString());
                    userEmail.setText(currentUser.getEmail().toString());
                }

            }
        };
        return result;
    }


}