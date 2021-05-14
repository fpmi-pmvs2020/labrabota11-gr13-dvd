package com.task.fbresult.util;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.firebase.ui.auth.data.model.User;
import com.task.fbresult.model.AlertDTO;
import com.task.fbresult.model.AlertInputDTO;
import com.task.fbresult.model.AlertRegisterUserDTO;
import com.task.fbresult.model.GraphicResult;
import com.task.fbresult.model.HourlyGraphicResult;
import com.task.fbresult.model.PeopleOnDuty;
import com.task.fbresult.model.Person;
import com.task.fbresult.model.PostModel;
import com.task.fbresult.retrofit.WebService;

import java.util.List;
import java.util.Map;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    @RequiresApi(api = Build.VERSION_CODES.R)
    static public Disposable peekAlert(String token, Consumer<AlertInputDTO> result, Consumer<Throwable> error){
        Map<String, String> dto = Map.of("key", token);
        return getWebService().peekAlert(dto)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result, error);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    static public void postAlert(AlertDTO alertDTO){
        getWebService().postAlert(alertDTO)
        .enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("post alert ", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Log.d("post alert ", "fail");
            }
        });
        ;
    }


    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.O)
    static public Disposable registerUser(Person user, Consumer<Map<String,String>> result, Consumer<Throwable> error){
        AlertRegisterUserDTO dto = new AlertRegisterUserDTO(user.getFirebaseId());
        return getWebService().registerUser(dto)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result, error);
    }

}
