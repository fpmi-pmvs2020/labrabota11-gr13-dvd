package com.task.fbresult.ui.peoples_on_duty;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.task.fbresult.DutyActivity;
import com.task.fbresult.R;
import com.task.fbresult.db.dao.DutyDao;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.DutyType;
import com.task.fbresult.model.GraphicResult;
import com.task.fbresult.model.PeopleOnDuty;
import com.task.fbresult.ui.adapters.NodeListener;
import com.task.fbresult.util.DAORequester;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DutyFragment extends Fragment implements NodeListener{

    private static final int DATA_UPLOADED = 42;
    private PeopleAdapter adapter;
    private Handler leftTimeHandler;
    private TextView leftTime;
    private Duty duty;

    public static DutyFragment newInstance(Bundle parameters) {
        DutyFragment dutyFragment = new DutyFragment();
        dutyFragment.setArguments(parameters);
        return dutyFragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_duty, container, false);
        duty = (Duty) getArguments().getSerializable(DutyActivity.DUTY_PARAMETERS);

        LinearLayout dutyHolder = view.findViewById(R.id.duty_frame);
        RecyclerView recycler = view.findViewById(R.id.duty_recycler);
        leftTime = view.findViewById(R.id.time_before_duty_left);

        View dutyInfo = inflateCurrentDuty(this.duty);
        dutyHolder.addView(dutyInfo, 1);
        setDayText(view, this.duty.getFrom());

        List<PeopleAdapter.Item> orderedListOfPerson = PeoplesProviders.getOrderedListOfPerson(this.duty);


        adapter = new PeopleAdapter(getContext(),orderedListOfPerson , this);
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new PeopleAdapter.SimpleDivider(getContext()));

        leftTimeHandler = new Handler();
        leftTimeHandler.post(updateTime);

        DutyFragmentViewModel model = new ViewModelProvider(this).get(DutyFragmentViewModel.class);
        model.getGraphic(duty).observe(getViewLifecycleOwner(), (graphicResult) -> {

            Map<Integer, List<Bitmap>> map = graphicResult.list.stream()
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


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void nodeClicked(int indexOf) {
        Toast.makeText(getContext(), adapter.items.get(indexOf).people.getFrom().toString(), Toast.LENGTH_LONG).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("DefaultLocale")
    private String getRemainingTime(LocalDateTime start, LocalDateTime end) {
        long day = ChronoUnit.DAYS.between(start, end);
        long hour = ChronoUnit.HOURS.between(start, end);
        long minutes = ChronoUnit.MINUTES.between(start, end);
        long seconds = ChronoUnit.SECONDS.between(start, end);

        if (day == 0) {
            return String.format("%d:%02d:%02d",
                    hour - day * 24,
                    minutes - hour * 60,
                    seconds - minutes * 60
            );
        }
        return String.format("%d %d:%02d:%02d",
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

        DutyType dutyType = DAORequester.getDutyType(duty);
        title.setText(dutyType.getTitle());
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


}
