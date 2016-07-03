package com.sourcey.hackathon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class CardViewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";
    public static final String MyPREFERENCES = "MyPrefs";

    Intent loginIntent, userIntent;
    SharedPreferences sharedpreferences;
    RequestQueue queue;
    String getUsers = "http://10.0.2.2:4000/user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        loginIntent = new Intent(this, LoginActivity.class);
        userIntent = new Intent(this, ProfileScreenXMLUIDesign.class);
        queue = Volley.newRequestQueue(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String user = sharedpreferences.getString("CurrentUser", null);
        //if(user == null){
            startActivity(loginIntent);
       // }

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
                startActivity(userIntent);
            }
        });
    }

    private ArrayList<DataObject> getDataSet() {
        final ArrayList results = new ArrayList<DataObject>();

//        JSONObject json = new JSONObject();
//        RequestFuture<JSONObject> future = RequestFuture.newFuture();
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getUsers, json, future, future);
//        queue.add(request);

//        try {
//            JSONObject response = future.get();
//            if (response != null && response.get("code").equals("200")) {
//                // add all the users to the DataObjects class
//
//            }
//            System.out.println(response);
//        } catch (InterruptedException e) {}
//          catch (ExecutionException e) {}
//          catch (Exception ex) {
//              for (int index = 0; index < 20; index++) {
//                  DataObject obj = new DataObject("Some Primary Text " + index, "Secondary " + index);
//                  results.add(index, obj);
////              }
        try{
            // Request a string response
            StringRequest stringRequest = new StringRequest(Request.Method.GET, getUsers,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Result handling
                            //System.out.println(response.substring(0,100));
                            try{
                                JSONArray json = new JSONArray(response);
                                List<JSONObject> list = new LinkedList<JSONObject>();
                                for(int i = 0; i < json.length(); i++){
                                    JSONObject j = new JSONObject(json.get(i).toString());
                                    DataObject obj = new DataObject(j.get("email").toString(), j.get("phoneNumber").toString());
                                    results.add(obj);

                                }
                                queue.stop();
                                mAdapter = new MyRecyclerViewAdapter(getDataSet());
                                mRecyclerView.setAdapter(mAdapter);
                            } catch(Exception ex) {

                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Error handling
                    System.out.println("Something went wrong!");
                    error.printStackTrace();

                }
            });
            queue.add(stringRequest);
        } catch (Exception ex) {

        }

        return results;
    }
}