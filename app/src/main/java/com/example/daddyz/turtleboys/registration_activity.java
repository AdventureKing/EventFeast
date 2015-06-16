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
import com.parse.ParseFile;
import com.parse.ParseUser;

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

    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);


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
                //Verify info
                //Create new account
                /*
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
                */
            }
        });
    }
}
