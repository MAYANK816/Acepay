package com.acepay;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.acepay.NotificatiomPackage.NotificationNew;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class NewNotification extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView txAlert,txMessage,txNotification;
    ImageView imNoti,imAlert,imMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imNoti = (ImageView) findViewById(R.id.imNoti);
        txAlert = (TextView) findViewById(R.id.txAlert);
        txMessage = (TextView) findViewById(R.id.txMessage);
        txNotification = (TextView) findViewById(R.id.txNotification);

        imMessage = (ImageView) findViewById(R.id.imMessage);
        imAlert = (ImageView) findViewById(R.id.imAlert);

        viewPager = (ViewPager) findViewById(R.id.pager);
        createViewPager(viewPager);

        /*tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);*/
    }

    private void createViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new NotificationNew(), "Tab 1");
        adapter.addFrag(new NotificationNew(), "Tab 2");
        adapter.addFrag(new NotificationNew(), "Tab 3");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public Fragment getItem(int position) {

            if(position==0){
                Drawable msg = getResources().getDrawable(R.drawable.bell);
//                int imf = Color.argb(255 ,47, 212,158);
//                msg.setColorFilter(imf, PorterDuff.Mode.MULTIPLY);
                imNoti.setImageDrawable(msg);


                Drawable tv = getResources().getDrawable(R.drawable.alert);
//                int in = Color.argb( 255,115,113,113);
//                tv.setColorFilter(in, PorterDuff.Mode.MULTIPLY);
                imAlert.setImageDrawable(tv);

                Drawable mes = getResources().getDrawable(R.drawable.message);
//                int ims = Color.argb( 255,115,113,113);
//                mes.setColorFilter(ims, PorterDuff.Mode.MULTIPLY);
                imMessage.setImageDrawable(mes);

                imNoti.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gradient_1)));
                imMessage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                imAlert.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));


                txMessage.setTextColor(getResources().getColor(R.color.blue));
                txAlert.setTextColor(getResources().getColor(R.color.blue));
                txNotification.setTextColor(getResources().getColor(R.color.gradient_1));
            }

            if(position==1) {

                Drawable bell = getResources().getDrawable(R.drawable.bell);
//                int inb = Color.argb( 255,115,113,113);
//                bell.setColorFilter(inb, PorterDuff.Mode.MULTIPLY);
                imNoti.setImageDrawable(bell);

                Drawable neds = getResources().getDrawable(R.drawable.alert);
//                int inh = Color.argb(255 ,47, 212,158);
//                neds.setColorFilter(inh, PorterDuff.Mode.MULTIPLY);
                imAlert.setImageDrawable(neds);

                Drawable dfd = getResources().getDrawable(R.drawable.message);
//                int ds = Color.argb( 255,115,113,113);
//                dfd.setColorFilter(ds, PorterDuff.Mode.MULTIPLY);
                imMessage.setImageDrawable(dfd);

                imNoti.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                imMessage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                imAlert.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gradient_1)));

                txMessage.setTextColor(getResources().getColor(R.color.blue));
                txAlert.setTextColor(getResources().getColor(R.color.gradient_1));
                txNotification.setTextColor(getResources().getColor(R.color.blue));

            }

            if(position==2) {

                Drawable dn = getResources().getDrawable(R.drawable.bell);
                imNoti.setImageDrawable(dn);

                Drawable alr = getResources().getDrawable(R.drawable.alert);
                imAlert.setImageDrawable(alr);

                Drawable ffer = getResources().getDrawable(R.drawable.message);
//                int dd = Color.argb(255 ,47, 212,158);
//                ffer.setColorFilter(dd, PorterDuff.Mode.MULTIPLY);
                imMessage.setImageDrawable(ffer);


                imNoti.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
                imMessage.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gradient_1)));
                imAlert.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));

                txMessage.setTextColor(getResources().getColor(R.color.gradient_1));
                txAlert.setTextColor(getResources().getColor(R.color.blue));
                txNotification.setTextColor(getResources().getColor(R.color.blue));

            }
            return mFragmentList.get(position);


        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(NotificationNew fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
