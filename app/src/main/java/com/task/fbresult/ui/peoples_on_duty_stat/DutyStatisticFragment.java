package com.task.fbresult.ui.peoples_on_duty_stat;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.task.fbresult.DutyActivity;
import com.task.fbresult.R;
import com.task.fbresult.model.Duty;
import com.task.fbresult.ui.adapters.NodeListener;
import com.task.fbresult.ui.peoples_on_duty.PeopleAdapter;
import com.task.fbresult.ui.peoples_on_duty.PeoplesProviders;

import java.time.Duration;

public class DutyStatisticFragment extends Fragment implements NodeListener, SeekBar.OnSeekBarChangeListener {
    private Duty duty;
    private PeopleAdapter adapter;
    private SeekBar seekBar;
    private DutyStatisticViewModel model;

    public static DutyStatisticFragment newInstance(Bundle parameters) {
        DutyStatisticFragment dutyFragment = new DutyStatisticFragment();
        dutyFragment.setArguments(parameters);
        return dutyFragment;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_duty_statistic, container, false);
        duty = (Duty)getArguments().getSerializable(DutyActivity.DUTY_PARAMETERS);

        RecyclerView recycler = view.findViewById(R.id.duty_recycler);

        adapter = new PeopleAdapter(getContext(), PeoplesProviders.getOrderedListOfPerson(this.duty), this);
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new PeopleAdapter.SimpleDivider(getContext()));
        view.findViewById(R.id.time_before_duty_left);
        seekBar = view.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);


        model = new ViewModelProvider(this).get(DutyStatisticViewModel.class);
        model.getGraphic(duty).observe(getViewLifecycleOwner(), (graphicResult) -> {
            for (PeopleAdapter.Item i : adapter.items){
                if(i.people.getPersonId() == 0) continue;
                i.images = graphicResult.get((int)i.people.getPersonId()).get(0);
            }

            adapter.notifyDataSetChanged();
        });

        seekBar.setMax((int)Duration.between(duty.getTo(),duty.getFrom()).abs().toHours());
        seekBar.setProgress(0);
        return view;
    }

    @Override
    public void nodeClicked(int indexOf) {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        model.requestHourlyDate(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
