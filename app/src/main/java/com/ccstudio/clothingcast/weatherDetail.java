package com.ccstudio.clothingcast;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ccstudio.clothingcast.weather.W_TodayData;
import com.ccstudio.clothingcast.weather.W_WeeklyData;
import com.ccstudio.clothingcast.weather.WeatherAdapter;
import com.ccstudio.clothingcast.weather.WeatherItem;
import com.ccstudio.clothingcast.weather.WeatherRecyclerAdapter;
import com.ccstudio.clothingcast.weather.weatherFunc;
import com.ccstudio.clothingcast.weather.weeklyAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link weatherDetail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class weatherDetail extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    WeatherAdapter w_adapter ;
    weeklyAdapter week_adapter ;
    HomeActivity activity;
    ArrayList<W_TodayData> todayDatas;
    ArrayList<W_WeeklyData >weeklyData;

    WeatherRecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity=(HomeActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        activity = null;
    }

    public weatherDetail() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment weatherDetail.
     */
    // TODO: Rename and change types and number of parameters
    public static weatherDetail newInstance(String param1, String param2) {
        weatherDetail fragment = new weatherDetail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_weather_detail, container, false);

        Button button = (Button) rootView.findViewById(R.id.button2);
        button.setOnClickListener( new View.OnClickListener(){
            public  void onClick( View v){
              activity.onFragmentChange(1);
            }
        });

        System.out.println("weatherDetail 화면 리스트 뷰 생성  ");

        initWeeklyViews(rootView);
        initWTodayViews(rootView);
//
//        View view = inflater.inflate(R.layout.fragment_weather_detail,null);
//        todayDatas = new ArrayList<>();
//        for (int key : HomeActivity.skystate_today.keySet()) {
//            if (HomeActivity.skystate_today.get(key) != null) {
//                int iconID = weatherFunc.GetIconPath(HomeActivity.skystate_today.get(key));
//                String inKey = Integer.toString(key);
//                String temperkey = HomeActivity.temperState_today.get(key);
//                todayDatas.add(new W_TodayData(iconID, inKey,temperkey));
//                System.out.println(key+"번째 아이템생성");
//            }
//        }
//
//        ListView  listView = (ListView) rootView.findViewById(R.id.lv_timeSkyState);
//        w_adapter = new WeatherAdapter( getContext(),todayDatas);
//
//
//        weeklyData = new ArrayList<>();
//        for (int key : HomeActivity.skystate.keySet())
//        {
//            if(HomeActivity.skystate.get(key)!= null)
//            {
//                int iconID = weatherFunc.GetIconPath(HomeActivity.skystate.get(key));
//                String inKey = Integer.toString(key);
//                String minTemper = HomeActivity.temperState.get(-1*key);
//                String maxTemper = HomeActivity.temperState.get(key);
//                int gap = Math.abs(key);
//                weeklyData.add(new W_WeeklyData( iconID,minTemper,maxTemper,GetDateAndWeekName(gap)));
//                System.out.println(key+"번째 아이템생성");
//
//            }
//        }
//
//        ListView  weeklyview = (ListView) rootView.findViewById(R.id.lv_weekview);
//        System.out.println("weatherDetail 화면 리스트 뷰 생성 완료  ");
//        week_adapter = new weeklyAdapter( getContext(),weeklyData);



//reclycler view 업데이트 --------------
        return rootView;
    }

    private void initWeeklyViews( View rootview){
        //recyclerView =(RecyclerView) rootview.findViewById(R.id.recyclerview);
        recyclerView =(RecyclerView) rootview.findViewById(R.id.rv_weekly);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerAdapter = new WeatherRecyclerAdapter(WeatherItem.WeatherCase.weekly);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        W_WeeklyData w_data ;
        for (int key : HomeActivity.skystate.keySet())
        {
            if(HomeActivity.skystate.get(key)!= null)
            {
                int iconID = weatherFunc.GetIconPath(HomeActivity.skystate.get(key));
                String inKey = Integer.toString(key);
                String minTemper = HomeActivity.temperState.get(-1*key);
                String maxTemper = HomeActivity.temperState.get(key);
                int gap = Math.abs(key);
                w_data = new W_WeeklyData( iconID,minTemper,maxTemper,GetDateAndWeekName(gap));
                recyclerAdapter.addItem(w_data);
                System.out.println(key+"번째 아이템생성"+minTemper+maxTemper);

            }
            else
            {
                System.out.println(key+"번째 아이템이 null 입니다");
            }
        }

        //recyclerAdapter.notifyDataSetChanged();
        recyclerAdapter.notifyItemInserted(0);
    }

    private void initWTodayViews(View rootview)
    {
        //recyclerView =(RecyclerView) rootview.findViewById(R.id.recyclerview);
        recyclerView =(RecyclerView) rootview.findViewById(R.id.rv_today);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerAdapter = new WeatherRecyclerAdapter(WeatherItem.WeatherCase.today);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);

        W_TodayData t_data ;
        System.out.println("데이터  생성"+"총 데이터 갯수 "+HomeActivity.skystate_today.size());
        todayDatas = new ArrayList<>();
        for (int key : HomeActivity.skystate_today.keySet()) {
            if (HomeActivity.skystate_today.get(key) != null) {
                int iconID = weatherFunc.GetIconPath(HomeActivity.skystate_today.get(key));
                String inKey = Integer.toString(key);
                String temperkey = HomeActivity.temperState_today.get(key);
               // todayDatas.add(new W_TodayData(iconID, inKey,temperkey));
                t_data = new  W_TodayData(iconID, inKey,temperkey);
                System.out.println(key+"번째 아이템생성");
                recyclerAdapter.addItem(t_data);
            }
            else
            {
                System.out.println(key+"번째 아이템이 null 입니다");
            }
        }


        //recyclerAdapter.notifyDataSetChanged();
        recyclerAdapter.notifyItemInserted(0);

    }

    public void  addItem(int iconpath, String fcstTime, String temperature)
    {
        w_adapter.addItem(iconpath,fcstTime,temperature);
    }

    static String []weekName = {" ","Sun .","Mon .","Tue .","Wed .","Thur .","Fri .","Sat ."};

    public static String GetDateAndWeekName( int gap)
    {
        //현재 시간을 받음
        Calendar calender = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
        String selectedweek =null;
        calender.add(Calendar.DATE,gap);
        int date = calender.get(Calendar.DAY_OF_MONTH);
        int dayofweek = calender.get(Calendar.DAY_OF_WEEK);
        selectedweek = date +", "+weekName[dayofweek];
        System.out.println("*****"+gap+" 일 후 날짜 -> "+date +"요일"+ selectedweek);

        return selectedweek;
    }


}