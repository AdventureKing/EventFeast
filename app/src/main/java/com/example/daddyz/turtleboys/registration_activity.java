package com.example.daddyz.turtleboys;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.daddyz.turtleboys.subclasses.GigUser;
import com.parse.LogInCallback;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.ParseException;
import com.parse.SignUpCallback;

//import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    private ParseFile userImage;
    private Date birthday;

    private DialogFragment birthdaySelector;
    private EditText birthdayText;

    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        birthdaySelector = new DatePickerFragment();
        birthdayText = (EditText) findViewById(R.id.birthday);
        birthdayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birthdaySelector.registerForContextMenu(birthdayText);
                birthdaySelector.show(getFragmentManager(), "Date Picker");
            }
        });


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

        userPassword = (EditText) findViewById(R.id.password);
        userPassword.setHint(R.string.password_text);

        userPasswordVerify = (EditText) findViewById(R.id.userConfirmPassword);
        userPasswordVerify.setHint(R.string.userPasswordVerify_text);

        registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                //Check to see if email and verify email fields are the same
                if ( !(email.getText().toString().equals(emailVerify.getText().toString())) ) {
                    Toast.makeText(getApplicationContext(), "Email and Verify email do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Check to see if passwords match
                if ( !(userPassword.getText().toString().equals(userPasswordVerify.getText().toString())) ) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Check if firstName is empty
                if ( firstName.equals("") ) {
                    Toast.makeText(getApplicationContext(), "First name is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ( lastName.equals("") ) {
                    Toast.makeText(getApplicationContext(), "Last name is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ( birthdayText.equals("") ) {
                    Toast.makeText(getApplicationContext(), "Birthday is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                */


                //turn birthday text in to date object
                //year month date
                //comes in as month day year
                //creating date for database
                String[] dates = birthdayText.getText().toString().split("/");
                int tempMonth=Integer.parseInt(dates[0]);
                int tempYear =Integer.parseInt(dates[2]);
                int tempDay = Integer.parseInt(dates[1]);

                //birthday = new Date(tempYear,tempMonth,tempDay);

                //Verify info
                //Create new account
                GigUser newUser;
                newUser = new GigUser();
                newUser.setUsername(userName.getText().toString());
                newUser.setPassword(userPassword.getText().toString());
                newUser.setEmail(email.getText().toString());
                newUser.setBirthday(birthday);
                newUser.setFirstName(firstName.getText().toString());
                newUser.setLastName(lastName.getText().toString());
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
                                    Toast.makeText(getApplicationContext(), "Missing Email Address", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }


                });


            }
        });
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
