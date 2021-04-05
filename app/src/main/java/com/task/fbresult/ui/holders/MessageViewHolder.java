package com.task.fbresult.ui.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.task.fbresult.R;
import com.task.fbresult.ui.adapters.NodeListener;

public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView title;
    public TextView author;
    public TextView recipient;
    public TextView from;
    public TextView to;
    public TextView date;
    public ImageView checkMark;
    NodeListener listener;

    public MessageViewHolder(View view, NodeListener listener) {
        super(view);
        this.listener = listener;

//        title = view.findViewById(R.id.tvMessageTitle);
//        author = view.findViewById(R.id.tvMessageAuthor);
//        recipient = view.findViewById(R.id.tvMessageRecipient);
//        from = view.findViewById(R.id.tvMessageStartTime);
//        to = view.findViewById(R.id.tvMessageEndTime);
//        date = view.findViewById(R.id.tvMessageDate);
//        checkMark = view.findViewById(R.id.imgViewMessageCheckMark);
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
