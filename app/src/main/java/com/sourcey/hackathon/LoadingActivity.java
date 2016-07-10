package com.sourcey.hackathon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.vlonjatg.progressactivity.ProgressActivity;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import android.os.Handler;

/**
 * Created by simon on 09/07/2016.
 */
public class LoadingActivity extends Activity {

    //Introduce an delay
    private final int WAIT_TIME = 2500;
    private String getBookingsURL =  "http://10.0.2.2:4000/merchantBooking";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        System.out.println("LoadingScreenActivity  screen started");
        setContentView(R.layout.loading_activity);
        findViewById(R.id.mainSpinner1).setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();

                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(mediaType, "{\n    \"merchant\" : \"AirportDriver\"\n}");
                    Request request = new Request.Builder()
                            .url(getBookingsURL)
                            .post(body)
                            .addHeader("content-type", "application/json")
                            .addHeader("cache-control", "no-cache")
                            .build();

                    Response response = client.newCall(request).execute();
                    JSONArray array = new JSONArray(response.body().string());
                } catch(Exception ex){
                    ex.printStackTrace();
                }

                System.out.println("Going to Profile Data");
	            /* Create an Intent that will start the ProfileData-Activity. */
                Intent mainIntent = new Intent(LoadingActivity.this, BookingsActivity.class);
                LoadingActivity.this.startActivity(mainIntent);
                LoadingActivity.this.finish();
            }
        }, WAIT_TIME);
    }
}
