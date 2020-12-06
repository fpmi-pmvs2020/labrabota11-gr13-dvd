package com.task.fbresult.ui.people_on_duty;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.task.fbresult.R;
import com.task.fbresult.ui.people_on_duty_stat.DutyStatisticFragment;

public class DutyPagerAdapter extends FragmentPagerAdapter {

    private static final int[] TAB_TITLES = new int[]{
            R.string.tab_text_1,
            R.string.tab_text_2
    };
    private final Context mContext;
    private final Bundle parameters;


    public DutyPagerAdapter(Context context, FragmentManager fm, Bundle par) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
        parameters = par;
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return DutyFragment.newInstance(parameters);
            case 1:
                return DutyStatisticFragment.newInstance(parameters);
            default: throw new UnknownError("strange behavior");
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }
}
