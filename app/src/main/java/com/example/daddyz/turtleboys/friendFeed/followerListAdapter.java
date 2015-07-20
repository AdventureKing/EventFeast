package com.example.daddyz.turtleboys.friendFeed;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.daddyz.turtleboys.R;
import com.example.daddyz.turtleboys.eventfeed.gEventImageObject;
import com.example.daddyz.turtleboys.subclasses.FollowUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by richardryangarcia on 7/17/15.
 */
public class followerListAdapter extends ArrayAdapter<FollowUser> {


    private Context context;
    private int resource;
    private ArrayList<FollowUser> followUserObjects;


    public followerListAdapter(Context context, int resource, ArrayList<FollowUser> followUserObjects) {
        super(context, resource, followUserObjects);
        this.context = context;
        this.resource = resource;
        this.followUserObjects = followUserObjects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get inflator so it will strech the view to fill the row data
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        //change row layout depending on row number
       // int rowType = getItemViewType(position);

        int layoutResource = 0; // determined by view type
  /*      int viewType = getItemViewType(position);
        Log.d("test", viewType + "");
        switch(viewType) {
            case 0:
                layoutResource = R.layout.eventfeedroweven; break;
            case 1:
                layoutResource = R.layout.eventfeedrowodd; break;
        }*/
        //set the view to the odd or even row view
        View row=inflater.inflate(R.layout.user_list_row,parent,false);

        //TextView source = (TextView) row.findViewById(R.id.sourceLine);
       // ImageView eventImage = (ImageView) row.findViewById(R.id.icon);
        TextView description = (TextView) row.findViewById(R.id.descLine);
        TextView citystate = (TextView) row.findViewById(R.id.citystateLine);
        //TextView date = (TextView)row.findViewById(R.id.dateLine);
        //TextView time = (TextView) row.findViewById(R.id.timeLine);
       // TextView venue = (TextView) row.findViewById(R.id.venueLine);
        //TextView urlpath = (TextView) row.findViewById(R.id.urlpathLine);

        String placeholderImageUrl = "http://www.grommr.com/Content/Images/placeholder-event-p.png";
        String imageUrl = placeholderImageUrl;
        String imageVenueUrl = placeholderImageUrl;

        //Loop through available image objects to populate image url
     /*   for(gEventImageObject image : followUserObjects.get(position).getImages()){
            if(null != image.getImage_external_url() && image.getImage_category().equals("attraction")){
                imageUrl = image.getImage_external_url();
                break;
            }
            if(null != image.getImage_external_url() && image.getImage_category().equals("venue")){
                imageVenueUrl = image.getImage_external_url();
                break;
            }
        }*/

        //If no attraction image url was picked up, set to venue URL.
        //Else it uses default placeholder image I placed above.
      /*  if(imageUrl.equals(placeholderImageUrl) && !imageVenueUrl.equals(placeholderImageUrl)){
            imageUrl = imageVenueUrl;
        }
        Picasso.with(context).load(imageUrl).resize(200, 150).into(eventImage);
*/

        //source.setText(objects.get(position).getEventSource());
        description.setText(followUserObjects.get(position).getFollowerUserId());
        citystate.setText(followUserObjects.get(position).getFollowedUserId());
      /*  date.setText(followUserObjects.get(position).getStart_date_month().get(2) + " " + eventObjects.get(position).getStart_date_day().get(0) + ", " + eventObjects.get(position).getStart_date_year().get(0));
        time.setText(followUserObjects.get(position).getStart_date_time().get(2));
        venue.setText(followUserObjects.get(position).getVenue_name());*/
        //urlpath.setText(objects.get(position).getEventURL());


        return row;
    }
}

