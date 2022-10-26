package com.acepay.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;

/**
 * Created by pro22 on 22/11/17.
 */

public class MyDefaultSliderView extends BaseSliderView {


    public MyDefaultSliderView(Context context)
    {
        super(context);
    }

    @Override
    public View getView()
    {

        View v = LayoutInflater.from(getContext()).inflate(com.daimajia.slider.library.R.layout.render_type_default,null);
        ImageView target = (ImageView)v.findViewById(com.daimajia.slider.library.R.id.daimajia_slider_image);
        bindEventAndShow(v, target);
        return v;
    }
}
