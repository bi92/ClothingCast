package com.ccstudio.clothingcast.weather;

import android.graphics.drawable.Drawable;

public class W_TodayData {

    private int iconPath;
    private  Drawable icon ;
    private weatherFunc.w_state skystate;
    private String curTemperature;
    private  String fcstTime;

    public  W_TodayData( int iconPath, String fcstTime , String curTemperature)
    {
        //this.skystate = skystate;
        this.iconPath = iconPath;
        this.curTemperature = curTemperature;
        fcstTime = (Integer.parseInt(fcstTime)<12)? "AM"+fcstTime : "PM"+ fcstTime;
        this.fcstTime = fcstTime;
    }
    public int getIconPath()
    {
        return  iconPath;
    }

    public  void setIcon(int iconPath)
    {
        this.icon = icon;
    }
    public  void setCurTemperature(String curTemperature)
    {
        this.curTemperature = curTemperature;
    }

    public  void setFcstTime(String FcstTime)
    {
        this.fcstTime = FcstTime;
    }

    public String getCurTemperature()
    {
        return curTemperature;
    }
    public  String getFcstTime()
    {
        return  fcstTime;
    }
}

