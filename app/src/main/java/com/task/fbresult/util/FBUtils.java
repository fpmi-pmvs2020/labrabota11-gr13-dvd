package com.task.fbresult.util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.task.fbresult.db.DBHelper;
import com.task.fbresult.db.fbdao.ConstraintPair;
import com.task.fbresult.db.fbdao.ConstraintType;
import com.task.fbresult.db.fbdao.FBPeopleOnDutyDao;
import com.task.fbresult.db.fbdao.FBPersonDao;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.DutyIntervalData;
import com.task.fbresult.model.PeopleOnDuty;
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

    public static boolean personIsOnDuty(Person person, Duty duty) {
        var persons = DAORequester.getPersonsOnDuty(duty);
        return persons.contains(person);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void saveNewPersonOnDutyInterval(DutyIntervalData dutyIntervalData, String newPersonId) {
        if (dutyIntervalData.getPeopleOnDutyId() != null) {
            var authorOnDuty = new FBPeopleOnDutyDao().getWithId(dutyIntervalData.getPeopleOnDutyId());
            var newAuthorOnDuty = new PeopleOnDuty(
                    newPersonId,
                    authorOnDuty.getDutyId(),
                    LocalDateTimeHelper.parseString(dutyIntervalData.getFrom()),
                    LocalDateTimeHelper.parseString(dutyIntervalData.getTo())
            );
            new FBPeopleOnDutyDao().save(newAuthorOnDuty);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void removeFromPeopleOnDutyInterval(DutyIntervalData dutyIntervalData) {
        var peopleOnDutyId = dutyIntervalData.getPeopleOnDutyId();
        FBPeopleOnDutyDao peopleOnDutyDao = new FBPeopleOnDutyDao();
        var peopleOnDuty = peopleOnDutyDao.getWithId(peopleOnDutyId);
        var basicStartTime = peopleOnDuty.fromAsLocalDateTime();
        var basicEndTime = peopleOnDuty.toAsLocalDateTime();
        var newFrom = LocalDateTimeHelper.parseString(dutyIntervalData.getFrom()).minusMinutes(1);
        var newTo = LocalDateTimeHelper.parseString(dutyIntervalData.getTo());
        if (!basicStartTime.isEqual(newFrom))
            createNewPeopleOnDuty(
                    peopleOnDuty.getFirebaseId(),
                    peopleOnDuty.getDutyId(),
                    peopleOnDuty.getFrom(),
                    dutyIntervalData.getFrom());

        if (!basicEndTime.isEqual(newTo))
            createNewPeopleOnDuty(
                    peopleOnDuty.getFirebaseId(),
                    peopleOnDuty.getDutyId(),
                    newTo.toString(),
                    peopleOnDuty.getTo()
            );

        peopleOnDutyDao.delete(peopleOnDuty);
    }

    public static void createNewPeopleOnDuty(String peopleOnDutyId, String dutyId,
                                             String newFrom, String newTo) {
        var peopleOnDuty = new FBPeopleOnDutyDao().getWithId(peopleOnDutyId);
        var tempPeopleOnDuty = new PeopleOnDuty();
        tempPeopleOnDuty.setDutyId(dutyId);
        tempPeopleOnDuty.setFrom(newFrom);
        tempPeopleOnDuty.setTo(newTo);
        tempPeopleOnDuty.setPersonId(peopleOnDuty.getPersonId());
        new FBPeopleOnDutyDao().save(tempPeopleOnDuty);
    }

    public static Query buildQueryWithConstraints(DatabaseReference reference, String parameterName, ConstraintPair constraintPair) {
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
