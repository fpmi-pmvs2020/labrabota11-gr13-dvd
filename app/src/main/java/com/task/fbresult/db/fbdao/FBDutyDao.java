package com.task.fbresult.db.fbdao;

import com.task.fbresult.db.DBHelper;
import com.task.fbresult.model.Duty;

public class FBDutyDao extends FBDao<Duty> {
    @Override
    String getTableName() {
        return DBHelper.DUTY_TABLE;
    }

    @Override
    Class<Duty> getModelClass() {
        return Duty.class;
    }

    @Override
    String getIdColumn() {
        return DBHelper.DUTY_ID_COLUMN;
    }
}
