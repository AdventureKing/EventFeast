package com.example.daddyz.turtleboys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginview);


        userName = (EditText) findViewById(R.id.userName);
        userName.setHint(R.string.username_text);
        userPassword = (EditText) findViewById(R.id.password);
        userPassword.setHint(R.string.password_text);
        login = (Button) findViewById(R.id.loginbutton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GigUser.logInInBackground(userName.getText().toString(), userPassword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser GigUser, com.parse.ParseException e) {
                        if (GigUser != null) {
                            // Hooray! The user is logged in.

                            Intent intent = new Intent(getApplicationContext(), maindrawer.class);
                            finish();
                            startActivity(intent);

                        } else {
                            // Signup failed. Look at the ParseException to see what happened.
                            Toast.makeText(getApplicationContext(), "Failed to login, please try again.", Toast.LENGTH_LONG).show();

                        }
                    }


                });
            }
        });
    }
}
