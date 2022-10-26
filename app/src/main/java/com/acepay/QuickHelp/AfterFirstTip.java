package com.acepay.QuickHelp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.acepay.R;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;

import java.math.BigDecimal;
import java.text.DecimalFormat;

//import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;



public class AfterFirstTip extends AppCompatActivity {
    LinearLayout layout_focus;
//    SimpleTooltip simpleTooltip;
    TextView tv_buy,tv_skip,tv_next,tv_selling,tv_tool,tvBitcoin,tvINR;
    int click=0;
    DecimalFormat formatter;
    Typeface tfArchitectsDaughter;
    ImageView iv_deposite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_first_tip);
        layout_focus=(LinearLayout)findViewById(R.id.lay_second);
        tv_buy=(TextView)findViewById(R.id.txBuy);
        tvBitcoin= (TextView) findViewById(R.id.tvBitcoin);
        tvINR= (TextView) findViewById(R.id.tvINR);
        tv_skip=(TextView)findViewById(R.id.tv_skip);
        tv_next=(TextView)findViewById(R.id.tv_next);
        tv_tool=(TextView)findViewById(R.id.tv_tool);
        tv_selling=(TextView)findViewById(R.id.txSelling);
        iv_deposite=(ImageView)findViewById(R.id.iv_deposite);

        formatter = new DecimalFormat("#,##,###.00");
        tfArchitectsDaughter = Typeface.createFromAsset(getAssets(), "Fonts/DroidSans-Bold.ttf");
        tvBitcoin.setTypeface(tfArchitectsDaughter);

        Double bit=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount));
        Double finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());

        if(bit>0)
        {
            tvBitcoin.setText(String.format("%.8f", finacal));
        }
        else
        {
            tvBitcoin.setText("0.00000000");
        }

        tvINR.setText(PreferenceFile.getInstance().getPreferenceData(this, Constant.INR_PRICE_BITCOIN));

        tv_tool.setText("This is approximate value of your bitcoin balance in Rs as per the current buy rate");
//        new SimpleTooltip.Builder(this)
//                .anchorView(layout_focus)
//                .padding((float) 4.0)
//                .gravity(Gravity.TOP)
//                .arrowHeight(1)
//                .arrowWidth(1)
//                .textColor(Color.WHITE)
//                .showArrow(true)
//                .dismissOnOutsideTouch(false)
//                .animated(false)
//                .transparentOverlay(false)
//                .overlayMatchParent(false)
//                .build()
//                .show();
//
       tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Log.e("AfterFirstTip","clicknext");
//                Intent intent=new Intent(AfterFirstTip.this,SecondtoolTip.class);
//                startActivity(intent);

            }
        });


    }

    public void alertDialog(Context context) {

        final Dialog dialog=new Dialog(AfterFirstTip.this, R.style.StatisticsDialog);

        dialog.setCancelable(false);

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView2 = li.inflate(R.layout.exit, null);
        dialog.setContentView(promptsView2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        TextView btnok=(TextView)promptsView2.findViewById(R.id.btnok);
        TextView btncancel=(TextView)promptsView2.findViewById(R.id.btncansel);


        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveTaskToBack(true);
                // finish();
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {

        alertDialog(this);


    }
}
