package com.swack.customer.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.swack.customer.activities.OrderHistoryActivity;
import com.swack.customer.fragment.BreakDownFragment;
import com.swack.customer.fragment.TransportFragment;

import java.util.ArrayList;

public class ViewPagerOrderAdapter extends FragmentStatePagerAdapter {
    private OrderHistoryActivity historyActivity;
    private int mNumOfTabs;

    public ViewPagerOrderAdapter(FragmentManager fm, int NumOfTabs, OrderHistoryActivity historyActivity)
    {
        super(fm);
        this.historyActivity = historyActivity;
        this.mNumOfTabs = NumOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                BreakDownFragment tab3 = new BreakDownFragment(historyActivity);
                return tab3;

            case 1:
                TransportFragment tab1 = new TransportFragment(historyActivity);
                return tab1;

            default:
                return null;
        }
    }


    @Override
    public int getCount()
    {
        return mNumOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "Breakdown";
            case 1:
                return "Transport";
            default:
                return null;
        }
    }
}
