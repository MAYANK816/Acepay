package com.acepay.Zendesk;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;
;import androidx.cardview.widget.CardView;

import com.acepay.R;
import com.acepay.ToolabarActivity;

public class RsDeposite extends ToolabarActivity implements View.OnClickListener{
    TextView txName;
    CardView acc_verification, rs_deposite, tv_pin, tv_withdraw, tv_sendrcv, tv_other;
    ImageView ivarrow;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rs_deposite);
        acc_verification = (CardView) findViewById(R.id.tv_acc_verification);
        rs_deposite = (CardView) findViewById(R.id.tv_rs_deposite);
        tv_pin = (CardView) findViewById(R.id.tv_pin);

//        ivarrow = (ImageView) findViewById(R.id.ivarrow);

//        ivarrow.setVisibility(View.VISIBLE);
        txName = (TextView) findViewById(R.id.txName);
        txName.setText("Rs deposit");

        acc_verification.setOnClickListener(this);
        rs_deposite.setOnClickListener(this);
        tv_pin.setOnClickListener(this);
//        ivarrow.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_acc_verification:
                intent = new Intent(RsDeposite.this, RsDepositeNext.class);
                intent.putExtra("first", "Amount less than 50,000");
                intent.putExtra("sec", "Amount between 50,000 and 5,00,000");
                intent.putExtra("third", "Amount more than 5,00,000");
                intent.putExtra("flag","");
                startActivity(intent);
                break;

            case R.id.tv_rs_deposite:
                intent = new Intent(RsDeposite.this, RsDepositeNext.class);
                intent.putExtra("first", "I have done payment but it is not showing up in my Metapay app");
                intent.putExtra("sec", "I have done payment but my order is canceled");
                intent.putExtra("flag","1");

                startActivity(intent);
                break;

            case R.id.tv_pin:
                intent = new Intent(RsDeposite.this, RsDepositeNext.class);
                // intent.putExtra("header","PIN");
                intent.putExtra("first","Amount less than 50,000");
                intent.putExtra("sec","Amount between 50,000 and 5,00,000");
                intent.putExtra("third","Amount more than 5,00,000");
                intent.putExtra("flag","");
                startActivity(intent);
                break;

//            case R.id.ivarrow:
//                finish();
//                break;
        }
    }

    @Override
    public void onBackPressed() {

        /*if (!doubleBackToExitPressedOnce) {
            finish();
            Log.e("Biding","once");
        }*/

        if (doubleBackToExitPressedOnce) {
            finishAffinity();
//            super.onBackPressed();
            Log.e("Biding","once");
//            return;
        }

        this.doubleBackToExitPressedOnce = true;
        //Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
                finish();
            }
        }, 1000);
    }

}
