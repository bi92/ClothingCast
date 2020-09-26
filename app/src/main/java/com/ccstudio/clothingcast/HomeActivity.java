package com.ccstudio.clothingcast;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ccstudio.clothingcast.View.DiaryActivity;
import com.ccstudio.clothingcast.View.MyClosetActivity;
import com.ccstudio.clothingcast.weather.W_TodayData;
import com.ccstudio.clothingcast.weather.requestInfo;
import com.ccstudio.clothingcast.weather.weatherFunc;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TreeMap;

public class HomeActivity extends AppCompatActivity {
    private static Object Public;
    private static Object JSONArray;
    weatherDetail weatherdetail ;
    ImageView iv_curweather;
    RecommendFragment recommendFragment;
    FragmentTransaction fragmentTransaction;

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //set bottom navigation bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.page_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Intent intent;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_diary:
                        intent = new Intent(getBaseContext(), DiaryActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;
//                    case R.id.page_home:
//                        intent = new Intent(getBaseContext(), HomeActivity.class);
//                        startActivity(intent);
//                        overridePendingTransition(0, 0);
//                        break;
                    case R.id.page_closet:
                        intent = new Intent(getBaseContext(), MyClosetActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;
                }
                return true;
            }
        });

        //날씨 데이터 받아오기

        new Thread()
        {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public  void run()
            {
                try {
                    ParseWeeklyData();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        weatherdetail = new weatherDetail();
        recommendFragment = new RecommendFragment();
        this.InitTodaySkyWidget();


        //adding recommendFragment to area_weatherDetail
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.area_weatherDetail,recommendFragment).commitAllowingStateLoss();

        iv_curweather = findViewById(R.id.curWeather);
        iv_curweather.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View view) {

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.area_weatherDetail, weatherdetail).commitAllowingStateLoss();

            //fragmentTransaction.commit();


            }
        });

    }



    public  void onFragmentChange(int index)
    {
        if(index == 1)
        {
            //FragmentManager manager = getSupportFragmentManager();
            //FragmentTransaction transaction = manager.beginTransaction();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.area_weatherDetail,recommendFragment).commitAllowingStateLoss();
            //transaction.commit();
        }
    }

    //타임아웃 시간 설정
    private static int conn_readyTimeout = 10000;
    private static int conn_timeout = 15000;






    //오전 - , 오후 +로 표시 - 하늘 상태
    public  static TreeMap<Integer, weatherFunc.w_state> skystate = new TreeMap<Integer, weatherFunc.w_state>();

    //최소  - , 최대 +로 표시 - 기온상태
    public  static TreeMap<Integer, String> temperState = new TreeMap<Integer, String>();

    //오전 - , 오후 +로 표시 - 하늘 상태 최근 3일
    public  static TreeMap<Integer, weatherFunc.w_state> skystate_today = new TreeMap<Integer, weatherFunc.w_state>();

    //오전 - , 오후 +로 표시 - 기온상태 최근 3일
    public  static TreeMap<Integer, String> temperState_today = new TreeMap<Integer, String>();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void ParseWeeklyData() throws IOException, ParseException {


        //3~10일 하늘 기상 정보 요청 (중기예보 하늘 상태)
        String skyJson =  GetJsonData( weatherFunc.reqData_sky);

        //3~10일 온도 정보 요청 (중기예보 온도 상태)
        String temperatureJson =  GetJsonData(weatherFunc.reqData_temper);

        //3~10일 온도 정보 요청 (중기예보 온도 상태)
        String recentSkyJson =  GetJsonData(weatherFunc.reqData_recent);

        if(skyJson!= null)
            GetParseByInfo(requestInfo.content.weeklySky, skyJson);
        else   System.out.println("skyJson 받아오기 실패");

        if(temperatureJson!= null)
            GetParseByInfo(requestInfo.content.weeklyTemper, temperatureJson);
        else   System.out.println("temperatureJson 받아오기 실패");

        if(recentSkyJson!=null)
            GetParseByInfo(requestInfo.content.recentSky,recentSkyJson);

//다음 base time확인
        while (temperState.get(2) == null)
        {
            int curbaseTime = Integer.parseInt(weatherFunc.reqData_recent.base_time);
            if(curbaseTime < 2300 )
            {
                curbaseTime += 300;
                weatherFunc.reqData_recent.base_time = (curbaseTime<1000 ) ?
                        "0"+Integer.toString(curbaseTime): Integer.toString(curbaseTime);
                //json  다시 리퀘스트
                recentSkyJson =  GetJsonData(weatherFunc.reqData_recent);
                if(recentSkyJson!=null)
                    GetParseByInfo(requestInfo.content.recentSky,recentSkyJson);
            }
            else
            {
                break;
            }

        }
//        //check keymap
        for (int i = 0; i<11 ; i++)
        {
            System.out.println("오전 날씨"+ i+ "일 후 ::"+ skystate.get(-i));
            System.out.println("오후 날씨"+ i+ "일 후 ::"+ skystate.get(i));
        }

        for (int i = 0; i<11 ; i++)
        {
            System.out.println(i+" 일 후  :: 최저 온도"+ temperState.get(-i));
            System.out.println(i +"일 후  :: 최고  온도"+ temperState.get(i));
        }


        //오늘 하루 하늘 상태 3시간 씩
        for( int key : skystate_today.keySet() ){
            System.out.println(key +"시 하늘 상태 "+ skystate_today.get(key));
        }
        for( int key : temperState_today.keySet() ){
            if(key<0)
            System.out.println("오전"+key +"시 기온 상태 "+ temperState_today.get(key));
            else
                System.out.println("오후"+key +"시 기온 상태 "+ temperState_today.get(key));

        }
        weatherDetail.GetDateAndWeekName(3);

    }

   public static String GetJsonData(requestInfo reqInfo) throws UnsupportedEncodingException, SocketTimeoutException, MalformedURLException {
//        String apiUrl = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst";
//        // 홈페이지에서 받은 키
//        String serviceKey = "QC%2FA0ug%2FNqWr8rfyf3C8xXUQs3j1CCi6M3BHeF4fZDrNaua6n8wNW7ItV2LWeqHiSw8sX6SckpiHTuI%2Bto0NTA%3D%3D";
//        String regionCode = "11B00000";	//예보 구역 코드

//        //현재 날짜
//        Date date = new Date();
//        String curdate =  new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
//        System.out.println(" curdate :::"+curdate);
//        String tmFc = curdate +"0600";	//발표시간 입력
//        String numOfRows =  Rows;	//한 페이지 결과 수
        weatherFunc.GetDayBetweenDates( "0830", "0902");
        //초기값 설정
        weatherFunc.DefineDefault();
        String type = "json";	//타입 xml, json ..

        HashMap<String, HashMap<String,String>> rst = new HashMap<>();
        HashMap<String, String> sky = new HashMap<>();
        HashMap<String, String> tpt = new HashMap<>();

        System.out.println("request value? "+reqInfo.apiUrl);
        StringBuilder urlBuilder = new StringBuilder(reqInfo.apiUrl);
        switch ( reqInfo.contentType)
        {
            case  weeklySky:
            case  weeklyTemper:
                urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "="+reqInfo.serviceKey);
                urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(reqInfo.rows, "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("regId","UTF-8") + "=" + URLEncoder.encode(reqInfo.regionCode, "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("tmFc","UTF-8") + "=" + URLEncoder.encode(reqInfo.requestDate, "UTF-8")); /* 조회하고싶은 날짜*/
                /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */
                urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode(type, "UTF-8"));	/* 타입 */

                break;

            case recentSky:
                urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "="+reqInfo.serviceKey);
                urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(reqInfo.rows, "UTF-8"));

                urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(reqInfo.requestDate, "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(reqInfo.base_time, "UTF-8"));
                urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(reqInfo.regionX, "UTF-8")); /* 조회하고싶은 날짜*/
                urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(reqInfo.regionY, "UTF-8")); /* 조회하고싶은 날짜*/

                urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode(type, "UTF-8"));	/* 타입 */

                break;

        }

        System.out.println("파싱 시작");

        /*
         * GET방식으로 전송해서 파라미터 받아오기
         */

        URL url = new URL(urlBuilder.toString());
        //어떻게 넘어가는지 확인하고 싶으면 아래 출력분 주석 해제
        System.out.println(url);
        String result = null;
        try{
            //웹 url 연결
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //timeout 시간설정
            conn.setConnectTimeout(conn_timeout);
            conn.setReadTimeout(conn_readyTimeout);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("GET");// 요청 방식 설정
            //conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();
            int resCode = conn.getResponseCode();
            BufferedReader rd ;
            StringBuilder sb = new StringBuilder();

            if(resCode >= 200 && resCode <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }


            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            //접속 해지
            conn.disconnect();
            result = sb.toString();
            System.out.println("json"+ result);


        }
        catch (SocketTimeoutException e)
        {
            throw new SocketTimeoutException();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return  result;
    }

    //주간 날씨 정보 중 하늘상태
    static <T>TreeMap GetWeeklyWeather( TreeMap<Integer,T> resultmap, JSONObject parse_list, requestInfo.content type) throws ParseException {


        System.out.println("뽑은 값은 ??"+parse_list.get("wf3Am"));

        //상태 코드드 : ex) wf5Pm
        //날씨예보 코드
        String code_weather = "wf";
        String code_temperature = "ta";
        String code_selected = null;

        String optionA = null;
        String optionB = null;
        switch (type)
        {
            case weeklySky:
                optionA = "Am";
                optionB = "Pm";
                code_selected =code_weather;
                break;

            case weeklyTemper:
                optionA = "Min";
                optionB = "Max";
                code_selected =code_temperature;
                break;
            case recentSky:
                break;
        }

        //날씨 정보 리스트 <일자 / 하늘상태>
        //중기 날씨예보 기간 3일후 ~10일 후까지
        for ( int i = 3; i<=10; i++)
        {
            String key_A = null;
            String key_B = null;
            if(type == requestInfo.content.weeklySky)
            {
                //오후 기준
                key_A = (i<8) ? code_selected + (i)+optionA : code_weather+(i) ;
                key_B = (i<8) ? code_selected + (i)+optionB : code_weather+(i) ;
            }
            else
            {
                //오후 기준
                key_A = code_selected + optionA + i;
                key_B = code_selected + optionB + i ;
            }


            String value = null;

            //오전 날씨
            if(parse_list.get(key_A)!=null)
            {
                //value =(String) parse_list.get(key_A);
                value =String.valueOf(parse_list.get(key_A));

                if(type == requestInfo.content.weeklySky)
                    resultmap.put(-i, (T) weatherFunc.CheckSkyState(value));
                else
                    resultmap.put(-i, (T) (value));
            }

            //오후 날씨
            if(parse_list.get(key_B)!=null)
            {
                //value =(String) parse_list.get(key_B);
                value =String.valueOf(parse_list.get(key_B));
                //map에 상태 넣기
                if(type == requestInfo.content.weeklySky)
                resultmap.put(i, (T) weatherFunc.CheckSkyState(value));
                else
                    resultmap.put(i, (T) (value));
            }
        }

        return  (TreeMap) resultmap;
    }


   //인포 별로 파싱하기
    static Boolean GetParseByInfo( requestInfo.content type, String source ) throws ParseException {

        //동일 부분
        System.out.println("+++++ json 문자열 객체화 시작+++++++");
        // Json parser를 만들어 만들어진 문자열 데이터를 객체화
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(source);
        // response 키를 가지고 데이터를 파싱
        JSONObject parse_response = (JSONObject) obj.get("response");
        //header
        JSONObject header = (JSONObject) parse_response.get("header");

        //정상적 json 확인
        String resultcode = (String)header.get("resultCode");

        if(! weatherFunc.IsDataPossible(resultcode))
            return false;

        // response 로 부터 body 찾기
        JSONObject parse_body = (JSONObject) parse_response.get("body");
        // body 로 부터 items 찾기
        JSONObject parse_items = (JSONObject) parse_body.get("items");
        JSONArray parse_item = (JSONArray) parse_items.get("item");
        JSONObject parse_list;

        //****************************************
        //date 차이 보기

        switch (type)
        {
            case weeklySky:
                //최종 정보값이 들어있는 오브젝트 (날씨 )
                 parse_list = (JSONObject)parse_item.get(0);

                //하늘 상태 파싱(3~ 10일 예보)
                GetWeeklyWeather( skystate, parse_list,type);

                break;

            case weeklyTemper:

                //최종 정보값이 들어있는 오브젝트 (날씨 )
                 parse_list = (JSONObject)parse_item.get(0);

                //하늘 상태 파싱(3~ 10일 예보)
                GetWeeklyWeather( temperState, parse_list,type);

                break;

            case recentSky:

                ParseRecentData( parse_item);
                System.out.println("동네 예보"+parse_item.get(3));
                break;
        }
        return  true;
    }

    //0-sky상태 1-  강수
     String[] pair_SkyPty = new String[2];
    static  TreeMap <Integer ,String[]> recent_skyPty  = new TreeMap <Integer, String[]>();

    //3시간 당 하늘 상태
    static TreeMap<Integer ,String[]> todayByTime_skyPty  = new TreeMap <Integer, String[]>();
    //동네예보 데이터 받기

    static void ParseRecentData(  JSONArray parse_item)
    {
        String fulldate =  new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        String today =  fulldate.substring(4,8);
        int size = parse_item.size();

        for(int i =0; i<size; i++)
        {
            JSONObject data = (JSONObject) parse_item.get(i);
            //카테고리
            //날짜
            String category = data.get("category").toString();


            if( category.equals("SKY")  || category.equals("TMN") || category.equals("PTY")
                    || category.equals("TMX") || category.equals("T3H"))
            {

                //예보 날짜
                String fcstDate = data.get("fcstDate").toString().substring(4,8);
                int fcstTime = Integer.parseInt((String)data.get("fcstTime"));
                int keyTime =  fcstTime/100;
                System.out.println(" 예보 날짜??"+data.get("fcstDate").toString().substring(4,8)+ "오늘 날짜?"+today);

                //0 현재 1- 하루뒤 2 이틀뒤
                int gap = weatherFunc.GetDayBetweenDates(fcstDate, today);
                System.out.println("category = ?"+ category+"gap의 상태는?"+ gap);

                //비교를 위한 sy - pty 세트
                String[] temp_skyPty ;
                temp_skyPty = (recent_skyPty.get(gap) == null) ? new String[2]: recent_skyPty.get(gap);

                //3시간 당 하늘상태
                String[] todayBytime_skyPty ;
                todayBytime_skyPty = (todayByTime_skyPty.get(keyTime) == null) ? new String[2]: todayByTime_skyPty.get(keyTime);
                switch (category)
                {
                    //하늘상태
                    case "SKY":
                        if( temp_skyPty[0] == null)
                        {
                            //임시 저장 - > 두개 다 채워졌을 때 하늘 상태 설정
                             temp_skyPty[0] = data.get("fcstValue").toString();
                            //skystate.put(gap, weatherFunc.CheckSkyState(data.get("fcstValue").toString()));

                            recent_skyPty.put(gap,temp_skyPty);
                        }

//
//                        //시간별 오늘 날씨 갖기 6~21시까지
                        if(fcstDate.equals(today) )
                        {
                            if( todayBytime_skyPty[0]== null)
                            {
                                todayBytime_skyPty[0]= data.get("fcstValue").toString();
                                System.out.println( keyTime+"시간별 하늘 상태" +todayBytime_skyPty[0].toString());
                                todayByTime_skyPty.put(keyTime, todayBytime_skyPty);
                                String[] tmp = todayByTime_skyPty.get(keyTime);
                                System.out.println( keyTime+"넣은 결과 " +tmp[0]+ " :::" + tmp[1]);
                            }
                        }
                        break;
                        //강수 형태
                    case "PTY":
                        if( temp_skyPty[1]== null)
                        {
                            //임시 저장 - > 두개 다 채워졌을 때 하늘 상태 설정
                            temp_skyPty[1] = data.get("fcstValue").toString();
                            recent_skyPty.put(gap,temp_skyPty);
                            System.out.println("강수 정보"+data.get("fcstValue").toString());
                        }

                        //시간별 오늘 날씨 갖기 6~21시까지
                        if(fcstDate.equals(today) )
                        {
                            if(todayBytime_skyPty[1]== null)
                            {
                                todayBytime_skyPty[1] = data.get("fcstValue").toString();
                                todayByTime_skyPty.put(keyTime, todayBytime_skyPty);
                            }

                        }

                        //3시간 기온
                    case "T3H":
                        if(fcstDate.equals(today))
                        temperState_today.put(keyTime,data.get("fcstValue").toString());

                        break;

                    //최소 온도
                    case "TMN":
                    //최대 온도
                    case "TMX":
                        int dir =0;
                        dir = (category.equals("TMN") ) ?  -1 : 1;
                        if(temperState.get(gap*dir) == null)
                        {
                            temperState.put( gap * dir , data.get("fcstValue").toString());
                            //System.out.println(category+"온도 찾기 성공"+ gap * dir);
                        }

                        break;
                }

            }
        }

        //skyPty -> 설정하기 일주일 하늘
        for( int key : recent_skyPty.keySet() ){
            if(skystate.get(key) == null)
            {
                String[] tempSet = recent_skyPty.get(key);
                skystate.put(key,weatherFunc.CheckSkyState(tempSet[0],tempSet[1]));
            }
        }

        //오늘 하루 하늘 상태 3시간 씩
        for( int key : todayByTime_skyPty.keySet() ){
            if(skystate_today.get(key) == null)
            {
                String[] tempSet = todayByTime_skyPty.get(key);
                skystate_today.put(key,weatherFunc.CheckSkyState(tempSet[0],tempSet[1]));
            }
        }

    }

    int[] todayTimeZone =
            {6,9,12,15,18,21};
    //3시간 뒤 기상정보 받기 6~ 21
    void GetTodayThreeTemper()
    {
        String curHour =  new SimpleDateFormat("HH ").toString();
        int target =  Integer.parseInt(curHour);
        int min = Integer.MAX_VALUE;
        int near =0;
        for (int i=0; i<todayTimeZone.length; i++)
        {
            int a = Math.abs(todayTimeZone[i] -target);
            if(min>a)
            {
                min = a;
                near = todayTimeZone[i];
            }
        }

    }

    static ArrayList<W_TodayData> todayDataList;
    //weatherDetail 위젯 설정
    public static void InitTodaySkyWidget() {

        //오늘 하루 3시간 당 예보
       todayDataList = new ArrayList<W_TodayData>();
        for (int key : skystate_today.keySet()) {
            if (skystate_today.get(key) != null) {
                todayDataList.add(new W_TodayData(weatherFunc.GetIconPath(skystate_today.get(key)) , Integer.toString(key), temperState_today.get(key)));

            }
        }
    }

//         //check keymap
//        for (int i = 0; i<11 ; i++)
//        {
//            System.out.println("오전 날씨"+ i+ "일 후 ::"+ skystate.get(-i));
//            System.out.println("오후 날씨"+ i+ "일 후 ::"+ skystate.get(i));
//        }
//
//        for (int i = 0; i<11 ; i++)
//        {
//            System.out.println(i+" 일 후  :: 최저 온도"+ temperState.get(-i));
//            System.out.println(i +"일 후  :: 최고  온도"+ temperState.get(i));
//        }
//
//
//        //오늘 하루 하늘 상태 3시간 씩
//        for( int key : todayByTime_skyPty.keySet() ){
//            System.out.println(key +"시 하늘 상태 "+ skystate_today.get(key));
//        }
//        for( int key : temperState_today.keySet() ){
//            if(key<0)
//                System.out.println("오전"+key +"시 기온 상태 "+ temperState_today.get(key));
//            else
//                System.out.println("오후"+key +"시 기온 상태 "+ temperState_today.get(key));
//
//        }




}