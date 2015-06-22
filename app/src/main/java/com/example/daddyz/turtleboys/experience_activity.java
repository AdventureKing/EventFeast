package com.example.daddyz.turtleboys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daddyz.turtleboys.subclasses.Camera;
import com.example.daddyz.turtleboys.subclasses.GigUser;
import com.parse.GetDataCallback;
import com.parse.LogOutCallback;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;


/**
 * Created by Jinbir on 6/13/2015.
 */
public class experience_activity extends AppCompatActivity {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView userName;
    private TextView userEmail;

    private Button b1;
    private ImageView preview;
    private Bitmap thumbnail;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    //private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    //public static final int MEDIA_TYPE_VIDEO = 2;

    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myexperiences1);
        // Enable Local Datastore.

        preview = (ImageView) findViewById(R.id.picturePreview);
        b1=(Button)findViewById(R.id.loginbutton);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*// create Intent to take a picture and return control to the calling application
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

                // start the image capture Intent
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);*/

                Camera camera = new Camera(experience_activity.this);
                fileUri = camera.startCamera();
            }
        });

        final GigUser currentUser = ParseUser.createWithoutData(GigUser.class, ParseUser.getCurrentUser().getObjectId());
        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Experiences");

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
                /*case R.id.inbox:
                    Toast.makeText(getApplicationContext(), "Inbox Selected", Toast.LENGTH_SHORT).show();
                    ContentFragment fragment = new ContentFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment);
                    fragmentTransaction.commit();
                    return true;*/

                    // For rest of the options we just show a toast on click

                    case R.id.main_page:
                        Intent intent = new Intent(getApplicationContext(), maindrawer.class);
                        startActivity(intent);
                        return true;
                    case R.id.myEvents:
                        Toast.makeText(getApplicationContext(), "Events Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.myFriends:
                        Toast.makeText(getApplicationContext(), "Friends selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.myPosts:
                        Toast.makeText(getApplicationContext(), "Post Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.messaging:
                        Toast.makeText(getApplicationContext(), "User wants to message", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.myListings:
                        Toast.makeText(getApplicationContext(), "User wants to look at their listings", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.logoutDrawer:
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(experience_activity.this);
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
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.myexperiencesdrawer);
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

        //Setup Progress Bar
        mProgressBar = (ProgressBar)findViewById(R.id.login_progress_bar);
        mProgressBar.setVisibility(View.GONE);

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                if(fileUri != null) {
                    Toast.makeText(this, "Image saved to:\n" +
                            fileUri.toString(), Toast.LENGTH_LONG).show();

                    //Update MediaStore with new file (ex:  Picture shows up in Gallery)
                    Intent broadcast = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    broadcast.setData(fileUri);
                    sendBroadcast(broadcast);
                    //Picture Preview

                    String path = fileUri.toString().substring(5); //Removes unnecessary "File:" from path
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    thumbnail = BitmapFactory.decodeFile(path, options);

                    try {
                        ExifInterface exif = new ExifInterface(path);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        Log.d("EXIF", "Exif: " + orientation);
                        Matrix matrix = new Matrix();
                        if (orientation == 6) {
                            matrix.postRotate(90);
                        }
                        else if (orientation == 3) {
                            matrix.postRotate(180);
                        }
                        else if (orientation == 8) {
                            matrix.postRotate(270);
                        }
                        thumbnail = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), matrix, true); // rotating bitmap
                    }
                    catch (Exception e) {

                    }

                    preview.setImageBitmap(thumbnail);


                }else{
                    Toast.makeText(this, "Image file location error", Toast.LENGTH_LONG).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }

        /*
        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Video saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
            }
        }
        */
    }

    /** Create a file Uri for saving an image or video */
   /* private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
   /* private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "GigIT");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } /*else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } *//*else {
            return null;
        }

        return mediaFile;
    }*/

}
