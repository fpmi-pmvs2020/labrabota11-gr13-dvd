package com.task.fbresult.ui.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.task.fbresult.R;
import com.task.fbresult.db.dao.DutyTypesDao;
import com.task.fbresult.db.dao.PeopleOnDutyDao;
import com.task.fbresult.db.dao.PersonDao;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.DutyTypes;
import com.task.fbresult.model.PeopleOnDuty;
import com.task.fbresult.model.Person;
import com.task.fbresult.ui.holders.DutyViewHolder;
import com.task.fbresult.util.LocalDateTimeHelper;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DutyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected final LayoutInflater inflater;
    private final NodeListener listener;
    public List<Duty> items;
    public final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;
    private final Person currentUser;

    public DutyAdapter(Context context,@NonNull List<Duty> items,@Nullable NodeListener listener) {
        inflater = LayoutInflater.from(context);
        this.items = items;
        this.listener = listener;
        String login = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String query = String.format(PersonDao.GET_USER_WITH_LOGIN_QUERY,login);
        currentUser = new PersonDao().get(query).get(0);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.duty_item_main, parent, false);
        return new DutyViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Duty duty = items.get(position);
        DutyTypesDao dutyTypesDao = new DutyTypesDao();
        List<DutyTypes> dutyTypes = dutyTypesDao.get(DutyTypesDao.GET_BY_ID_QUERY + duty.getType());
        DutyViewHolder holder = (DutyViewHolder)viewHolder;

        holder.title.setText(dutyTypes.get(0).getTitle());
        holder.from.setText(LocalDateTimeHelper.getFormattedTime(duty.getFrom()));
        holder.to.setText(LocalDateTimeHelper.getFormattedTime(duty.getTo()));
        holder.max.setText(String.valueOf(duty.getMaxPeople()));
        String query = String.format(PeopleOnDutyDao.GET_PEOPLE_ON_DUTY_WITH_DUTY_ID,duty.getId());
        List<PeopleOnDuty>peopleOnDuties = new PeopleOnDutyDao().get(query);
        for(PeopleOnDuty peopleOnDuty:peopleOnDuties){
            if(peopleOnDuty.getPersonId() == currentUser.getId()) {
                holder.checkMark.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
