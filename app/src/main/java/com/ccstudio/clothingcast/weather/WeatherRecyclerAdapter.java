package com.ccstudio.clothingcast.weather;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ccstudio.clothingcast.R;

import java.util.ArrayList;

public class WeatherRecyclerAdapter  extends RecyclerView.Adapter<WeatherItemView> {

    private ArrayList<WeatherItem> listData = new ArrayList<>();

    //클릭 상태 저장 어레이
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private  int prePosition = -1;
    private WeatherItem.WeatherCase sel_type;

    public  WeatherRecyclerAdapter( WeatherItem.WeatherCase sel_type)
    {
        this.sel_type = sel_type;
    }

    @NonNull
    @Override
    public WeatherItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if(sel_type == WeatherItem.WeatherCase.today)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.w_listitem_today,parent,false);
            return new ViewHolderToday(view);
        }
        else if(sel_type == WeatherItem.WeatherCase.weekly)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.w_listitem_week,parent,false);
            return  new ViewHolderWeekly(view);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherItemView holder, int position) {

        if(holder instanceof  ViewHolderToday)
        {
            ViewHolderToday viewHolderToday = (ViewHolderToday) holder;
            viewHolderToday.onBind((W_TodayData) listData.get(position));
        }
        else if(holder instanceof ViewHolderWeekly)
        {
            ViewHolderWeekly viewHolderWeekly = (ViewHolderWeekly)holder;
            viewHolderWeekly.onBind((W_WeeklyData)listData.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItem(WeatherItem data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
        System.out.println("item size "+listData.size());
    }
}
