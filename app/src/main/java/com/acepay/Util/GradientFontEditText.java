package com.acepay.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.acepay.R;


public class GradientFontEditText extends EditText {

    public GradientFontEditText(Context context) {
        super(context);
        init();
    }

    public GradientFontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GradientFontEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        //Setting the gradient if layout is changed
        if (changed) {
           /* getPaint().setShader(new LinearGradient(0, 0, getWidth(), getHeight(),
                    ContextCompat.getColor(getContext(), R.color.gradient_1),
                    ContextCompat.getColor(getContext(), R.color.blue),
                    Shader.TileMode.REPEAT));*/

           //2,3,7,55.......5,0,5,100
            Shader textShader3=new LinearGradient(0, 0, 0, 75,
                    new int[]{getResources().getColor(R.color.gradient_1),getResources().getColor(R.color.blue),
                            getResources().getColor(R.color.light_green), Color.MAGENTA},
                    new float[]{0, 1,2,3}, Shader.TileMode.CLAMP);
/*
 Shader textShader3=new LinearGradient(5, 0, 5, 100,
                    new int[]{Color.YELLOW,Color.RED,Color.MAGENTA,Color.BLACK}, new float[]{0, 1,2,3}, Shader.TileMode.REPEAT);
*/

            getPaint().setShader(textShader3);
//            getPaint().setShadowLayer(5, 4, 4, Color.BLACK);

        }/*else{
            Shader textShader4=new LinearGradient(0, 0, 0, 75,
                    new int[]{getResources().getColor(R.color.text_grey_color),getResources().getColor(R.color.text_grey_color)},
                    new float[]{0,1}, Shader.TileMode.CLAMP);
            getPaint().setShader(textShader4);
        }*/
    }

    @SuppressLint("WrongConstant")
    public void init() {
//        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Poppinslight.TTF");
//        setTypeface(tf ,1);

    }
}

