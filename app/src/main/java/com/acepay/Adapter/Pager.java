package com.acepay.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.acepay.NotificatiomPackage.NotificationNew;

public class Pager extends FragmentStatePagerAdapter {
    int tabCount;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                NotificationNew tab1 = new NotificationNew();
                return tab1;
            case 1:
                NotificationNew tab2 = new NotificationNew();
                return tab2;
            case 2:
                NotificationNew tab3 = new NotificationNew();
                return tab3;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}


