package com.task.fbresult.db.fbdao;

import com.task.fbresult.db.DBHelper;
import com.task.fbresult.model.DutyType;

public class FBDutyTypesDao extends FBDao<DutyType> {
    @Override
    String getTableName() {
        return DBHelper.TYPES_TABLE;
    }

    @Override
    Class<DutyType> getModelClass() {
        return DutyType.class;
    }

    @Override
    String getIdColumn() {
        return DBHelper.TYPES_ID_COLUMN;
    }
}
