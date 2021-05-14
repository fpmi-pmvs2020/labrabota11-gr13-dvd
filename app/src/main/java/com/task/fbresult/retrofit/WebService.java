package com.task.fbresult.retrofit;

import com.task.fbresult.model.AlertDTO;
import com.task.fbresult.model.AlertInputDTO;
import com.task.fbresult.model.GraphicResult;
import com.task.fbresult.model.HourlyGraphicResult;
import com.task.fbresult.model.PostModel;
import com.task.fbresult.model.AlertRegisterUserDTO;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface WebService {


    @POST("/notify/register")
    io.reactivex.Observable<Map<String, String>> registerUser(
            @Body AlertRegisterUserDTO registerUserDTO
    );


    @POST("/notify/send")
    Call<ResponseBody> postAlert(
            @Body AlertDTO alert
    );

    @POST("/notify/getAll")
    io.reactivex.Observable<AlertInputDTO> peekAlert(
            @Body Map<String, String> key
    );



    @POST("stripes")
    io.reactivex.Observable<GraphicResult> getFullGraphic(
            @Body PostModel model
    );


    @POST("stripesHourly")
    io.reactivex.Observable<HourlyGraphicResult> getHourlyGraphics(
            @Body PostModel list
    );


    static WebService create() {
        //"http://app-server.hopto.org:8081"
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://168.235.81.127:8081")
                .build();

        return retrofit.create(WebService.class);
    }

}
