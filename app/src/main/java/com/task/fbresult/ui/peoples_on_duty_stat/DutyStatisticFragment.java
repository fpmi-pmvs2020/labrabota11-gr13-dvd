package com.task.fbresult.ui.peoples_on_duty_stat;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.task.fbresult.model.GraphicResult;
import com.task.fbresult.ui.adapters.NodeListener;
import com.task.fbresult.ui.peoples_on_duty.DutyFragment;
import com.task.fbresult.ui.peoples_on_duty.DutyFragmentViewModel;
import com.task.fbresult.ui.peoples_on_duty.PeopleAdapter;
import com.task.fbresult.ui.peoples_on_duty.PeoplesProviders;

import java.time.Duration;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DutyStatisticFragment extends Fragment implements NodeListener, SeekBar.OnSeekBarChangeListener {
    private Duty duty;
    private PeopleAdapter adapter;
    private SeekBar seekBar;
    Map<Integer, List<Bitmap>> map;

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
        seekBar.setProgress(0);
        seekBar.setMax((int)Duration.between(duty.getTo(),duty.getFrom()).abs().toHours());


        DutyFragmentViewModel model = new ViewModelProvider(this).get(DutyFragmentViewModel.class);
        model.getGraphic(duty).observe(getViewLifecycleOwner(), (graphicResult) -> {

            map = graphicResult.list.stream()
                    .collect(Collectors.groupingBy(
                            GraphicResult.Result::getPersonId,
                            Collectors.mapping(
                                    GraphicResult.Result::getImage,
                                    Collectors.toList()
                            )
                    ));

            for (PeopleAdapter.Item i : adapter.items)
                i.images = map.get((int)i.people.getPersonId()).get(0);

            adapter.notifyDataSetChanged();
        });
        return view;
    }

    @Override
    public void nodeClicked(int indexOf) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
