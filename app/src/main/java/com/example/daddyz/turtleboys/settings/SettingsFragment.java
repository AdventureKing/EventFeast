package com.example.daddyz.turtleboys.settings;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.daddyz.turtleboys.R;
import com.parse.ParseClassName;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.PushService;

/**
 * Created by Gregory Hooks Jr on 6/28/2015.
 */
public class SettingsFragment extends Fragment {

    private View rootView;
    private Toolbar toolbar;
    private ActionBar actionBar;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootView = inflater.inflate(R.layout.settings, container, false);

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        //The following line allowed the onOptionsCreated() method to be called
        setHasOptionsMenu(true);

        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Settings");

        //Set up back arrow icon on toolbar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                container.findViewById(R.id.frame).setVisibility(View.VISIBLE);
            }
        });

        //Clear Previous Activity's Frame
        container.findViewById(R.id.frame).setVisibility(View.INVISIBLE);

        //Setup Preferences Fragment to display settings
        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager
                .beginTransaction();
        PrefsFragment mPrefsFragment = new PrefsFragment();
        mFragmentTransaction.replace(R.id.settings_frame, mPrefsFragment);
        mFragmentTransaction.commit();

        Log.d("CustomAdapter", "SettingsFragment onCreateView successful");

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        if (menu != null){
            menu.clear();

            //The following is how to hide individual menu items
            //menu.findItem(R.id.action_gallery).setVisible(false);
            //menu.findItem(R.id.action_settings).setVisible(false);
        }
    }

    public void onBackPressed() {
        Log.d("Test","This is being called in maindrawer");
        if(getFragmentManager().getBackStackEntryCount() != 0) {
            getFragmentManager().popBackStack();
        }
    }


    public static class PrefsFragment extends PreferenceFragment {

        private CheckBoxPreference notificationPreference;
        private CheckBoxPreference pushPreference;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.settings_preference_fragment);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            //Notification Preference
            notificationPreference = (CheckBoxPreference)  getPreferenceManager().findPreference("notification_preference");
            notificationPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    // insert custom code
                    return true;
                }
            });

            //Push Preference
            pushPreference = (CheckBoxPreference)  getPreferenceManager().findPreference("push_preference");
            pushPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    // insert custom code
                    return true;
                }
            });

            //return inflater.inflate(R.layout.settings, container, false);
            return (super.onCreateView(inflater, container, savedInstanceState));
        }
    }

    @ParseClassName("Receiver")
    public static class Receiver extends ParsePushBroadcastReceiver{
        private static final String TAG = "Receiver";

        /*@Override
        protected void onPushOpen(Context context, Intent intent){...}*/

        @Override
        protected void onPushReceive(Context context, Intent intent){
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            if (preferences.getBoolean("push_preference", true)){
                super.onPushReceive(context, intent);
            }
        }
    }
}
