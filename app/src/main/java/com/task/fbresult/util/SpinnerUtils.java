package com.task.fbresult.util;

import android.content.Context;
import android.os.Build;
import android.widget.ArrayAdapter;

import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.var;

public class SpinnerUtils {
    public static ArrayAdapter<String> getStringAdapterOf(Context context, List<String> values) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T> ArrayAdapter<String> getStringAdapterOf(Context context, List<T> values,
                                                              Function<? super T, String> mapper) {
        var items = values.stream()
                .map(mapper)
                .collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
}
