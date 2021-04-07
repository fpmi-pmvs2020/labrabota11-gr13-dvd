package com.task.fbresult.util;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.task.fbresult.model.AlertDTO;
import com.task.fbresult.model.GraphicResult;
import com.task.fbresult.model.HourlyGraphicResult;
import com.task.fbresult.model.PeopleOnDuty;
import com.task.fbresult.model.Person;
import com.task.fbresult.model.PostModel;
import com.task.fbresult.retrofit.WebService;

import java.util.List;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WebUtils {

    static public WebService getWebService(){
        return WebService.create();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("CheckResult")
    static public Disposable getGraphics(int height, int width, List<PeopleOnDuty> list, Consumer<GraphicResult> result, Consumer<Throwable> error ) {
        return getWebService().getFullGraphic(new PostModel(width,  height, list))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result, error);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    static public Disposable getHourlyGraphic(int height, int width, List<PeopleOnDuty>list, Consumer<HourlyGraphicResult> result, Consumer<Throwable> error){
        return getWebService().getHourlyGraphics(new PostModel(width,  height, list))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result, error);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    static public Disposable peekAlert(Person forPerson, Consumer<List<AlertDTO>> result, Consumer<Throwable> error){
        return getWebService().peekAlert(forPerson.getFirebaseId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result, error);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    static public void postAlert(AlertDTO alertDTO){
        getWebService().postAlert(alertDTO);
    }


}
