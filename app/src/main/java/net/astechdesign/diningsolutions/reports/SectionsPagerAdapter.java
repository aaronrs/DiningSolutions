package net.astechdesign.diningsolutions.reports;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return StockReportFragment.newInstance();
            case 1: return EarningsReportFragment.newInstance();
            case 2: return RecordFragment.newInstance();
        }
        return StockReportFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Stock Report";
            case 1:
                return "Earnings Report";
            case 2:
                return "Records";
        }
        return null;
    }
}
