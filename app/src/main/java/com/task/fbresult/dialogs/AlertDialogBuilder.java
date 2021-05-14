package com.task.fbresult.dialogs;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.Context;
import android.os.Build;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;

import com.task.fbresult.R;
import com.task.fbresult.model.AlertDTO;
import com.task.fbresult.model.AlertNotificationDTO;
import com.task.fbresult.model.Person;
import com.task.fbresult.util.FBUtils;
import com.task.fbresult.util.WebUtils;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

public class AlertDialogBuilder extends DialogBuilder {

    private SwitchCompat alertSwitch;
    private TextView tvAlertType;
    private Spinner spAlertType;
    private EditText etDescription;
    private String token;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    void setData(String[] values) {
        sendAlert();
//        CompletableFuture.runAsync(this::sendAlert);
    }


    public AlertDialog build(String title, String message, FieldsDisplay fieldsDisplay, String token) {
        this.token = token;
        return super.build(title, message, fieldsDisplay);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendAlert(){
        //todo send alert

        AlertDTO alertDTO = getAlertDTO();
        WebUtils.postAlert(alertDTO);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private AlertDTO getAlertDTO(){
        Person currentUserAsPerson = FBUtils.getCurrentUserAsPerson();

        String currentType = getCurrentType();
        String message = etDescription.getText().toString();

        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        String formattedString = ZonedDateTime.now().format(formatter);

        Log.d("formatted Date time ", formattedString);
        AlertNotificationDTO not = new AlertNotificationDTO(currentType,message,currentUserAsPerson.getFirebaseId(), formattedString);
        return new AlertDTO(token, not);
    }
    private String getCurrentType(){
        if(alertSwitch.isChecked()) return "";
        return (String)spAlertType.getSelectedItem();
    }
}
