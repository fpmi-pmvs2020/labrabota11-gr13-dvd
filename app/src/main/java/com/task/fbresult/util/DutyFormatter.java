package com.task.fbresult.util;

import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.task.fbresult.R;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.DutyType;
import com.task.fbresult.model.Person;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class DutyFormatter {
    private final Duty duty;
    private final Resources resources;

    public DutyFormatter(Duty duty, Resources resources) {
        this.duty = duty;
        this.resources = resources;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getStartTime() {
        LocalTime localTime = duty.fromAsLocalDateTime().toLocalTime();
        return getFormattedTime(localTime);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getFinishTime() {
        LocalTime localTime = duty.toAsLocalDateTime().toLocalTime();
        return getFormattedTime(localTime);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDate() {
        LocalDate dutyDate = duty.fromAsLocalDateTime().toLocalDate();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM");
        return dutyDate.format(dateTimeFormatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getFormattedTime(LocalTime localTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return localTime.format(dateTimeFormatter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDaysLeftAsString() {
        DutyManager dutyManager = new DutyManager(duty);
        long daysBetween = dutyManager.getDaysLeft();
        if (daysBetween == 0)
            return resources.getString(R.string.today);
        else
            return String.format(resources.getString(R.string.days_left), daysBetween);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDayOfWeek() {
        LocalDateTime localDateTime = duty.fromAsLocalDateTime();
        Locale currentLocale = resources.getConfiguration().locale;
        return localDateTime.getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, currentLocale);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getPartnersAsString() {
        DutyManager dutyManager = new DutyManager(duty);
        List<Person> partners = dutyManager.getPartners();
        StringBuilder stringBuilder = new StringBuilder();
        for (Person partner : partners)
            stringBuilder.append(partner.getName() + " " + partner.getSurname() + ", ");
        if (stringBuilder.length() == 0)
            return resources.getString(R.string.no_partners);
        return stringBuilder.substring(0, stringBuilder.length() - 2);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getTitle() {
        DutyType type = DAORequester.getDutyType(duty);
        return type.getTitle();
    }

    public String getMaxPeople() {
        return String.valueOf(duty.getMaxPeople());
    }
}
