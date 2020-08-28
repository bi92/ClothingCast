package com.ccstudio.clothingcast.weather;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ccstudio.clothingcast.R;

import java.util.ArrayList;

public class ViewHolderWeekly extends WeatherItemView {

    public ImageView iv_icon;
    public TextView t_fcstdate;
    public TextView t_minTemper;
    public TextView t_maxTemper;

    W_WeeklyData data;
    ArrayList<W_WeeklyData> sample = new ArrayList<W_WeeklyData>();

    public ViewHolderWeekly(@NonNull View itemView) {
        super(itemView);
        //layout  컴포넌트 지정
        iv_icon = itemView.findViewById(R.id.iv_weekIcon);
        t_fcstdate = itemView.findViewById(R.id.t_fcstWholedate);
        t_maxTemper = itemView.findViewById(R.id.t_maxTemper);
        t_minTemper = itemView.findViewById(R.id.t_minTemper);


    }

    public void  onBind( WeatherItem data)
    {
        this.data = (W_WeeklyData)data;
        t_fcstdate.setText( this.data.getWholeDateText());

        if( this.data.getMaxTemper()!=null)
        {
            t_maxTemper.setText( this.data.getMaxTemper());
        }
        else
        {
            t_maxTemper.setText(0);
        }

        if( this.data.getMinTemper()!=null) {
            t_minTemper.setText( this.data.getMinTemper());
        }
        else
        {
            t_minTemper.setText(0);
        }

        //데이터 넣기
        iv_icon.setImageResource( this.data.getIconPath());
    }
}
