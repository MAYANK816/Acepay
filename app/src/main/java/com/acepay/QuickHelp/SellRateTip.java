//package com.acepay.QuickHelp;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
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
//import com.acepay.Dashboard;
//import com.acepay.R;
//import com.acepay.Util.Constant;
//import com.acepay.Util.PreferenceFile;
//
//import java.lang.ref.PhantomReference;
//
//import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
//
//public class SellRateTip extends AppCompatActivity {
//    LinearLayout layout_focus;
//    SimpleTooltip simpleTooltip;
//    TextView tv_buy,tv_skip,txSell,tv_next,tv_selling,tv_tool;
//    int click=0;
//    ImageView iv_deposite;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sell_rate_tip);
//
//       layout_focus=(LinearLayout)findViewById(R.id.lay_me);
//        tv_buy=(TextView)findViewById(R.id.txBuy);
//        tv_skip=(TextView)findViewById(R.id.tv_skip);
//        tv_next=(TextView)findViewById(R.id.tv_next);
//        txSell=(TextView)findViewById(R.id.txSell);
//        tv_tool=(TextView)findViewById(R.id.tv_tool);
//        tv_selling=(TextView)findViewById(R.id.txSelling);
//        iv_deposite=(ImageView)findViewById(R.id.iv_deposite);
//
//        String text= PreferenceFile.getInstance().getPreferenceData(this, Constant.SELLRATE);
//
//        String[] separated = text.split("=");
//        Log.e("separedted-->",separated.toString());
//        String newtext=separated[1];
//
//        txSell.setText(newtext);
//        tv_tool.setText("This is your current sell rate for 1 acepay");
//        new SimpleTooltip.Builder(this)
//                .anchorView(layout_focus)
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
//
//        tv_skip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(SellRateTip.this, Dashboard.class);
//                startActivity(intent);
//
//            }
//        });
//        tv_next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(SellRateTip.this,BidTool.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    public void alertDialog(Context context) {
//
//        final Dialog dialog  = new Dialog(context);
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
