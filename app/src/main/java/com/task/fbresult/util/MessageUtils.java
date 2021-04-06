package com.task.fbresult.util;

import com.task.fbresult.model.MyMessage;

import lombok.var;

public class MessageUtils {
    public static boolean equalsMessageExists(MyMessage message){
        var myMessages = DAORequester.getPersonToOtherMessages(FBUtils.getCurrentUserAsPerson());
        for(var myMessage:myMessages){
            if(myMessage.equals(message)){
                return true;
            }
        }
        return false;
    }
}
