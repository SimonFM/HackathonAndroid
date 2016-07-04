package com.sourcey.hackathon;

import org.json.JSONObject;

public class DataObject {
    private String mText1;
    private String mText2;
    private JSONObject jsonObject;
    private String ImageURL;

    DataObject (String text1, String text2, JSONObject json){
        mText1 = text1;
        mText2 = text2;
        jsonObject = json;
    }

    public String getmText1() { return mText1; }

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public String getmText2() {
        return mText2;
    }

    public void setmText2(String mText2) {
        this.mText2 = mText2;
    }

    public JSONObject getJson() {
        return jsonObject;
    }
}
