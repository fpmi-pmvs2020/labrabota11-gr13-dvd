package com.task.fbresult.db;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.task.fbresult.db.fbdao.FBDutyDao;
import com.task.fbresult.db.fbdao.FBDutyTypesDao;
import com.task.fbresult.db.fbdao.FBPeopleOnDutyDao;
import com.task.fbresult.db.fbdao.FBPersonDao;
import com.task.fbresult.db.fbdao.FBRoleDao;
import com.task.fbresult.generators.DutyGenerator;
import com.task.fbresult.generators.DutyTypesGenerator;
import com.task.fbresult.generators.PeopleOnDutyGenerator;
import com.task.fbresult.generators.PersonGenerator;
import com.task.fbresult.generators.RoleGenerator;

public class DBFillers {


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void fillData() {
        FBDutyDao dutyDao = new FBDutyDao();
        FBDutyTypesDao dutyTypesDao = new FBDutyTypesDao();
        FBPeopleOnDutyDao peopleOnDutyDao = new FBPeopleOnDutyDao();
        FBPersonDao personDao = new FBPersonDao();
        FBRoleDao roleDao = new FBRoleDao();

        dutyDao.clean();
        dutyTypesDao.clean();
        peopleOnDutyDao.clean();
        personDao.clean();
        roleDao.clean();

        RoleGenerator.generate().forEach(roleDao::save);
        DutyTypesGenerator.generate().forEach(dutyTypesDao::save);
        PersonGenerator.generate().forEach(personDao::save);
        DutyGenerator.generate().forEach(dutyDao::save);
        PeopleOnDutyGenerator.generate().forEach(peopleOnDutyDao::save);
        Log.d("DBFiller","db was filled!");
    }
}
