package com.example.daddyz.turtleboys.followtabview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.daddyz.turtleboys.R;

import java.util.List;
import java.util.Map;

/**
 * Created by snow on 8/12/2015.
 */
public class genrefollowlistadapter extends BaseExpandableListAdapter {



    private Activity context;
    private Map<String, List<String>> genreCollections;
    private List<String> followSelections;

    public genrefollowlistadapter(Activity context, List<String> followSelections,
                                 Map<String, List<String>> genreCollections) {
        this.context = context;
        this.genreCollections = genreCollections;
        this.followSelections = followSelections;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return genreCollections.get(followSelections.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

//what happens when you hit the follow button
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String laptop = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            //set view to child item view wiith the follow icon picture
            convertView = inflater.inflate(R.layout.follow_genre_child_item, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.sub_genres);

        ImageView follow = (ImageView) convertView.findViewById(R.id.follow);
        follow.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to follow?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //add stuff here
                                dialog.cancel();
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        item.setText(laptop);
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return genreCollections.get(followSelections.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return followSelections.get(groupPosition);
    }

    public int getGroupCount() {
        return followSelections.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
//what the genresections look like
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String genreName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //set view to the group item view which is just a text view
            convertView = infalInflater.inflate(R.layout.follow_genre_group_item,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.sub_genres);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(genreName);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    }

