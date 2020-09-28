package com.swack.customer.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.swack.customer.activities.OrderHistoryActivity3;
import com.swack.customer.fragment.BreakDownFragment2;
import com.swack.customer.fragment.BreakDownFragment3;
import com.swack.customer.fragment.TransportFragment3;

public class ViewPagerOrderAdapter3 extends FragmentStatePagerAdapter {
    private OrderHistoryActivity3 historyActivity;
    private int mNumOfTabs;
    private String lonnngi,lattti;

    public ViewPagerOrderAdapter3(FragmentManager fm, int NumOfTabs, OrderHistoryActivity3 historyActivity,String lattti,String
            lonnngi)
    {
        super(fm);
        this.historyActivity = historyActivity;
        this.mNumOfTabs = NumOfTabs;
        this.lattti = lattti;
        this.lonnngi = lonnngi;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                BreakDownFragment3 tab3 = new BreakDownFragment3(historyActivity,lattti,lonnngi);
                return tab3;

            case 1:
                TransportFragment3 tab1 = new TransportFragment3(historyActivity);
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
