package com.sourcey.hackathon;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONObject;


public class ProfileScreenXMLUIDesign extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_design_profile_screen_xml_ui_design);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            try{
                JSONObject json = new JSONObject(extras.getString("json"));
                String email = json.getString("email");
                TextView mTextView = (TextView) findViewById(R.id.user_profile_name);
                mTextView.setText(email);
            } catch(Exception ex) {

            }

        }
    }
}