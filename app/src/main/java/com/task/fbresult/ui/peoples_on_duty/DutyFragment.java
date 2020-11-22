package com.task.fbresult.ui.peoples_on_duty;

import android.os.Build;
import android.os.Bundle;
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
import com.task.fbresult.model.DutyTypes;
import com.task.fbresult.model.PeopleOnDuty;
import com.task.fbresult.ui.adapters.NodeListener;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class DutyFragment extends Fragment implements NodeListener {

    private LinearLayout dutyHolder;
    private RecyclerView recycler;
    private Duty duty;
    private PeopleAdapter adapter;

    public static DutyFragment newInstance() {
        return new DutyFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.duty_fragment, container, false);
        duty = getCurrentDuty();

        View duty = inflateCurrentDuty(this.duty);

        dutyHolder = view.findViewById(R.id.duty_frame);
        dutyHolder.addView(duty);

        recycler = view.findViewById(R.id.duty_recycler);
        adapter = new PeopleAdapter(getContext(), getPeoples(this.duty), this);
        recycler.setAdapter(adapter);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Duty getCurrentDuty() {
        //todo return current duty;
        return new DutyDao().get(DutyDao.GET_ALL_QUERY).get(0);
    }

    List<PeopleOnDuty> getPeoples(Duty duty) {
        return new PeopleOnDutyDao().get(String.format(PeopleOnDutyDao.GET_PEOPLE_ON_DUTY_WITH_DUTY_ID, duty.getId()));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private View inflateCurrentDuty(Duty duty) {
        View currentDuty = View.inflate(getContext(), R.layout.duty_item_main, null);
        TextView title = currentDuty.findViewById(R.id.tvDutyTitle);
        TextView tag = currentDuty.findViewById(R.id.tvDutyPartners);
        TextView from = currentDuty.findViewById(R.id.tvDutyStartTime);
        TextView to = currentDuty.findViewById(R.id.tvDutyEndTime);
        TextView max = currentDuty.findViewById(R.id.tvDutyAmounts);

        DutyTypesDao dutyTypesDao = new DutyTypesDao();
        List<DutyTypes> dutyTypes = dutyTypesDao.get(DutyTypesDao.GET_BY_ID_QUERY + duty.getType());

        title.setText(dutyTypes.get(0).getTitle());
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;
        from.setText(duty.getFrom().format(formatter));
        to.setText(duty.getTo().format(formatter));
        max.setText(String.valueOf(duty.getMaxPeople()));
        return currentDuty;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void nodeClicked(int indexOf) {
        Toast.makeText(getContext(), adapter.items.get(indexOf).getFrom().toString(), Toast.LENGTH_LONG).show();
    }
}
