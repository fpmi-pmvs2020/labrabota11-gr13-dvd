package com.task.fbresult.dialogs;

import android.content.Context;
import android.os.Build;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;

import com.task.fbresult.R;
import com.task.fbresult.model.AlertDTO;
import com.task.fbresult.model.Person;
import com.task.fbresult.util.FBUtils;

import java.util.concurrent.CompletableFuture;

public class AlertDialogBuilder extends DialogBuilder {

    private SwitchCompat alertSwitch;
    private TextView tvAlertType;
    private Spinner spAlertType;
    private EditText etDescription;

    public AlertDialogBuilder(Context context) {
        super(context);
    }

    @Override
    View findMainWindow() {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.alert_dialog, null);
    }

    @Override
    void checkAndSetFields() {
        getUI();
    }

    private void getUI(){
        alertSwitch = mainWindow.findViewById(R.id.alertSwitch);
        tvAlertType = mainWindow.findViewById(R.id.tvAlertType);
        spAlertType = mainWindow.findViewById(R.id.spAlertType);
        etDescription = mainWindow.findViewById(R.id.editTextDescription);
        alertSwitch.setOnCheckedChangeListener((bt, isChecked) -> setEnableList(!isChecked));
    }

    void setEnableList(boolean isEnabled){
        if(isEnabled){
            spAlertType.setVisibility(View.VISIBLE);
            tvAlertType.setVisibility(View.VISIBLE);
            alertSwitch.setText(R.string.alert_from_list);
        }else{
            tvAlertType.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            tvAlertType.setLines(3);
            spAlertType.setVisibility(View.INVISIBLE);
            tvAlertType.setVisibility(View.INVISIBLE);
            alertSwitch.setText(R.string.alert_custom);

        }
        spAlertType.setEnabled(isEnabled);
        tvAlertType.setEnabled(isEnabled);
    }

    @Override
    String[] getFieldValues() throws Exception {
        //ignored
        return new String[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    void setData(String[] values) {
        CompletableFuture.runAsync(this::sendAlert);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendAlert(){
        //todo send alert

//        AlertDTO alertDTO = getAlertDTO();
//        WebUtils.postAlert(alertDTO);
    }

    private AlertDTO getAlertDTO(){
        Person currentUserAsPerson = FBUtils.getCurrentUserAsPerson();
        String currentType = getCurrentType();
        String message = etDescription.getText().toString();
        return new AlertDTO(currentType, message, currentUserAsPerson.getFirebaseId());
    }
    private String getCurrentType(){
        if(alertSwitch.isChecked()) return "";
        return (String)spAlertType.getSelectedItem();
    }
}
