package com.task.fbresult.db.fbdao;

import com.task.fbresult.db.DBHelper;
import com.task.fbresult.model.Person;

public class FBPersonDao extends FBDao<Person>{

    @Override
    String getTableName() {
        return DBHelper.PERSON_TABLE;
    }

    @Override
    Class<Person> getModelClass() {
        return Person.class;
    }

    @Override
    String getIdColumn() {
        return DBHelper.PERSON_ID_COLUMN;
    }
}
