package com.task.fbresult.ui.peoples_on_duty;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.task.fbresult.DutyActivity;
import com.task.fbresult.R;

public class DutyStatisticFragment extends Fragment {



    public static DutyStatisticFragment newInstance() {
        return new DutyStatisticFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.duty_statistic_fragment, container, false);
    }

}
