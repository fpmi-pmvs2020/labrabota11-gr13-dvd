package com.task.fbresult.ui.all_duties;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.task.fbresult.R;
import com.task.fbresult.db.DBHelper;
import com.task.fbresult.db.DBRequester;
import com.task.fbresult.model.Duty;
import com.task.fbresult.util.DutyManager;

import java.util.List;

public class AllDutiesFragment extends Fragment {

    private AllDutiesViewModel allDutiesViewModel;
    View root;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        allDutiesViewModel =
                new ViewModelProvider(this).get(AllDutiesViewModel.class);
        root = inflater.inflate(R.layout.fragment_gallery, container, false);
        showAllUserDuties();
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showAllUserDuties() {
        LinearLayout dutiesLayout = root.findViewById(R.id.allDutiesLayout);
        List<Duty> duties = loadDuties();
        for (Duty duty : duties) {
            View dutyView = getViewWithDuty(duty);
            dutiesLayout.addView(dutyView);
        }
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

    private List<Duty> loadDuties() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DBHelper dbHelper = DBHelper.getInstance(getContext(),null);
        DBRequester dbRequester = dbHelper.getDBRequester();
        //return dbRequester.getDutiesForName(user.getDisplayName());
        throw new UnsupportedOperationException("method is not realised yet");
    }
}