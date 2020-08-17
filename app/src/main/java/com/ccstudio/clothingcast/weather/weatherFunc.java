package com.ccstudio.clothingcast.weather;
import com.ccstudio.clothingcast.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class weatherFunc {
    //하늘 상태
    //- 맑음
    //- 구름많음, 구름많고 비, 구름많고 눈, 구름많고 비/눈, 구름많고 눈/비
    //- 흐림, 흐리고 비 흐리고 눈, 흐리고 비/눈, 흐리고 눈/비
    public  enum w_state{
        sunny,
        cloudy,
        cloudy_rain,
        cloudy_snow,
        cloudy_rainOrsnow,
        fog,
        fog_rain,
        fog_snow,
        fog_rainOrsnow,
    }

    public static requestInfo reqData_sky = new requestInfo();
    public static requestInfo reqData_temper = new requestInfo();
    public static requestInfo reqData_recent = new requestInfo();
    public static String curdate;

    //- Base_time : 0200, 0500, 0800, 1100, 1400, 1700, 2000, 2300 (1일 8회)
    public  static  void DefineDefault()
    {
         curdate =  new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        System.out.println("weatherfuction 안의 date"+curdate);
        //중기 하늘 3~10일 하늘상태
        reqData_sky.SetUrlData(
                "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst",
                "QC%2FA0ug%2FNqWr8rfyf3C8xXUQs3j1CCi6M3BHeF4fZDrNaua6n8wNW7ItV2LWeqHiSw8sX6SckpiHTuI%2Bto0NTA%3D%3D",
                "1",
                curdate+"0600",
                requestInfo.content.weeklySky

        );

        reqData_sky.SetRegion("11B00000");

        //중기 기온 상태 3~10일 최고 최저 기온
        reqData_temper.SetUrlData(
                "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa",
                "QC%2FA0ug%2FNqWr8rfyf3C8xXUQs3j1CCi6M3BHeF4fZDrNaua6n8wNW7ItV2LWeqHiSw8sX6SckpiHTuI%2Bto0NTA%3D%3D",
                "1",
                curdate+"0600",
                requestInfo.content.weeklyTemper

        );
        reqData_temper.SetRegion("11B10101");

        reqData_recent.SetUrlData(
                "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst",
                "QC%2FA0ug%2FNqWr8rfyf3C8xXUQs3j1CCi6M3BHeF4fZDrNaua6n8wNW7ItV2LWeqHiSw8sX6SckpiHTuI%2Bto0NTA%3D%3D",
                "300",
                curdate,
                requestInfo.content.recentSky

        );

        //서울 좌표
        reqData_recent.SetRegion("60","127");

    }

    public enum recentCategory
    {
        SKY,
        TMN,
        TMX
    }
    class recentData
    {
        public recentCategory myCategory; //얘보 정보값

        public String  fcstDate; //예보 날짜
        public  String  fcstTime; //예보 시간
        public  String fcstValue; //예보 결과

        public void   SetRecentData(JSONObject j_object) throws JSONException {
            switch ( j_object.get("category").toString())
            {
                //하늘상태
                case "SKY":
                    myCategory = recentCategory.SKY;
                    break;

                //최소 온도
                case "TMN":
                    myCategory = recentCategory.TMN;
                 break;

                //최대 온도
                case "TMX":
                    myCategory = recentCategory.TMX;
                    break;
            }
            fcstTime = j_object.get("fcstTime").toString();

        }
    }


    public static  boolean IsDataPossible( String resultcode)
    {
        if( resultcode.equals("00"))
        return  true;
        else
            return  false;
    }

    public static Integer GetDayBetweenDates( String date1 ,String date2)
    {
        try{

            SimpleDateFormat format = new SimpleDateFormat("MMdd");

            // date1, date2 두 날짜를 parse()를 통해 Date형으로 변환.
            Date FirstDate = format.parse(date1);
            Date SecondDate = format.parse(date2);

            // Date로 변환된 두 날짜를 계산한 뒤 그 리턴값으로 long type 변수를 초기화 하고 있다.
            // 연산결과 -950400000. long type 으로 return 된다.
            long calDate = FirstDate.getTime() - SecondDate.getTime();

            // Date.getTime() 은 해당날짜를 기준으로1970년 00:00:00 부터 몇 초가 흘렀는지를 반환해준다.
            // 이제 24*60*60*1000(각 시간값에 따른 차이점) 을 나눠주면 일수가 나온다.
            long calDateDays = calDate / ( 24*60*60*1000);

            calDateDays = Math.abs(calDateDays);

            System.out.println(date1+ "과 " +date2 +"날짜 차이: "+ (int)calDateDays);
            return  (int) calDateDays;
        }
        catch(ParseException e)
        {
            // 예외 처리
            return  null;
        }

    }



    //- 하늘상태(SKY) 코드 : 맑음(1), 구름많음(3), 흐림(4)
    //- 강수형태(PTY) 코드 : 없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4), 빗방울(5),
    // 빗방울/눈날림(6), 눈날림(7)
   public static w_state CheckSkyState(  String str)
    {
        weatherFunc.w_state result = null;
        switch (str)
        {
            case"맑음":
            case "1":
                result = weatherFunc.w_state.sunny;
                break;

            case"구름많음":
            case "3":
                result = w_state.cloudy;
                break;

            case"구름많고 비":
                result = w_state.cloudy_rain;
                break;
            case"구름많고 눈 ":
                result = w_state.cloudy_snow;
                break;
            case"구름많고 비/눈":
            case"구름많고 눈/비":
                result = w_state.cloudy_rainOrsnow;
                break;

            case"흐림":
            case "4":
                result = w_state.fog;
                break;

            case"흐리고 비":
                result = w_state.fog_rain;
                break;
            case"흐리고 눈":
                result = w_state.fog_snow;
                break;
            case"흐리고 비/눈":
            case"흐리고 눈/비":
                result = w_state.fog_rainOrsnow;
                break;
        }
        return result;
    }


    public static w_state CheckSkyState(String SKY , String PTY)
    {
        weatherFunc.w_state result = null;

        String combinedCode = null;
        if(SKY.equals("1"))
        {
            result = weatherFunc.w_state.sunny;
        }
        else if(SKY.equals("3"))
        {
            switch (PTY)
            {
                case"0":
                    result = w_state.cloudy;
                    break;

                case"3": //구름많고 눈
                    result = w_state.cloudy_snow;
                    break;

                case"4": //구름많고 비
                case"5": //구름많고 비
                    result = w_state.cloudy_rain;
                    break;

                case"2": //구름많고 비/눈
                case"6": //구름많고 비/눈
                    result = w_state.cloudy_rainOrsnow;
                    break;

            }
        }
        else if(SKY.equals("4"))
        {
            switch (PTY)
            {
                case"0"://흐림
                    result = w_state.fog;
                    break;

                case"3": //흐리고 눈
                    result = w_state.fog_snow;
                    break;

                case"4": //흐리고 비
                case"5": //흐리고 비
                    result = w_state.fog_rain;
                    break;

                case"2": //흐리고 비/눈
                case"6": //흐리고 빗방울/눈방울
                    result = w_state.fog_rainOrsnow;
                    break;

            }
        }

        return result;
    }

    public static  int GetIconPath( w_state curstate)
    {
        int path = 0;
        switch (curstate)
        {
            case sunny:
                path = R.drawable.w_sunny;
                break;
            case cloudy:
                path = R.drawable.w_cloudy;
                break;

            case cloudy_rain:
                path = R.drawable.w_cloudy_rain;
                break;

            case cloudy_snow:
                path = R.drawable.w_cloudy_snow;
                break;

            case cloudy_rainOrsnow:
                path = R.drawable.w_cloudy_rainsnow;
                break;

            case fog:
                path = R.drawable.w_fog;
                break;
            case fog_rain:
                path = R.drawable.w_fog_rain;
                break;
            case fog_snow:
                path = R.drawable.w_fog_rainsnow;
                break;
            case fog_rainOrsnow:
                path = R.drawable.w_fog;
                break;
        }

        return path;
    }
}
