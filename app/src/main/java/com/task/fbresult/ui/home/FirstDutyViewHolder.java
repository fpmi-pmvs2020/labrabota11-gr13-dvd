package com.task.fbresult.ui.home;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.task.fbresult.R;
import com.task.fbresult.model.Duty;
import com.task.fbresult.util.DutyFormatter;

public class FirstDutyViewHolder {
    Resources resources;
    View child;
    TextView tvDutyTitle;
    TextView tvDutyDate;
    TextView tvWeekDay;
    TextView tvPartners;
    TextView tvRemainedTime;
    TextView tvStartTime;
    TextView tvFinishTime;
    TextView tvPositionCount;

    public FirstDutyViewHolder(Context context){
        resources = context.getResources();
        child  = View.inflate(context, R.layout.duty_first_item,null);
        tvDutyTitle = child.findViewById(R.id.tvDutyTitle);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View formatViewWithDuty(Duty duty){
        if(duty == null)
            tvDutyTitle.setText(resources.getString(R.string.no_duty));
        else {
            initNecessaryFields();
            DutyFormatter dutyFormatter = new DutyFormatter(duty,resources);
            formatDutyOnDisplay(dutyFormatter);
        }
        return child;
    }

    private void initNecessaryFields(){
        tvDutyDate = child.findViewById(R.id.tvDutyDate);
        tvWeekDay = child.findViewById(R.id.tvDutyWeekDay);
        tvPartners = child.findViewById(R.id.tvDutyPartners);
        tvRemainedTime = child.findViewById(R.id.tvDutyTimeRemained);
        tvStartTime = child.findViewById(R.id.tvDutyStartTime);
        tvFinishTime = child.findViewById(R.id.tvDutyEndTime);
        tvPositionCount = child.findViewById(R.id.tvDutyPosAmount);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void formatDutyOnDisplay(DutyFormatter dutyFormatter){
        tvDutyTitle.setText(dutyFormatter.getTitle());
        tvDutyDate.setText(dutyFormatter.getDate());
        tvWeekDay.setText(dutyFormatter.getDayOfWeek());
        tvPartners.setText(dutyFormatter.getPartnersAsString());
        tvRemainedTime.setText(dutyFormatter.getDaysLeftAsString());
        tvStartTime.setText(dutyFormatter.getStartTime());
        tvFinishTime.setText(dutyFormatter.getFinishTime());
        tvPositionCount.setText(dutyFormatter.getMaxPeople());
    }

}
