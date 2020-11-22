package com.task.fbresult.ui.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.task.fbresult.R;
import com.task.fbresult.ui.adapters.NodeListener;

public class DutyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView title;
    public TextView tag;
    public TextView from;
    public TextView to;
    public TextView max;
    public ImageView checkMark;
    NodeListener listener;

    public DutyViewHolder(View view, NodeListener listener) {
        super(view);
        this.listener = listener;

        title = view.findViewById(R.id.tvDutyTitle);
        tag = view.findViewById(R.id.tvDutyPartners);
        from = view.findViewById(R.id.tvDutyStartTime);
        to = view.findViewById(R.id.tvDutyEndTime);
        max = view.findViewById(R.id.tvDutyAmounts);
        checkMark = view.findViewById(R.id.imgViewCheckMark);

        view.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (listener != null)
            listener.nodeClicked(getAbsoluteAdapterPosition());
    }
}
