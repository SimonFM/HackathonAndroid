package com.sourcey.hackathon;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;


public class ProfileScreenXMLUIDesign extends AppCompatActivity {

    private Button scheduleButton;
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

        scheduleButton = (Button) findViewById(R.id.schedule_button);

        scheduleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intentToBook = new Intent(getApplicationContext(), BookingsActivity.class);
                TextView mTextView = (TextView) findViewById(R.id.user_profile_name);
                intentToBook.putExtra("name", mTextView.getText());
                startActivity(intentToBook);
            }

        });
    }
}