package com.task.fbresult.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.util.Base64;
import java.util.List;

public class GraphicResult implements Serializable {
    public List<Result> list;


    public static class Result implements Serializable {
        public String personId;
        public String fileString;

        public String getPersonId() {
            return personId;
        }

        public String getFileString() {
            return fileString;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public Bitmap getImage() {

            byte[] decode = Base64.getDecoder().decode(fileString);
            return BitmapFactory.decodeByteArray(decode, 0, decode.length);
        }
    }

}
