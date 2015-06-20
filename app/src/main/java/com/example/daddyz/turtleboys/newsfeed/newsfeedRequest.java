package com.example.daddyz.turtleboys.newsfeed;


import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by snow on 6/18/2015.
 */
public class newsfeedRequest extends Activity {

        newsfeedObject[] tableItems;


        public newsfeedRequest(){
            fillTable();
        }

        private void fillTable() {

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://45.55.142.106/ws/rest/findEvents/jazz";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            if(response != null){
                                tableItems = convertToNFOs(response);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    tableItems = null;
                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);

        }

        public newsfeedObject[] getTableData(){
            return tableItems;
        }

        public newsfeedObject[] convertToNFOs(String response) {

            System.out.println(response);

            newsfeedObject[] NFOs = new newsfeedObject[1];
            NFOs[0].setEventCity("TX");
            NFOs[0].setEventDesc("Linkin Park");
            NFOs[0].setEventDate("02/15/2016");

            return NFOs;
        }

}
