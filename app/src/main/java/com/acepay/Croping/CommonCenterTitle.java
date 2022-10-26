package com.acepay.Croping;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.acepay.R;


public class CommonCenterTitle extends AppCompatActivity implements View.OnClickListener
{

    public androidx.appcompat.widget.Toolbar toolbar;
    public TextView done,title,cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_center_title);
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        done=(TextView)findViewById(R.id.done);
        cancel=(TextView)findViewById(R.id.cancel);
        done.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.done:
                doneAction();
                break;
            case R.id.cancel:
                cancelAction();
                break;
        }
    }

    public void doneAction(){};
    public void cancelAction(){};

}
