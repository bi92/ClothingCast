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

public class weeklyAdapter extends ArrayAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<W_WeeklyData> sample = new ArrayList<W_WeeklyData>();


    public weeklyAdapter(Context context, ArrayList<W_WeeklyData> data )
    {
        super(context,0,data);
        this.mContext = context;
        this.sample = data;
    }

    class ViewHolder{
        public  ImageView iv_icon;
        public TextView t_fcstdate;
        public TextView t_minTemper;
        public TextView t_maxTemper;
    }


    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public W_WeeklyData getItem(int position) {
        return sample.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup viewGroup) {

        final weeklyAdapter.ViewHolder viewHolder;
        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

            convertView = inflater.inflate(R.layout.w_listitem_week,viewGroup,false);
        }

        viewHolder = new ViewHolder();


        //layout  컴포넌트 지정
        viewHolder.iv_icon = (ImageView)convertView.findViewById(R.id.iv_weekIcon);
        viewHolder.t_fcstdate = (TextView)convertView.findViewById(R.id.t_fcstWholedate);
        viewHolder.t_maxTemper = (TextView)convertView.findViewById(R.id.t_maxTemper);
        viewHolder.t_minTemper = (TextView)convertView.findViewById(R.id.t_minTemper);

        final  W_WeeklyData todayData = (W_WeeklyData)sample.get(index);
        viewHolder.t_fcstdate.setText(todayData.getWholeDateText());

        if(todayData.getMaxTemper()!=null)
        {
            viewHolder.t_maxTemper.setText(todayData.getMaxTemper());
        }
        else
        {
            viewHolder.t_maxTemper.setText(0);
        }

        if(todayData.getMinTemper()!=null) {
            viewHolder.t_minTemper.setText(todayData.getMinTemper());
        }
        else
        {
            viewHolder.t_minTemper.setText(0);
        }

        //데이터 넣기
        viewHolder.iv_icon.setImageResource(todayData.getIconPath());
        return convertView;

    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(int icon, String wholeWeekName, String minTemper, String maxTemper) {

        W_WeeklyData  item = new W_WeeklyData(icon, minTemper, maxTemper,wholeWeekName);

        item.setIcon(icon);
        item.setminTemper(minTemper);
        item.setMaxTemper(maxTemper);
        item.setwholeDateText(wholeWeekName);

        sample.add(item);
    }
}
