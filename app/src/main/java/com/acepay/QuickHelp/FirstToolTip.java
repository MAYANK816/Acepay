//package com.acepay.QuickHelp;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.Typeface;
//import android.hardware.camera2.CameraConstrainedHighSpeedCaptureSession;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.acepay.R;
//import com.acepay.Util.Constant;
//import com.acepay.Util.PreferenceFile;
//
//import java.math.BigDecimal;
//import java.text.DecimalFormat;
//
//import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
//
//public class FirstToolTip extends AppCompatActivity {
//    LinearLayout layout_focus,tvBitcoin;
//    SimpleTooltip simpleTooltip;
//    TextView tv_buy,tv_skip,tv_next,tv_selling,tvINR;
//    int click=0;
//    TextView txbit,txInrrate;
//    ImageView iv_deposite;
//    DecimalFormat formatter;
//    Typeface tfArchitectsDaughter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_first_tool_tip);
//        layout_focus=(LinearLayout)findViewById(R.id.lay_out_focus);
//        tv_buy=(TextView)findViewById(R.id.txBuy);
//        txInrrate=(TextView)findViewById(R.id.txInrrate);
//        tvBitcoin=(LinearLayout)findViewById(R.id.tvBitcoin);
//        tv_skip=(TextView)findViewById(R.id.tv_skip);
//        txbit=(TextView)findViewById(R.id.txbit);
//        tv_next=(TextView)findViewById(R.id.tv_next);
//        tvINR=(TextView)findViewById(R.id.tvINR);
//        tv_selling=(TextView)findViewById(R.id.txSelling);
//        iv_deposite=(ImageView)findViewById(R.id.iv_deposite);
//
//        formatter = new DecimalFormat("#,##,###");
//        tfArchitectsDaughter = Typeface.createFromAsset(getAssets(), "Fonts/DroidSans-Bold.ttf");
//        txbit.setTypeface(tfArchitectsDaughter);
//
//        Double bit=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this, Constant.BTC_amount));
//        Double finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());
//
//        if(bit>0)
//        {
//            txbit.setText(String.format("%.8f", finacal));
//        }
//        else
//        {
//            txbit.setText("0.00000000");
//        }
//
//        Log.e("finacal-->",finacal+"");
//        Log.e("inr-->",PreferenceFile.getInstance().getPreferenceData(this,Constant.INR_PRICE_BITCOIN));
//        txInrrate.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.INR_PRICE_BITCOIN));
//
//        tvINR.setText("This is your bitcoin balance. You can buy or sell it by  click on Buy or Sell button on Dashboard.");
//        new SimpleTooltip.Builder(this)
//                .anchorView(tvBitcoin)
//                .padding((float) 4.0)
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
//        tv_skip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            finish();
//
//            }
//        });
//        tv_next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//                Intent intent=new Intent(FirstToolTip.this,SecondtoolTip.class);
//                startActivity(intent);
//
//            }
//        });
//    }
//
//    public void alertDialog(Context context) {
//
//        final Dialog dialog=new Dialog(this);
//        dialog.setCancelable(false);
//
//        LayoutInflater li = LayoutInflater.from(context);
//        View promptsView2 = li.inflate(R.layout.exit, null);
//        dialog.setContentView(promptsView2);
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.show();
//
//        TextView btnok=(TextView)promptsView2.findViewById(R.id.btnok);
//        TextView btncancel=(TextView)promptsView2.findViewById(R.id.btncansel);
//
//
//        btnok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                moveTaskToBack(true);
//                // finish();
//            }
//        });
//
//        btncancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//    }
//
//    @Override
//    public void onBackPressed() {
//
//        alertDialog(this);
//
//
//    }
//}
