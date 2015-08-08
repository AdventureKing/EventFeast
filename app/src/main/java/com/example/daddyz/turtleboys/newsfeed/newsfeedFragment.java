package com.example.daddyz.turtleboys.newsfeed;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.daddyz.turtleboys.R;
import com.example.daddyz.turtleboys.maindrawer;

import java.util.ArrayList;

/**
 * Created by snow on 7/11/2015.
 */
//had to change to android.app.Fragment
public class newsfeedFragment extends Fragment implements Response.Listener,
        Response.ErrorListener {

    public static final String REQUEST_TAG = "MainVolleyActivity";
    private Button mButton;

    private TextView mTextView;
    private RequestQueue mQueue;
    private View rootView;
    private ListView list;
    private newsfeedAdapter adapter;
    private ArrayList<newsfeedObject> newsfeedList;
    private  FloatingActionButton fab;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = inflater.inflate(R.layout.newsfeedlistfragment, container, false);
        //swipe layout
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);
        //pull down to refresh the list and set the colors of the loading icon
        mSwipeRefreshLayout.setColorSchemeResources(R.color.loadingorange, R.color.loadinggreen, R.color.loadingblue);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();

            }
        });
        //list
        list = (ListView) rootView.findViewById(R.id.listView);
        mTextView = (TextView) rootView.findViewById(R.id.textView);
        mButton = (Button) rootView.findViewById(R.id.button);

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                newsfeedPostForm fragment = new newsfeedPostForm();

                ((maindrawer) getActivity()).getFragmentManager().beginTransaction().replace(R.id.drawer, fragment, "NewsFeedPostForm").addToBackStack("NewsFeedPostForm").commit();
                Toast.makeText(getActivity(), "User Wants to make a post", Toast.LENGTH_SHORT).show();
            }
        });


        newsfeedList = new ArrayList<newsfeedObject>();
        newsfeedObject obj = new newsfeedObject();
        obj.setUsername("@richardg");
        obj.setTitle("just posted yesterday at Stubbs (SXSW 2015)");
        obj.setDescription("This concert is awesome!");
        obj.setLikeCount(34);
        obj.setPostImageUrl("http://images1.citypages.com/imager/u/blog/6646111/haileys_4_ed_steele.jpg");
        newsfeedObject obj3 = new newsfeedObject();
        obj3.setUsername("@brandon");
        obj3.setTitle("posted on 08/05 in Downtown San Antonio (Music Festival 2015)");
        obj3.setDescription("The music is amazing!");
        obj3.setLikeCount(8);
        obj3.setPostImageUrl("http://www.popphoto.com/sites/popphoto.com/files/styles/medium_1x_/public/import/2013/files/_images/201307/176643_418406198229685_870129014_o.jpg?itok=j9cVxPL7");
        newsfeedObject obj4 = new newsfeedObject();
        obj4.setUsername("@gregg");
        obj4.setTitle("posted on 08/04 in San Antonio at LOL Comedy Club (Comedy Festival)");
        obj4.setDescription("This dude is so funny!");
        obj4.setLikeCount(67);
        obj4.setPostImageUrl("http://static01.nyt.com/images/2012/09/06/arts/COMEDY/COMEDY-articleLarge.jpg");
        newsfeedObject obj5 = new newsfeedObject();
        obj5.setUsername("@gregg");
        obj5.setTitle("posted on 08/03 in San Antonio at AT&T Center (Spurs vs. Cavs)");
        obj5.setDescription("Spurs are about to win this one!");
        obj5.setLikeCount(25);
        obj5.setPostImageUrl("https://upload.wikimedia.org/wikipedia/commons/a/af/Spurs_Coyote_sign.JPG");
        newsfeedObject obj6 = new newsfeedObject();
        obj6.setUsername("@jared");
        obj6.setTitle("posted on 08/03 in San Antonio at AT&T Center (Greenday Concert)");
        obj6.setDescription("Green day concert is crazy!");
        obj6.setLikeCount(25);
        obj6.setPostImageUrl("http://www.playmakeronline.com/wp-content/uploads/2013/03/DSC_0255.jpg");
        newsfeedObject obj7 = new newsfeedObject();
        obj7.setUsername("@gregg");
        obj7.setTitle("posted on 08/03 in San Antonio at Alamodome (UTSA vs. Louisiana Tech)");
        obj7.setDescription("UTSA Tailgating season again!");
        obj7.setLikeCount(66);
        obj7.setPostImageUrl("http://cdn.therivardreport.com/wp-content/uploads/2012/11/RomoUTSA.jpg");
        newsfeedObject obj8 = new newsfeedObject();
        obj8.setUsername("@brandon");
        obj8.setTitle("posted on 08/02 in San Antonio at La Cantera (Apple Workshop)");
        obj8.setDescription("Learning some new things at this workshop");
        obj8.setLikeCount(25);
        obj8.setPostImageUrl("http://www.digitalharbor.org/wp-content/uploads/2014/08/13040569274_c6475389ac_z_wrsnw8.jpg");
        newsfeedObject obj9 = new newsfeedObject();
        obj9.setUsername("@zachr");
        obj9.setTitle("posted on 08/01 in Austin at Convention Center (TechCrunch Disrupt)");
        obj9.setDescription("Trying to create something epic..");
        obj9.setLikeCount(25);
        obj9.setPostImageUrl("https://tctechcrunch2011.files.wordpress.com/2013/09/disrupt-hackathon.png?w=738");
        newsfeedObject obj10 = new newsfeedObject();
        obj10.setUsername("@gregg");
        obj10.setTitle("posted on 07/25 in San Antonio at AT&T Center (Prince Concert)");
        obj10.setDescription("Purple rain!");
        obj10.setLikeCount(98);
        obj10.setPostImageUrl("http://www.mtvhive.com/wp-content/uploads/2013/03/Prince-SXSW-2013-2-640.jpg");
        newsfeedList.add(obj);
        newsfeedList.add(obj3);
        newsfeedList.add(obj4);
        newsfeedList.add(obj5);
        newsfeedList.add(obj6);
        newsfeedList.add(obj7);
        newsfeedList.add(obj8);
        newsfeedList.add(obj9);
        newsfeedList.add(obj10);
        adapter = new newsfeedAdapter(getActivity(), R.layout.eventfeedroweven, newsfeedList);
        list.setAdapter(adapter);
        Log.d("CustomAdapter", "Newsfeed Launched successfully");
        list.setClickable(true);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //get object at that position
                newsfeedObject obj = (newsfeedObject) list.getItemAtPosition(position);

                newsfeedPostDetail fragment = new newsfeedPostDetail();
                fragment.setObj(obj);

                ((maindrawer) getActivity()).getFragmentManager().beginTransaction().replace(R.id.drawer, fragment, "NewsFeedPostDetail").addToBackStack("NewsFeedPostDetail").commit();

            }
        });

        return rootView;
    }


    //refresh the list
    private void refreshContent() {


        //load list

        //set new list to the main view
        adapter = new newsfeedAdapter(getActivity(), R.layout.eventfeedroweven, newsfeedList);
        list.setAdapter(adapter);


        //refreshing is done at this point make the icon disappear

        mSwipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getActivity(), "User Wants to refresh the list", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onErrorResponse(VolleyError volleyError) {

    }

    @Override
    public void onResponse(Object o) {

    }
}
