package com.task.fbresult.ui.all_duties;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.task.fbresult.R;
import com.task.fbresult.db.dao.DutyDao;
import com.task.fbresult.db.dao.PersonDao;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.Person;
import com.task.fbresult.ui.adapters.TimedDutyAdapter;

import java.util.List;
import java.util.Objects;

public class UserDutiesFragment extends Fragment {

    private UserDutiesViewModel userDutiesViewModel;
    View root;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userDutiesViewModel =
                new ViewModelProvider(this).get(UserDutiesViewModel.class);
        root = inflater.inflate(R.layout.fragment_person_duties, container, false);
        showAllUserDuties();
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showAllUserDuties() {
        RecyclerView dutiesRecycler = root.findViewById(R.id.recPersonDuties);
        List<Duty> duties = loadDuties();
        dutiesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        TimedDutyAdapter dutyAdapter = new TimedDutyAdapter(getContext(), duties, indexOf -> {
            TimedDutyAdapter adapter = (TimedDutyAdapter) dutiesRecycler.getAdapter();
            Duty selectedDuty = Objects.requireNonNull(adapter).items.get(indexOf);
            loadDutyActivity(selectedDuty);
        });
        dutiesRecycler.setAdapter(dutyAdapter);
    }

    private void loadDutyActivity(Duty duty){
        //TODO:
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private View getViewWithDuty(Duty duty) {
        /*View child = View.inflate(getContext(), R.layout.duty_first_item, null);
        //TextView tvDate = child.findViewById(R.id.tvDutyTitle);
        TextView tvWeekDay = child.findViewById(R.id.tvDutyWeekDay);
        //TextView tvPartner = child.findViewById(R.id.tvDutyTag);
        TextView tvRemainedTime = child.findViewById(R.id.tvDutyTimeRemained);
        //tvDate.setText(duty.getDate());
        //tvPartner.setText(duty.getPartner());
        DutyManager dutyManager = new DutyManager(duty, getResources());
        tvRemainedTime.setText(dutyManager.getDaysLeftAsString());
        tvWeekDay.setText(dutyManager.getDayOfWeek());
        return child;*/
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<Duty> loadDuties() {
        String login = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String userQuery = String.format(PersonDao.GET_USER_WITH_LOGIN_QUERY,login);
        Person currentUser = new PersonDao().get(userQuery).get(0);
        String dutiesQuery = String.format(DutyDao.GET_DUTIES_WITH_PERSON_ID,currentUser.getId());
        return new DutyDao().get(dutiesQuery);
    }
}