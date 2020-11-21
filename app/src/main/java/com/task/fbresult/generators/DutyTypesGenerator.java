package com.task.fbresult.generators;

import com.task.fbresult.model.DutyTypes;

import java.util.ArrayList;
import java.util.List;

public class DutyTypesGenerator {
    public static List<DutyTypes> generate(){
        List<DutyTypes> ans = new ArrayList<>();
        ans.add(new DutyTypes("EVENING_DUTIES"));
        ans.add(new DutyTypes("SETTLEMENT_DUTIES"));
        ans.add(new DutyTypes("NIGHT_DUTIES"));
        return ans;
    }
}
