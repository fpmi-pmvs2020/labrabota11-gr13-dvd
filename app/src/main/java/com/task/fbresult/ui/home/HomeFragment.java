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
import com.task.fbresult.db.dao.DutyDao;
import com.task.fbresult.db.dao.PeopleOnDutyDao;
import com.task.fbresult.db.dao.PersonDao;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.PeopleOnDuty;
import com.task.fbresult.model.Person;
import com.task.fbresult.util.LocalDateTimeHelper;

import java.time.LocalDateTime;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configureCalendar() {
        CalendarView calendarView = root.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            LocalDateTime localDateTime = LocalDateTime.of(year,month,dayOfMonth,0,0);
            String dateAsString = LocalDateTimeHelper.getDateTimeAsString(localDateTime);
            showSelectedDuty(dateAsString);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showSelectedDuty(String selectedDate) {
        LinearLayout linearLayout = root.findViewById(R.id.selectedDayDutyLayout);
        try {
            linearLayout.removeViewAt(1);
        } catch (Exception e) {

        }
        Duty duty = loadSelectedDuty(selectedDate);
        View child = getViewWithFirstDuty(duty);
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
        View child = getViewWithFirstDuty(firstDuty);
        linearLayout.addView(child);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private View getViewWithFirstDuty(Duty duty) {
        FirstDutyViewHolder viewHolder = new FirstDutyViewHolder(getContext());
        return viewHolder.formatViewWithDuty(duty);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Duty loadFirstDuty() {
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Person currentUser = new PersonDao()
                .get(PersonDao.GET_USER_WITH_LOGIN_QUERY+userEmail).get(0);
        PeopleOnDuty peopleOnDuty = new PeopleOnDutyDao()
                .get(String.format(PeopleOnDutyDao.GET_FIRST_PEOPLE_ON_DUTY_WITH_PERSON_ID,
                        currentUser.getId())).get(0);
        return new DutyDao()
                .get(DutyDao.GET_DUTY_WITH_ID+peopleOnDuty.getDutyId()).get(0);
    }
}