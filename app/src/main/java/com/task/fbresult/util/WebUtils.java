package com.task.fbresult.util;

import android.annotation.SuppressLint;

import com.task.fbresult.model.GraphicResult;
import com.task.fbresult.model.PeopleOnDuty;
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

    @SuppressLint("CheckResult")
    static public Disposable getGraphics(int height, int width, List<PeopleOnDuty> list, Consumer<GraphicResult> result, Consumer<Throwable> error ) {
        return getWebService().getFullGraphic(new PostModel(width,  height, list))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result, error);
    }
}
