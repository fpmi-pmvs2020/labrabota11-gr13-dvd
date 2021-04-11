package com.task.fbresult.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.task.fbresult.db.fbdao.FBMessageDao;
import com.task.fbresult.db.fbdao.FBPersonDao;
import com.task.fbresult.model.DutyIntervalData;
import com.task.fbresult.model.MessageState;
import com.task.fbresult.model.MyMessage;
import com.task.fbresult.model.Person;

import java.util.List;
import java.util.stream.Collectors;

import lombok.var;

public class MessageUtils {
    public static boolean equalsMessageExists(MyMessage message) {
        var myMessages = DAORequester.getPersonToOtherMessages(FBUtils.getCurrentUserAsPerson());
        for (var myMessage : myMessages) {
            if (myMessage.equals(message)) {
                return true;
            }
        }
        return false;
    }

    public static boolean currentPersonIsAuthorOf(MyMessage myMessage) {
        var currentPerson = FBUtils.getCurrentUserAsPerson();
        var authorId = myMessage.getAuthorIntervalData().getPersonId();
        return currentPerson.getFirebaseId()
                .equals(authorId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void removeMessagesOfPersonOnExcept(DutyIntervalData dutyIntervalData,MyMessage addedMessage) {
        var person = new FBPersonDao().getWithId(dutyIntervalData.getPersonId());
        var fbMessageDao = new FBMessageDao();
        var neededMessages = getSentMessagesFor(dutyIntervalData, person);
        neededMessages.addAll(getIncomeMessageFor(dutyIntervalData, person));
        neededMessages.forEach(myMessage -> {
            if(!myMessage.equals(addedMessage)) {
                myMessage.setMessageState(MessageState.DECLINED);
                fbMessageDao.update(myMessage);
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private static List<MyMessage> getSentMessagesFor(DutyIntervalData dutyIntervalData, Person person) {
        var authorMessages = DAORequester.getPersonToOtherMessages(person);
        return authorMessages.stream()
                .filter(myMessage -> messageIsForInterval(myMessage,dutyIntervalData,true))
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static List<MyMessage> getIncomeMessageFor(DutyIntervalData dutyIntervalData, Person person) {
        var incomeMessages = DAORequester.getPersonIncomingMessages(person);
        return incomeMessages.stream()
                .filter(message -> messageIsForInterval(message, dutyIntervalData, false))
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static boolean messageIsForInterval(MyMessage myMessage, DutyIntervalData dutyIntervalData, boolean isAuthor) {
        var messageDutyData = isAuthor ? myMessage.getAuthorIntervalData() : myMessage.getRecipientIntervalData();
        var messageInterval = LocalDateTimeInterval.of(messageDutyData);
        var dutyInterval = LocalDateTimeInterval.of(dutyIntervalData);
        return messageInterval.intersects(dutyInterval);
    }
}
