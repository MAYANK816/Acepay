package com.acepay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class newSupportEmail extends AppCompatActivity {
    EditText subject,body;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_support_email);
        subject = (EditText) findViewById(R.id.subject);
        body =  (EditText)findViewById(R.id.bodyedit);
        button = (Button) findViewById(R.id.emsend);

        // attach setOnClickListener to button with Intent object define in it
        button.setOnClickListener(view -> {

            String emailsubject = subject.getText().toString();
            String emailbody = body.getText().toString();

            // define Intent object with action attribute as ACTION_SEND
            Intent intent = new Intent(Intent.ACTION_SEND);

            // add three fields to intent using putExtra function
            //yaha support add krni hai

            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"mayankkhatri024@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, emailsubject);
            intent.putExtra(Intent.EXTRA_TEXT, emailbody);

            // set type of intent
            intent.setType("message/rfc822");

            // startActivity with intent with chooser as Email client using createChooser function
            startActivity(Intent.createChooser(intent, "Choose an Email client :"));
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}