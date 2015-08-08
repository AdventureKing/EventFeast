package com.example.daddyz.turtleboys.newsfeed;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.daddyz.turtleboys.R;
import com.example.daddyz.turtleboys.VolleyPostObjectRequest;
import com.example.daddyz.turtleboys.VolleyRequestQueue;
import com.example.daddyz.turtleboys.subclasses.GigUser;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by snow on 7/13/2015.
 */
public class newsfeedPostForm extends Fragment implements Response.Listener,
        Response.ErrorListener{

    public static final String REQUEST_TAG = "NewsFeedPost";
    private View view;
    private newsfeedObject obj;
    private Toolbar toolbar;
    private ActionBar actionbar;
    private EditText userMessage;
    private TextView userLocation;
    private ImageView upload;
    private RequestQueue mQueue;
    private final GigUser currentUser = ParseUser.createWithoutData(GigUser.class, ParseUser.getCurrentUser().getObjectId());

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
                //Toast.makeText(getActivity(), userMessage.getText().toString(), Toast.LENGTH_SHORT).show();
                onPostSubmit();
                getFragmentManager().popBackStack();
                frame.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mQueue = VolleyRequestQueue.getInstance(this.getActivity().getApplicationContext()).getRequestQueue();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Context context = getActivity();

        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Log.i("Timeout Error: ", error.toString());
        } else if (error instanceof AuthFailureError) {
            Log.i("Auth Failure Error: ", error.toString());
        } else if (error instanceof ServerError) {
            Log.i("Server Error: ", error.toString());
        } else if (error instanceof NetworkError) {
            Log.i("Network Error: ", error.toString());
        } else if (error instanceof ParseError) {
            Log.i("Parse Error: ", error.toString());
        }
    }

    @Override
    public void onResponse(Object response) {
        Log.i("Newsfeed info:","Post has been submitted!");
        Log.i("Newsfeed Post Response:",response.toString());
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

    public void onPostSubmit(){
        if(userMessage.getText().toString().length() > 0) {
            Toast.makeText(getActivity(), "submitting the post..", Toast.LENGTH_SHORT).show();

            Map<String, String> params = new HashMap<String, String>();
            params.put("userId", currentUser.getObjectId());
            params.put("eventId", "254");
            params.put("eventSource", "internal");
            params.put("postMsg", userMessage.getText().toString());
            params.put("userLat", Double.toString(currentUser.getUserHome().getLatitude()));
            params.put("userLng", Double.toString(currentUser.getUserHome().getLongitude()));

            String url = "http://api.dev.turtleboys.com/v1/user/neo/post";
            VolleyPostObjectRequest jsObjRequest = new VolleyPostObjectRequest(Request.Method.POST, url, params, this, this);

            mQueue.add(jsObjRequest);
        } else{
            Toast.makeText(getActivity(), "Empty Message. Nothing posted.", Toast.LENGTH_SHORT).show();
        }
    }

}
