package com.example.daddyz.turtleboys;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.daddyz.turtleboys.subclasses.GigUser;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

//import java.text.ParseException;

/**
 * Created by Jinbir on 6/15/2015.
 */
public class registration_activity extends Activity {
    private EditText firstName;
    private EditText lastName;
    private EditText userName;
    private EditText email;
    private EditText emailVerify;
    private EditText userPassword;
    private EditText userPasswordVerify;
    private ParseImageView userImageFile;
    private ParseFile userImageParseFile;
    private Date birthday;
    private static final int SELECT_PHOTO = 100;
    private DialogFragment birthdaySelector;
    private EditText birthdayText;

    //private DialogFragment imageSelector;
    private Fragment imageSelector;
    private EditText imageText;

    private Button registerButton;
    private Button cancelButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        userImageFile = (ParseImageView) findViewById(R.id.profile_image);


        birthdaySelector = new DatePickerFragment();
        birthdayText = (EditText) findViewById(R.id.birthday);
        birthdayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birthdaySelector.registerForContextMenu(birthdayText);
                birthdaySelector.show(getFragmentManager(), "Date Picker");
            }
        });





        userImageFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECT_PHOTO);
            }
        });

        //user image is filled




        firstName = (EditText) findViewById(R.id.firstName);
        firstName.setHint(R.string.firstName_text);

        lastName = (EditText) findViewById(R.id.lastName);
        lastName.setHint(R.string.lastName_text);

        userName = (EditText) findViewById(R.id.userName);
        userName.setHint(R.string.username_text);

        email = (EditText) findViewById(R.id.email);
        email.setHint(R.string.email_text);

        emailVerify = (EditText) findViewById(R.id.emailVerify);
        emailVerify.setHint(R.string.emailVerify_text);

        userPassword = (EditText) findViewById(R.id.userPassword);
        userPassword.setHint(R.string.password_text);

        userPasswordVerify = (EditText) findViewById(R.id.userPasswordVerify);
        userPasswordVerify.setHint(R.string.userPasswordVerify_text);

        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"BUTTON CLICKED", Toast.LENGTH_SHORT).show();

                //Check to see if email and verify email fields are the same
                if ( !(email.getText().toString().equals(emailVerify.getText().toString())) ) {
                    Toast.makeText(getApplicationContext(), R.string.emailNoMatch, Toast.LENGTH_SHORT).show();
                    return;
                }

                //Check to see if passwords match
                if ( !(userPassword.getText().toString().equals(userPasswordVerify.getText().toString())) ) {
                    Toast.makeText(getApplicationContext(), R.string.passwordNoMatch, Toast.LENGTH_SHORT).show();
                    return;
                }

                //Check if firstName is empty
                if ( firstName.getText().toString().isEmpty() ) {
                    Toast.makeText(getApplicationContext(), R.string.firstNameEmpty, Toast.LENGTH_SHORT).show();
                    return;
                }

                //Check if lastName is empty
                if ( lastName.getText().toString().isEmpty() ) {
                    Toast.makeText(getApplicationContext(), R.string.lastNameEmpty, Toast.LENGTH_SHORT).show();
                    return;
                }

                //Check if userName is empty
                if ( userName.getText().toString().isEmpty() ) {
                    Toast.makeText(getApplicationContext(), R.string.userNameEmpty, Toast.LENGTH_SHORT).show();
                    return;
                }

                //Check if email is empty
                if ( email.getText().toString().isEmpty() ) {
                    Toast.makeText(getApplicationContext(), R.string.emailEmpty, Toast.LENGTH_SHORT).show();
                    return;
                }

                //Check if password is empty
                if ( userPassword.getText().toString().isEmpty() ) {
                    Toast.makeText(getApplicationContext(), R.string.passwordEmpty, Toast.LENGTH_SHORT).show();
                    return;
                }

                //Check if birthdayText is empty
                /*
                if ( birthdayText.getText().toString().equals(R.string.birthdaySelect) ) {
                    Toast.makeText(getApplicationContext(), "Birthday is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                */





                //turn birthday text in to date object
                //year month date
                //comes in as month day year
                //creating date for database
                String[] dates = birthdayText.getText().toString().split("/");
                if ( dates.length < 3 ) {
                    Toast.makeText(getApplicationContext(), R.string.birthdaySelect , Toast.LENGTH_SHORT).show();
                    return;
                }

                int tempMonth=Integer.parseInt(dates[0]);
                tempMonth -= 1;
                int tempYear =Integer.parseInt(dates[2]);
                tempYear -= 1900;
                int tempDay = Integer.parseInt(dates[1]);



                birthday = new Date(tempYear,tempMonth,tempDay);


                //Verify info
                //Create new account
                GigUser newUser = new GigUser();
                newUser.setUsername(userName.getText().toString());
                newUser.setPassword(userPassword.getText().toString());
                newUser.setEmail(email.getText().toString());
                newUser.setBirthday(birthday);
                newUser.setFirstName(firstName.getText().toString());
                newUser.setLastName(lastName.getText().toString());

                if(userImageParseFile != null) {

                    newUser.setUserImage(userImageParseFile);
                    try {
                        userImageParseFile.save();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

                //Toast.makeText(getApplicationContext(),"Gonna make user", Toast.LENGTH_SHORT).show();

               newUser.signUpInBackground(new SignUpCallback() {

                   @Override
                   public void done(com.parse.ParseException e) {
                       if (e == null) {


                           Intent intent = new Intent(getApplicationContext(), maindrawer.class);
                           finish();
                           startActivity(intent);
                       } else {
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
}
