package com.acepay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MobileRecharge extends AppCompatActivity implements View.OnClickListener{
    ImageView ivarrow;
    TextView txName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_recharge);

        ivarrow= (ImageView) findViewById(R.id.ivarrow);
        txName= (TextView) findViewById(R.id.txName);

        ivarrow.setOnClickListener(this);
        txName.setVisibility(View.VISIBLE);
        txName.setText("Mobile Recharge");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(MobileRecharge.this,Dashboard.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()){

            case R.id.ivarrow:

                intent=new Intent(MobileRecharge.this,Dashboard.class);
                startActivity(intent);

                break;
        }
    }
}
