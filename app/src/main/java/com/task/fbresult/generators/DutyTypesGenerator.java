package com.task.fbresult.generators;

import com.task.fbresult.model.DutyType;

import java.util.ArrayList;
import java.util.List;

public class DutyTypesGenerator {
    public static List<DutyType> generate(){
        List<DutyType> ans = new ArrayList<>();
        ans.add(new DutyType("ВЕЧЕРНЕЕ"));
        ans.add(new DutyType("ДНЕВНОЕ"));
        ans.add(new DutyType("НОЧНОЕ"));
        return ans;
    }
}
