package com.example.daddyz.turtleboys.followtabview.followTab;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daddyz.turtleboys.R;
import com.example.daddyz.turtleboys.followtabview.genreTab.genrefollowfragment;

/**
 * Created by snow on 7/23/2015.
 */
public class followtabview extends Fragment {

    //the whole view
    private View inflatedView;
    //the over all pager that moves between the tabs
    private ViewPager viewPager;



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //lock the drawer because we are inception in this bitch
        //main_activity->fragment->fragment

        inflatedView = inflater.inflate(R.layout.feedtablayout, container, false);

        TabLayout tabLayout = (TabLayout) inflatedView.findViewById(R.id.sliding_tabs);

        //change tab text to white
        tabLayout.setTabTextColors(ColorStateList.valueOf(Color.WHITE));
        //create tab for event feed
        tabLayout.addTab(tabLayout.newTab().setText("Users"));
        //create tab for newsfeed
        tabLayout.addTab(tabLayout.newTab().setText("Genres"));

        //thing that moves between the tabs helps do all the hard stuff
        viewPager = (ViewPager) inflatedView.findViewById(R.id.viewpager);

        //the tab bar functionality is layed out here
        viewPager.setAdapter(new PagerAdapter
                (getFragmentManager(), tabLayout.getTabCount()));
        //set the on page listener so it will change position
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //what happens when a tab is selected
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            //dont need to mess with but needs to be overrided
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            //Possibaly a call for high end load logic here
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


        //sets up what should happen when a tab is sleected
        public PagerAdapter(FragmentManager fm, int mNumOfTabs) {
            super(fm);
            this.mNumOfTabs = mNumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            //what ever tab they are on it will load that fragment or switch back to it up to user
            //to pull down to update
            switch (position) {
                case 0:
                    //if user is on first tab
                    followListFragment tab1 = new followListFragment();
                    return tab1;
                case 1:
                    //if user is on second tab
                    genrefollowfragment tab2 = new genrefollowfragment();
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