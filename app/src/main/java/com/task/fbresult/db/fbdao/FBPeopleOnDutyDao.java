package com.task.fbresult.db.fbdao;

import com.task.fbresult.db.DBHelper;
import com.task.fbresult.model.PeopleOnDuty;

public class FBPeopleOnDutyDao extends FBDao<PeopleOnDuty> {
    @Override
    String getTableName() {
        return DBHelper.PER_ON_DUTY_TABLE;
    }

    @Override
    Class<PeopleOnDuty> getModelClass() {
        return PeopleOnDuty.class;
    }

    @Override
    String getIdColumn() {
        return DBHelper.PER_ON_DUTY_ID_COLUMN;
    }
}
