package com.task.fbresult.generators;

import java.util.Random;

public class UsersGenerator{
    private final static int namesNumber = 30;
    private final static String[] names = {
            "Дмитрий","Иван","Петр","Виктор","Егор","Даниил","Михаил","Илья"
    };

    private final static String[] surnames = {
            "Иванов","Петров","Сидоров","Смирненко","Иванчук","Каспаров","Мерзоян"
    };

    private final static String[] phones = {
            "+375111111111", "+375222222222","+375333333333","+375444444444"
    };

    public static String[][] generate() {
        String[]result = new String[namesNumber];
        Random random = new Random();
        for(int i = 0; i< namesNumber; i++){
            int nameIndex = random.nextInt(names.length);
            int surnameIndex = random.nextInt(surnames.length);
            result[i] = names[nameIndex]+" "+surnames[surnameIndex];
        }
        return new String[][]{result};
    }
}
