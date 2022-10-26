package com.acepay;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Handler;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.acepay.FingerPrint.FingerPrintLister;
import com.acepay.FingerPrint.FingerprintAuthenticationHandler;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class Setting extends ToolabarActivity implements View.OnClickListener, RetrofitResponse {

//    ImageView ivarrow;
    TextView txName,num;
    SeekBar seekbar;
    LinearLayout lnChangePin,lnLockTransaction,ln_seekbar;
    Switch sw_vibrate,sw_sound,sw_rate_alert,lock_pin,lock_outgoing_transaction,finger_print;
    String datacheak;
    LinearLayout lnFingerPrintlayer;
    String rate;
    String seekbarValue="";
    boolean doubleBackToExitPressedOnce = false;

    public KeyguardManager keyguardManager;
    public FingerprintManager fingerprintManager;
    private KeyStore keyStore;
    private Cipher cipher;
    private static final String KEY_NAME = "studytutorial";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Log.e("onCreate","onCreate");

        lnChangePin= (LinearLayout) findViewById(R.id.lnChangePin);
        lnLockTransaction= (LinearLayout) findViewById(R.id.lnLockTransaction);
//        ivarrow= (ImageView) findViewById(R.id.ivarrow);
        txName= (TextView) findViewById(R.id.txName);
        lnFingerPrintlayer= (LinearLayout) findViewById(R.id.lnFingerPrintlayer);
        sw_sound=(Switch)findViewById(R.id.sw_sound);
        lock_outgoing_transaction=(Switch)findViewById(R.id.lock_outgoing_transaction);
        lock_pin=(Switch)findViewById(R.id.lock_pin);
        sw_vibrate=(Switch)findViewById(R.id.sw_vibrate);
        sw_rate_alert=(Switch)findViewById(R.id.sw_rate_alert);
        finger_print=(Switch)findViewById(R.id.finger_print);

        num = (TextView)findViewById(R.id.tv_num);
        seekbar = (SeekBar)findViewById(R.id.seekbar);
        ln_seekbar= (LinearLayout) findViewById(R.id.ln_seekbar);

        seekbar.setMax(100000);

        if(PreferenceFile.getInstance().getPreferenceData(Setting.this, Constant.LOCK_PIN)!=null)
        {
            if(PreferenceFile.getInstance().getPreferenceData(Setting.this,Constant.LOCK_PIN).equals("1")){
                lock_pin.setChecked(true);
            }
            else
            {
                lock_pin.setChecked(false);
            }
        }
        else
        {
            lock_pin.setChecked(true);
        }

        //Finger Print
         if(PreferenceFile.getInstance().getPreferenceData(Setting.this,Constant.Finger_Lock)!=null)
                {

                    Log.e("finger_printsetting",PreferenceFile.getInstance().getPreferenceData(Setting.this,Constant.Finger_Lock));
                    if(PreferenceFile.getInstance().getPreferenceData(Setting.this,Constant.Finger_Lock).equals("1")){
                        finger_print.setChecked(true);
                    }
                    else {
                        finger_print.setChecked(false);
                    }
                }
                else
                {
                    finger_print.setChecked(false);
                }

        if(PreferenceFile.getInstance().getPreferenceData(Setting.this,Constant.RATE_STATUS)!=null)
        {
            if(PreferenceFile.getInstance().getPreferenceData(Setting.this,Constant.RATE_STATUS).equals("1")){
                sw_rate_alert.setChecked(true);
                ln_seekbar.setVisibility(View.VISIBLE);
            }
            else {
                sw_rate_alert.setChecked(false);
                ln_seekbar.setVisibility(View.GONE);
            }
        }
        else
        {
            sw_rate_alert.setChecked(false);
            ln_seekbar.setVisibility(View.GONE);

        }

        if(PreferenceFile.getInstance().getPreferenceData(Setting.this,Constant.Lock_Transaction)!=null)
        {
            if(PreferenceFile.getInstance().getPreferenceData(Setting.this,Constant.Lock_Transaction).equals("1")){
                lock_outgoing_transaction.setChecked(true);
            }
            else {
                lock_outgoing_transaction.setChecked(false);
            }
        }
        else
        {
            lock_outgoing_transaction.setChecked(false);
        }


        finger_print.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                Log.e("finger_print",finger_print.isChecked()+"");

                if (finger_print.isChecked()){

                    //lock_outgoing_transaction.setChecked(true);

                    Intent intent1 = new Intent(Setting.this, CheckSecurePin.class);
                    intent1.putExtra("key", "finger_print");
                    intent1.putExtra("lock", "1");
                    startActivity(intent1);

                }
                else
                {
                    // lock_outgoing_transaction.setChecked(false);
                    Intent intent1 = new Intent(Setting.this, CheckSecurePin.class);
                    intent1.putExtra("key", "finger_print");
                    intent1.putExtra("lock", "0");
                    startActivity(intent1);
                    // PreferenceFile.getInstance().saveData(Setting.this,Constant.Lock_Transaction,"0");
                }


            }
        });

        if (Build.VERSION.SDK_INT >= 23) {

            FingerprintAuthenticationHandler.bindListener(new FingerPrintLister() {
                @Override
                public void fingerPrintcall(String e, Boolean success) {


                    if (success) {
                        Log.e("success", "yes");
                    }

                    else {

                        Log.e("failture", "yes");

                    }

                }
            });
        }

        lock_pin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                    {
                        if (lock_pin.isChecked()){
                            Log.e("onchekedchange","yes");
                            PreferenceFile.getInstance().saveData(Setting.this,Constant.LOCK_PIN,"1");
                            datacheak=PreferenceFile.getInstance().getPreferenceData(Setting.this,Constant.LOCK_PIN);
                            Log.e("LOCK_PIN",datacheak);
                            lock_pin.setChecked(true);
                        }
                        else {
                            PreferenceFile.getInstance().saveData(Setting.this,Constant.LOCK_PIN,"0");
                            datacheak=PreferenceFile.getInstance().getPreferenceData(Setting.this,Constant.LOCK_PIN);
                            Log.e("LOCK_PIN",datacheak);
                        }
                    }
                });

        lnLockTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("click-->","yes");

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Setting.this);
                alertDialogBuilder.setTitle(getResources().getString(R.string.app_name));

                if(lock_outgoing_transaction.isChecked()) {
                    alertDialogBuilder.setMessage("To allow outgoing Acepay transactions. Please enter PIN and do OTP SMS verification.");
                }
                else {
                    alertDialogBuilder.setMessage("Please confirm that you want to lock all your outgoing Acepay transactions. You can enable it again anytime by doing OTP SMS verification.");
                }
                alertDialogBuilder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        if (!lock_outgoing_transaction.isChecked()){

                            //lock_outgoing_transaction.setChecked(true);

                            Intent intent1 = new Intent(Setting.this, CheckSecurePin.class);
                            intent1.putExtra("key", "lock_transaction");
                            intent1.putExtra("lock", "1");
                            startActivity(intent1);
                            /*PreferenceFile.getInstance().saveData(Setting.this,Constant.Lock_Transaction,"1");
                            datacheak=PreferenceFile.getInstance().getPreferenceData(Setting.this,Constant.Lock_Transaction);
                            Log.e("lock_outgoing_transaction",datacheak);
                            lock_outgoing_transaction.setChecked(true);*/
                        }
                        else
                        {
                           // lock_outgoing_transaction.setChecked(false);
                            Intent intent1 = new Intent(Setting.this, CheckSecurePin.class);
                            intent1.putExtra("key", "lock_transaction");
                            intent1.putExtra("lock", "0");
                            startActivity(intent1);
                           // PreferenceFile.getInstance().saveData(Setting.this,Constant.Lock_Transaction,"0");
                        }
                        arg0.dismiss();
                    }
                });
                alertDialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });


        if(PreferenceFile.getInstance().getPreferenceData(this,Constant.SOUND)!=null) {
            if (PreferenceFile.getInstance().getPreferenceData(this, Constant.SOUND).equals("on")) {
                sw_sound.setChecked(true);
            }
            else {
                sw_sound.setChecked(false);
            }
        }  if(PreferenceFile.getInstance().getPreferenceData(this,Constant.VIBRATE)!=null) {
            if (PreferenceFile.getInstance().getPreferenceData(this, Constant.VIBRATE).equals("on")) {
                sw_vibrate.setChecked(true);
                Log.e("IfPrevious", "yes");
            }
            else {
                sw_vibrate.setChecked(false);
            }
        }

        sw_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if (sw_sound.isChecked()){
                    PreferenceFile.getInstance().saveData(Setting.this,Constant.SOUND,"on");
                    sw_sound.setChecked(true);
                }
                else {
                    PreferenceFile.getInstance().saveData(Setting.this,Constant.SOUND,"off");
                    sw_sound.setChecked(false);
                }
            }
        });

        sw_vibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (sw_vibrate.isChecked()){
                    PreferenceFile.getInstance().saveData(Setting.this,Constant.VIBRATE,"on");
                    sw_vibrate.setChecked(true);
                }
                else {
                    PreferenceFile.getInstance().saveData(Setting.this,Constant.VIBRATE,"off");
                    sw_vibrate.setChecked(false);
                }

            }
        });



        lnChangePin.setOnClickListener(this);

//        ivarrow.setOnClickListener(this);
        txName.setVisibility(View.VISIBLE);
        txName.setText("Settings");

        if(PreferenceFile.getInstance().getPreferenceData(this,Constant.ALERTRATEMY)!=null) {
            Log.e("hjhjhj","jjj");
            rate = PreferenceFile.getInstance().getPreferenceData(this, Constant.ALERTRATEMY);
            if (!rate.equals("")) {
                seekbarValue = rate;
                int myvalu = Integer.parseInt(rate);

                Log.e("myvalue-->",myvalu+"");
                seekbarValue = String.valueOf(myvalu);

                Log.e("seekbarValue-->",seekbarValue+"");
                seekbar.setProgress(myvalu);
                num.setText(seekbarValue+" "+PreferenceFile.getInstance().getPreferenceData(this,Constant.Currency_Symbol));
            }
        }

        seekbarMethod();

        if (Build.VERSION.SDK_INT >= 23) {

            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

            checkFingerPrintScanner();
        }

    }

    protected void checkFingerPrintScanner(){
        // Check whether the device has a Fingerprint sensor.
        if(!fingerprintManager.isHardwareDetected()){
            /**
             * An error message will be displayed if the device does not contain the fingerprint hardware.
             * However if you plan to implement a default authentication method,
             */
            //textView.setText("Your Device does not have a Fingerprint Sensor");
        }
        else {
            // Checks whether fingerprint permission is set on manifest
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
              //  textView.setText("Fingerprint authentication permission not enabled");
            }
            else{
                // Check whether at least one fingerprint is registered
                if (!fingerprintManager.hasEnrolledFingerprints()) {
                   // textView.setText("Register at least one fingerprint in Settings");
                }
                else{
                    // Checks whether lock screen security is enabled or not
                    if (!keyguardManager.isKeyguardSecure()) {
                       // textView.setText("Lock screen security not enabled in Settings");
                    }else
                    {
                        lnFingerPrintlayer.setVisibility(View.VISIBLE);

                        /*generateKey();

                        if (cipherInit()) {
                            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                            FingerprintAuthenticationHandler helper = new FingerprintAuthenticationHandler(this);
                            helper.startAuth(fingerprintManager, cryptoObject);
                        }*/
                    }
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }

        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT |
                    KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void seekbarMethod(){

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int myvalue= i+100;
                seekbarValue = String.valueOf(myvalue);
                num.setText(seekbarValue+" "+PreferenceFile.getInstance().getPreferenceData(Setting.this,Constant.Currency_Symbol));
                Log.e("value-->",seekbarValue+" ");
               // seekBar.setMax(100000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("stpo","stpo");
                rateService();

            }
        });

        sw_rate_alert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {

                if (sw_rate_alert.isChecked()){
                    PreferenceFile.getInstance().saveData(Setting.this,Constant.RATE_STATUS,"1");
                    ln_seekbar.setVisibility(View.VISIBLE);

                }
                else
                {
                    PreferenceFile.getInstance().saveData(Setting.this,Constant.RATE_STATUS,"0");
                    ln_seekbar.setVisibility(View.GONE);

                    JSONObject postParam = new JSONObject();
                    try {
                        postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(Setting.this, Constant.ID));
                        postParam.put("rate", "100");
                        postParam.put("status", "Off");
                        Log.e("postparamrateService", postParam.toString());

                        if (Constant.isConnectingToInternet(Setting.this)) {
                            Log.e("connect--->", "yes");
                            new Retrofit2(Setting.this, Setting.this, postParam, Constant.REQ_ALERT_RATE, Constant.ALERT_RATE, "3").callService(false);
                        } else {

                            Log.e("connect--->", "no");
                            Constant.alertDialog(Setting.this, getResources().getString(R.string.check_connection));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void rateService(){
        JSONObject postParam = new JSONObject();
        try {
            postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(Setting.this, Constant.ID));
            postParam.put("rate", seekbarValue);
            postParam.put("status", "On");
            Log.e("postparamrateService", postParam.toString());

            if (Constant.isConnectingToInternet(Setting.this)) {
                Log.e("connect--->", "yes");
                new Retrofit2(Setting.this, Setting.this, postParam, Constant.REQ_ALERT_RATE, Constant.ALERT_RATE, "3").callService(false);
            } else {

                Log.e("connect--->", "no");
                Constant.alertDialog(Setting.this, getResources().getString(R.string.check_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {


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

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume","onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("onRestart","onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy","onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("onPause","onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("onStop","onStop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("onStart","onStart");
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()){

//            case R.id.ivarrow:
//
//                finish();
//                break;

            case R.id.lnChangePin:

                intent=new Intent(Setting.this,ChangePassword.class);
                startActivity(intent);

                break;
        }

    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {
        switch (requestCode) {

            case Constant.REQ_ALERT_RATE:

                try {
                    JSONObject result = new JSONObject(response.body().string());
                    Log.e("result-->", result.toString());
                    String status = result.getString("response");
                    String message = result.getString("message");

                    if (status.equals("true")) {

                        JSONObject data=result.getJSONObject("data");
                        rate=data.getString("rate");
                        PreferenceFile.getInstance().saveData(this, Constant.ALERTRATEMY,rate);

                    } else {

                        Constant.alertDialog(this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

        }
}
