package com.ccstudio.clothingcast.weather;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ccstudio.clothingcast.R;

public class ViewHolderToday extends WeatherItemView {

    public ImageView iv_icon;
    public TextView fcstTime;
    public TextView temperature;
    W_TodayData data;

    public ViewHolderToday(@NonNull View itemView) {
        super(itemView);

        //layout  컴포넌트 지정
        iv_icon = itemView.findViewById(R.id.iv_timeSkystate);
        fcstTime = itemView.findViewById(R.id.t_fcstTime);
        temperature = itemView.findViewById(R.id.t_timeTemperature);

    }


    public void  onBind( W_TodayData data)
    {
        this.data = (W_TodayData)data;

       // final  W_TodayData todayData = (W_TodayData)this.data.get(index);
        fcstTime.setText(this.data.getFcstTime());
        temperature.setText(this.data.getCurTemperature());
        //데이터 넣기
        iv_icon.setImageResource(this.data.getIconPath());
    }
}
