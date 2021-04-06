package com.task.fbresult;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.MyMessage;
import com.task.fbresult.ui.adapters.MessageAdapter;
import com.task.fbresult.ui.people_on_duty.DutyPagerAdapter;

import java.io.Serializable;

public class MessageActivity extends AppCompatActivity {

    public static final String MESSAGE_PARAMETERS = "message_key";
    private MyMessage message;

    public static void getInstance(MyMessage message, Context context) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(MessageActivity.MESSAGE_PARAMETERS, message);
        context.startActivity(new Intent(context, DutyActivity.class).putExtras(bundle));
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Bundle extras = getIntent().getExtras();
        message = (MyMessage) extras.getSerializable(MessageActivity.MESSAGE_PARAMETERS);

        Toolbar toolbar = findViewById(R.id.duty_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void confirmExchange(){

    }

    private void refuseExchange(){

    }

    private void setButtonsEnable(boolean isEnable){

    }


}
