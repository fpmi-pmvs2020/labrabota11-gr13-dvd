package com.task.fbresult.util;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.task.fbresult.DutyActivity;
import com.task.fbresult.R;
import com.task.fbresult.db.fbdao.FBPeopleOnDutyDao;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.DutyIntervalData;

import lombok.var;

public class IntervalViewGenerator {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static View getViewOf(DutyIntervalData dutyIntervalData, Context context, boolean isYour) {
        View view = View.inflate(context, R.layout.duty_interval_item, null);
        var peopleOnDuty = new FBPeopleOnDutyDao().getWithId(dutyIntervalData.getPeopleOnDutyId());
        var duty = DAORequester.getDutyWithPeopleOnDuty(peopleOnDuty);

        configureViewExchangeTitle(view, isYour);
        configureViewDutyDate(view, duty);
        configureViewDutyType(view, duty);
        configureViewIntervalOwner(view, dutyIntervalData);
        configureViewInterval(view, dutyIntervalData);
        configureViewDutyTime(view, duty);
        configureViewLinkButton(view, duty, context);

        return view;
    }

    private static void configureViewDutyDate(View view, Duty duty) {
        var tvDutyDate = (TextView) view.findViewById(R.id.tvMsgDutyDate);
        tvDutyDate.setText(duty.getFrom().split("T")[0]);
    }

    private static void configureViewExchangeTitle(View view, boolean isYour) {
        var tvIntervalExchangeTitle = (TextView) view.findViewById(R.id.intervalExchangeTitle);
        tvIntervalExchangeTitle.setText(
                isYour ?
                        view.getResources().getString(R.string.your_duty)
                        : view.getContext().getString(R.string.other_duty)
        );
    }

    private static void configureViewDutyType(View view, Duty duty) {
        var tvMsgDutyType = (TextView) view.findViewById(R.id.tvMsgDutyType);
        var dutyType = DAORequester.getDutyType(duty);
        tvMsgDutyType.setText(dutyType.getTitle());
    }

    private static void configureViewIntervalOwner(View view, DutyIntervalData dutyIntervalData) {
        var tvIntervalOwner = (TextView) view.findViewById(R.id.tvIntervalOwner);
        var person = DAORequester.getPersonFromIntervalData(dutyIntervalData);
        tvIntervalOwner.setText(person.getFio());
    }

    private static void configureViewInterval(View view, DutyIntervalData dutyIntervalData) {
        var tvInterval = (TextView) view.findViewById(R.id.tvInterval);
        tvInterval.setText(
                dutyIntervalData.getFrom().split("T")[1]
                        + " - "
                        + dutyIntervalData.getTo().split("T")[1]
        );
    }

    private static void configureViewDutyTime(View view, Duty duty) {
        var tvDutyTime = (TextView) view.findViewById(R.id.tvMsgDutyTime);
        String[] fromValues = duty.getFrom().split("T");
        tvDutyTime.setText(fromValues[1] + " - " + duty.getTo().split("T")[1]);
    }

    private static void configureViewLinkButton(View view, Duty duty, Context context) {
        var linkButton = (TextView) view.findViewById(R.id.linkButton);
        linkButton.setOnClickListener(v -> DutyActivity.getInstance(duty, context));
    }
}
