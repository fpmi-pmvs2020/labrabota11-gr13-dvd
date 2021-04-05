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

import com.task.fbresult.R;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.DutyType;
import com.task.fbresult.model.PeopleOnDuty;
import com.task.fbresult.model.Person;
import com.task.fbresult.ui.holders.DutyViewHolder;
import com.task.fbresult.util.DAORequester;
import com.task.fbresult.util.FBUtils;
import com.task.fbresult.util.LocalDateTimeHelper;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DutyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected final LayoutInflater inflater;
    private final NodeListener listener;
    public List<Duty> items;
    private final Person currentUser = FBUtils.getCurrentUserAsPerson();

    public DutyAdapter(Context context, @NonNull List<Duty> items,
                       @Nullable NodeListener listener) {
        inflater = LayoutInflater.from(context);
        this.items = items;
        this.listener = listener;
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
        DutyType dutyType = DAORequester.getDutyType(duty);
        DutyViewHolder holder = (DutyViewHolder)viewHolder;

        holder.title.setText(dutyType.getTitle());
        holder.from.setText(LocalDateTimeHelper.getFormattedTime(duty.fromAsLocalDateTime()));
        holder.to.setText(LocalDateTimeHelper.getFormattedTime(duty.toAsLocalDateTime()));
        holder.max.setText(String.valueOf(duty.getMaxPeople()));
        List<PeopleOnDuty>peopleOnDuties = DAORequester.getPeopleOnDuty(duty);
        for(PeopleOnDuty peopleOnDuty:peopleOnDuties){
            if(peopleOnDuty.getPersonId().equals(currentUser.getFirebaseId())) {
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
