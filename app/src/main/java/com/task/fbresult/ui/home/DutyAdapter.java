package com.task.fbresult.ui.home;

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
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.DutyTypes;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DutyAdapter extends RecyclerView.Adapter<DutyAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final NodeListener listener;
    public final List<Duty> items;
    public final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;

    public DutyAdapter(Context context,@NonNull List<Duty> items,@Nullable NodeListener listener) {
        inflater = LayoutInflater.from(context);
        this.items = items;
        this.listener = listener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.duty_item_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Duty item1 = items.get(position);
        DutyTypesDao dutyTypesDao = new DutyTypesDao();
        List<DutyTypes> dutyTypes = dutyTypesDao.get(DutyTypesDao.GET_BY_ID_QUERY + item1.getType());

        holder.title.setText(dutyTypes.get(0).getTitle());
        holder.from.setText(item1.getFrom().format(formatter));
        holder.to.setText(item1.getTo().format(formatter));
        holder.max.setText(item1.getMaxPeople());

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
        public TextView max;

        public ViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.tvDutyTitle);
            tag = view.findViewById(R.id.tvDutyPartners);
            from = view.findViewById(R.id.tvDutyStartTime);
            to = view.findViewById(R.id.tvDutyEndTime);
            max = view.findViewById(R.id.tvDutyAmounts);

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (listener != null)
                listener.nodeClicked(getAbsoluteAdapterPosition());
        }
    }
}
