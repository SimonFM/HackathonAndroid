package com.sourcey.hackathon;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewLoader;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public abstract class BaseActivity extends AppCompatActivity implements WeekView.EventClickListener,
        MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener {
    private static final int TYPE_WEEK_VIEW = 7;
    private WeekView mWeekView;
    private String getBookingsURL =  "http://10.0.2.2:4000/merchantBooking";
    private String bookingURL = "http://10.0.2.2:4000/booking";
    private String singleUserURL = "http://10.0.2.2:4000/singleUser";
    ArrayList<JSONArray> array = new ArrayList<>();
    public static final String MyPREFERENCES = "MyPrefs";
    Dialog dialog;
    String email = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mWeekView = (WeekView) findViewById(R.id.weekView);
        mWeekView.setOnEventClickListener(this);
        mWeekView.setMonthChangeListener(this);
        mWeekView.setEventLongPressListener(this);
        mWeekView.setEmptyViewLongPressListener(this);
        mWeekView.setNumberOfVisibleDays(TYPE_WEEK_VIEW);
        mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
        mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        setupDateTimeInterpreter(false);
        String merchantName = "";
        getEmptyBookings(merchantName);
        dialog = new Dialog(this);

        dialog.setContentView(R.layout.activity_booking_dialog);
        dialog.setTitle("Booking Confirmation");

        //adding text dynamically
        TextView txt = (TextView) dialog.findViewById(R.id.textView);
        txt.setText("Are you sure you want this time?");
    }


    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     * @param shortDate True if the date values should be short.
     */
    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                if (shortDate) {
                    weekday = String.valueOf(weekday.charAt(0));
                }
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(this, "Clicked " + event.getName() + event.getStartTime().getTime().getHours() + " : " + event.getStartTime().getTime().getMinutes(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(this, "Long pressed event: " + event.getName() + event.getStartTime().getTime().getHours() + " : " + event.getStartTime().getTime().getMinutes(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyViewLongPress(final Calendar time) {
        Toast.makeText(this, "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();


        Bundle extras = getIntent().getExtras();
        final String merchant = extras.getString("name");
        final String currentUserEmail = getUserEmail();
        final JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("email", currentUserEmail);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        Button dismissButton = (Button) dialog.findViewById(R.id.button);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        Button confirmButton = (Button) dialog.findViewById(R.id.button2);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            OkHttpClient client = new OkHttpClient();
                            MediaType mediaType = MediaType.parse("application/json");
                            RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
                            Request request = new Request.Builder()
                                    .url(singleUserURL)
                                    .post(body)
                                    .addHeader("content-type", "application/json")
                                    .addHeader("cache-control", "no-cache")
                                    .build();

                            Response response = client.newCall(request).execute();
                            if (response.isSuccessful()) {
                                JSONArray req = new JSONArray(response.body().string());
                                JSONObject reqBody = req.getJSONObject(0);

                                int year = time.get(Calendar.YEAR);
                                int month = time.get(Calendar.MONTH);
                                int day = time.get(Calendar.DAY_OF_MONTH);
                                int hour = time.get(Calendar.HOUR_OF_DAY);
                                reqBody.put("merchant", merchant);
                                reqBody.put("startHour", hour);
                                reqBody.put("endHour", hour + 1);
                                reqBody.put("startDate", day + "/" + month + "/" + year);
                                reqBody.put("endDate", day + "/" + month + "/" + year);
                                reqBody.put("lessonCount", 1);

                                body = RequestBody.create(mediaType, reqBody.toString());
                                request = new Request.Builder()
                                        .url(bookingURL)
                                        .post(body)
                                        .addHeader("content-type", "application/json")
                                        .addHeader("cache-control", "no-cache")
                                        .build();
                                response = client.newCall(request).execute();
                                if (response.isSuccessful()) {
                                    Message msg = handler.obtainMessage();
                                    msg.arg1 = 1;
                                    handler.sendMessage(msg);

                                } else {
                                    Message msg = handler.obtainMessage();
                                    msg.arg1 = 2;
                                    handler.sendMessage(msg);
                                }
                            } else {
                                Message msg = handler.obtainMessage();
                                msg.arg1 = 2;
                                handler.sendMessage(msg);                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();


            }
        });

    }

    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch(msg.arg1){
                case 1:
                    Toast.makeText(getApplicationContext(), "Booking successful", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "Unable to book this slot, please try again", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private String getUserEmail(){
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        return  prefs.getString("CurrentUser", null);
    }

    private void getEmptyBookings(final String merchant){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();

                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(mediaType, "{\n    \"merchant\" : \"" + merchant + "\"\n}");
                    Request request = new Request.Builder()
                            .url(getBookingsURL)
                            .post(body)
                            .addHeader("content-type", "application/json")
                            .addHeader("cache-control", "no-cache")
                            .build();

                    Response response = client.newCall(request).execute();
                    if(response.isSuccessful()){
                        array.add(new JSONArray(response.body().string()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
