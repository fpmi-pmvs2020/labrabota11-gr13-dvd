package com.task.fbresult.ui.peoples_on_duty_stat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.task.fbresult.DutyActivity;
import com.task.fbresult.R;
import com.task.fbresult.model.Duty;
import com.task.fbresult.ui.peoples_on_duty.DutyFragment;

public class DutyStatisticFragment extends Fragment {


    private Duty duty;

    public static DutyStatisticFragment newInstance(Bundle parameters) {
        DutyStatisticFragment dutyFragment = new DutyStatisticFragment();
        dutyFragment.setArguments(parameters);
        return dutyFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_duty_statistic, container, false);
        duty = (Duty)getArguments().getSerializable(DutyActivity.DUTY_PARAMETERS);

        return view;
    }

}
