package com.task.fbresult.ui.user_duties;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.task.fbresult.R;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.Person;
import com.task.fbresult.ui.adapters.TimedDutyAdapter;
import com.task.fbresult.util.DAORequester;
import com.task.fbresult.util.FBUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiresApi(api = Build.VERSION_CODES.O)
public class UserDutiesFragment extends Fragment {

    private UserDutiesViewModel userDutiesViewModel;
    View root;

    Spinner spinner;
    RecyclerView dutiesRecycler;

    List<Duty>duties;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userDutiesViewModel =
                new ViewModelProvider(this).get(UserDutiesViewModel.class);
        root = inflater.inflate(R.layout.fragment_person_duties, container, false);
        duties = loadDuties();
        configureRecycler();
        configureSpinner();
        showDutiesBelongsSpinnerPosition(spinner.getSelectedItemPosition());
        return root;
    }

    private void configureRecycler(){
        dutiesRecycler = root.findViewById(R.id.recPersonDuties);
        dutiesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        TimedDutyAdapter dutyAdapter = new TimedDutyAdapter(getContext(), new ArrayList<>(), indexOf -> {
            TimedDutyAdapter adapter = (TimedDutyAdapter) dutiesRecycler.getAdapter();
            Duty selectedDuty = Objects.requireNonNull(adapter).items.get(indexOf);
            loadDutyActivity(selectedDuty);
        });
        dutiesRecycler.setAdapter(dutyAdapter);
    }

    private void configureSpinner(){
        spinner = root.findViewById(R.id.spnDutyType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.duty_types, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showDutiesBelongsSpinnerPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //unreachable
            }
        });
    }

    private void showDutiesBelongsSpinnerPosition(int spnPosition){
        TimedDutyAdapter dutyAdapter = (TimedDutyAdapter)dutiesRecycler.getAdapter();
        changeDutiesOnAdapterBelongsSpnPosition(dutyAdapter,spnPosition);
        dutyAdapter.notifyDataSetChanged();
    }

    private void changeDutiesOnAdapterBelongsSpnPosition(TimedDutyAdapter dutyAdapter, int spnPosition){
        switch (spnPosition){
            case 0:
                dutyAdapter.setDuties(getNextDuties());
                break;
            case 1:
                dutyAdapter.setDuties(getPastDuties());
                break;
            case 2:
                dutyAdapter.setDuties(duties);
                break;
        }
    }

    private List<Duty> getNextDuties(){
        LocalDateTime localDateTime = LocalDateTime.now();
        return duties.stream()
                .filter(duty->duty.getFrom().isAfter(localDateTime))
                .collect(Collectors.toList());
    }

    private List<Duty>getPastDuties(){
        LocalDateTime localDateTime = LocalDateTime.now();
        return duties.stream()
                .filter(duty->duty.getFrom().isBefore(localDateTime))
                .collect(Collectors.toList());
    }

    private void loadDutyActivity(Duty duty){
        //TODO:
    }

    private List<Duty> loadDuties() {
        Person currentUser = FBUtils.getCurrentUserAsPerson();
        List<Duty>duties = DAORequester.getPersonDuties(currentUser);
        duties.sort((first,second)->first.getFrom().compareTo(second.getFrom()));
        return duties;
    }
}