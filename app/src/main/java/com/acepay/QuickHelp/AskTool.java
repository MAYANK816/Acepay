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
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.acepay.R;
//
//import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
//
//public class AskTool extends AppCompatActivity {
//    ImageView iv_ask;
//    TextView tv_info, tv_buy,tv_skip,tv_next,tv_selling;;
//    SimpleTooltip simpleTooltip;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ask_tool);
//        iv_ask=(ImageView)findViewById(R.id.iv_ask);
//        tv_info=(TextView)findViewById(R.id.tv_info);
//        tv_skip=(TextView)findViewById(R.id.tv_skip);
//        tv_next=(TextView)findViewById(R.id.tv_next);
//        tv_info.setText("Here you can place bid offer rate  to sell bicoin at rates higher than current rates");
//        new SimpleTooltip.Builder(this)
//                .anchorView(iv_ask)
//                .padding((float) 4.0)
//                .arrowHeight(1)
//                .arrowWidth(1)
//                .arrowColor(Color.LTGRAY)
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
//                Intent intent=new Intent(AskTool.this,DashBoardHelp.class);
//                startActivity(intent);
//
//            }
//        });
//        tv_next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(AskTool.this,ReceveTip.class);
//                startActivity(intent);
//
//            }
//        });
//    }
//
//    public void alertDialog(Context context) {
//
//        final Dialog dialog  = new Dialog(this);
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
