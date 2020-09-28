package com.swack.customer.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.swack.customer.activities.OrderHistoryActivity2;
import com.swack.customer.fragment.BreakDownFragment2;
import com.swack.customer.fragment.TransportFragment2;

public class ViewPagerOrderAdapter2 extends FragmentStatePagerAdapter {
    private OrderHistoryActivity2 historyActivity;
    private int mNumOfTabs;

    public ViewPagerOrderAdapter2(FragmentManager fm, int NumOfTabs, OrderHistoryActivity2 historyActivity)
    {
        super(fm);
        this.historyActivity = historyActivity;
        this.mNumOfTabs = NumOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                BreakDownFragment2 tab3 = new BreakDownFragment2(historyActivity);
                return tab3;

            case 1:
                TransportFragment2 tab1 = new TransportFragment2(historyActivity);
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
