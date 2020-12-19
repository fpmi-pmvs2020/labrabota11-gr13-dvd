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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.task.fbresult.DutyActivity;
import com.task.fbresult.R;
import com.task.fbresult.db.dao.DutyDao;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.Person;
import com.task.fbresult.ui.adapters.DutyAdapter;
import com.task.fbresult.ui.holders.FirstDutyViewFiller;
import com.task.fbresult.util.DAORequester;
import com.task.fbresult.util.FBUtils;
import com.task.fbresult.util.LocalDateTimeHelper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private View root;
    RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.dayDutyRecycler);
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
            LocalDateTime localDateTime = LocalDateTime.of(year,month+1,dayOfMonth,0,0);
            showSelectedDuty(localDateTime);
            LinearLayout linearLayout = root.findViewById(R.id.firstDutyLayout);
            linearLayout.setVisibility(View.GONE);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showSelectedDuty(LocalDateTime localDateTime) {
        List<Duty> duties = loadSelectedDuty(localDateTime);

        DutyAdapter dutyAdapter = new DutyAdapter(getContext(), duties, indexOf -> {
            DutyAdapter adapter = (DutyAdapter)recyclerView.getAdapter();
            Duty selectedDuty = Objects.requireNonNull(adapter).items.get(indexOf);
            loadDutyActivity(selectedDuty);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(dutyAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<Duty> loadSelectedDuty(LocalDateTime localDateTime) {
        LocalDateTime nextDay = localDateTime.plusDays(1);
        String currDayAsString = LocalDateTimeHelper.getDateTimeAsString(localDateTime);
        String nextDayAsString = LocalDateTimeHelper.getDateTimeAsString(nextDay);
        String query = String.format(DutyDao.GET_DUTIES_WITH_DAY,currDayAsString,nextDayAsString);
        return new DutyDao().get(query);
    }

    private void loadDutyActivity(Duty duty){
        DutyActivity.getInstance(duty,getContext());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showFirstDuty() {
        LinearLayout linearLayout = root.findViewById(R.id.firstDutyLayout);
        Duty firstDuty = loadFirstDuty();
        View child = getViewWithFirstDuty(firstDuty);
        child.setOnClickListener(v -> loadDutyActivity(firstDuty));
        linearLayout.addView(child);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private View getViewWithFirstDuty(Duty duty) {
        FirstDutyViewFiller viewHolder = new FirstDutyViewFiller(root.getContext());
        return viewHolder.formatViewWithDuty(duty);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Duty loadFirstDuty() {
        Person currentUser = FBUtils.getCurrentUserAsPerson();
        return DAORequester.getFirstOfNextDutiesOfPerson(currentUser);
    }
}