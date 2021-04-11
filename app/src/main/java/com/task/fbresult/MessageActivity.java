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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.task.fbresult.db.fbdao.FBMessageDao;
import com.task.fbresult.model.DutyIntervalData;
import com.task.fbresult.model.MessageState;
import com.task.fbresult.model.MyMessage;
import com.task.fbresult.util.IntervalViewGenerator;
import com.task.fbresult.util.MessageUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.var;

import static com.task.fbresult.util.FBUtils.removeFromPeopleOnDutyInterval;
import static com.task.fbresult.util.FBUtils.saveNewPersonOnDutyInterval;
import static com.task.fbresult.util.MessageUtils.removeMessagesOfPersonOnExcept;

public class MessageActivity extends AppCompatActivity {

    public static final String MESSAGE_PARAMETERS = "message_key";
    private MyMessage message;
    private TextView tvMessageStatus;
    private TextView tvMessageActivityTitle;
    private FloatingActionButton acceptBt;
    private FloatingActionButton declineBt;


    public static void getInstance(MyMessage message, Context context) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(MessageActivity.MESSAGE_PARAMETERS, message);
        context.startActivity(new Intent(context, MessageActivity.class).putExtras(bundle));
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
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

        if (!MessageUtils.currentPersonIsAuthorOf(message))
            markAsChecked();

        getUI();
        initUI();
        tryToUpdateMessageStatusUI();
    }

    private void markAsChecked() {
        if (message.getMessageState() == MessageState.SENT)
            message.setMessageState(MessageState.READ);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void confirmExchange(View view) {
        if (message.getMessageState() == MessageState.READ)
            showConfirmDialog(getString(R.string.confirm_warning),
                    getString(R.string.confirm_warning_text),
                    (dialog, button) -> {
                        updateMessageStatus(MessageState.ACCEPTED);
                        configureChangesInDB();
                        tryToUpdateMessageStatusUI();
                    });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configureChangesInDB() {
        var authorIntervalData = message.getAuthorIntervalData();
        var recipientIntervalData = message.getRecipientIntervalData();
        configureNewPersonOnDuty(authorIntervalData, recipientIntervalData.getPersonId());
        configureNewPersonOnDuty(recipientIntervalData, authorIntervalData.getPersonId());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configureNewPersonOnDuty(DutyIntervalData dutyIntervalData, String newPersonId) {
        saveNewPersonOnDutyInterval(dutyIntervalData, newPersonId);
        removeFromPeopleOnDutyInterval(dutyIntervalData);
        removeMessagesOfPersonOnExcept(dutyIntervalData, message);
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

    private void updateMessageStatus(MessageState status) {
        message.setMessageState(status);
        new FBMessageDao().update(message);
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

    private void setButtonsVisibility(boolean isVisible) {
        int value = isVisible ? View.VISIBLE : View.INVISIBLE;

        acceptBt.setEnabled(isVisible);
        declineBt.setEnabled(isVisible);
        acceptBt.setVisibility(value);
        declineBt.setVisibility(value);
    }

    private void getUI() {
        tvMessageActivityTitle = findViewById(R.id.tvMessageActivityTitle);
        tvMessageStatus = findViewById(R.id.message_status);
        acceptBt = findViewById(R.id.accept_message_button);
        declineBt = findViewById(R.id.decline_message_button);

        acceptBt.setOnClickListener(this::confirmExchange);
        declineBt.setOnClickListener(this::refuseExchange);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.R)
    private void initUI() {
        boolean currentPersonIsAuthor = MessageUtils.currentPersonIsAuthorOf(message);
        var intervalsList = new ArrayList<DutyIntervalData>();
        intervalsList.add(message.getAuthorIntervalData());

        if (message.getRecipientIntervalData().getPeopleOnDutyId() != null)
            intervalsList.add(message.getRecipientIntervalData());
        else
            tvMessageActivityTitle.setText(getString(R.string.in_credit).toUpperCase());
        if (!currentPersonIsAuthor)
            Collections.reverse(intervalsList);
        pushViewsOf(intervalsList);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void pushViewsOf(List<DutyIntervalData> dutyIntervalData) {
        var intervalsLayout = (LinearLayout) findViewById(R.id.intervalsLayout);
        for (int i = 0; i < dutyIntervalData.size(); i++) {
            var data = dutyIntervalData.get(i);
            var view = getViewOf(data, i == 0);
            intervalsLayout.addView(view);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private View getViewOf(DutyIntervalData dutyIntervalData, boolean isYour) {
        return IntervalViewGenerator.getViewOf(
                dutyIntervalData,
                getBaseContext(),
                isYour);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void tryToUpdateMessageStatusUI() {
        if (isMessageForMe()) {
            updateMessageStatusUI();
        } else {
            setButtonsVisibility(false);
        }
    }

    private boolean isMessageForMe() {
        return !MessageUtils.currentPersonIsAuthorOf(message);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateMessageStatusUI() {

        switch (message.getMessageState()) {
            case READ:
                tvMessageStatus.setText(R.string.message_status_wait);
                break;
            case ACCEPTED:
                setButtonsVisibility(false);
                tvMessageStatus.setText(R.string.message_status_confirm);
                tvMessageStatus.setTextColor(getColor(R.color.green));
                break;
            case DECLINED:
                setButtonsVisibility(false);
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
