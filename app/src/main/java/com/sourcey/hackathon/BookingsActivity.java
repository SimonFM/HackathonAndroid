package com.sourcey.hackathon;

import com.alamkanak.weekview.WeekViewEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookingsActivity extends BaseActivity {

    ArrayList<WeekViewEvent> mEvents = new ArrayList<>();

    @Override
    public void onStart(){
        super.onStart();
        if(array.size() > 0){
            mEvents = makeList(array.get(0));
        }
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        ArrayList<WeekViewEvent> bookedEvents;

        if (mEvents.size() > 0) {
            bookedEvents = mEvents;
        } else {
            while(array.size() < 0);
            bookedEvents = makeList(array.get(0));
            mEvents = bookedEvents;
        }
        return bookedEvents;
    }

    private ArrayList<WeekViewEvent> makeList(JSONArray jsonArray){
        ArrayList<WeekViewEvent> events = new ArrayList<>();
        try{
            for (int i = 0; i < jsonArray.length(); i++){
                WeekViewEvent event = makeEvent(jsonArray.getJSONObject(i));
                if(event != null){
                    events.add(event);
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return events;
    }


    private WeekViewEvent makeEvent(JSONObject object){
        WeekViewEvent event = null;
        try{
            Calendar startTime = Calendar.getInstance();
            Calendar endTime = (Calendar) startTime.clone();

            Integer startHour = Integer.parseInt(object.get("startHour").toString());
            Integer endHour = Integer.parseInt(object.get("endHour").toString());
            String[] var = object.get("startDate").toString().split("/");
            if(var.length <= 3){
                Integer month = Integer.parseInt(var[1].replaceAll(" ", ""));
                Integer year = Integer.parseInt(var[2].replaceAll(" ", ""));
                Integer day = Integer.parseInt(var[0].replaceAll(" ", ""));

                startTime.set(year, month, day, startHour, 1);
                endTime.set(year, month, day, endHour, 59);
                event = new WeekViewEvent(1, getEventTitle(startTime), startTime, endTime);
                event.setColor(getResources().getColor(R.color.event_color_02));
                if(object.get("ref") != null){
                    event.setName("BOOKED");
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return event;
    }

}
