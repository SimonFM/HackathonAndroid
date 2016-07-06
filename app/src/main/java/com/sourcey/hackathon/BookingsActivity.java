package com.sourcey.hackathon;

import com.alamkanak.weekview.WeekViewEvent;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A basic example of how to use week view library.
 * Created by Raquib-ul-Alam Kanak on 1/3/2014.
 * Website: http://alamkanak.github.io
 */
public class BookingsActivity extends BaseActivity {

    private String getBookingsURL =  "http://10.0.2.2:4000/booking";

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // Populate the week view with some events.
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

//        for(int i = 0; i < 7; i++){
//            for(int j = 9; j < 21; j++){
//                Calendar startTime = Calendar.getInstance();
//                startTime.set(Calendar.DAY_OF_WEEK, i);
//                startTime.set(Calendar.HOUR_OF_DAY, j);
//                startTime.set(Calendar.MINUTE, 0);
//                startTime.set(Calendar.MONTH, newMonth);
//                startTime.set(Calendar.YEAR, newYear);
//
//                Calendar endTime = (Calendar) startTime.clone();
//                endTime.add(Calendar.HOUR, j + 1);
//                endTime.add(Calendar.MONTH, newMonth);
//                endTime.add(Calendar.DAY_OF_WEEK, i);
//                WeekViewEvent event = new WeekViewEvent(i, getEventTitle(startTime), startTime, endTime);
//                event.setColor(getResources().getColor(R.color.event_color_01));
//                events.add(event);
//
//            }
//        }


//        try{
//            JSONArray jsonArray = getEvents();
//
//            for(int i = 0; i < jsonArray.length();i++){
//                Integer startMonth = getStartMonth(jsonArray.getJSONObject(i));
//                Integer endMonth = getEndMonth(jsonArray.getJSONObject(i));
//                Integer startDay = getStartDay(jsonArray.getJSONObject(i));
//                Integer endDay = getEndDay(jsonArray.getJSONObject(i));
//                Integer startYear = getStartYear(jsonArray.getJSONObject(i));
//                Integer endYear = getEndYear(jsonArray.getJSONObject(i));
//                Integer startHour = getStartHour(jsonArray.getJSONObject(i));
//                Integer endHour = getEndHour(jsonArray.getJSONObject(i));
//
//                Calendar startTime = Calendar.getInstance();
//                startTime.set(Calendar.DAY_OF_MONTH, startDay);
//                startTime.set(Calendar.HOUR_OF_DAY, startHour);
//                startTime.set(Calendar.MINUTE, 0);
//                startTime.set(Calendar.MONTH, startMonth);
//                startTime.set(Calendar.YEAR, startYear);
//
//                Calendar endTime = (Calendar) startTime.clone();
//                endTime.add(Calendar.HOUR, endHour);
//                endTime.add(Calendar.MONTH, endMonth);
//                endTime.add(Calendar.DAY_OF_MONTH, endDay);
//                WeekViewEvent event = new WeekViewEvent(i, getEventTitle(startTime), startTime, endTime);
//                event.setColor(getResources().getColor(R.color.event_color_01));
//                events.add(event);
//            }
//        } catch(Exception ex) {
//
//        }


        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.set(Calendar.YEAR, newYear);

        long value = 7;

        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 1);
        endTime.set(Calendar.MONTH, newMonth - 1);
        WeekViewEvent event = new WeekViewEvent(1, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_01));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 30);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 4);
        endTime.set(Calendar.MINUTE, 30);
        endTime.set(Calendar.MONTH, newMonth-1);
        event = new WeekViewEvent(10, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_02));
        events.add(event);




        return events;
    }


    private Integer getStartMonth(JSONObject object){
        try{
            String date = object.getString("startMonth");
            String[] dateInfo = date.split("/");
            String startMonth = dateInfo[1];

            if(startMonth.startsWith("0")){
                startMonth = startMonth.replaceAll("^0+", "");
            }
            return new Integer(startMonth);
        } catch(Exception ex) {
            return 0;
        }
    }

    private Integer getStartDay(JSONObject object){
        try{
            String date = object.getString("startDate");
            String[] dateInfo = date.split("/");
            String startDay = dateInfo[1];

            if(startDay.startsWith("0")){
                startDay = startDay.replaceAll("^0+", "");
            }
            return new Integer(startDay);
        } catch(Exception ex) {
            return 0;
        }
    }

    private Integer getStartYear(JSONObject object){
        try{
            String date = object.getString("startYear");
            String[] dateInfo = date.split("/");
            String startYear = dateInfo[1];

            if(startYear.startsWith("0")){
                startYear = startYear.replaceAll("^0+", "");
            }
            return new Integer(startYear);
        } catch(Exception ex) {
            return 0;
        }
    }

    private Integer getEndMonth(JSONObject object){
        try{
            String date = object.getString("endMonth");
            String[] dateInfo = date.split("/");
            String endMonth = dateInfo[1];

            if(endMonth.startsWith("0")){
                endMonth = endMonth.replaceAll("^0+", "");
            }
            return new Integer(endMonth);
        } catch(Exception ex) {
            return 0;
        }
    }

    private Integer getEndDay(JSONObject object){
        try{
            String date = object.getString("endDate");
            String[] dateInfo = date.split("/");
            String endDay = dateInfo[1];

            if(endDay.startsWith("0")){
                endDay = endDay.replaceAll("^0+", "");
            }
            return new Integer(endDay);
        } catch(Exception ex) {
            return 0;
        }
    }

    private Integer getEndYear(JSONObject object){
        try{
            String date = object.getString("endYear");
            String[] dateInfo = date.split("/");
            String endYear = dateInfo[1];

            if(endYear.startsWith("0")){
                endYear = endYear.replaceAll("^0+", "");
            }
            return new Integer(endYear);
        } catch(Exception ex) {
            return 0;
        }
    }
    private Integer getStartHour(JSONObject object){
        try{
            String date = object.getString("startHour");
            String[] dateInfo = date.split("/");
            String endDay = dateInfo[1];

            if(endDay.startsWith("0")){
                endDay = endDay.replaceAll("^0+", "");
            }
            return new Integer(endDay);
        } catch(Exception ex) {
            return 0;
        }
    }

    private Integer getEndHour(JSONObject object){
        try{
            String date = object.getString("endHour");
            String[] dateInfo = date.split("/");
            String endYear = dateInfo[1];

            if(endYear.startsWith("0")){
                endYear = endYear.replaceAll("^0+", "");
            }
            return new Integer(endYear);
        } catch(Exception ex) {
            return 0;
        }
    }

    private JSONArray getEvents(){
        boolean wasSuccess = true;
        JSONArray array = new JSONArray();
        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(getBookingsURL)
                    .get()
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                wasSuccess = true;
                array = new JSONArray(response.body().string());

            } else {
                wasSuccess = false;
            }
        } catch(Exception ex){

        }
        return array;
    }

}
