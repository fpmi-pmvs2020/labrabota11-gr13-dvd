package com.task.fbresult.db;

public class DBHelper{
    //region fields
    public static final String DB_NAME = "fb-result";
    public static final String DB_LOG = "myDbLogs";
    public static final String PER_ON_DUTY_TABLE =              "PeopleOnDuty";
    public static final String PER_ON_DUTY_ID_COLUMN =          "firebaseId";
    public static final String PER_ON_DUTY_PERSON_ID_COLUMN =   "personId";
    public static final String PER_ON_DUTY_DUTY_ID_COLUMN =     "dutyId";
    public static final String PER_ON_DUTY_FROM_COLUMN =        "from";
    public static final String PER_ON_DUTY_TO_COLUMN =          "to";

    public static final String PERSON_TABLE =                   "People";
    public static final String PERSON_ID_COLUMN =               "firebaseId";
    public static final String PERSON_LOGIN_COLUMN =            "login";
    public static final String PERSON_FIO_COLUMN =              "fio";
    public static final String PERSON_TEL_COLUMN =              "telephone";
    public static final String PERSON_ADDRESS_COLUMN =          "address";
    public static final String PERSON_BIRTH_COLUMN =            "birthDate";
    public static final String PERSON_ROLE_COLUMN =             "roleId";
    public static final String PERSON_IMAGE_COLUMN =             "image";

    public static final String DUTY_TABLE =                     "Duties";
    public static final String DUTY_ID_COLUMN =                 "firebaseId";
    public static final String DUTY_FROM_COLUMN =               "from";
    public static final String DUTY_TO_COLUMN =                 "to";
    public static final String DUTY_TYPE_ID_COLUMN =            "typeId";
    public static final String DUTY_MAX_PEOPLE_COLUMN =         "maxPeople";

    public static final String ROLE_TABLE =                     "Roles";
    public static final String ROLE_ID_COLUMN =                 "firebaseId";
    public static final String ROLE_NAME_COLUMN =               "name";

    public static final String TYPES_TABLE =                    "DutyTypes";
    public static final String TYPES_ID_COLUMN =                "firebaseId";
    public static final String TYPES_TITLE_COLUMN =             "title";

    public static final String MESSAGES_TABLE =                 "messages";
    public static final String MESSAGES_ID_COLUMN =             "firebaseId";
    public static final String MESSAGES_AUTHOR_COLUMN =         "authorId";
    public static final String MESSAGES_RECIPIENT_COLUMN =      "recipientId";
    public static final String MESSAGES_DUTY_ID_COLUMN =        "dutyId";

    //endregion fields
}
