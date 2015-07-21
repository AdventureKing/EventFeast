package com.example.daddyz.turtleboys.newsfeed;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daddyz.turtleboys.R;

/**
 * Created by snow on 7/13/2015.
 */
public class newsfeedPostForm extends Fragment {

    private View view;
    private newsfeedObject obj;
    private Toolbar toolbar;
    private ActionBar actionbar;
    private EditText userMessage;
    private TextView userLocation;
    private ImageView upload;

    private Button postButton;


    private static final int SELECT_PHOTO = 100;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //lock the drawer because we are inception in this bitch
        //main_activity->fragment->fragment

        view = inflater.inflate(R.layout.newsfeed_post_form, container, false);

        //toolbar setup

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        actionbar.setTitle("Newsfeed Post Form");


        //get the frame from the main view and set it invisible
        final FrameLayout frame = (FrameLayout) container.findViewById(R.id.frame);
        frame.setVisibility(View.INVISIBLE);


        //set hints on user description
        userMessage = (EditText) view.findViewById(R.id.postBody);
        userMessage.setHint("Enter a Post Message");

        //Set up back arrow icon on toolbar
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(frame);
            }
        });


        //get user image to upload
        upload = (ImageView) view.findViewById(R.id.userImageToUpload);
        upload.setClickable(true);
        //click on image to upload to view
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, SELECT_PHOTO);
            }
        });





        //hit this button and post to the newsfeed that is in the location textfield wutang
        postButton = (Button) view.findViewById(R.id.postButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "User MAKES A POST", Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
                frame.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    public void onBackPressed(FrameLayout frame) {

        if (getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
            frame.setVisibility(View.VISIBLE);

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (imageReturnedIntent != null) {
            Uri imageUri = imageReturnedIntent.getData();
            upload.setImageURI(imageUri);
        }

    }
}
