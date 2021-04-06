package com.task.fbresult.ui.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.task.fbresult.R;
import com.task.fbresult.ui.adapters.NodeListener;

public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView senderFIO;

    public TextView fromTime;
    public TextView toTime;
    public TextView date;
    public ImageView checkMark;
    public ImageView checkMarkGreen;
    public ImageView crossMark;
    NodeListener listener;

    public MessageViewHolder(View view, NodeListener listener) {
        super(view);
        this.listener = listener;

        senderFIO = view.findViewById(R.id.tvMessageTittle);
        checkMark = view.findViewById(R.id.imgViewCheckMark);
        checkMarkGreen = view.findViewById(R.id.imgViewCheckMarkGreen);
        crossMark = view.findViewById(R.id.imgViewCrossMark);
        fromTime = view.findViewById(R.id.tvDutyStartTime);
        toTime = view.findViewById(R.id.tvDutyEndTime);
        date = view.findViewById(R.id.tvDutyDate);
        view.setOnClickListener(this);

    }

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void onClick(View v) {
        if (listener != null)
            listener.nodeClicked(getAbsoluteAdapterPosition());
    }
}
