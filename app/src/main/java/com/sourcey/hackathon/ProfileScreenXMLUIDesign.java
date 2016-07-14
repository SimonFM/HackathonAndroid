package com.sourcey.hackathon;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ProfileScreenXMLUIDesign extends AppCompatActivity {

    private Button scheduleButton;
    private JSONObject json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_design_profile_screen_xml_ui_design);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            try{
                json = new JSONObject(extras.getString("json"));
                String name = json.getString("name");
                String description = json.getString("description");
                TextView mTextView = (TextView) findViewById(R.id.user_profile_name);
                mTextView.setText(name);
                TextView bio = (TextView) findViewById(R.id.user_profile_short_bio);
                bio.setText(description);
            } catch(Exception ex) {
                ex.printStackTrace();
            }

        }
        scheduleButton = (Button) findViewById(R.id.schedule_button);

        scheduleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try{
                    Intent intentToBook = new Intent(getApplicationContext(), BookingsActivity.class);
                    TextView mTextView = (TextView) findViewById(R.id.user_profile_name);
                    intentToBook.putExtra("name", mTextView.getText());
                    intentToBook.putExtra("email", json.getString("email"));
                    startActivity(intentToBook);
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }

        });
    }
}