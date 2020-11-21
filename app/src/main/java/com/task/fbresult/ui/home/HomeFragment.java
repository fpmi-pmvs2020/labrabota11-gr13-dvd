package com.task.fbresult.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.task.fbresult.R;
import com.task.fbresult.db.DBHelper;
import com.task.fbresult.db.DBRequester;
import com.task.fbresult.model.Duty;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private View root;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        configureCalendar();
        FirebaseAuth.getInstance().addAuthStateListener(param -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null)
                showFirstDuty();
        });
        return root;
    }

    private void configureCalendar() {
        CalendarView calendarView = root.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String dayStr = String.valueOf(dayOfMonth);
            if (dayStr.length() == 1)
                dayStr = "0" + dayStr;
            month++;
            String monthStr = String.valueOf(month);
            if (monthStr.length() == 1)
                monthStr = "0" + month;

            String selectedDate = dayStr + "." +
                    monthStr + "." +
                    year;
            showSelectedDuty(selectedDate);
        });
    }

    private void showSelectedDuty(String selectedDate) {
        LinearLayout linearLayout = root.findViewById(R.id.selectedDayDutyLayout);
        try {
            linearLayout.removeViewAt(1);
        } catch (Exception e) {

        }
        Duty duty = loadSelectedDuty(selectedDate);
        View child = getViewWithDuty(duty);
        linearLayout.addView(child);
    }

    private Duty loadSelectedDuty(String selectedDate) {
        DBHelper dbHelper = DBHelper.getInstance(getContext(), null);
        DBRequester dbRequester = dbHelper.getDBRequester();
        return dbRequester.getDutyWithDate(selectedDate);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showFirstDuty() {
        LinearLayout linearLayout = root.findViewById(R.id.firstDutyLayout);
        Duty firstDuty = loadFirstDuty();
        View child = getViewWithDuty(firstDuty);
        linearLayout.addView(child);
    }

    private View getViewWithDuty(Duty duty) {
//        View child = View.inflate(getContext(),R.layout.duty_item,null);
//        //TextView tvDate = child.findViewById(R.id.tvDutyTitle);
//        TextView tvWeekDay = child.findViewById(R.id.tvDutyWeekDay);
//        //TextView tvPartner = child.findViewById(R.id.tvDutyTag);
//        TextView tvRemainedTime = child.findViewById(R.id.tvDutyTimeRemained);
//
//        if(duty == null)
//            tvDate.setText(getString(R.string.no_duty));
//        else {
//            tvDate.setText(duty.getDate());
//            tvPartner.setText(String.format(getString(R.string.partner),duty.getPartner()));
//            DutyManager dutyManager = new DutyManager(duty,getResources());
//            tvWeekDay.setText(dutyManager.getDayOfWeek());
//            tvRemainedTime.setText(dutyManager.getDaysLeftAsString());
//        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Duty loadFirstDuty() {
        DBHelper dbHelper = DBHelper.getInstance(getContext(),
                null);
        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        DBRequester dbRequester = dbHelper.getDBRequester();
        return dbRequester.getFirstDutyWithName(userName);
    }
}