package com.example.daddyz.turtleboys.friendFeed;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.daddyz.turtleboys.R;
import com.example.daddyz.turtleboys.VolleyJSONObjectRequest;
import com.example.daddyz.turtleboys.VolleyRequestQueue;
import com.example.daddyz.turtleboys.friendFeed.dummy.DummyContent;
import com.example.daddyz.turtleboys.subclasses.FollowUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class followerListFragment extends Fragment implements Response.Listener,AbsListView.OnItemClickListener, Response.ErrorListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String REQUEST_TAG = "User List Fragment";
    //private Button mButton;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private followerListAdapter adapter;
    private RequestQueue mQueue;
    private ArrayList<FollowUser> userArray;
    private ListView list;
    private View rootView;
    private OnFragmentInteractionListener mListener;

    private AbsListView mListView;


    // TODO: Rename and change types of parameters
    public static userListFragment newInstance(String param1, String param2) {
        userListFragment fragment = new userListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public followerListFragment() {
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        //  mTextView.setText(error.getMessage());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // TODO: Change Adapter to display your content
        adapter = new followerListAdapter(getActivity(),  R.layout.user_list_follow_row, userArray,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        rootView = inflater.inflate(R.layout.listfragment, container, false);
        list = (ListView) rootView.findViewById(R.id.listView);
        list.setClickable(true);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        mQueue = VolleyRequestQueue.getInstance(this.getActivity().getApplicationContext())
                .getRequestQueue();
        String url = "http://api.dev.turtleboys.com/v1/followers/list";
        final VolleyJSONObjectRequest jsonRequest = new VolleyJSONObjectRequest(Request.Method
                .GET, url,
                new JSONObject(), this, this);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);
    }


    @Override
    public void onResponse(Object response) {
        JSONObject mainObject = ((JSONObject) response);

        try{
            Log.i("Response: ", mainObject.getString("result") + ": "  + mainObject.getString("message"));
        } catch(NullPointerException err){
            Log.i("ERROR", "No Response Given - userListFragment");
        } catch(JSONException err){
            Log.i("ERROR", "No Response Given - userListFragment");
        }

        loadEvents(response);
    }

    public void loadEvents(Object response){
        userArray = createFollowUserObjectsFromResponse(response);
        if(null != this.getActivity()) {
            adapter = new followerListAdapter(this.getActivity(), R.layout.user_list_follow_row, userArray, this);
            list.setAdapter(adapter);
            ((BaseAdapter) list.getAdapter()).notifyDataSetChanged();
        }

        rootView.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

    public ArrayList<FollowUser> createFollowUserObjectsFromResponse(Object response){
        ArrayList<FollowUser> userTmpArray = new ArrayList<>();

        try{
            JSONObject mainObject = ((JSONObject) response);
            JSONObject itemsObject = mainObject.getJSONObject("items");
            Iterator<?> keys = itemsObject.keys();


            while( keys.hasNext() ) {
                String key = (String)keys.next();
                if ( itemsObject.get(key) instanceof JSONObject ) {
                    FollowUser fUser = new FollowUser();
                    fUser.setUserId(((JSONObject) itemsObject.get(key)).getString("userId"));
                    fUser.setUsername(((JSONObject) itemsObject.get(key)).getString("username"));
                    fUser.setFirstName(firstLetterCaps(((JSONObject) itemsObject.get(key)).getString("firstName")));
                    fUser.setLastName(firstLetterCaps(((JSONObject) itemsObject.get(key)).getString("lastName")));
                    fUser.setEmail(((JSONObject) itemsObject.get(key)).getString("email"));
                    fUser.setFollowing(((JSONObject) itemsObject.get(key)).getInt("following"));

                    userTmpArray.add(fUser);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userTmpArray;
    }

    public void updateItemAtPosition(int position, ArrayList<FollowUser> userObjects) {
        this.userArray = userObjects;

        int visiblePosition = this.list.getFirstVisiblePosition();
        View view = this.list.getChildAt(position - visiblePosition);
        this.list.getAdapter().getView(position, view, this.list);

        adapter.notifyDataSetChanged();

        //Toast.makeText(this.getActivity().getApplicationContext(), "Hi! I updated you!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Function to capitalize first letter in string and
     * lower case the remaining characters.
     *
     * @param data
     * @return
     */
    static public String firstLetterCaps ( String data )
    {
        String firstLetter = data.substring(0,1).toUpperCase();
        String restLetters = data.substring(1).toLowerCase();
        return firstLetter + restLetters;
    }
}
