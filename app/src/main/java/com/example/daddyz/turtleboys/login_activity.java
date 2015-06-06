package com.example.daddyz.turtleboys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;

import java.text.ParseException;

/**
 * Created by Jinbir on 6/6/2015.
 */
public class login_activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginview);
        EditText userName;
        EditText userPassword;


        userName = (EditText) findViewById(R.id.userName);
        userName.setHint(R.string.username_text);
        userPassword = (EditText) findViewById(R.id.password);
        userPassword.setHint(R.string.password_text);

        ParseUser.logInInBackground(userName.getText().toString(), userPassword.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, com.parse.ParseException e) {
                if (parseUser != null) {
                    // Hooray! The user is logged in.
                    //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    //finish();

                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    Toast.makeText(getApplicationContext(), "Didn't work", Toast.LENGTH_SHORT).show();

                }
            }


        });



    }
}
