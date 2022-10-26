package com.acepay;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;


public class DeleteScreen extends AppCompatActivity {

    TextView tvhyper,tvNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_screen);
        tvhyper= (TextView) findViewById(R.id.tvhyper);
        tvNumber= (TextView) findViewById(R.id.tvNumber);

//        tvhyper.setMovementMethod(LinkMovementMethod.getInstance());
//        String text = "<a href='http://18.216.88.154/metapay'> metapay.com </a>";
//        tvhyper.setText(Html.fromHtml(text));

        tvNumber.setText(PreferenceFile.getInstance().getPreferenceData(this, Constant.phone));
    }
}
