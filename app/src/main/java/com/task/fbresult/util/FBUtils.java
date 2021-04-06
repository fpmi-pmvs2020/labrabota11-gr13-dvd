package com.task.fbresult.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.task.fbresult.db.DBHelper;
import com.task.fbresult.db.fbdao.ConstraintPair;
import com.task.fbresult.db.fbdao.ConstraintType;
import com.task.fbresult.db.fbdao.FBPersonDao;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.Person;

import java.util.HashMap;

import lombok.var;

public class FBUtils {
    public static Person getCurrentUserAsPerson() {
        String login = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        return new FBPersonDao().get(
                DBHelper.PERSON_LOGIN_COLUMN,
                new ConstraintPair(login, ConstraintType.EQUALS)
        ).get(0);
    }

    public static boolean personIsOnDuty(Person person, Duty duty){
        var persons = DAORequester.getPersonsOnDuty(duty);
        return persons.contains(person);
    }

    public static Query buildQueryWithConstraints(DatabaseReference reference, String parameterName, ConstraintPair constraintPair){
        var query = reference.orderByChild(parameterName);
            switch (constraintPair.getConstraintType()) {
                case EQUALS:
                    query = query.equalTo(constraintPair.getParamValue());
                    break;
                case LESS:
                    query = query.endBefore(constraintPair.getParamValue());
                    break;
                case GREATER:
                    query = query.startAfter(constraintPair.getParamValue());
                    break;
                case LESS_OR_EQUALS:
                    query = query.endAt(constraintPair.getParamValue());
                    break;
                case GREATER_OR_EQUALS:
                    query = query.startAt(constraintPair.getParamValue());
                    break;
                default:
                    throw new IllegalArgumentException("UNSUPPORTED CONSTRAINT");
            }
            return query;
    }
}
