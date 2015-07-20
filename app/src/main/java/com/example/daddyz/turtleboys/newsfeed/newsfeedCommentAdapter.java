package com.example.daddyz.turtleboys.newsfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.daddyz.turtleboys.R;

import java.util.ArrayList;

/**
 * Created by snow on 7/18/2015.
 */
public class newsfeedCommentAdapter extends ArrayAdapter<newsfeedCommentObjects> {

    private Context context;
    private int resource;
    private ArrayList<newsfeedCommentObjects> commentObjects;

    public newsfeedCommentAdapter(Context context, int resource, ArrayList<newsfeedCommentObjects> commentObjects) {
        super(context, resource,commentObjects);
        this.context = context;
        this.resource = resource;

        //comment objects filled in the newsfeedPostDetail.java
        this.commentObjects = commentObjects;
    }
    //return even or odd row
    public int getItemViewType(int position) {
        // return a value between 0 and (getViewTypeCount - 1)
        //return position % 2;
        return position % 1;
    }
    //total type of rows that are shown if we add more we need to change this to a 3
    public int getViewTypeCount() {
        // return the total number of view types. this value should never change at runtime
        return 2;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //get inflator so it will strech the view to fill the row data
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //get view and apply the postdetailcommentrow xml to it
        View row = inflater.inflate(R.layout.postdetailcommentrow, parent, false);


        //get the section of the array your in and apply to view
        TextView postComment = (TextView) row.findViewById(R.id.post);
        postComment.setText(commentObjects.get(position).getUserComment());
        TextView userNameComment = (TextView) row.findViewById(R.id.userName);
        userNameComment.setText(commentObjects.get(position).getUserName());




        return row;
    }
}
