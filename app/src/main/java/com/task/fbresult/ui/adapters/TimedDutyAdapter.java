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
    int datesCount;
    HashMap<Integer,LocalDate>dates = new HashMap<>();
    TreeSet<Integer> datesIndexes = new TreeSet<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public TimedDutyAdapter(Context context, @NonNull List<Duty> items, NodeListener listener) {
        super(context,items,listener);
        this.items = getDutiesWithNulls();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<Duty> getDutiesWithNulls(){
        List<Duty>result = new ArrayList<>();
        LocalDate lastDate = LocalDate.of(1,1,1);
        for(int i = 0;i<items.size();i++){
            LocalDate itemDate = items.get(i).getFrom().toLocalDate();
            if(!itemDate.isEqual(lastDate)){
                datesIndexes.add(result.size());
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
        switch (viewType){
            case 0:
                return super.onCreateViewHolder(parent,viewType);
            case 1:
                View view = inflater.inflate(R.layout.date_view, parent, false);
                return new TimeViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return datesIndexes.contains(position)?1:0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if(datesIndexes.contains(position)){
            TimeViewHolder holder = (TimeViewHolder)viewHolder;
            holder.tvDate.setText(LocalDateTimeHelper.getFormattedDate(dates.get(position)));
        }else{
            super.onBindViewHolder(viewHolder,position);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int getItemCount() {
        return items.size()+datesCount;
    }
}
