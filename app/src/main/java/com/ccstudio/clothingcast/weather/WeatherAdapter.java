package com.ccstudio.clothingcast.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccstudio.clothingcast.R;

import java.util.ArrayList;

public class WeatherAdapter extends ArrayAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<W_TodayData> sample = new ArrayList<W_TodayData>();


    public WeatherAdapter(Context context, ArrayList<W_TodayData> data )
    {
        super(context,0,data);
        this.mContext = context;
        this.sample = data;
    }

    class ViewHolder{
        public  ImageView iv_icon;
        public TextView fcstTime;
        public TextView temperature;
    }


    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public W_TodayData getItem(int position) {
        return sample.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup viewGroup) {

        final ViewHolder viewHolder;
        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

            convertView = inflater.inflate(R.layout.w_listitem_today,viewGroup,false);
        }

        viewHolder = new ViewHolder();


        //layout  컴포넌트 지정
        viewHolder.iv_icon = (ImageView)convertView.findViewById(R.id.iv_timeSkystate);
        viewHolder.fcstTime = (TextView)convertView.findViewById(R.id.t_fcstTime);
        viewHolder.temperature = (TextView)convertView.findViewById(R.id.t_timeTemperature);

        final  W_TodayData todayData = (W_TodayData)sample.get(index);
        viewHolder.fcstTime.setText(todayData.getFcstTime());
        viewHolder.temperature.setText(todayData.getCurTemperature());
        //데이터 넣기
        viewHolder.iv_icon.setImageResource(todayData.getIconPath());
        return convertView;

    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(int icon, String FcstTime, String Temperature) {

        W_TodayData  item = new W_TodayData(icon, FcstTime, Temperature);

        item.setIcon(icon);
        item.setCurTemperature(Temperature);
        item.setFcstTime(FcstTime);

        sample.add(item);
    }


}
