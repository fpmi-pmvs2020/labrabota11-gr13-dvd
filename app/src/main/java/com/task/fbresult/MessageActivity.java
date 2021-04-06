package com.task.fbresult;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.task.fbresult.model.MessageState;
import com.task.fbresult.model.MyMessage;

public class MessageActivity extends AppCompatActivity {

    public static final String MESSAGE_PARAMETERS = "message_key";
    private MyMessage message;
    private TextView tvMessageStatus;
    private TextView tvReceiverTime;
    private TextView tvReceiverFio;
    private TextView tvSenderTime;
    private TextView tvSenderFio;
    private TextView tvSenderDutyTime;
    private TextView tvSenderDutyType;
    private FloatingActionButton acceptBt;
    private FloatingActionButton declineBt;

    public static void getInstance(MyMessage message, Context context) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(MessageActivity.MESSAGE_PARAMETERS, message);
        context.startActivity(new Intent(context, MessageActivity.class).putExtras(bundle));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Bundle extras = getIntent().getExtras();
        message = (MyMessage) extras.getSerializable(MessageActivity.MESSAGE_PARAMETERS);

        Toolbar toolbar = findViewById(R.id.message_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        markAsChecked();
        getUI();
        initUI();
        tryToUpdateMessageStatusUI();
    }

    private void markAsChecked() {
        if (message.getMessageState() == MessageState.SENT)
            message.setMessageState(MessageState.READ);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void confirmExchange(View view) {
        if (message.getMessageState() == MessageState.READ)
            showConfirmDialog(getString(R.string.confirm_warning),
                    getString(R.string.confirm_warning_text),
                    (dialog, button) -> {
                        updateMessageStatus(MessageState.ACCEPTED);
                        tryToUpdateMessageStatusUI();
                    });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void refuseExchange(View view) {
        if (message.getMessageState() == MessageState.READ) {
            showConfirmDialog(getString(R.string.confirm_warning),
                    getString(R.string.confirm_warning_text),
                    (dialog, button) -> {
                        updateMessageStatus(MessageState.DECLINED);
                        tryToUpdateMessageStatusUI();
                    });
        }

    }

    private void updateMessageStatus(MessageState status){
        message.setMessageState(status);
        //todo database saving
    }

    private void showConfirmDialog(String title, String message, DialogInterface.OnClickListener positiveListener) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, positiveListener)
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                }).show();
    }

    private void setButtonVisibility(boolean isVisible) {
        int value;
        if (isVisible) {
            value = View.VISIBLE;
        } else {
            value = View.INVISIBLE;
        }

        acceptBt.setEnabled(isVisible);
        declineBt.setEnabled(isVisible);
        acceptBt.setVisibility(value);
        declineBt.setVisibility(value);
    }

    private void getUI() {
        tvSenderDutyType = findViewById(R.id.dutySenderType);
        tvSenderDutyTime = findViewById(R.id.dutySenderTime);
        tvSenderFio = findViewById(R.id.message_sender_fio);
        tvSenderTime = findViewById(R.id.message_sender_duty_time);
        tvReceiverFio = findViewById(R.id.message_receiver_fio);
        tvReceiverTime = findViewById(R.id.message_receiver_duty_time);
        tvMessageStatus = findViewById(R.id.message_status);
        acceptBt = findViewById(R.id.accept_message_button);
        declineBt = findViewById(R.id.decline_message_button);

        acceptBt.setOnClickListener(this::confirmExchange);
        declineBt.setOnClickListener(this::refuseExchange);
    }

    private void initUI() {
        //todo after updating message
        tvSenderDutyType.setText("");
        tvSenderDutyTime.setText("");
        tvSenderFio.setText("");
        tvSenderTime.setText("");
        tvReceiverFio.setText("");
        tvReceiverTime.setText("");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void tryToUpdateMessageStatusUI() {
        if (isMessageForMe()) {
            updateMessageStatusUI();
        } else {
            setButtonVisibility(false);
        }
    }

    private boolean isMessageForMe() {
        //TODO after updating message
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateMessageStatusUI() {

        switch (message.getMessageState()) {
            case READ:
                tvMessageStatus.setText(R.string.message_status_wait);
                break;
            case ACCEPTED:
                setButtonVisibility(false);
                tvMessageStatus.setText(R.string.message_status_confirm);
                tvMessageStatus.setTextColor(getColor(R.color.green));
                break;
            case DECLINED:
                setButtonVisibility(false);
                tvMessageStatus.setText(R.string.message_status_declined);
                tvMessageStatus.setTextColor(getColor(R.color.red));
                break;
        }
    }



    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                System.out.println("");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.duty, menu);
        return true;
    }


}
