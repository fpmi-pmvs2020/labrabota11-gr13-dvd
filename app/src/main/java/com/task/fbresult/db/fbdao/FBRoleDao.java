package com.task.fbresult.db.fbdao;

import com.task.fbresult.db.DBHelper;
import com.task.fbresult.model.Role;

public class FBRoleDao extends FBDao<Role> {
    @Override
    String getTableName() {
        return DBHelper.ROLE_TABLE;
    }

    @Override
    Class<Role> getModelClass() {
        return Role.class;
    }

    @Override
    String getIdColumn() {
        return DBHelper.ROLE_ID_COLUMN;
    }
}
