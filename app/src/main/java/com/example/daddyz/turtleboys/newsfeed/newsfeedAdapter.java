package com.example.daddyz.turtleboys.newsfeed;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.daddyz.turtleboys.R;

/**
 * Created by snow on 6/17/2015.
 */
public class newsfeedAdapter extends ArrayAdapter<newsfeedObject> {
    private Context context;
    private int resource;
    private newsfeedObject[] objects;
    private int[] colors = new int[] { 0x30FF0000, 0x300000FF };
    public newsfeedAdapter(Context context, int resource, newsfeedObject[] objects) {
        super(context, resource,objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }
    public int getItemViewType(int position) {
        // return a value between 0 and (getViewTypeCount - 1)
        return position % 2;
    }
    public int getViewTypeCount() {
        // return the total number of view types. this value should never change at runtime
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get inflator so it will strech the view to fill the row data
        LayoutInflater inflater=
                ((Activity) context).getLayoutInflater();

        //change row layout depending on row number
        int rowType = getItemViewType(position);
        int layoutResource = 0; // determined by view type
        int viewType = getItemViewType(position);
        Log.d("test", viewType + "");
        switch(viewType) {
            case 0:
                layoutResource = R.layout.newsfeedrow; break;
            case 1:
                layoutResource = R.layout.newsfeedrowodd; break;
        }
        //set the view to the odd or even row view
        View row=inflater.inflate(layoutResource,parent,false);

        //put info into view
        TextView title= (TextView)
                row.findViewById(R.id.secondLine);
        TextView date =(TextView)row.findViewById(R.id.date);
        TextView number=(TextView)
                row.findViewById(R.id.firstLine);
        title.setText((CharSequence)
                objects[position].description2);
        number.setText(objects[position].description);
        date.setText(objects[position].date);

        return row;
    }
}
