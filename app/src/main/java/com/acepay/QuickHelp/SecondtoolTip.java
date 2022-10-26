//package com.acepay.QuickHelp;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.TextView;
//
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.acepay.R;
//
//import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
//
//public class SecondtoolTip extends AppCompatActivity {
//
//    TextView tv_skip,tv_next,tv_selling,txBuy,txtool;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_secondtool_tip);
//        txBuy=(TextView)findViewById(R.id.txBuy);
//        txtool=(TextView)findViewById(R.id.txtool);
//        tv_skip=(TextView)findViewById(R.id.tv_skip);
//        tv_next=(TextView)findViewById(R.id.tv_next);
//        txtool.setText("Tap here to buy bitcoin ");
//
//        new SimpleTooltip.Builder(this)
//                .anchorView(txBuy)
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
//             finish();
//
//            }
//        });
//        tv_next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//                Intent intent=new Intent(SecondtoolTip.this,ThirdtooTip.class);
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
