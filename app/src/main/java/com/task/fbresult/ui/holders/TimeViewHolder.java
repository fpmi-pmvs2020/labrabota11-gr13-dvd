package com.task.fbresult.ui.holders;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.task.fbresult.R;

public class TimeViewHolder extends RecyclerView.ViewHolder {

    public TextView tvData;

    public TimeViewHolder(View view,int tvId) {
        super(view);
        tvData = view.findViewById(tvId);
    }
}
