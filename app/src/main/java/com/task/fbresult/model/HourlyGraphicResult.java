package com.task.fbresult.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.util.Base64;
import java.util.List;

public class HourlyGraphicResult implements Serializable {
    public List<List<GraphicResult.Result >> timeMap;
    public String minuteRuleString;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Bitmap minuteRuleString() {
        byte[] decode = Base64.getDecoder().decode(minuteRuleString);
        return BitmapFactory.decodeByteArray(decode, 0, decode.length);
    }
}
