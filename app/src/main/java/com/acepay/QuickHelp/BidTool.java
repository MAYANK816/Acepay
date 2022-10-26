//package com.acepay.QuickHelp;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.acepay.Dashboard;
//import com.acepay.R;
//
//import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
//
//public class BidTool extends AppCompatActivity {
//
//    ImageView iv_bid;
//    TextView tv_info, tv_buy,tv_skip,tv_next,tv_selling;;
//    SimpleTooltip simpleTooltip;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bid_tool);
//        iv_bid=(ImageView)findViewById(R.id.iv_bid);
//        tv_info=(TextView)findViewById(R.id.tv_info);
//        tv_skip=(TextView)findViewById(R.id.tv_skip);
//        tv_next=(TextView)findViewById(R.id.tv_next);
//        tv_info.setText("Here you can place bid offer rate  to buy bicoin at rates lower than current rates");
//        new SimpleTooltip.Builder(this)
//                .anchorView(iv_bid)
//                .padding((float) 4.0)
//                .arrowHeight(0)
//                .arrowWidth(0)
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
//                Intent intent=new Intent(BidTool.this, Dashboard.class);
//                startActivity(intent);
//            }
//        });
//        tv_next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(BidTool.this,AskTool.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    public void alertDialog(Context context) {
//
//        final Dialog dialog  = new Dialog(context);
//
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
