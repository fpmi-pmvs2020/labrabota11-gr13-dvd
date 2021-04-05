package com.task.fbresult.ui.adapters;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.task.fbresult.R;
import com.task.fbresult.model.Duty;
import com.task.fbresult.ui.holders.TimeViewHolder;
import com.task.fbresult.util.LocalDateTimeHelper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class TimedDutyAdapter extends DutyAdapter {
    HashMap<Integer,LocalDate>dates;
    TreeSet<Integer> daysIndexes;
    TreeSet<Integer> monthIndexes;
    Context context;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public TimedDutyAdapter(Context context, @NonNull List<Duty> items, NodeListener listener) {
        super(context,items,listener);
        this.context = context;
        this.items = getDutiesWithNulls(items);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDuties(List<Duty>duties){
        this.items = getDutiesWithNulls(duties);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<Duty> getDutiesWithNulls(List<Duty>items){
        dates = new HashMap<>();
        daysIndexes = new TreeSet<>();
        monthIndexes = new TreeSet<>();

        int lastMonth = 0;
        List<Duty>result = new ArrayList<>();
        LocalDate lastDate = LocalDate.of(1,1,1);
        for(int i = 0;i<items.size();i++){
            LocalDate itemDate = items.get(i).fromAsLocalDateTime().toLocalDate();
            if(!itemDate.isEqual(lastDate)){
                if(lastMonth!=itemDate.getMonthValue()){
                    lastMonth = itemDate.getMonthValue();
                    monthIndexes.add(result.size());
                    result.add(null);
                }
                daysIndexes.add(result.size());
                dates.put(result.size(),itemDate);
                lastDate = itemDate;
                result.add(null);
            }
            result.add(items.get(i));
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case 0:
                return super.onCreateViewHolder(parent,viewType);
            case 1:
                view = inflater.inflate(R.layout.day_view, parent, false);
                return new TimeViewHolder(view,R.id.tvDayView);
            case 2:
                view = inflater.inflate(R.layout.month_view, parent, false);
                return new TimeViewHolder(view,R.id.tvMonthView);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if(daysIndexes.contains(position))
            return 1;
        else if(monthIndexes.contains(position))
            return 2;
        else
            return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if(daysIndexes.contains(position)){
            TimeViewHolder holder = (TimeViewHolder)viewHolder;
            holder.tvData.setText(LocalDateTimeHelper.getFormattedDate(dates.get(position)));
        }else if(monthIndexes.contains(position)){
            TimeViewHolder holder = (TimeViewHolder)viewHolder;
            holder.tvData.setText(LocalDateTimeHelper
                    .getFormattedMonthAndYear(dates.get(position+1),context));
        }else{
            super.onBindViewHolder(viewHolder,position);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int getItemCount() {
        return items.size();
    }
}
