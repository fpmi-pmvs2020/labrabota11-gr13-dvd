package com.task.fbresult.generators;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.task.fbresult.db.dao.RoleDao;
import com.task.fbresult.model.Person;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersonGenerator {
    private final static int namesNumber = 6;
    private final static String[] names = {
            "Дмитрий", "Иван", "Петр", "Виктор", "Егор", "Даниил", "Михаил", "Илья"
    };

    private final static String[] surnames = {
            "Иванов", "Петров", "Сидоров", "Смирненко", "Иванчук", "Каспаров", "Мерзоян"
    };

    private final static String[] patronimic = {
            "Иванов", "Петров", "Сидоров", "Смирненко", "Иванчук", "Каспаров", "Мерзоян"
    };


    @SuppressLint("NewApi")
    public static List<Person> generate() {

        List<Person> ans = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < namesNumber; i++) {

            String tel = "+375"+Stream.generate(() -> random.nextInt(10)).limit(9).map(String::valueOf).collect(Collectors.joining());
            Person person = new Person(
                    "admin" + (i == 0 ? "" : i) + "@mail.com",
                    surnames[random.nextInt(surnames.length)] + " " +
                            names[random.nextInt(names.length)] + " " +
                            patronimic[random.nextInt(patronimic.length)],
                    tel,
                    "minsk",
                    LocalDate.now(),
                    new RoleDao().get(RoleDao.GET_ALL_QUERY).stream().findAny().get().getId()
            );
            ans.add(person);
        }
        return ans;
    }
}
