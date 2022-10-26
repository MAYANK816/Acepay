package com.acepay;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.inputmethodservice.Keyboard;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.acepay.FingerPrint.FingerPrintLister;
import com.acepay.FingerPrint.FingerprintAuthenticationHandler;
import com.acepay.Util.App;
import com.acepay.Util.Constant;
import com.acepay.Util.GpsTracker;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;
import com.acepay.Widget.PinEntryEditText;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Enumeration;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class LoginNew extends AppCompatActivity implements View.OnClickListener, RetrofitResponse
{
    TextView tvattemptss,tvforgot,txTxt,txUnathorized, tv_two, tv_three,
            v_four, tv_five, tv_six, tv_seven, tv_eight, tv_nine, tv_zero, tv_back,tv_ok;
    TextView edfirst, edSecond, edthird, edFouth;
    ImageView ivarrow,imFis,imS,imth,imfo;
    String confirm_secure_pin;
    Double latitude,longitude;
    GpsTracker gpsTracker;
    LinearLayout lnLayerforgot;
    String text="";
    ImageView image;
    Dialog dialog;
    int x;
    public static Boolean active=false;
    private LinearLayout eight;
    private LinearLayout five;
    private LinearLayout four;
    private LinearLayout ok;
    private LinearLayout nine;
    private StringBuffer num;
    private LinearLayout one;
    private LinearLayout seven;
    private LinearLayout six;
    private LinearLayout back;
    private LinearLayout three;
    private LinearLayout two;
    private LinearLayout zero;
    private PinEntryEditText txtPinEntry;
    private ImageView pin1,pin2,pin3,pin4;
    public KeyguardManager keyguardManager;
    public FingerprintManager fingerprintManager;
    private KeyStore keyStore;
    private Cipher cipher;
    private static final String KEY_NAME = "studytutorial";
    String operator = "", macAddress = "",androidSDK, IPaddress,androidVersion, androidBrand, androidManufacturer, androidModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        lnLayerforgot = (LinearLayout) findViewById(R.id.lnLayerforgot);
        tvattemptss = (TextView) findViewById(R.id.tvattempts);
        tvforgot = (TextView) findViewById(R.id.tvforgot);
        tvforgot.setOnClickListener(this);
        active = true;
        if (Build.VERSION.SDK_INT >= 23)
        {
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        }
        NetwordDetect();

        androidSDK = String.valueOf(android.os.Build.VERSION.SDK_INT);
        androidVersion = android.os.Build.VERSION.RELEASE;
        androidBrand = android.os.Build.BRAND;
        androidManufacturer = android.os.Build.MANUFACTURER;
        androidModel = android.os.Build.MODEL;

        if(getIntent().hasExtra("key"))
        {
            String type="";

            if(getIntent().hasExtra("type"))
            {
                type=getIntent().getStringExtra("type");
            }

            Log.e("click","yes");
            Log.e("status", PreferenceFile.getInstance().getPreferenceData(this, Constant.ComeFrom)+" "+
                    PreferenceFile.getInstance().getPreferenceData(this, Constant.CheckApp));

            if ( PreferenceFile.getInstance().getPreferenceData(this,
                    Constant.ComeFrom).equals("1") &&
                    PreferenceFile.getInstance().
                            getPreferenceData(this, Constant.CheckApp).equals("0"))
            {

                if (type.equals(""))
                {
                    Log.e("if", "yes");
                    Intent intent = new Intent(LoginNew.this, Dashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    PreferenceFile.getInstance().saveData(this, Constant.ComeFrom, "0");
                    PreferenceFile.getInstance().saveData(this, Constant.CheckApp, "0");
                }else {
                    Intent intent = new Intent(LoginNew.this, NotificationPage.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("type",type);
                    startActivity(intent);
                    PreferenceFile.getInstance().saveData(this, Constant.ComeFrom, "0");
                    PreferenceFile.getInstance().saveData(this, Constant.CheckApp, "0");
            }
            }
            else {
                    Log.e("else", "yes");
                    Intent intent = new Intent(LoginNew.this, LoginNew.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    PreferenceFile.getInstance().saveData(this, Constant.ComeFrom, "0");
                    PreferenceFile.getInstance().saveData(this, Constant.CheckApp, "0");
            }
        }
        else
        {
            PreferenceFile.getInstance().saveData(this,Constant.CheckApp,"0");
        }

        if(PreferenceFile.getInstance().getPreferenceData(LoginNew.this,Constant.Finger_Lock)!=null)
        {
            if(PreferenceFile.getInstance().getPreferenceData(LoginNew.this,Constant.Finger_Lock).equals("1")){

                if (Build.VERSION.SDK_INT >= 23) {
                    checkFingerPrintScanner();
                }
            }
            
        }

        //Log.e("secure pin-->",PreferenceFile.getInstance().getPreferenceData(this,Constant.secure_pin));

        if(PreferenceFile.getInstance().getPreferenceData(this,Constant.COUNT_SECURITY)==null){

            lnLayerforgot.setVisibility(View.GONE);
        }
        else
        {
            int count = Integer.parseInt(PreferenceFile.getInstance().getPreferenceData(this,Constant.COUNT_SECURITY));
            Log.e("count-->",count+"");

                lnLayerforgot.setVisibility(View.VISIBLE);
                count = 4 - count;
                Log.e("count-->",count+"");

                if(count==1){
                    Log.e("count-->",count+"yes");
                    Constant.alertDialog(this,"warning! This is your Last attempt otherwise your account has been blocked.");
                    tvattemptss.setText(count + " attempt remaining. ");
                }
                else {
                    Log.e("count-->",count+"no");
                    tvattemptss.setText(count + " attempts remaining. ");
                }
        }
        initializeViews();
        getLocation();

        if (Build.VERSION.SDK_INT >= 23) {

            FingerprintAuthenticationHandler.bindListener(new FingerPrintLister() {
                @Override
                public void fingerPrintcall(String e, Boolean success) {

                    Log.e("FicationHandler", "yes");

                    if (success) {

                        Log.e("success", "yes"+PreferenceFile.getInstance().getPreferenceData(LoginNew.this,
                                Constant.LOCK_PIN));
                        dialog.dismiss();
                        //todo vaishali
                        if (PreferenceFile.getInstance().getPreferenceData(LoginNew.this,
                                Constant.LOCK_PIN) != null) {
                            if (PreferenceFile.getInstance().getPreferenceData(LoginNew.this,
                                    Constant.LOCK_PIN).equals("0")) {
                                Intent intent = new Intent(LoginNew.this, Dashboard.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }
//                        callfingermethodlogin();
                            txUnathorized.setVisibility(View.GONE);
                        }
                    }
                    else {

                        txUnathorized.setVisibility(View.VISIBLE);

                        Log.e("failture", "yes");
                      /*  dialog  = new Dialog(LoginNew.this);
                        image.setImageResource(R.drawable.dot_icon);
                        txTxt.setText("Unauthorized Fingerprint.");*/
                       /* if (Build.VERSION.SDK_INT >= 23) {
                            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
                        }*/

                    }
                }
            });
        }
    }

    //FIngerPrint
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void checkFingerPrintScanner()
    {
        if(!fingerprintManager.isHardwareDetected())
        {
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

                        ImageView ivarrow;
                        dialog=new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                       // dialog  = new Dialog(this);
                        LayoutInflater li = LayoutInflater.from(this);
                        View promptsView2 = li.inflate(R.layout.fingerprint_login, null);
                        image= (ImageView) promptsView2.findViewById(R.id.ivkeypad);
                        txTxt= (TextView) promptsView2.findViewById(R.id.txTxt);
                        txUnathorized= (TextView) promptsView2.findViewById(R.id.txUnathorized);
                      ImageView  imgProfile= (ImageView) promptsView2.findViewById(R.id.imgProfile);

                        ivarrow= (ImageView) promptsView2.findViewById(R.id.ivarrow);

                        txTxt.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Username));

                        if(PreferenceFile.getInstance().getPreferenceData(this,Constant.Image)!=null){

                            Log.e("imagepath--->",Constant.ImagePath+PreferenceFile.getInstance().getPreferenceData(this,Constant.Image));

                            Picasso.with(this)
                                    .load(Constant.ImagePath+PreferenceFile.getInstance().getPreferenceData(this,Constant.Image)).resize(400,400).placeholder(getResources().getDrawable(R.drawable.placeholder))
                                    .into(imgProfile);
                        }

                        ivarrow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                finish();
                            }
                        });

                        image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog.dismiss();
                            }
                        });

                        dialog.setContentView(promptsView2);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();

                        generateKey();

                        if (cipherInit()) {
                            FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                            FingerprintAuthenticationHandler helper = new FingerprintAuthenticationHandler(this);
                            helper.startAuth(fingerprintManager, cryptoObject);
                        }
                    }
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit()
    {
        try
        {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException e)
        {
            throw new RuntimeException("Failed to get Cipher", e);
        }
        try
        {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        }
        catch (KeyPermanentlyInvalidatedException e)
        {
            return false;
        }
        catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e)
        {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey()
    {
        try
        {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
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

    public void callfingermethodlogin()
    {

        SharedPreferences sharedPreferences = App.getIdPref();
        Log.e("tokenkey--->", sharedPreferences.getString("TOKEN", ""));

        JSONObject postParam = new JSONObject();
        try {
            postParam.put("id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
            postParam.put("secure_pin", PreferenceFile.getInstance().getPreferenceData(this,Constant.secure_pin));
            postParam.put("lat", String.valueOf(latitude));
            postParam.put("lng", String.valueOf(longitude));

           // postParam.put("device_token", sharedPreferences.getString("TOKEN", ""));
            postParam.put("device_token","eert56564xxAAASS");
            postParam.put("device_id",Settings.Secure.getString(LoginNew.this.getContentResolver(), Settings.Secure.ANDROID_ID));

            postParam.put("ip",IPaddress );
            postParam.put("device_model",androidModel);
            postParam.put("network",operator);
            postParam.put("device_brand",androidBrand);


            Log.e("postparam--->", postParam.toString());

            if (Constant.isConnectingToInternet(LoginNew.this)) {
                Log.e("connect--->", "yes");
                new Retrofit2(LoginNew.this, LoginNew.this, postParam, Constant.REQ_LOGIN, Constant.LOGIN, "3").callService(true);
            } else {

                Log.e("connect--->", "no");
                Constant.alertDialog(LoginNew.this, getResources().getString(R.string.check_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop()
    {
        super.onStop();
        active = false;
    }

    void callfunction()
    {
        confirm_secure_pin=text;
        //Log.e("confirm_secure_pin-->",confirm_secure_pin);

        if (confirm_secure_pin.equals(""))
        {
            Constant.alertDialog(this, getResources().getString(R.string.please_enter_secure_pin));
        }

        else if(PreferenceFile.getInstance().
                getPreferenceData(this,Constant.secure_pin)!=null)
        {
            if (confirm_secure_pin.
                    equals(PreferenceFile.getInstance().getPreferenceData(this, Constant.secure_pin)))
            {
                SharedPreferences sharedPreferences = App.getIdPref();
                Log.e("tokenkey---> ", sharedPreferences.getString("TOKEN", ""));

                JSONObject postParam = new JSONObject();
                try
                {
                    postParam.put("id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
                    postParam.put("secure_pin", confirm_secure_pin);
                    postParam.put("lat", String.valueOf(latitude));
                    postParam.put("lng", String.valueOf(longitude));
                    postParam.put("ip",IPaddress );
                    postParam.put("device_model",androidModel);
                    postParam.put("network",operator);
                    postParam.put("device_brand",androidBrand);

//                    postParam.put("device_token", sharedPreferences.getString("TOKEN", ""));
                    postParam.put("device_token","shdgegrfye3743765354ndfnfdbj");
                    postParam.put("device_id",Settings.Secure.getString(LoginNew.this.getContentResolver(), Settings.Secure.ANDROID_ID));

                    Log.e("postparam--->", postParam.toString());

                    if (Constant.isConnectingToInternet(LoginNew.this))
                    {
                        Log.e("connect--->", "yes");
                        new Retrofit2(LoginNew.this, LoginNew.this, postParam, Constant.REQ_LOGIN, Constant.LOGIN, "3").
                                callService(true);
                    }
                    else
                    {
                        Log.e("connect--->", "no");
                        Constant.alertDialog(LoginNew.this, getResources().getString(R.string.check_connection));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else
                {
                txtPinEntry.setText(null);
                num.delete(0, num.length());
                Log.e("show-->","yes");
                if(PreferenceFile.getInstance().getPreferenceData(this,Constant.COUNT_SECURITY)==null)
                {
                    PreferenceFile.getInstance().saveData(this,Constant.COUNT_SECURITY,"1");

                    int count= Integer.parseInt(PreferenceFile.getInstance().getPreferenceData(this,Constant.COUNT_SECURITY));

                    if(count < 4)
                    {
                        lnLayerforgot.setVisibility(View.VISIBLE);
                        count = 4 - count;

                        if(count==1)
                        {
                            Log.e("count-->",count+"yes");
                            Constant.alertDialog(this,"warning! This is your Last attempt otherwise your account has been blocked.");
                            tvattemptss.setText(count + " attempt remaining. ");
                        }
                        else
                        {
                            tvattemptss.setText(count + " attempts remaining. ");
                        }
                        //tvattemptss.setText(count + " attemptss remaining. ");
                    }
                }
                else {
                    x= Integer.parseInt(PreferenceFile.getInstance().getPreferenceData(this,Constant.COUNT_SECURITY));
                    Log.e("x-->",x+"");
                    x++;
                    PreferenceFile.getInstance().saveData(this,Constant.COUNT_SECURITY,String.valueOf(x));
                    int count= Integer.parseInt(PreferenceFile.getInstance().getPreferenceData(this,Constant.COUNT_SECURITY));

                    if(count<4) {

                        lnLayerforgot.setVisibility(View.VISIBLE);
                        count = 4 - count;
                        if(count==1){
                            Log.e("count-->",count+"yes");
                            Constant.alertDialog(this,"warning! This is your Last attempt otherwise your account has been block.");
                            tvattemptss.setText(count + " attempt remaining. ");
                        }
                        else {

                            tvattemptss.setText(count + " attempts remaining. ");
                        }
                    }
                    if(x==4){

                        JSONObject postParam = new JSONObject();
                        try {
                            postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
                            postParam.put("phone", PreferenceFile.getInstance().getPreferenceData(this, Constant.phone));
                            Log.e("postparam--->", postParam.toString());

                            if (Constant.isConnectingToInternet(LoginNew.this)) {
                                Log.e("connect--->", "yes");
                                new Retrofit2(LoginNew.this, LoginNew.this, postParam, Constant.REQ_Block_USER, Constant.Block_USER, "3").callService(true);
                            }
                            else {

                                Log.e("connect--->", "no");
                                Constant.alertDialog(LoginNew.this, getResources().getString(R.string.check_connection));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Log.e("regenrate secure-->",x+"");
                    }

                }

              /*edfirst.setText("");
                edSecond.setText("");
                edthird.setText("");
                edFouth.setText("");
                imFis.setVisibility(View.GONE);
                imS.setVisibility(View.GONE);
                imth.setVisibility(View.GONE);
                imfo.setVisibility(View.GONE);
                edfirst.setVisibility(View.VISIBLE);
                edSecond.setVisibility(View.VISIBLE);
                edthird.setVisibility(View.VISIBLE);
                edFouth.setVisibility(View.VISIBLE);
                edfirst.requestFocus();*/

                if(x<4)
                {
                    Constant.alertDialog(this, getResources().getString(R.string.incorrect_secure_pin));
                }
            }
        }

        else {
            if(PreferenceFile.getInstance().getPreferenceData(this,Constant.COUNT_SECURITY)==null)
            {
                x =0;
            }
            else {
                x=Integer.parseInt(PreferenceFile.getInstance().getPreferenceData(this,Constant.COUNT_SECURITY));
            }

            if(x < 4) {

                SharedPreferences sharedPreferences = App.getIdPref();
                Log.e("tokenkey--->", sharedPreferences.getString("TOKEN", ""));

                JSONObject postParam = new JSONObject();
                try {
                    postParam.put("id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
                    postParam.put("secure_pin", confirm_secure_pin);
                    postParam.put("lat", String.valueOf(latitude));
                    postParam.put("lng", String.valueOf(longitude));

                    postParam.put("ip",IPaddress );
                    postParam.put("device_model",androidModel);
                    postParam.put("network",operator);
                    postParam.put("device_brand",androidBrand);

//                    postParam.put("device_token", sharedPreferences.getString("TOKEN", ""));
                    postParam.put("device_token", "Sawrrr655465t");
                    postParam.put("device_id",Settings.Secure.getString(LoginNew.this.getContentResolver(), Settings.Secure.ANDROID_ID));

                    Log.e("postparam--->", postParam.toString());

                    if (Constant.isConnectingToInternet(LoginNew.this)) {
                        Log.e("connect--->", "yes");
                        new Retrofit2(LoginNew.this, LoginNew.this, postParam,
                                Constant.REQ_LOGIN, Constant.LOGIN, "3").callService(true);
                    } else {

                        Log.e("connect--->", "no");
                        Constant.alertDialog(LoginNew.this, getResources().getString(R.string.check_connection));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if(x==4)
            {

                JSONObject postParam = new JSONObject();
                try {
                    postParam.put("user_id", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID));
                    postParam.put("phone", PreferenceFile.getInstance().getPreferenceData(this, Constant.phone));
                    Log.e("postparam--->", postParam.toString());

                    if (Constant.isConnectingToInternet(LoginNew.this))
                    {
                        Log.e("connect--->", "yes");
                        new Retrofit2(LoginNew.this, LoginNew.this, postParam, Constant.REQ_Block_USER,
                                Constant.Block_USER, "3").callService(true);
                    }
                    else
                    {
                        Log.e("connect--->", "no");
                        Constant.alertDialog(LoginNew.this, getResources().getString(R.string.check_connection));
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

                Log.e("regenrate secure-->",x+"");
            }
        }

    }

    private void initializeViews()
    {
        txtPinEntry = (PinEntryEditText) findViewById(R.id.txt_pin_entry);
        //pin1 = (ImageView) findViewById(R.id.pin1);
        ivarrow = (ImageView) findViewById(R.id.ivarrow);
//        pin2 = (ImageView) findViewById(R.id.pin2);
//        pin3 = (ImageView) findViewById(R.id.pin3);
//        pin4 = (ImageView) findViewById(R.id.pin4);

        if (txtPinEntry != null)
        {
            txtPinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    if (str.length()==4)
                    {
                        text=str.toString();
                        callfunction();
                    }
                }

                @Override
                public void onTextChange(CharSequence str) {
                   //switch(str.length())
//                    {
//                        case 0:
//                            pin1.setImageResource(R.color.transparent);
//                            pin2.setImageResource(R.color.transparent);
//                            pin3.setImageResource(R.color.transparent);
//                            pin4.setImageResource(R.color.transparent);
//                            break;
//                        case 1:
//                            pin1.setImageResource(R.drawable.password);
//                            break;
//                        case 2:
//                            pin2.setImageResource(R.drawable.password);
//                            break;
//                        case 3:
//                            pin3.setImageResource(R.drawable.password);
//                            break;
//                        case 4:
//                            pin4.setImageResource(R.drawable.password);
//                            break;
                    //}
                }
            });
        }
        this.num = new StringBuffer();
        this.num.append("");

        this.one = (LinearLayout) findViewById(R.id.one);
        this.two = (LinearLayout) findViewById(R.id.two);
        this.three = (LinearLayout) findViewById(R.id.three);
        this.four = (LinearLayout) findViewById(R.id.four);
        this.five = (LinearLayout) findViewById(R.id.five);
        this.six = (LinearLayout) findViewById(R.id.six);
        this.seven = (LinearLayout) findViewById(R.id.seven);
        this.eight = (LinearLayout) findViewById(R.id.eight);
        this.nine = (LinearLayout) findViewById(R.id.nine);
        this.zero = (LinearLayout)findViewById(R.id.zero);
        this.back = (LinearLayout) findViewById(R.id.back);
        this.ok = (LinearLayout)findViewById(R.id.ok);

        this.one.setOnClickListener(this);
        this.two.setOnClickListener(this);
        this.three.setOnClickListener(this);
        this.four.setOnClickListener(this);
        this.five.setOnClickListener(this);
        this.six.setOnClickListener(this);
        this.seven.setOnClickListener(this);
        this.eight.setOnClickListener(this);
        this.nine.setOnClickListener(this);
        this.zero.setOnClickListener(this);
        this.back.setOnClickListener(this);
        this.ok.setOnClickListener(this);
    }

    public void getLocation()
    {

        gpsTracker = new GpsTracker(LoginNew.this);

        if (gpsTracker.canGetLocation())
        {
            longitude = gpsTracker.getLongitude();
            latitude = gpsTracker.getLatitude();

        }
        else
        {

            AlertDialog.Builder alertDialog;

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

                alertDialog = new AlertDialog.Builder(this,android.R.style.Theme_Material_Light_Dialog_Alert);
            }else {
                alertDialog = new AlertDialog.Builder(this);
            }

            alertDialog.setCancelable(false);
            // Setting Dialog Title
            alertDialog.setTitle(this.getResources().getString(R.string.gps_setting));//gps_setting
            alertDialog.setMessage(this.getResources().getString(R.string.gps_not_enable));
            alertDialog.setPositiveButton(this.getResources().getString(R.string.action_settings), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {
                    startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);

                }
            });

            alertDialog.show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            new CountDownTimer(50000, 2000)
            {
                public void onTick(long millisUntilFinished)
                {
                    latitude=GpsTracker.latitude;
                    longitude=GpsTracker.longitude;
                    Log.e("lati-->",latitude+" longi--->"+longitude);
                }
                public void onFinish()
                {

                }
            }.start();
        }
    }


    public void onClick(View view) {
        if(this.txtPinEntry.getText().toString().length()>4)
        {
            txtPinEntry.setText(null);
            num.delete(0, num.length());
        }

        switch (view.getId())
        {

            case R.id.tvforgot:

                Intent intent1=new Intent(LoginNew.this,ForgotPin.class);
                startActivity(intent1);
                return;

            case R.id.one:
                this.num.append("1");
                this.txtPinEntry.setText(this.num);
                return;
            case R.id.two:
                this.num.append("2");
                this.txtPinEntry.setText(this.num);
                return;
            case R.id.three:
                this.num.append("3");
                this.txtPinEntry.setText(this.num);
                return;
            case R.id.four:
                this.num.append("4");
                this.txtPinEntry.setText(this.num);
                return;
            case R.id.five:
                this.num.append("5");
                this.txtPinEntry.setText(this.num);
                return;
            case R.id.six:
                this.num.append("6");
                this.txtPinEntry.setText(this.num);
                return;
            case R.id.seven:
                this.num.append("7");
                this.txtPinEntry.setText(this.num);
                return;
            case R.id.eight:
                this.num.append("8");
                this.txtPinEntry.setText(this.num);
                return;
            case R.id.nine:
                this.num.append("9");
                this.txtPinEntry.setText(this.num);
                return;
            case R.id.back:
                if(num.length()>0)
                {
                    num.deleteCharAt(num.length()-1);
                    Log.e("num", num.toString()+" "+num.length());

                    this.txtPinEntry.setText(num.substring(0));
                }
                return;
            case R.id.zero:
                this.num.append("0");
                this.txtPinEntry.setText(this.num);
                return;
            case R.id.ok:
                callfunction();
                return;
            default:
                return;
        }
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response)
    {

        switch (requestCode) {

            case Constant.REQ_LOGIN:

                Log.e("InsideLoginnn ","inside");
                try {
                if (response.isSuccessful())
                 {

                        try {
                            JSONObject result1 = new JSONObject(response.body().string());
                            Log.e("req_sign_up--->", "yes");
                            Log.e("resultttt-->", result1.toString());
                            String status = result1.getString("response");
                            String message = result1.getString("message");

                            if (status.equals("true"))
                            {
                                PreferenceFile.getInstance().saveData(this,Constant.COUNT_SECURITY,"1");
                                JSONObject result = result1.getJSONObject("data");

                                Log.e("status--->", status + "");
                                Log.e("id-->", result.getString("id"));

                                PreferenceFile.getInstance().saveData(this, Constant.ID, result.getString("id"));
                                PreferenceFile.getInstance().saveData(this, Constant.phone, result.getString("phone"));
                                PreferenceFile.getInstance().saveData(this, Constant.country_code, result.getString("country_code"));
                                PreferenceFile.getInstance().saveData(this, Constant.secure_pin, result.getString("secure_pin"));
                                PreferenceFile.getInstance().saveData(this, Constant.Inr_Amount, result.getString("inr_amount"));
                                PreferenceFile.getInstance().saveData(this, Constant.BTC_amount, result.getString("btc_amount"));
                                PreferenceFile.getInstance().saveData(this, Constant.Lock_Transaction, result.getString("transaction_log"));

                                PreferenceFile.getInstance().saveData(this, Constant.Courtry_id, result.getString("country"));
                                PreferenceFile.getInstance().saveData(this, Constant.REFERCODE, result.getString("refer_code"));

                                Log.e("idddddd-->", result.getString("id"));

                                if(!result.getString("first_name").equals("null")){

                                    PreferenceFile.getInstance().saveData(this, Constant.Username, result.getString("first_name")+" "+result.getString("last_name"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Email, result.getString("email"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Dob, result.getString("dob"));
                                    PreferenceFile.getInstance().saveData(this, Constant.email_verification, result.getString("verification"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Gender, result.getString("gender"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Image, result.getString("image"));

                                    Log.e("resultimage-->",result.getString("image"));
                                    PreferenceFile.getInstance().saveData(this, Constant.City_name, result.getString("city"));
                                    PreferenceFile.getInstance().saveData(this, Constant.VERIFY_PAN, result.getString("verify_pan"));
                                    PreferenceFile.getInstance().saveData(this, Constant.VERIFY_BANK, result.getString("verify_bank"));
                                    PreferenceFile.getInstance().saveData(this, Constant.VERIFY_Adhaar, result.getString("verify_add"));

                                    if(!result.isNull("block_address")){

                                        PreferenceFile.getInstance().saveData(this, Constant.BITCOIN_ADDRESS, result.getString("block_address"));
                                    }

                                    JSONObject StateName=result.getJSONObject("StateName");
                                    PreferenceFile.getInstance().saveData(this, Constant.State_name, StateName.getString("name"));
                                    PreferenceFile.getInstance().saveData(this, Constant.State_id, StateName.getString("id"));

                                    Log.e("city-->",PreferenceFile.getInstance().getPreferenceData(this,Constant.City_name));
                                    Log.e("city-->",PreferenceFile.getInstance().getPreferenceData(this,Constant.phone));

                                    JSONObject CountryName=result.getJSONObject("CountryName");
                                    PreferenceFile.getInstance().saveData(this, Constant.Country_name, CountryName.getString("name"));
                                    PreferenceFile.getInstance().saveData(this, Constant.Country_id, CountryName.getString("id"));

                                    if(!result.isNull("BankName")){

                                        Log.e("bank-->","yes");

                                        JSONObject BankName=result.getJSONObject("BankName");
                                        PreferenceFile.getInstance().saveData(this,Constant.BANK_NAME,BankName.getString("bank_name"));
                                        PreferenceFile.getInstance().saveData(this,Constant.ACCOUNT_TYPE,BankName.getString("account_type"));
                                        PreferenceFile.getInstance().saveData(this,Constant.BRANCH,BankName.getString("branch"));
                                        PreferenceFile.getInstance().saveData(this,Constant.PASSBOOK_IMAGE,BankName.getString("passbook_image"));
                                        PreferenceFile.getInstance().saveData(this,Constant.IFSC,BankName.getString("ifsc"));
                                        PreferenceFile.getInstance().saveData(this,Constant.ACCOUNT_HOLDER,BankName.getString("account_holder"));
                                        PreferenceFile.getInstance().saveData(this,Constant.ACCOUNT_NUMBER,BankName.getString("account_number"));
                                    }

                                    if(!result.isNull("AddName")) {

                                        Log.e("AddName -->","yes");
                                        JSONObject AddName=result.getJSONObject("AddName");
                                        PreferenceFile.getInstance().saveData(this,Constant.Adhaar_image,AddName.getString("aadhar"));
                                        PreferenceFile.getInstance().saveData(this,Constant.Adhaar_image_back,AddName.getString("aadhar_back"));
                                        PreferenceFile.getInstance().saveData(this,Constant.Adhaar_number,AddName.getString("aadhar_number"));
                                        PreferenceFile.getInstance().saveData(this,Constant.Adhaar_line1,AddName.getString("line1"));
                                        PreferenceFile.getInstance().saveData(this,Constant.Adhaar_line2,AddName.getString("line2"));
                                        PreferenceFile.getInstance().saveData(this,Constant.LANDMARK,AddName.getString("landmark"));
                                        PreferenceFile.getInstance().saveData(this,Constant.Aadhar_city,AddName.getString("city"));
                                        PreferenceFile.getInstance().saveData(this,Constant.Aadhar_state,AddName.getString("state"));

                                        JSONObject state=AddName.getJSONObject("StateName");
                                        PreferenceFile.getInstance().saveData(this,Constant.Aadhar_state,state.getString("name"));

                                    }
                                    if(!result.isNull("PanName")) {
                                        Log.e("PanName-->","yes");

                                        JSONObject PanName=result.getJSONObject("PanName");
                                        PreferenceFile.getInstance().saveData(this,Constant.Pan_name,PanName.getString("name"));
                                        PreferenceFile.getInstance().saveData(this,Constant.Pan_last,PanName.getString("last_name"));
                                        PreferenceFile.getInstance().saveData(this,Constant.Pan_image,PanName.getString("image"));
                                        PreferenceFile.getInstance().saveData(this,Constant.Pan_number,PanName.getString("pan_number"));
                                        PreferenceFile.getInstance().saveData(this,Constant.Pan_dob,PanName.getString("dob"));
                                        PreferenceFile.getInstance().saveData(this,Constant.Pan_gender,PanName.getString("gender"));
                                    }

                                    Intent intent = new Intent(LoginNew.this, Dashboard.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                }
                                else {

                                    Intent intent = new Intent(LoginNew.this, EditProfile.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }

                                PreferenceFile.getInstance().saveData(this,Constant.COUNT_SECURITY,"1");

                            }

                            else
                            {
                                chkNumber();
//                            if(status.equalsIgnoreCase("false"))
//                            {
//                                chkNumber();
//                            }
                            }

                        } catch (JSONException e){
                            Log.e("exception-->", e.toString());
                        }
                        catch (IOException e) {

                        }
                    }
                   }

                catch (Exception ex)
                   {
                    ex.printStackTrace();
                   }

                break;


            case Constant.REQ_CHECK_NO:
                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    String status = result1.getString("response");
                    String message = result1.getString("message");

                    Log.e("REQ_CHECK_NOLoginnew", "onServiceResponse: "+result1.toString() );

                    if (status.equals("true")) {

                        if(PreferenceFile.getInstance().getPreferenceData(this,Constant.COUNT_SECURITY)==null)
                        {
                            PreferenceFile.getInstance().saveData(this,Constant.COUNT_SECURITY,"1");
                            int count= Integer.parseInt(PreferenceFile.getInstance().getPreferenceData(this,Constant.COUNT_SECURITY));

                            if(count < 3) {

                                lnLayerforgot.setVisibility(View.VISIBLE);
                                count = 4 - count;

                                if(count==1){
                                    Log.e("count-->",count+"yes");
                                    Constant.alertDialog(this,"warning! This is your Last attempt otherwise your account has been block.");
                                    tvattemptss.setText(count + " attempt remaining. ");
                                }
                                else {

                                    tvattemptss.setText(count + " attempts remaining. ");
                                }
                            }
                        }
                        else {

                            x= Integer.parseInt(PreferenceFile.getInstance().getPreferenceData(this,Constant.COUNT_SECURITY));
                            x++;
                            PreferenceFile.getInstance().saveData(this,Constant.COUNT_SECURITY,String.valueOf(x));
                            Log.e("x-->",x+"");

                            int count= Integer.parseInt(PreferenceFile.getInstance().getPreferenceData(this,Constant.COUNT_SECURITY));

                            if(count< 3) {

                                lnLayerforgot.setVisibility(View.VISIBLE);
                                count = 4 - count;

                                if(count==1){
                                    Log.e("count-->",count+"yes");
                                    Constant.alertDialog(this,"warning! This is your Last attempt otherwise your account has been blocked.");
                                    tvattemptss.setText(count + " attempt remaining. ");
                                }
                                else {

                                    tvattemptss.setText(count + " attempts remaining. ");
                                }
                            }
                        }
                        txtPinEntry.setText(null);
                        num.delete(0, num.length());

                        Log.e("data--->", status + "");
                        Constant.alertDialog(LoginNew.this, message);

                    }

                    else {

                        final Dialog dialog = new Dialog(LoginNew.this);

                        dialog.setCancelable(false);


                        dialog.setContentView(R.layout.constant_dialog);

                        int width = WindowManager.LayoutParams.MATCH_PARENT;
                        int height = WindowManager.LayoutParams.WRAP_CONTENT;
                        dialog.getWindow().setLayout(width, height);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                        params.gravity = Gravity.CENTER;

                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();

                        TextView tvText = (TextView) dialog.findViewById(R.id.tvText);
                        TextView btnok = (TextView) dialog.findViewById(R.id.btnok);
                        tvText.setText("User does not exist.");

                        btnok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // moveTaskToBack(true);
                                PreferenceFile.getInstance().logout();
                                Intent i=new Intent(LoginNew.this,DeleteScreen.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                dialog.dismiss();

                            }
                        });





                    }


                }

                catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case Constant.REQ_Block_USER:

                if (response.isSuccessful())
                {
                    JSONObject result1 = null;
                    try {
                        result1 = new JSONObject(response.body().string());
                        Log.e("req_sign_up--->", "yes");
                        Log.e("resultttt-->", result1.toString());
                        String Bastatus = result1.getString("response");
                        String message = result1.getString("message");

                        PreferenceFile.getInstance().saveData(this,Constant.COUNT_SECURITY,"4");
                        PreferenceFile.getInstance().saveData(this,Constant.Accunt_status,"Inactive");

                        Constant.alertWithIntent(this,"Your Account has been Blocked.",BlockScreen.class);


                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;

        }
    }


    //Check the internet connection.
    private void NetwordDetect() {

        boolean WIFI = false;

        boolean MOBILE = false;

        ConnectivityManager CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] networkInfo = CM.getAllNetworkInfo();

        for (NetworkInfo netInfo : networkInfo) {

            if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))

                if (netInfo.isConnected())

                    WIFI = true;

            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))

                if (netInfo.isConnected())

                    MOBILE = true;
        }

        if(WIFI == true)
        {
            IPaddress = GetDeviceipWiFiData();
            Log.e("ipwifi-->",IPaddress);

            TelephonyManager tMgr =  (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            operator = tMgr.getNetworkOperatorName();
            Log.e("operator-->",operator);

        }

        if(MOBILE == true)
        {
            IPaddress = GetDeviceipMobileData();
            Log.e("inMobile-->",IPaddress);

            TelephonyManager tManager = (TelephonyManager) getBaseContext()
                    .getSystemService(Context.TELEPHONY_SERVICE);

            operator=tManager.getNetworkOperatorName();
            Log.e("tManager-->",tManager.getNetworkOperatorName());

        }
    }

    public String GetDeviceipMobileData(){
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements();) {
                NetworkInterface networkinterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkinterface.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("Current IP", ex.toString());
        }
        return null;
    }

    public String GetDeviceipWiFiData()
    {

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        macAddress=  wm.getConnectionInfo().getMacAddress();
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        return ip;

    }


    private void chkNumber(){

        JSONObject postParam = new JSONObject();

        try {

            postParam.put("phone", PreferenceFile.getInstance().getPreferenceData(this, Constant.phone));

            Log.e("PostParams ",postParam.toString());

            if (Constant.isConnectingToInternet(LoginNew.this)) {
                new Retrofit2(LoginNew.this, LoginNew.this, postParam, Constant.REQ_CHECK_NO,
                        Constant.CHECK_NO, "3").callService(true);
            } else {
                Constant.alertDialog(LoginNew.this, getResources().getString(R.string.check_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

