package com.task.fbresult;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.task.fbresult.dialogs.ExchangeDialogBuilder;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.MyMessage;
import com.task.fbresult.ui.adapters.MessageAdapter;
import com.task.fbresult.ui.holders.MessageViewHolder;
import com.task.fbresult.ui.people_on_duty.DutyPagerAdapter;

import java.io.Serializable;
import java.util.Objects;

import lombok.var;

public class MessageActivity extends AppCompatActivity {

    public static final String MESSAGE_PARAMETERS = "message_key";
    private MyMessage message;

    public static void getInstance(MyMessage message, Context context) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(MessageActivity.MESSAGE_PARAMETERS, message);
        context.startActivity(new Intent(context, MessageActivity.class).putExtras(bundle));
    }

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

    }

    private void confirmExchange(){

    }

    private void refuseExchange(){

    }

    private void setButtonsEnable(boolean isEnable){

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //do whatever
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
