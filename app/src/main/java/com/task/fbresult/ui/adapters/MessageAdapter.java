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
import com.task.fbresult.db.fbdao.FBDutyDao;
import com.task.fbresult.db.fbdao.FBPeopleOnDutyDao;
import com.task.fbresult.db.fbdao.FBPersonDao;
import com.task.fbresult.model.DutyIntervalData;
import com.task.fbresult.model.MyMessage;
import com.task.fbresult.model.PeopleOnDuty;
import com.task.fbresult.model.Person;
import com.task.fbresult.ui.holders.MessageViewHolder;
import com.task.fbresult.util.FBUtils;
import com.task.fbresult.util.MessageUtils;

import java.util.List;

import lombok.var;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected final LayoutInflater inflater;
    private final NodeListener listener;
    public List<MyMessage> items;

    public MessageAdapter(Context context, @NonNull List<MyMessage> items,
                          @Nullable NodeListener listener) {
        inflater = LayoutInflater.from(context);
        this.items = items;
        this.listener = listener;
    }

    public List<MyMessage> getItems() {
        return items;
    }

    public void setItems(List<MyMessage> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.message_item_main, parent, false);
        return new MessageViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyMessage myMessage = items.get(position);
        MessageViewHolder holder = (MessageViewHolder) viewHolder;
        Person sender = getOtherPersonForMessage(myMessage);

        holder.senderFIO.setText(sender.getFio());
        DutyIntervalData messageTimeInfo;
        if(myMessage.getRecipientIntervalData().getPeopleOnDutyId()!=null) {
            messageTimeInfo = myMessage.getRecipientIntervalData();
        }else{
            messageTimeInfo = myMessage.getAuthorIntervalData();
        }

        holder.toTime.setText(messageTimeInfo.getTo().split("T")[1]);
        holder.fromTime.setText(messageTimeInfo.getFrom().split("T")[1]);
        holder.date.setText(messageTimeInfo.getFrom().split("T")[0]);

        hideMarks(holder);

        switch (myMessage.getMessageState()){
            case READ:
                holder.checkMark.setVisibility(View.VISIBLE);
                break;
            case ACCEPTED:
                setAccepted(holder);
                break;
            case DECLINED:
                setRefused(holder);
        }

    }

    private PeopleOnDuty getPeopleOnDutyFromIntervalData(DutyIntervalData dutyIntervalData){
        return new FBPeopleOnDutyDao().getWithId(dutyIntervalData.getPeopleOnDutyId());
    }

    private Person getOtherPersonForMessage(MyMessage myMessage){
        if(MessageUtils.currentPersonIsAuthorOf(myMessage))
            return getPersonFromDutyIntervalData(myMessage.getRecipientIntervalData());
        else
            return getPersonFromDutyIntervalData(myMessage.getAuthorIntervalData());
    }

    private Person getPersonFromDutyIntervalData(DutyIntervalData dutyIntervalData){
        return new FBPersonDao().getWithId(dutyIntervalData.getPersonId());
    }

    private void hideMarks(MessageViewHolder holder){
        holder.checkMark.setVisibility(View.INVISIBLE);
        holder.checkMarkGreen.setVisibility(View.INVISIBLE);
        holder.crossMark.setVisibility(View.INVISIBLE);
    }

    private void setAccepted(MessageViewHolder holder) {
        holder.checkMark.setVisibility(View.INVISIBLE);
        holder.checkMarkGreen.setVisibility(View.VISIBLE);
    }

    private void setRefused(MessageViewHolder holder) {
        holder.checkMark.setVisibility(View.INVISIBLE);
        holder.crossMark.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
