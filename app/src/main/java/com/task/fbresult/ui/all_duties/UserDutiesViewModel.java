package com.task.fbresult.ui.all_duties;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserDutiesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UserDutiesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}