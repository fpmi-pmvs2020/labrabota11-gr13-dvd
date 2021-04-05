package com.task.fbresult.db.fbdao;

import com.task.fbresult.db.DBHelper;
import com.task.fbresult.model.MyMessage;

public class FBMessageDao extends FBDao<MyMessage>{
    @Override
    String getTableName() {
        return DBHelper.MESSAGES_TABLE;
    }

    @Override
    Class<MyMessage> getModelClass() {
        return MyMessage.class;
    }

    @Override
    String getIdColumn() {
        return DBHelper.MESSAGES_ID_COLUMN;
    }
}
