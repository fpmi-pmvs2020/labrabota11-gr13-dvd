package com.task.fbresult.ui.peoples_on_duty;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.task.fbresult.R;
import com.task.fbresult.db.dao.DutyTypesDao;
import com.task.fbresult.db.dao.PersonDao;
import com.task.fbresult.model.PeopleOnDuty;
import com.task.fbresult.model.Person;
import com.task.fbresult.ui.adapters.NodeListener;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final NodeListener listener;
    public final List<PeopleOnDuty> items;
    public final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;

    public PeopleAdapter(Context context, @NonNull List<PeopleOnDuty> items, @Nullable NodeListener listener) {
        inflater = LayoutInflater.from(context);
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PeopleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.people_on_duty_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleAdapter.ViewHolder holder, int position) {
        PeopleOnDuty item1 = items.get(position);
        DutyTypesDao dutyTypesDao = new DutyTypesDao();

        long personId = item1.getPersonId();
        Person person = (new PersonDao().get(String.format(PersonDao.GET_USER_WITH_ID , personId))).get(0);

        holder.title.setText(person.getFio());
        holder.from.setText(item1.getFrom().format(formatter));
        holder.to.setText(item1.getTo().format(formatter));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;
        public TextView tag;
        public TextView from;
        public TextView to;

        public ViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.tvPeopleDutyName);
            tag = view.findViewById(R.id.tvPeopleDutyPartners);
            from = view.findViewById(R.id.tvPeopleDutyStartTime);
            to = view.findViewById(R.id.tvPeopleDutyEndTime);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null)
                listener.nodeClicked(getAbsoluteAdapterPosition());
        }
    }

    public static class Item{
        public PeopleOnDuty people;
        public Set<PeopleOnDutyState> state;

        public Item(PeopleOnDuty people, Set<PeopleOnDutyState> me) {
            this.people = people;
            state = me;
        }
    }
}

