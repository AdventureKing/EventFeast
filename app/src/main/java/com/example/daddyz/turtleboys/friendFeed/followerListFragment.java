package com.example.daddyz.turtleboys.friendFeed;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.daddyz.turtleboys.R;
import com.example.daddyz.turtleboys.eventfeed.CustomJSONObjectRequest;
import com.example.daddyz.turtleboys.eventfeed.CustomVolleyRequestQueue;
import com.example.daddyz.turtleboys.eventfeed.eventfeedAdapter;
import com.example.daddyz.turtleboys.friendFeed.dummy.DummyContent;
import com.example.daddyz.turtleboys.subclasses.FollowUser;
import com.example.daddyz.turtleboys.subclasses.GigUser;

import org.json.JSONObject;

import java.util.ArrayList;

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
    public static final String REQUEST_TAG = "MainVolleyActivity";
    //private Button mButton;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private followerListAdapter adapter;
    private RequestQueue mQueue;
    private ArrayList<FollowUser> followerArray;
    private ListView list;
    private View rootView;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static followerListFragment newInstance(String param1, String param2) {
        followerListFragment fragment = new followerListFragment();
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
        adapter = new followerListAdapter(getActivity(),  R.layout.user_list_row, followerArray );

       /* mAdapter = new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS);*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        rootView = inflater.inflate(R.layout.listfragment, container, false);
        list = (ListView) rootView.findViewById(R.id.listView);
        list.setClickable(true);

        // Set the adapter
       // mListView = (AbsListView) view.findViewById(android.R.id.list);
       // ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
       // mListView.setOnItemClickListener(this);


        return rootView;
    }

    public ArrayList<FollowUser> createFakeArrayList(){
        FollowUser user = new FollowUser(1, 0, "gh76hg546", "ads4353545h");
        FollowUser user2 = new FollowUser(2, 0, "lolo8769", "asdfd342222");
        FollowUser user3 = new FollowUser(3, 1, "876jhgg78", "098698kjhkjh");

        ArrayList<FollowUser> list = new ArrayList();
        list.add(user);
        list.add(user2);
        list.add(user3);

        return list;
    }

    @Override
    public void onStart() {
        super.onStart();

        mQueue = CustomVolleyRequestQueue.getInstance(this.getActivity().getApplicationContext())
                .getRequestQueue();
        String url = "https://api.turtleboys.com/v1/events/find/kendrick";
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method
                .GET, url,
                new JSONObject(), this, this);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);
    }


    @Override
    public void onResponse(Object response) {
        loadEvents(response);
    }

    public void loadEvents(Object response){
        followerArray = createFakeArrayList();

        adapter = new followerListAdapter(getActivity(), R.layout.user_list_row, followerArray);
        list.setAdapter(adapter);
        ((BaseAdapter)list.getAdapter()).notifyDataSetChanged();

        rootView.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    }

  /*  @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

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
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
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

}
