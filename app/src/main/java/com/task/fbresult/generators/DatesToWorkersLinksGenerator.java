package com.task.fbresult.generators;

import java.util.Random;

public class DatesToWorkersLinksGenerator{
    private static int datesNumber = 40;
    private static int workersNumber = 30;

    public static String[][]generate(){
        Random random = new Random();
        String[][]result = new String[2][datesNumber*2];
        for(int i = 0;i<datesNumber*2;i+=2){
            int firstWorkerIdx = random.nextInt(workersNumber);
            int secondWorkerIdx = random.nextInt(workersNumber);
            result[0][i] = String.valueOf(i/2);
            result[0][i+1] = result[0][i];
            result[1][i] = String.valueOf(firstWorkerIdx);
            result[1][i+1] = String.valueOf(secondWorkerIdx);
        }
        return result;
    }

}
