package com.example.daddyz.turtleboys;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.daddyz.turtleboys.R;
import com.example.daddyz.turtleboys.maindrawer;
import com.example.daddyz.turtleboys.subclasses.GigUser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

//import java.text.ParseException;

/**
 * Created by Jinbir on 6/15/2015.
 */
public class change_account_info_activity extends Activity {
    private EditText firstName;
    private EditText lastName;
    private EditText userName;
    private EditText email;
    private EditText emailVerify;
    private EditText oldUserPassword;
    private EditText userPassword;
    private EditText userPasswordVerify;
    private ParseImageView userImageFile;
    private ParseFile userImageParseFile;
    private static final int SELECT_PHOTO = 100;
    private final GigUser currentUser = ParseUser.createWithoutData(GigUser.class, ParseUser.getCurrentUser().getObjectId());
    private GigUser oldPasswordCheck;
    private boolean changePassword;
    private boolean updateGallery;
    private ProgressBar mProgressBar;

    private Button saveButton;
    private Button cancelButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_account_info_activity);

        View view = findViewById(R.id.account_info);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                        INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

        userImageFile = (ParseImageView) findViewById(R.id.profile_image);

        //Setup User's Thumbnail
        ParseFile image = (ParseFile) currentUser.getUserImage();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap thumbnail = null;
        try {
            thumbnail = BitmapFactory.decodeByteArray(image.getData(), 0, image.getData().length, options);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException n){}
        userImageFile.setImageBitmap(thumbnail);
        userImageFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECT_PHOTO);
            }
        });

        //Setup Progress Bar
        mProgressBar = (ProgressBar)findViewById(R.id.login_progress_bar);
        mProgressBar.setVisibility(View.GONE);



        firstName = (EditText) findViewById(R.id.firstName);
        firstName.setHint(R.string.firstName_text);
        firstName.setText(currentUser.getFirstName());

        lastName = (EditText) findViewById(R.id.lastName);
        lastName.setHint(R.string.lastName_text);
        lastName.setText(currentUser.getLastName());

        userName = (EditText) findViewById(R.id.userName);
        userName.setHint(R.string.username_text);
        userName.setText(currentUser.getUsername());

        email = (EditText) findViewById(R.id.email);
        email.setHint(R.string.email_text);
        email.setText(currentUser.getEmail());

        emailVerify = (EditText) findViewById(R.id.emailVerify);
        emailVerify.setHint(R.string.emailVerify_text);
        emailVerify.setText(currentUser.getEmail());

        oldUserPassword = (EditText) findViewById(R.id.oldUserPassword);
        oldUserPassword.setHint(R.string.account_old_password);

        userPassword = (EditText) findViewById(R.id.userPassword);
        userPassword.setHint(R.string.account_password_option);

        userPasswordVerify = (EditText) findViewById(R.id.userPasswordVerify);
        userPasswordVerify.setHint(R.string.account_password_verify_option);

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"BUTTON CLICKED", Toast.LENGTH_SHORT).show();

                //Show ProgressBar and lock screen
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                mProgressBar.setVisibility(View.VISIBLE);

                //Check to see if email and verify email fields are the same
                if ( !(email.getText().toString().equals(emailVerify.getText().toString())) ) {
                    mProgressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getApplicationContext(), R.string.emailNoMatch, Toast.LENGTH_SHORT).show();
                    return;
                }

                //Check to see if old password and Parse entry match
                try {
                    oldPasswordCheck.logIn(currentUser.getUsername(), oldUserPassword.getText().toString());
                } catch (ParseException e) {
                    // Nope, the user entered the wrong password
                    mProgressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getApplicationContext(), R.string.old_password_incorrect, Toast.LENGTH_LONG).show();
                    return;
                }

                if (userPassword.getText().toString().matches("^\\s*$") && userPasswordVerify.getText().toString().matches("^\\s*$")) {
                    changePassword = false;
                }else {
                    //Check if new password is empty
                    if ( userPassword.getText().toString().isEmpty() ) {
                        mProgressBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getApplicationContext(), R.string.new_password_empty, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //Check to see if new passwords match
                    if (!(userPassword.getText().toString().equals(userPasswordVerify.getText().toString()))) {
                        mProgressBar.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Toast.makeText(getApplicationContext(), R.string.new_password_mismatch, Toast.LENGTH_LONG).show();
                        return;
                    }
                    changePassword = true;
                }

                //Check if firstName is empty
                if ( firstName.getText().toString().isEmpty() ) {
                    mProgressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getApplicationContext(), R.string.firstNameEmpty, Toast.LENGTH_SHORT).show();
                    return;
                }

                //Check if lastName is empty
                if ( lastName.getText().toString().isEmpty() ) {
                    mProgressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getApplicationContext(), R.string.lastNameEmpty, Toast.LENGTH_SHORT).show();
                    return;
                }

                //Check if userName is empty
                if ( userName.getText().toString().isEmpty() ) {
                    mProgressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getApplicationContext(), R.string.userNameEmpty, Toast.LENGTH_SHORT).show();
                    return;
                }

                //Check if userName is the same
                if ( userName.getText().toString().trim().equals(currentUser.getUsername())){
                    updateGallery = false;
                }else{
                    updateGallery = true;
                }

                //Check if email is empty
                if ( email.getText().toString().isEmpty() ) {
                    mProgressBar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Toast.makeText(getApplicationContext(), R.string.emailEmpty, Toast.LENGTH_SHORT).show();
                    return;
                }

                //Save old information in case invalid data is given
                final String oldName = currentUser.getUsername();
                final String oldEmail = currentUser.getEmail();
                final String oldFirst = currentUser.getFirstName();
                final String oldLast = currentUser.getLastName();
                final ParseFile oldPicture = currentUser.getUserImage();

                //Verify info
                //Save new account info

                currentUser.setUsername(userName.getText().toString().trim());
                if (changePassword)
                    currentUser.setPassword(userPassword.getText().toString());
                currentUser.setEmail(email.getText().toString().trim());
                currentUser.setFirstName(firstName.getText().toString().trim());
                currentUser.setLastName(lastName.getText().toString().trim());

                if(userImageParseFile != null) {

                    currentUser.setUserImage(userImageParseFile);
                    try {
                        userImageParseFile.save();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

                //Toast.makeText(getApplicationContext(),"Gonna make user", Toast.LENGTH_SHORT).show();
                currentUser.saveInBackground(new SaveCallback() {

                    @Override
                    public void done(ParseException e) {
                        if (e == null) {

                            Toast.makeText(getApplicationContext(), "Your new account information has been saved!", Toast.LENGTH_LONG).show();

                            if (updateGallery) {  //The following code is dangerous if it is renaming a directory to itself
                                //Update Gallery Directory if it exists
                                File[] oldPath = new File(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES), "GigIT/" + oldName).listFiles();
                                File newPath = new File(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES), "GigIT/" + currentUser.getUsername());
                                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES), "GigIT/" + oldName);
                                if (mediaStorageDir.exists()) {
                                    if (!mediaStorageDir.renameTo(new File(Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_PICTURES), "GigIT/" + currentUser.getUsername()))) {
                                        Log.d("Account_Settings", "failed to create directory");
                                        Toast.makeText(getApplicationContext(), "Failed to rename User Picture Directory", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }
                                AsyncTaskLoadFiles fileLoader = new AsyncTaskLoadFiles(change_account_info_activity.this, oldPath, newPath);
                                fileLoader.execute();
                            }

                           finish();

                            //Hide ProgressBar and Unlock Screen
                            mProgressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        } else {
                            mProgressBar.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            currentUser.setUsername(oldName);
                            currentUser.setEmail(oldEmail);
                            currentUser.setFirstName(oldFirst);
                            currentUser.setLastName(oldLast);
                            currentUser.setUserImage(oldPicture);
                            switch (e.getCode()) {
                                case ParseException.USERNAME_MISSING:
                                    Toast.makeText(getApplicationContext(), "Missing Username", Toast.LENGTH_SHORT).show();
                                    break;
                                case ParseException.USERNAME_TAKEN:
                                    Toast.makeText(getApplicationContext(), "Username Already Taken", Toast.LENGTH_SHORT).show();
                                    break;
                                case ParseException.INVALID_EMAIL_ADDRESS:
                                    Toast.makeText(getApplicationContext(), "Invalid Email Address", Toast.LENGTH_SHORT).show();
                                    break;
                                case ParseException.EMAIL_TAKEN:
                                    Toast.makeText(getApplicationContext(), "Email Address Already Taken", Toast.LENGTH_SHORT).show();
                                    break;
                                case ParseException.EMAIL_MISSING:
                                    Toast.makeText(getApplicationContext(), "Missing Email Address", Toast.LENGTH_SHORT).show();
                                    break;
                                case ParseException.PASSWORD_MISSING:
                                    Toast.makeText(getApplicationContext(), "Missing Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }


                });

            }
        });

        cancelButton = (Button) findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getApplicationContext(), registration_activity.class);
                //startActivity(intent);
                finish();

                //Toast.makeText(getApplicationContext(), "Redirection to registration", Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK) {
            //fill it with data
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(data.getData());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            byte[] buf = new byte[1024];
            int n;
            try {
                while (-1 != (n = imageStream.read(buf)))
                    baos.write(buf, 0, n);
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] videoBytes = baos.toByteArray(); //this is the video in bytes.
            userImageParseFile = new ParseFile(videoBytes);

            //Setup Thumbnail
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap thumbnail = BitmapFactory.decodeByteArray(videoBytes, 0, videoBytes.length, options);

            //Rotate image if needed
            int orientation;
            Cursor cursor = getContentResolver().query(data.getData(),
                    new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

            if (cursor.getCount() != -1) {
                orientation = -1;
            }
            cursor.moveToFirst();
            orientation = cursor.getInt(0);
            Matrix matrix = new Matrix();
            if (orientation == 90) {
                matrix.postRotate(90);
            }
            else if (orientation == 180) {
                matrix.postRotate(180);
            }
            else if (orientation == 270) {
                matrix.postRotate(270);
            }
            thumbnail = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), matrix, true); // rotating bitmap
            userImageFile.setImageBitmap(thumbnail);
        }
    }

    public class AsyncTaskLoadFiles extends AsyncTask<Void, String, Void> {

        private File targetDirector;
        private File[] oldPath;
        private File newPath;
        private Context context;
        private ContentResolver resolver;

        public AsyncTaskLoadFiles(Context context, File[] oldStuff, File newStuff) {
            this.context = context;
            this.oldPath = oldStuff;
            this.newPath = newStuff;
            resolver = this.context.getContentResolver();
        }

        @Override
        protected Void doInBackground(Void... params) {

            for (File oldFile : oldPath){
                resolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?", new String[] { oldFile.getAbsolutePath() });
            }

            ArrayList<String> gigITDirectory = new ArrayList<String>();
            File[] newFiles = newPath.listFiles();
            for (File newFile : newFiles){
                gigITDirectory.add(newFile.getAbsolutePath());
            }
            String[] gigITDirectoryFiles = new String[gigITDirectory.size()];
            gigITDirectoryFiles = gigITDirectory.toArray(gigITDirectoryFiles);

            MediaScannerConnection.scanFile(getApplicationContext(), gigITDirectoryFiles, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String s, Uri uri) { }
            });
            return null;
        }

    }

}
