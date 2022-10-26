package com.acepay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import java.util.Timer;
import java.util.TimerTask;

public class NewSplash extends AppCompatActivity {

    private Timer timer;
    Context context;
    private static int SPLASH_TIME_OUT = 2000;
    public static Boolean active=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_splash);
        active = true;
        context=this;

        try
        {
            PreferenceFile.getInstance().saveData(this,"namee","Ravina");
            Log.e("GetName ",PreferenceFile.getInstance().getPreferenceData(NewSplash.this,"namee"));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        try
        {
            if(PreferenceFile.getInstance().getPreferenceData(this, Constant.COUNT_SECURITY)==null){
                Log.e("count-->","null");
                PreferenceFile.getInstance().saveData(this,Constant.COUNT_SECURITY,"1");
            }

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }

    @Override
    public void onResume() {

        super.onResume();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if(PreferenceFile.getInstance().getPreferenceData(NewSplash.this, Constant.ID)!=null){

                    if(PreferenceFile.getInstance().getPreferenceData(NewSplash.this,Constant.Accunt_status)!=null) {
                        if (PreferenceFile.getInstance().getPreferenceData(NewSplash.this, Constant.Accunt_status).equals("Inactive")) {

                            finish();
                            Intent intent = new Intent(NewSplash.this, BlockScreen.class);
                            startActivity(intent);
                        }
                        else {

                            Log.e("SPLASH", "ELSEEE" +
                                            PreferenceFile.getInstance().getPreferenceData(NewSplash.this,
                                                    Constant.LOCK_PIN)+"" +
                                    " fgfd "+PreferenceFile.getInstance().getPreferenceData(NewSplash.this,
                                    Constant.Finger_Lock));




                            finish();

                            if (PreferenceFile.getInstance().getPreferenceData(NewSplash.this,
                                    Constant.LOCK_PIN) != null) {

                                if (PreferenceFile.getInstance().getPreferenceData(NewSplash.this,
                                        Constant.LOCK_PIN).equals("1")
                                        /*|| PreferenceFile.getInstance().getPreferenceData(NewSplash.this,
                                        Constant.Finger_Lock).trim().equals("1")*/) {


                                    if (PreferenceFile.getInstance().getPreferenceData(NewSplash.this,
                                            Constant.Finger_Lock) != null) {
                                        if (PreferenceFile.getInstance().getPreferenceData(NewSplash.this,
                                                Constant.Finger_Lock).trim().equals("1")) {

                                            Intent intent = new Intent(NewSplash.this, LoginNew.class);
                                            startActivity(intent);
                                        } else if (PreferenceFile.getInstance().getPreferenceData(NewSplash.this,
                                                Constant.Finger_Lock).trim().equals("0")) {
                                            Intent intent = new Intent(NewSplash.this, LoginNew.class);
                                            startActivity(intent);
                                        }else {
                                            Intent intent = new Intent(NewSplash.this, Dashboard.class);
                                            startActivity(intent);
                                        }
                                    } else {

                                        Intent intent = new Intent(NewSplash.this, LoginNew.class);
                                        startActivity(intent);
                                    }

                                } else {

                                    Log.e("SPLASH", "Acc STATUS Dashboard"
                                            + PreferenceFile.getInstance().getPreferenceData(NewSplash.this,
                                            Constant.LOCK_PIN)+" "+
                                            PreferenceFile.getInstance().getPreferenceData(NewSplash.this,
                                                    Constant.Finger_Lock));

                                    if (PreferenceFile.getInstance().getPreferenceData(NewSplash.this,
                                            Constant.Finger_Lock) != null) {
                                        if (PreferenceFile.getInstance().getPreferenceData(NewSplash.this,
                                                Constant.Finger_Lock).trim().equals("1")) {

                                            Intent intent = new Intent(NewSplash.this, LoginNew.class);
                                            startActivity(intent);
                                        }else if (PreferenceFile.getInstance().getPreferenceData(NewSplash.this,
                                                Constant.Finger_Lock).trim().equals("0")) {
                                            Intent intent = new Intent(NewSplash.this, Dashboard.class);
                                            startActivity(intent);
                                        }
                                        else {
                                            Intent intent = new Intent(NewSplash.this, Dashboard.class);
                                            startActivity(intent);
                                        }
                                    } else {
                                        Intent intent = new Intent(NewSplash.this, Dashboard.class);
                                        startActivity(intent);
                                    }
                                }
                            } else if (PreferenceFile.getInstance().getPreferenceData(NewSplash.this,
                                    Constant.Finger_Lock) != null) {

                                if (PreferenceFile.getInstance().getPreferenceData(NewSplash.this,
                                        Constant.Finger_Lock) != null) {

                                    if (PreferenceFile.getInstance().getPreferenceData(NewSplash.this,
                                            Constant.Finger_Lock).trim().equals("1")) {

                                        Intent intent = new Intent(NewSplash.this, LoginNew.class);
                                        startActivity(intent);

                                    } else {

                                        Intent intent = new Intent(NewSplash.this, Dashboard.class);
                                        startActivity(intent);
                                    }

                                } else {

                                    Log.e("SPLASH", "Acc STATUS Dashboard"
                                            + PreferenceFile.getInstance().getPreferenceData(NewSplash.this,
                                            Constant.LOCK_PIN)/*+" FPPLOOCK "+PreferenceFile.getInstance().getPreferenceData
                                            (NewSplash.this, Constant.Finger_Lock)*/);

                                    Intent intent = new Intent(NewSplash.this, Dashboard.class);
                                    startActivity(intent);
                                }
                            } else {
                                Intent intent = new Intent(NewSplash.this, LoginNew.class);
                                startActivity(intent);
                            }


                        }
                    }
                    else {

                        finish();
                        Intent intent = new Intent(NewSplash.this, LoginNew.class);
                        startActivity(intent);

                    }

                }
                else {
                    finish();
//                    Intent intent = new Intent(NewSplash.this, Splash.class);
                    Intent intent = new Intent(NewSplash.this, BSlider.class);
                    startActivity(intent);
                }


            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onStop() {
        super.onStop();
        timer.cancel();
        active = false;

    }
}
