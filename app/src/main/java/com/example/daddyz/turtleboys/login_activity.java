package com.example.daddyz.turtleboys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.daddyz.turtleboys.subclasses.GigUser;
import com.parse.LogInCallback;
import com.parse.ParseUser;

/**
 * Created by Jinbir on 6/6/2015.
 */
public class login_activity extends Activity {
    private EditText userName;
    private EditText userPassword;
    private Button login;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginview);
        View view = findViewById(R.id.login_page);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                        INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });


        userName = (EditText) findViewById(R.id.userName);
        userName.setHint(R.string.username_text);
        userPassword = (EditText) findViewById(R.id.password);
        userPassword.setHint(R.string.password_text);
        login = (Button) findViewById(R.id.loginButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Start loading icon for login
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                GigUser.logInInBackground(userName.getText().toString().trim(), userPassword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser GigUser, com.parse.ParseException e) {
                        if (GigUser != null) {
                            // Hooray! The user is logged in.

                            //Stop loading icon
                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            Intent intent = new Intent(getApplicationContext(), maindrawer.class);
                            finish();
                            startActivity(intent);

                        } else {
                            // Signup failed. Look at the ParseException to see what happened.
                            //Stop loading icon
                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Failed to login, please try again.", Toast.LENGTH_LONG).show();

                        }
                    }


                });
            }
        });

        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), registration_activity.class);
                startActivity(intent);

                //Toast.makeText(getApplicationContext(), "Redirection to registration", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
