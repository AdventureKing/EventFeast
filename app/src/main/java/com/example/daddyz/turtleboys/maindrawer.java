package com.example.daddyz.turtleboys;

/**
 * Created by snow on 6/13/2015.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daddyz.turtleboys.newsfeed.NewsFeedFragment;
import com.example.daddyz.turtleboys.subclasses.GigUser;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);




        if (GigUser.getCurrentUser() == null){
            Intent intent = new Intent(getApplicationContext(), login_activity.class);
            startActivity(intent);
            finish();
        }else {

            final GigUser currentUser = ParseUser.createWithoutData(GigUser.class,ParseUser.getCurrentUser().getObjectId());
            // Initializing Toolbar and setting it as the actionbar
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            //generate newsfeed
            //stuff that goes in a row
            //create a list fragment and show
            NewsFeedFragment fragment = new NewsFeedFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();



            //set username and email in the header and user image
            userName = (TextView) findViewById(R.id.username);
            userName.setText(currentUser.getUsername().toString());
            userEmail = (TextView) findViewById(R.id.email);
            userEmail.setText(currentUser.getEmail().toString());

            //get ParseImageView from xml
            ParseFile image = (ParseFile) currentUser.getUserImage();
            final ParseImageView imageView = (ParseImageView) findViewById(R.id.profile_image);
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
                        case R.id.inbox:
                            Toast.makeText(getApplicationContext(), "Inbox Selected", Toast.LENGTH_SHORT).show();
                            NewsFeedFragment fragment = new NewsFeedFragment();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.frame, fragment);
                            fragmentTransaction.commit();
                            return true;

                        // For rest of the options we just show a toast on click

                        case R.id.starred:
                            //Toast.makeText(getApplicationContext(), "Stared Selected", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), experience_activity.class);
                            startActivity(intent);
                            return true;
                        case R.id.search_event:
                            Toast.makeText(getApplicationContext(), "Event Searched", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.drafts:

                            Toast.makeText(getApplicationContext(), "Drafts Selected", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.connect:
                            Toast.makeText(getApplicationContext(), "User wants to connect to other Users", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.logoutDrawer:
                            currentUser.logOutInBackground(new LogOutCallback() {
                                @Override
                                public void done(com.parse.ParseException e) {
                                    finish();
                                    Intent intent = new Intent(getApplicationContext(), login_activity.class);
                                    Toast.makeText(getApplicationContext(), "Logout Successful!", Toast.LENGTH_SHORT).show();

                                    startActivity(intent);

                                }
                            });
                            return true;
                        case R.id.account_settings:
                            Toast.makeText(getApplicationContext(), "Account Settings Should be displayed", Toast.LENGTH_SHORT).show();
                            return true;
                        default:
                            Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                            return true;

                    }
                }
            });

            // Initializing Drawer Layout and ActionBarToggle
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
            ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

                @Override
                public void onDrawerClosed(View drawerView) {
                    // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                    super.onDrawerClosed(drawerView);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                    super.onDrawerOpened(drawerView);
                }
            };

            //Setting the actionbarToggle to drawer layout
            drawerLayout.setDrawerListener(actionBarDrawerToggle);

            //calling sync state is necessay or else your hamburger icon wont show up
            actionBarDrawerToggle.syncState();


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
            return true;
        }
        if(id == R.id.action_logout){
            GigUser.logOutInBackground(new LogOutCallback(){
                @Override
                public void done(com.parse.ParseException e) {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), login_activity.class);
                    Toast.makeText(getApplicationContext(), "Logout Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            });

        }

        return super.onOptionsItemSelected(item);
    }
}