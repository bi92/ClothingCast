package com.ccstudio.clothingcast.weather;

import android.graphics.drawable.Drawable;

public class W_WeeklyData {

    private int iconPath;
    private Drawable icon ;
    private weatherFunc.w_state skystate;

    private String minTemper;
    private String maxTemper;
    private  String fcstDate;
    private  String fcstWeekName;

    private String wholeDateText ;
    public  W_WeeklyData( int iconPath, String minTemper ,String maxTemper, String wholeDateText)
    {
        //this.skystate = skystate;
        this.iconPath = iconPath;
        this.minTemper = minTemper;
        this.maxTemper = maxTemper;
        this.fcstDate = fcstDate;
        this.fcstWeekName = fcstWeekName;
        this.wholeDateText = wholeDateText;
    }
    public int getIconPath()
    {
        return  iconPath;
    }

    public  void setIcon(int iconPath)
    {
        this.icon = icon;
    }
    public  void setminTemper(String minTemper)
    {
        this.minTemper = minTemper;
    }

    public  void setMaxTemper(String maxTemper)
    {
        this.maxTemper = maxTemper;
    }

    public  void setwholeDateText(String wholeDateText)
    {
        this.wholeDateText = wholeDateText;
    }

    public  void setfcstWeekName(String fcstWeekName)
    {
        this.fcstWeekName = fcstWeekName;
    }

    public String getWholeDateText()
    {
        return wholeDateText;
    }

    public  String getMaxTemper()
    {
        if(maxTemper ==null)
            return  "0";
        else
        return  maxTemper;
    }

    public  String getMinTemper()
    {
        if(minTemper ==null)
            return  "0";
        else
        return  minTemper;
    }

    public  String getFcstWeekName()
    {
        return  fcstWeekName;
    }

}
