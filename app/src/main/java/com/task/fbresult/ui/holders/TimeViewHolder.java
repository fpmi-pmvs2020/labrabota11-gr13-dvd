package com.task.fbresult.ui.holders;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.task.fbresult.R;

public class TimeViewHolder extends RecyclerView.ViewHolder {

    public TextView tvDate;

    public TimeViewHolder(View view) {
        super(view);
        tvDate = view.findViewById(R.id.tvDateView);
    }
}
