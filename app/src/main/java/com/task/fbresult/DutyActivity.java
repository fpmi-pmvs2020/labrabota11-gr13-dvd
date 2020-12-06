package com.task.fbresult;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.task.fbresult.model.Duty;
import com.task.fbresult.ui.people_on_duty.DutyPagerAdapter;


public class DutyActivity extends AppCompatActivity {

    public static final String DUTY_PARAMETERS = "duty_key";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty);

        Toolbar toolbar = findViewById(R.id.duty_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        DutyPagerAdapter adapter = new DutyPagerAdapter(this, getSupportFragmentManager(), getIntent().getExtras());
        ViewPager viewPager = findViewById(R.id.duty_pager);
        viewPager.setAdapter(adapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }

    public static void getInstance(Duty duty, Context context){
        Bundle bundle = new Bundle();
        bundle.putSerializable(DutyActivity.DUTY_PARAMETERS,duty);
        context.startActivity(new Intent(context,DutyActivity.class).putExtras(bundle));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}