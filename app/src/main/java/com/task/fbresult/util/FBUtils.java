package com.task.fbresult.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Query;
import com.task.fbresult.db.DBHelper;
import com.task.fbresult.db.fbdao.ConstraintPair;
import com.task.fbresult.db.fbdao.ConstraintType;
import com.task.fbresult.db.fbdao.FBPersonDao;
import com.task.fbresult.model.Person;

import java.util.HashMap;

import lombok.var;

public class FBUtils {
    public static Person getCurrentUserAsPerson() {
        String login = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        var constraints = new HashMap<String,ConstraintPair>();
        constraints.put(DBHelper.PERSON_LOGIN_COLUMN,new ConstraintPair(login, ConstraintType.EQUALS));
        return new FBPersonDao().get(constraints).get(0);
    }

    public static Query buildQueryWithConstraints(Query query, String parameterName, ConstraintPair constraintPair){
            query = query.orderByChild(parameterName);
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
