package com.task.fbresult.ui.peoples_on_duty;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.task.fbresult.model.Duty;
import com.task.fbresult.model.GraphicResult;
import com.task.fbresult.util.DAORequester;
import com.task.fbresult.util.WebUtils;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class DutyFragmentViewModel extends ViewModel {

    private MutableLiveData<GraphicResult> graphic;

    public void loadData(Duty duty){
        Disposable graphics = WebUtils.getGraphics(65,240, DAORequester.getPeopleOnDuty(duty), successHandler, errorHandler);
    }

    public DutyFragmentViewModel() {
    }

    public MutableLiveData<GraphicResult> getGraphic(Duty duty) {
        if (graphic == null) {
            graphic = new MutableLiveData<>();
            loadData(duty);
        }
        return graphic;
    }

    Consumer<GraphicResult> successHandler = (result) ->{
        Log.d("myLog", "Downloading success: " + result);
        graphic.postValue(result);
    };



    Consumer<Throwable> errorHandler = (error) ->{
        Log.d("myLog", "Downloading failed: " + error);
        error.printStackTrace();
    };

}
