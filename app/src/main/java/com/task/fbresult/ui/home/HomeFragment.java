package com.task.fbresult.ui.home;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.task.fbresult.DutyActivity;
import com.task.fbresult.R;
import com.task.fbresult.db.fbdao.FBDutyDao;
import com.task.fbresult.dialogs.AlertDialogBuilder;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.Person;
import com.task.fbresult.ui.adapters.DutyAdapter;
import com.task.fbresult.ui.adapters.NodeListener;
import com.task.fbresult.ui.holders.FirstDutyViewFiller;
import com.task.fbresult.util.DAORequester;
import com.task.fbresult.util.FBUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {
    private static final String FIRST_DUTY_KEY = "firstDuty";
    private static final String DUTIES_KEY = "duties";

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
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                showFirstDuty();
            }
        });
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configureCalendar() {
        CalendarView calendarView = root.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            LocalDateTime localDateTime = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0);
            showSelectedDuty(localDateTime);
            LinearLayout linearLayout = root.findViewById(R.id.firstDutyLayout);
            linearLayout.setVisibility(View.GONE);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showSelectedDuty(LocalDateTime localDateTime) {
        Handler handler = new Handler(this.getActivity().getMainLooper(), msg -> {
            if (msg.what == 1) {
                Bundle data = msg.getData();
                List<Duty> duties = data.getParcelableArrayList(DUTIES_KEY);
                NodeListener nodeListener = indexOf -> {
                    DutyAdapter adapter = (DutyAdapter) recyclerView.getAdapter();
                    Duty selectedDuty = Objects.requireNonNull(adapter).items.get(indexOf);
                    loadDutyActivity(selectedDuty);
                };

                DutyAdapter dutyAdapter = new DutyAdapter(getContext(), duties, nodeListener);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(dutyAdapter);
            }
            return true;
        });

        new Thread(() -> {
            Message message = handler.obtainMessage(1);
            Bundle data = new Bundle();
            List<Duty> duties = loadSelectedDuty(localDateTime);
            data.putParcelableArrayList(DUTIES_KEY, (ArrayList<? extends Parcelable>) duties);
            message.setData(data);
            handler.sendMessage(message);

        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<Duty> loadSelectedDuty(LocalDateTime localDateTime) {
        LocalDateTime nextDay = localDateTime.plusDays(1);
        return new FBDutyDao().getAll().stream()
                .filter(duty -> duty.fromAsLocalDateTime().isAfter(localDateTime) && duty.toAsLocalDateTime().isBefore(nextDay))
                .collect(Collectors.toList());
    }

    private void loadDutyActivity(Duty duty) {
        DutyActivity.getInstance(duty, getContext());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showFirstDuty() {
        LinearLayout linearLayout = root.findViewById(R.id.firstDutyLayout);
        try {

            Handler handler = new Handler(this.getActivity().getMainLooper(), msg -> {
                if (msg.what == 1) {
                    Bundle data = msg.getData();
                    Duty firstDuty = (Duty) data.get(FIRST_DUTY_KEY);
//                    System.out.println(firstDuty.toString());
                    View child = getViewWithFirstDuty(firstDuty);
                    child.setOnClickListener(v -> loadDutyActivity(firstDuty));
                    linearLayout.addView(child);
                }
                return true;
            });

            new Thread(() -> {
                Message message = handler.obtainMessage(1);
                Bundle data = new Bundle();
                Duty firstDuty = loadFirstDuty();
                data.putSerializable(FIRST_DUTY_KEY, firstDuty);
                message.setData(data);
                handler.sendMessage(message);

            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
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