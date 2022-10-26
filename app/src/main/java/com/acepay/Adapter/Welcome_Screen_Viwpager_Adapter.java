package com.acepay.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.acepay.Model.TermsCondition;
import com.acepay.R;

import java.util.ArrayList;


public class Welcome_Screen_Viwpager_Adapter extends PagerAdapter {

    private Context context;
    ArrayList<TermsCondition> arrayList;
    private LayoutInflater inflater;

    public Welcome_Screen_Viwpager_Adapter(Context context, ArrayList<TermsCondition> arrayList){

        this.context=context;
        this.arrayList=arrayList;
    }

    @Override
    public int getCount() {

        return arrayList.size();

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view.equals(object);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View) object);


    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        System.gc();

         TextView tvFirst,tvSecond,tvThird;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.team_condition, container, false);
        tvFirst=(TextView)viewLayout.findViewById(R.id.tvFirst);
        tvSecond=(TextView)viewLayout.findViewById(R.id.tvSecond);
        tvThird=(TextView)viewLayout.findViewById(R.id.tvThird);

        tvFirst.setText(arrayList.get(position).getTerm_one());
        tvSecond.setText(arrayList.get(position).getTerm_two());
        tvThird.setText(arrayList.get(position).getTerm_three());
        ((ViewPager) container).addView(viewLayout);
        return viewLayout;

    }








}
