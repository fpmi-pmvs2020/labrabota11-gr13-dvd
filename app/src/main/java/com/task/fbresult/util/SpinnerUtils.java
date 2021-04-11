package com.task.fbresult.util;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class SpinnerUtils {
    public static ArrayAdapter<String>getStringAdapterOf(Context context, List<String> values){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
}
