package com.example.daddyz.turtleboys.newsfeed;

import android.content.Context;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daddyz.turtleboys.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by snow on 7/11/2015.
 */
public class newsfeedAdapter extends ArrayAdapter<newsfeedObject> {

    private Context context;
    private int resource;
    private ArrayList<newsfeedObject> newsfeedObjects;
    private Boolean AnimationFlag;
    private int lastPosition = -1;

    public newsfeedAdapter(Context context, int resource, ArrayList<newsfeedObject> newsfeedObjects) {
        super(context, resource,newsfeedObjects);
        this.context = context;
        this.resource = resource;
        this.newsfeedObjects = newsfeedObjects;
        this.AnimationFlag = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("animation_preference", false);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.newsfeedrow, parent, false);

        ImageView postImage = (ImageView) rowView.findViewById(R.id.icon);

        TextView messageBox1 = (TextView) rowView.findViewById(R.id.example);
        messageBox1.setText(newsfeedObjects.get(position).getUsername() + " " + newsfeedObjects.get(position).getTitle());
        TextView description = (TextView) rowView.findViewById(R.id.description);
        description.setText(newsfeedObjects.get(position).getDescription());
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        if(null != newsfeedObjects.get(position).getPostImageUrl()){
            Picasso.with(context).load(newsfeedObjects.get(position).getPostImageUrl()).placeholder(R.drawable.events_calendar_icon).resize(300, 150).into(imageView);
        } else{
            Picasso.with(context).load(R.drawable.events_calendar_icon).into(imageView);
        }

        ImageView likeButton;
        ImageView reportButton;
        TextView likeCount = (TextView) rowView.findViewById(R.id.likeCount);
        likeCount.setText(Integer.toString(newsfeedObjects.get(position).getLikeCount()));
        likeButton = (ImageView) rowView.findViewById(R.id.likeimage);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "User LIKES THIS POST", Toast.LENGTH_SHORT).show();
            }
        });
        reportButton = (ImageView) rowView.findViewById(R.id.report);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "User REPORTS THIS POST", Toast.LENGTH_SHORT).show();
            }
        });

        //slide up animation
        if (AnimationFlag) {
            Animation animation = AnimationUtils.loadAnimation(getContext(), (position > lastPosition) ? R.anim.up_from_bottom : R.anim.bottom_from_up);
            rowView.startAnimation(animation);
            lastPosition = position;
        }
        return rowView;
    }
}


