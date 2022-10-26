package com.acepay.Adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.acepay.ReceiveBitcoin;
import com.acepay.SendBitcoin;
import com.acepay.SendingReceiving.ReceivingBitcoin;
import com.acepay.SendingReceiving.SendedBitcoin;


/**
 * Created by pro22 on 27/10/17.
 */

public class ViewPagerAdapter  extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public ViewPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                SendedBitcoin tab1 = new SendedBitcoin();
                return tab1;
            case 1:
                ReceivingBitcoin tab2 = new ReceivingBitcoin();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}