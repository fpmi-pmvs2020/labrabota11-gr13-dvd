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
import com.task.fbresult.dialogs.DialogBuilderFactory;
import com.task.fbresult.dialogs.DialogType;
import com.task.fbresult.dialogs.ExchangeDialogBuilder;
import com.task.fbresult.dialogs.ExchangeOnCurrentDialogBuilder;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.Person;
import com.task.fbresult.ui.people_on_duty.DutyPagerAdapter;
import com.task.fbresult.util.FBUtils;

import lombok.var;


public class DutyActivity extends AppCompatActivity {

    public static final String DUTY_PARAMETERS = "duty_key";
    private Duty duty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty);

        Bundle extras = getIntent().getExtras();
        duty = (Duty) extras.getSerializable(DutyActivity.DUTY_PARAMETERS);

        Toolbar toolbar = findViewById(R.id.duty_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        DutyPagerAdapter adapter = new DutyPagerAdapter(this, getSupportFragmentManager(), extras);
        ViewPager viewPager = findViewById(R.id.duty_pager);
        viewPager.setAdapter(adapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }

    public static void getInstance(Duty duty, Context context) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DutyActivity.DUTY_PARAMETERS, duty);
        context.startActivity(new Intent(context, DutyActivity.class)
                .putExtras(bundle).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_exchange:
                var exchangeType = getExchangeType();
                showDialog(exchangeType);
                return true;
            case android.R.id.home:
                //do whatever
                finish();
                System.out.println("");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog(DialogType dialogType) {
        var factory = new DialogBuilderFactory(this);
        var builder = (ExchangeDialogBuilder) factory.getDialogBuilder(dialogType);
        builder.setCurrentDuty(duty);
        AlertDialog dialog = builder.build(null, null, () -> {
        });
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.light_blue_oval_shape);
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.duty, menu);
        return true;
    }

    private DialogType getExchangeType() {
        if (itIsCurrentUserDuty())
            return DialogType.EXCHANGE_ON_CURRENT;
        else
            return DialogType.MAKE_EXCHANGE;
    }

    private boolean itIsCurrentUserDuty() {
        Person current = FBUtils.getCurrentUserAsPerson();
        return FBUtils.personIsOnDuty(current, duty);
    }

}