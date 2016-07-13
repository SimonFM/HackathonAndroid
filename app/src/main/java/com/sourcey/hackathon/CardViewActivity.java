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
import android.widget.ImageView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class CardViewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";
    public static final String MyPREFERENCES = "MyPrefs";
    private boolean selection = false;

    Intent loginIntent, userIntent;
    SharedPreferences sharedpreferences;
    //String getMerchants = "http://10.157.194.119:4000/user";
    String getMerchants = "http://10.0.2.2:4000/merchant";
    String userBookingsURL = "http://10.0.2.2:4000/userBookings";
    ArrayList<DataObject> merchants;
    private String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        loginIntent = new Intent(this, LoginActivity.class);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        user = sharedpreferences.getString("CurrentUser", null);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(getListOfMerchants());
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(getListOfMerchants());
        mRecyclerView.setAdapter(mAdapter);

        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                userIntent = new Intent(getApplicationContext(), ProfileScreenXMLUIDesign.class);
                DataObject obj = merchants.get(position);
                userIntent.putExtra("json", obj.getJson().toString());
                Log.i(LOG_TAG, " Clicked on Item " + position);
                startActivity(userIntent);
            }
        });
    }

    private ArrayList<DataObject> getListOfMerchants() {
        final ArrayList results = new ArrayList<DataObject>();
        final boolean[] wasSuccess = {true};

        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(getMerchants)
                            .get()
                            .addHeader("content-type", "application/json")
                            .addHeader("cache-control", "no-cache")
                            .build();

                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        wasSuccess[0] = true;
                        JSONArray array = new JSONArray(response.body().string());

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject json = array.getJSONObject(i);
                            DataObject obj = new DataObject(json.get("name").toString(), json.get("email").toString(), json);
                            results.add(obj);
                        }
                    } else {
                        wasSuccess[0] = false;
                    }
                } catch (Exception ex) {

                }
//                for (int i = 0; i < 20; i++) {
//                    DataObject obj = new DataObject("Name: " + i, "description: "+i, new JSONObject());
//                    results.add(obj);
//                }
            }
        }).start();
        merchants = results;
        return results;
    }

    private ArrayList<DataObject> getListOfUserBookings() {
        final ArrayList results = new ArrayList<>();
        final boolean[] wasSuccess = {true};

        if(user.isEmpty()){

        } else {
            new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        OkHttpClient client = new OkHttpClient();
                        MediaType mediaType = MediaType.parse("application/json");
                        RequestBody body = RequestBody.create(mediaType, "{\r\n    \"email\" : \""+ user + "\"\r\n}");
                        Request request = new Request.Builder()
                                .url(userBookingsURL)
                                .post(body)
                                .addHeader("content-type", "application/json")
                                .addHeader("cache-control", "no-cache")

                                .build();

                        Response response = client.newCall(request).execute();
                        if (response.isSuccessful()) {
                            wasSuccess[0] = true;
                            JSONArray array = new JSONArray(response.body().string());

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject json = array.getJSONObject(i);
                                DataObject obj = new DataObject(json.get("name").toString(), json.get("email").toString(), json);
                                results.add(obj);
                            }
                        } else {
                            wasSuccess[0] = false;
                        }
                    } catch (Exception ex) {

                    }
//                for (int i = 0; i < 20; i++) {
//                    DataObject obj = new DataObject("Name: " + i, "description: "+i, new JSONObject());
//                    results.add(obj);
//                }
                }
            }).start();
            merchants = results;
        }


        return results;
    }
}