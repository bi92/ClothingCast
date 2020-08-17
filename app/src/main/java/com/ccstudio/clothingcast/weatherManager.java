package com.ccstudio.clothingcast;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;


public class weatherManager extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("날씨 매니저 시작");

    }

    public static void ParseData() throws IOException, ParseException {

        String apiUrl = "http://newsky2.kma.go.kr/service/MiddleFrcstInfoService/getMiddleLandWeather";
        // 홈페이지에서 받은 키
        String serviceKey = "QC%2FA0ug%2FNqWr8rfyf3C8xXUQs3j1CCi6M3BHeF4fZDrNaua6n8wNW7ItV2LWeqHiSw8sX6SckpiHTuI%2Bto0NTA%3D%3D";
        String regId = "11B00000";	//예보 구역 코드
        String tmFc = "202008110600";	//발표시간 입력
        String numOfRows = "1";	//한 페이지 결과 수
        String type = "json";	//타입 xml, json 등등 ..
        HashMap<String, HashMap<String,String>> rst = new HashMap<>();
        HashMap<String, String> sky = new HashMap<>();
        HashMap<String, String> tpt = new HashMap<>();

        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "="+serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("regId","UTF-8") + "=" + URLEncoder.encode(regId, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("tmFc","UTF-8") + "=" + URLEncoder.encode(tmFc, "UTF-8")); /* 조회하고싶은 날짜*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8")); /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */
        urlBuilder.append("&" + URLEncoder.encode("_type","UTF-8") + "=" + URLEncoder.encode(type, "UTF-8"));	/* 타입 */

        System.out.println("파싱 시작");

        /*
         * GET방식으로 전송해서 파라미터 받아오기
         */
        URL url = new URL(urlBuilder.toString());
        //어떻게 넘어가는지 확인하고 싶으면 아래 출력분 주석 해제
        System.out.println(url);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        String result= sb.toString();


        // Json parser를 만들어 만들어진 문자열 데이터를 객체화
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(result);
        // response 키를 가지고 데이터를 파싱
        JSONObject parse_response = (JSONObject) obj.get("response");
        // response 로 부터 body 찾기
        JSONObject parse_body = (JSONObject) parse_response.get("body");
        // body 로 부터 items 찾기
        JSONObject parse_items = (JSONObject) parse_body.get("items");
        // items로 부터 itemlist 를 받기
        JSONObject parse_item = (JSONObject) parse_items.get("item");

        Object[] keyObj = parse_item.keySet().toArray();
        String day = null;
        for(int i = 0 ; i < keyObj.length; i++) {
            String key = keyObj[i].toString();
            String str="";		//오전인지 오후인지 저장하는 변수 변수

            if(key.substring(0,2).equals("wf")) {	//하늘 상태
                try {	//key에서 숫자만 추출
                    day = key.substring(2,key.lastIndexOf("m")-1);
                    str = key.substring(key.lastIndexOf("m")-1);
                }catch(Exception e){
                    day = key.substring(2);
                }

                sky.put(day+str, parse_item.get(key).toString());
            }
            else if(key.substring(0,4).equals("rnSt")) {	//강수확률
                try {	//key에서 숫자만 추출
                    day = key.substring(4,key.lastIndexOf("m")-1);
                    str = key.substring(key.lastIndexOf("m")-1);
                }catch(Exception e){
                    day = key.substring(4);
                }

                tpt.put(day+str, parse_item.get(key).toString());
            }

        }

        Iterator<String> skyKeys = sky.keySet().iterator();
        Iterator<String> tptKeys = tpt.keySet().iterator();
        while( skyKeys.hasNext() ){
            String skyKey = skyKeys.next();
            String tptKey = tptKeys.next();
            System.out.println(skyKey+"\t하늘 : "+sky.get(skyKey) +", 강수확률 : "+tpt.get(tptKey)+"%");
        }

    }

}
