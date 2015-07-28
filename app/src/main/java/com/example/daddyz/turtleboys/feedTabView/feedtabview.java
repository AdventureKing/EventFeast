package com.example.daddyz.turtleboys.feedTabView;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.daddyz.turtleboys.R;
import com.example.daddyz.turtleboys.eventfeed.EventFeedFragment;
import com.example.daddyz.turtleboys.newsfeed.newsfeedFragment;

/**
 * Created by snow on 7/23/2015.
 */
public class feedtabview extends Fragment {


    private View view;

    private ViewPager viewPager;
    private ActionBar actionBar;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //lock the drawer because we are inception in this bitch
        //main_activity->fragment->fragment

        View inflatedView = inflater.inflate(R.layout.feedtablayout, container, false);

        TabLayout tabLayout = (TabLayout) inflatedView.findViewById(R.id.sliding_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Newsfeed"));
        tabLayout.addTab(tabLayout.newTab().setText("EventFeed"));
        final ViewPager viewPager = (ViewPager) inflatedView.findViewById(R.id.viewpager);

        //the tab bar functionality is layed out here
        viewPager.setAdapter(new PagerAdapter
                (getFragmentManager(), tabLayout.getTabCount()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return inflatedView;
    }

    //custom adapter for the view pager
    //the view pager is the thing that goes back and fowarth
    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;



        public PagerAdapter(FragmentManager fm, int mNumOfTabs) {
            super(fm);
            this.mNumOfTabs = mNumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    //if user is on first tab
                    newsfeedFragment tab1 = new newsfeedFragment();
                    return tab1;
                case 1:
                    //if user is on second tab
                    EventFeedFragment tab2 = new EventFeedFragment();
                    return tab2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            //total return should always be 2
            return mNumOfTabs;
        }
    }
}