package com.ccstudio.clothingcast.weather;

public class requestInfo {

    public  enum content{
        weeklySky,
        weeklyTemper,
        recentSky
    }

    public String serviceKey;
    public String apiUrl;
    public String rows ;

    public String regionCode ;
    public String regionX ;
    public String regionY ;
    public String requestDate ;
    public String base_time = "0200";



    public content contentType;

    public  void SetUrlData( String requestUrl, String serviceKey,
                             String rows, String requestDate,
                             content contentType)
    {
        this.serviceKey = serviceKey;
        this.apiUrl = requestUrl;
        this.rows = rows;
        this.requestDate = requestDate;
        this.contentType =contentType;
    }

    public  void SetBaseTime( String time)
    {
        this.base_time = time;
    }
    public  void SetRegion( String regionCode)
    {
        this.regionCode = regionCode;
    }

    public  void SetRegion( String regionX, String regionY)
    {

        this.regionX = regionX;
        this.regionY = regionY;
    }



}
