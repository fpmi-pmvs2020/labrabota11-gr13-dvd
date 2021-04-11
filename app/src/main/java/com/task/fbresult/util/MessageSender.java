package com.task.fbresult.util;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.task.fbresult.R;
import com.task.fbresult.db.fbdao.FBMessageDao;
import com.task.fbresult.model.DutyIntervalData;
import com.task.fbresult.model.MyMessage;
import com.task.fbresult.model.PeopleOnDuty;

import lombok.var;

public class MessageSender {
    private final PeopleOnDuty authorOnDuty;
    private PeopleOnDuty goalPeopleOnDuty = null;
    private final Context context;

    private final String recipientId;

    public MessageSender(Context context, PeopleOnDuty authorOnDuty, PeopleOnDuty goalPeopleOnDuty){
        this.authorOnDuty = authorOnDuty;
        this.goalPeopleOnDuty = goalPeopleOnDuty;
        recipientId = goalPeopleOnDuty.getPersonId();
        this.context = context;
    }

    public MessageSender(Context context, PeopleOnDuty authorOnDuty, String recipientId){
        this.authorOnDuty = authorOnDuty;
        this.recipientId = recipientId;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void tryToSendMessageWith(LocalDateTimeInterval authorDutyInterval,
                                     LocalDateTimeInterval otherDutyInterval) throws Exception{
            DutyIntervalData otherIntervalData;
            if (goalPeopleOnDuty != null) {
                checkIntervals(authorDutyInterval, otherDutyInterval);
                otherIntervalData = DutyIntervalData.of(goalPeopleOnDuty, otherDutyInterval);
            }else{
                checkGoalPersonOnInterval(authorDutyInterval);
                otherIntervalData = DutyIntervalData.of(recipientId);
            }

            DutyIntervalData myIntervalData = DutyIntervalData.of(authorOnDuty, authorDutyInterval);
            var message = new MyMessage(myIntervalData, otherIntervalData);
            if (MessageUtils.equalsMessageExists(message))
                throw new Exception(context.getString(R.string.message_already_sent));
            sendMessage(message);
    }

    private void sendMessage(MyMessage message) {
        FBMessageDao dao = new FBMessageDao();
        dao.save(message);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkIntervals(LocalDateTimeInterval myDutyInterval,
                                LocalDateTimeInterval otherDutyInterval) throws Exception {
        if (myDutyInterval.getHoursBetween() != otherDutyInterval.getHoursBetween()) {
            throw new Exception(context.getString(R.string.not_equals_interval_length));
        }
        if (DutyUtils.alreadyWorksOnThatTime(authorOnDuty.getPersonId(), otherDutyInterval, goalPeopleOnDuty)) {
            throw new Exception(context.getString(R.string.you_work_on_the_same_time));
        }
        checkGoalPersonOnInterval(myDutyInterval);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkGoalPersonOnInterval(LocalDateTimeInterval localDateTimeInterval) throws Exception {
        if (DutyUtils.alreadyWorksOnThatTime(recipientId, localDateTimeInterval, authorOnDuty)) {
            throw new Exception(context.getString(R.string.you_work_on_the_same_time));
        }
    }


}
