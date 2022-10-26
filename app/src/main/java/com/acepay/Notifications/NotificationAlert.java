package com.acepay.Notifications;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.acepay.Dashboard;


public class NotificationAlert extends AppCompatActivity
{

    String type, message1,book_id;

    private TextView title,message,ok,cancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);

        if(getIntent().getExtras()!=null) {
            type = getIntent().getStringExtra("type");

            message1 = getIntent().getStringExtra("message");

            Log.e("msg >> ", message1);

                showAlert(type, message1);
        }
    }

    private void showAlert(final String type,String message)

    {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(NotificationAlert.this);
        builder.setTitle("Meta Pay");
        builder.setMessage(message);
        builder.setCancelable(false);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)

            {


               Intent  intent = new Intent( NotificationAlert.this, Dashboard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(121);
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });


        int height= WindowManager.LayoutParams.WRAP_CONTENT;

        int width= WindowManager.LayoutParams.WRAP_CONTENT;

        AlertDialog alertDialog=builder.create();

        WindowManager.LayoutParams params=alertDialog.getWindow().getAttributes();

        params.gravity= Gravity.CENTER;

        alertDialog.getWindow().setLayout(width,height);

        alertDialog.show();

    }




}