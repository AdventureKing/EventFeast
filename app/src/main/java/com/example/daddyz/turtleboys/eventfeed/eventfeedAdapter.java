package com.example.daddyz.turtleboys.eventfeed;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.daddyz.turtleboys.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by snow on 6/17/2015.
 */
public class eventfeedAdapter extends ArrayAdapter<gEventObject> {

    private Context context;
    private int resource;
    private ArrayList<gEventObject> eventObjects;
    private Boolean AnimationFlag;
    private int lastPosition = -1;


    public eventfeedAdapter(Context context, int resource, ArrayList<gEventObject> eventObjects) {
        super(context, resource,eventObjects);
        this.context = context;
        this.resource = resource;
        this.eventObjects = eventObjects;
        this.AnimationFlag = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("animation_preference", false);
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
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        //change row layout depending on row number
        int rowType = getItemViewType(position);

        int layoutResource = 0; // determined by view type
        int viewType = getItemViewType(position);
        Log.d("test", viewType + "");
        switch(viewType) {
            case 0:
                layoutResource = R.layout.eventfeedroweven; break;
            case 1:
                layoutResource = R.layout.eventfeedrowodd; break;
        }
        //set the view to the odd or even row view
        View row=inflater.inflate(layoutResource,parent,false);

        //TextView source = (TextView) row.findViewById(R.id.sourceLine);
        ImageView eventImage = (ImageView) row.findViewById(R.id.icon);
        TextView description = (TextView) row.findViewById(R.id.descLine);
        TextView citystate = (TextView) row.findViewById(R.id.citystateLine);
        TextView date = (TextView)row.findViewById(R.id.dateLine);
        TextView time = (TextView) row.findViewById(R.id.timeLine);
        TextView venue = (TextView) row.findViewById(R.id.venueLine);
        //TextView urlpath = (TextView) row.findViewById(R.id.urlpathLine);

        String placeholderImageUrl = "http://www.grommr.com/Content/Images/placeholder-event-p.png";
        String imageUrl = placeholderImageUrl;
        String imageVenueUrl = placeholderImageUrl;

        //Loop through available image objects to populate image url
        for(gEventImageObject image : eventObjects.get(position).getImages()){
            if(null != image.getImage_external_url() && image.getImage_category().equals("attraction")){
                imageUrl = image.getImage_external_url();
                break;
            }
            if(null != image.getImage_external_url() && image.getImage_category().equals("venue")){
                imageVenueUrl = image.getImage_external_url();
                break;
            }
        }

        //If no attraction image url was picked up, set to venue URL.
        //Else it uses default placeholder image I placed above.
        if(imageUrl.equals(placeholderImageUrl) && !imageVenueUrl.equals(placeholderImageUrl)){
            imageUrl = imageVenueUrl;
        }
        Picasso.with(context).load(imageUrl).resize(200, 150).into(eventImage);

        //source.setText(objects.get(position).getEventSource());
        description.setText(eventObjects.get(position).getTitle());
        citystate.setText(eventObjects.get(position).getCity_name() + "," + eventObjects.get(position).getState_name());
        date.setText(eventObjects.get(position).getStart_date_month().get(2) + " " + eventObjects.get(position).getStart_date_day().get(0) + ", " + eventObjects.get(position).getStart_date_year().get(0));
        time.setText(eventObjects.get(position).getStart_date_time().get(2));
        venue.setText(eventObjects.get(position).getVenue_name());
        //urlpath.setText(objects.get(position).getEventURL());

        if (AnimationFlag) {
            Animation animation = AnimationUtils.loadAnimation(getContext(), (position > lastPosition) ? R.anim.up_from_bottom : R.anim.bottom_from_up);
            row.startAnimation(animation);
            lastPosition = position;
        }


        return row;
    }
}
