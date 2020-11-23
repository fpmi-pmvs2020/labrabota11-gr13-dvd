package com.task.fbresult.util;

import com.google.firebase.auth.FirebaseAuth;
import com.task.fbresult.db.dao.PersonDao;
import com.task.fbresult.model.Person;

public class FBUtils {
    public static Person getCurrentPerson(){
        String login = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String userQuery = String.format(PersonDao.GET_USER_WITH_LOGIN_QUERY,login);
        return new PersonDao().get(userQuery).get(0);
    }
}
