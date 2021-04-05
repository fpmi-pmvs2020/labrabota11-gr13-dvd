package com.task.fbresult.ui.people_on_duty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.task.fbresult.R;
import com.task.fbresult.db.DBHelper;
import com.task.fbresult.db.fbdao.FBPersonDao;
import com.task.fbresult.model.PeopleOnDuty;
import com.task.fbresult.model.Person;
import com.task.fbresult.ui.adapters.NodeListener;
import com.task.fbresult.util.DAORequester;

import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final NodeListener listener;
    public final List<Item> items;
    public final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    private final Context context;
    public int width;

    public PeopleAdapter(Context context, @NonNull List<Item> items, @Nullable NodeListener listener) {
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        this.items = items;
        this.listener = listener;
    }

    @SuppressLint("ResourceType")
    @NonNull
    @Override
    public PeopleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.people_on_duty_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleAdapter.ViewHolder holder, int position) {
        Item item = items.get(position);

        if (item.images != null)
            holder.image.setImageBitmap(item.images);

        if (item.state.contains(PeopleOnDutyState.TITLE)){
            holder.devider.setText("");
            return;
        }

        else if (item.state.contains(PeopleOnDutyState.ME))
            holder.markMe();
        else if (item.state.contains(PeopleOnDutyState.IN_PROGRESS))
            holder.markInProgress();
        else if (item.state.contains(PeopleOnDutyState.IN_FUTURE))
            holder.markInFuture();

        Person person = DAORequester.getPersonInPeopleOnDuty(item.people);

        holder.title.setText(person.getSurnameWithInitials());
        holder.from.setText(item.people.fromAsLocalDateTime().format(formatter));
        holder.to.setText(item.people.toAsLocalDateTime().format(formatter));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;
        public TextView from;
        public TextView to;
        public TextView devider;
        public View view;
        public ImageView image;

        @SuppressLint("ResourceType")
        public ViewHolder(View view) {
            super(view);
            this.view = view;

            //View byId = view.findViewById(23);

            title = view.findViewById(R.id.tvPeopleDutyName);
            devider = view.findViewById(R.id.tvDevider);
            from = view.findViewById(R.id.tvPeopleDutyStartTime);
            to = view.findViewById(R.id.tvPeopleDutyEndTime);
            to = view.findViewById(R.id.tvPeopleDutyEndTime);
            image = view.findViewById(R.id.ivGraphic);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null)
                listener.nodeClicked(getAbsoluteAdapterPosition());
        }

        public void markMe() {
            view.setBackgroundResource(R.drawable.side_lines_red);
        }

        public void markInProgress() {
            view.setBackgroundResource(R.drawable.side_lines_green);
        }

        public void markEnded() {
            view.setBackgroundResource(R.drawable.side_lines_grey);
        }

        public void markInFuture() {
            view.setBackgroundResource(R.drawable.side_lines_yellow);
        }
    }

    public static class Item {
        public PeopleOnDuty people;
        public Set<PeopleOnDutyState> state;
        public Bitmap images;

        public Item(PeopleOnDuty people, Set<PeopleOnDutyState> me) {
            this.people = people;
            state = me;
        }

        public Item(PeopleOnDuty people, Set<PeopleOnDutyState> state, Bitmap images) {
            this.people = people;
            this.state = state;
            this.images = images;
        }
    }

    public static class SimpleDivider extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        @SuppressLint("UseCompatLoadingForDrawables")
        public SimpleDivider(Context context) {
            mDivider = context.getResources().getDrawable(R.drawable.duty_recycler_devider);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, @NotNull RecyclerView.State state) {
            //divider padding give some padding whatever u want or disable
            int left = parent.getPaddingLeft() + 80;
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

    }
}

