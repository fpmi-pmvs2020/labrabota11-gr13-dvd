package com.task.fbresult.ui.peoples_on_duty_stat;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.task.fbresult.model.Duty;
import com.task.fbresult.model.GraphicResult;
import com.task.fbresult.model.HourlyGraphicResult;
import com.task.fbresult.util.DAORequester;
import com.task.fbresult.util.WebUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


@RequiresApi(api = Build.VERSION_CODES.R)
public class DutyStatisticViewModel extends ViewModel {

    private HourlyGraphicResult allResult;
    private MutableLiveData<Map<Integer, List<Bitmap>>> hourlyResult;


    public void loadData(Duty duty) {
        Disposable graphics = WebUtils.getHourlyGraphic(65, 240, DAORequester.getPeopleOnDuty(duty), successHandler, errorHandler);
    }

    public DutyStatisticViewModel() {
    }

    public MutableLiveData<Map<Integer, List<Bitmap>>> getGraphic(Duty duty) {
        if (allResult == null) {
            allResult = new HourlyGraphicResult();
            hourlyResult = new MutableLiveData<>();
            loadData(duty);
        }
        return hourlyResult;
    }

    private final Consumer<HourlyGraphicResult> successHandler = (result) -> {
        Log.d("myLog", "Hourly downloading success: " + result);
        allResult = result;
        requestHourlyDate(0);
    };


    private final Consumer<Throwable> errorHandler = (error) -> {
        Log.d("myLog", "Hourly downloading failed: " + error);
        error.printStackTrace();
    };



    public void requestHourlyDate(int hourIndex) {
        if (allResult == null || allResult.timeMap == null || allResult.timeMap.size() <= hourIndex)
            return;
        Map<Integer, List<Bitmap>> collect = allResult.timeMap.get(hourIndex).stream()
                .collect(Collectors.groupingBy(
                        GraphicResult.Result::getPersonId,
                        Collectors.mapping(
                                GraphicResult.Result::getImage,
                                Collectors.toList()
                        )));

        collect.put(0, List.of(allResult.minuteRuleString()));
        hourlyResult.postValue(collect);
    }
}
