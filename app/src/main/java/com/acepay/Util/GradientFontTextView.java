package com.acepay.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.acepay.R;


public class GradientFontTextView extends TextView
{

    public GradientFontTextView(Context context)
    {
        super(context);
        init();
    }

    public GradientFontTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public GradientFontTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);

        //Setting the gradient if layout is changed
        if (changed) {

            Shader textShader3=new LinearGradient(0, 0, 0, 75,
                    new int[]{getResources().getColor(R.color.gradient_1),getResources().getColor(R.color.blue),
                            getResources().getColor(R.color.light_green), Color.MAGENTA},
                    new float[]{0, 1,2,3}, Shader.TileMode.CLAMP);

            getPaint().setShader(textShader3);
        }
    }

    @SuppressLint("WrongConstant")
    public void init()
    {
//        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Poppinslight.TTF");
//        setTypeface(tf ,1);
    }
}

