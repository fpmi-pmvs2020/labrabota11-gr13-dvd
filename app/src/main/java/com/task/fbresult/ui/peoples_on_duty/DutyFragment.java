package com.task.fbresult.ui.peoples_on_duty;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.task.fbresult.R;
import com.task.fbresult.db.dao.DutyDao;
import com.task.fbresult.db.dao.DutyTypesDao;
import com.task.fbresult.db.dao.PeopleOnDutyDao;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.DutyType;
import com.task.fbresult.model.PeopleOnDuty;
import com.task.fbresult.ui.adapters.NodeListener;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

public class DutyFragment extends Fragment implements NodeListener {

    private PeopleAdapter adapter;
    private Handler leftTimeHandler;
    private TextView leftTime;
    private Duty duty;

    public static DutyFragment newInstance() {
        return new DutyFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_duty, container, false);
        duty = getCurrentDuty();

        LinearLayout dutyHolder = view.findViewById(R.id.duty_frame);
        RecyclerView recycler = view.findViewById(R.id.duty_recycler);
        leftTime = view.findViewById(R.id.time_before_duty_left);

        View dutyInfo = inflateCurrentDuty(this.duty);
        dutyHolder.addView(dutyInfo, 1);
        setDayText(view, this.duty.getFrom());


        adapter = new PeopleAdapter(getContext(), PeoplesProviders.getOrderedListOfPerson(this.duty), this);
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new PeopleAdapter.SimpleDivider(getContext()));

        leftTimeHandler = new Handler();
        leftTimeHandler.post(updateTime);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Duty getCurrentDuty() {
        //todo return current duty;
        return new DutyDao().get(String.format(DutyDao.GET_DUTY_WITH_ID, 1222)).get(0);
    }

//    List<PeopleOnDuty> getPeoples(Duty duty) {
//        return new PeopleOnDutyDao().get(String.format(PeopleOnDutyDao.GET_PEOPLE_ON_DUTY_WITH_DUTY_ID, duty.getId()));
//    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void nodeClicked(int indexOf) {
        Toast.makeText(getContext(), adapter.items.get(indexOf).people.getFrom().toString(), Toast.LENGTH_LONG).show();
    }

    Runnable updateTime = new Runnable() {
        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run() {
            Duration between = Duration.between(LocalDateTime.now(), duty.getFrom());

            if (between.isNegative()) {
                if (!Duration.between(LocalDateTime.now(), duty.getTo()).isNegative()) {
                    leftTime.setText("Remaining time" + getRemainingTime(LocalDateTime.now(), duty.getTo()));
                    leftTimeHandler.postDelayed(this, 200);
                } else {
                    leftTime.setText("Ended");
                }
                return;
            }

            leftTime.setText(getRemainingTime(LocalDateTime.now(), duty.getFrom()));
            leftTimeHandler.postDelayed(this, 200);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("DefaultLocale")
    private String getRemainingTime(LocalDateTime start, LocalDateTime end){
        long day = ChronoUnit.DAYS.between  (start,end);
        long hour = ChronoUnit.HOURS.between(start,end);
        long minutes = ChronoUnit.MINUTES.between(start, end);
        long seconds = ChronoUnit.SECONDS.between(start, end);

        if (day ==0){
            return String.format("%d:%02d:%02d",
                    hour - day * 24,
                    minutes - hour * 60,
                    seconds - minutes * 60
            );
        }
        return  String.format("%d %d:%02d:%02d",
                day,
                hour - day * 24,
                minutes - hour * 60,
                seconds - minutes * 60
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private View inflateCurrentDuty(Duty duty) {
        View currentDuty = View.inflate(getContext(), R.layout.duty_item_main, null);
        currentDuty.setBackgroundResource(0);

        TextView title = currentDuty.findViewById(R.id.tvDutyTitle);
        TextView tag = currentDuty.findViewById(R.id.tvDutyPartners);
        TextView from = currentDuty.findViewById(R.id.tvDutyStartTime);
        TextView to = currentDuty.findViewById(R.id.tvDutyEndTime);
        TextView max = currentDuty.findViewById(R.id.tvDutyAmounts);

        DutyTypesDao dutyTypesDao = new DutyTypesDao();
        List<DutyType> dutyTypes = dutyTypesDao.get(DutyTypesDao.GET_BY_ID_QUERY + duty.getType());

        title.setText(dutyTypes.get(0).getTitle());
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;
        from.setText(duty.getFrom().format(formatter));
        to.setText(duty.getTo().format(formatter));
        max.setText(String.valueOf(duty.getMaxPeople()));
        return currentDuty;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String setDayText(View view, LocalDateTime dateTime) {
        String format = dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy EEEE", Locale.getDefault()));
        ((TextView) view.findViewById(R.id.duty_day)).setText(format);
        return format;
    }

}
