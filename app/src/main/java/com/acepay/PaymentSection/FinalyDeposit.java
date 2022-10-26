package com.acepay.PaymentSection;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

;

import com.acepay.Dashboard;
import com.acepay.R;
import com.acepay.ToolabarActivity;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class FinalyDeposit extends ToolabarActivity implements View.OnClickListener, RetrofitResponse {
    ImageView  ivSelectImage;
    TextView tvNext, txName, ed_refrance_number;
    MultipartBody.Part body;
    String picturePath = "", transfer_type = "";
    RadioButton rb_rtgs, rb_neft, imps, intra_bnk;
    String operator = "", macAddress = "",androidSDK, IPaddress,androidVersion, androidBrand, androidManufacturer, androidModel;
    File file;
    boolean doubleBackToExitPressedOnce = false;
    private HashMap<String, RequestBody> map;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finaly_deposit);
        tvNext = (TextView) findViewById(R.id.tvNext);
        txName = (TextView) findViewById(R.id.txName);

        rb_rtgs = (RadioButton) findViewById(R.id.rb_rtgs);
        intra_bnk = (RadioButton) findViewById(R.id.intra_bnk);
        rb_neft = (RadioButton) findViewById(R.id.rb_neft);
        imps = (RadioButton) findViewById(R.id.imps);

        ed_refrance_number = (TextView) findViewById(R.id.ed_refrance_number);
        ivSelectImage = (ImageView) findViewById(R.id.ivSelectImage);

        txName.setVisibility(View.VISIBLE);
        txName.setText("Normal Transfer");

        androidSDK = String.valueOf(android.os.Build.VERSION.SDK_INT);
        androidVersion = android.os.Build.VERSION.RELEASE;
        androidBrand = android.os.Build.BRAND;
        androidManufacturer = android.os.Build.MANUFACTURER;
        androidModel = android.os.Build.MODEL;

        Log.e("DeviceDetails", androidSDK + " " + androidVersion + " " + androidBrand + " " + androidManufacturer + " " + androidModel);

//        ivarrow = (ImageView) findViewById(R.id.ivarrow);

        tvNext.setOnClickListener(this);
//        ivarrow.setOnClickListener(this);
        ivSelectImage.setOnClickListener(this);

        rb_rtgs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                transfer_type=rb_rtgs.getText().toString();
                intra_bnk.setChecked(false);
                rb_neft.setChecked(false);
                imps.setChecked(false);

            }
        });

        intra_bnk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                transfer_type=intra_bnk.getText().toString();
                rb_rtgs.setChecked(false);
                rb_neft.setChecked(false);
                imps.setChecked(false);


            }
        });

        rb_neft.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                transfer_type=rb_neft.getText().toString();
                rb_rtgs.setChecked(false);
                intra_bnk.setChecked(false);
                imps.setChecked(false);


            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.alertDialogTwoButtons(FinalyDeposit.this,"Are you sure you want to cancel this deposit?");
            }
        });

        imps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                transfer_type=imps.getText().toString();
                rb_rtgs.setChecked(false);
                intra_bnk.setChecked(false);
                rb_neft.setChecked(false);

            }
        });

        if(getIntent()!=null){
            PreferenceFile.getInstance().saveData(FinalyDeposit.this, Constant.NORMAL_DEP_AMT, getIntent().getStringExtra("amount"));
            PreferenceFile.getInstance().saveData(FinalyDeposit.this, Constant.NORMAL_DEP_BANK_NAME, getIntent().getStringExtra("bank_name"));
            PreferenceFile.getInstance().saveData(FinalyDeposit.this, Constant.NORMAL_DEP_KEY, "normal");
            PreferenceFile.getInstance().saveData(FinalyDeposit.this, Constant.NORMAL_DEP_IFSC, getIntent().getStringExtra("ifsc"));
            PreferenceFile.getInstance().saveData(FinalyDeposit.this, Constant.NORMAL_DEP_BRANCH_NAME, getIntent().getStringExtra("brach_name"));
            PreferenceFile.getInstance().saveData(FinalyDeposit.this, Constant.NORMAL_DEP_ACC_HOLDER_NAME, getIntent().getStringExtra("account_holder_name"));
            PreferenceFile.getInstance().saveData(FinalyDeposit.this, Constant.NORMAL_DEP_BANK_ID, getIntent().getStringExtra("bank_id"));
            PreferenceFile.getInstance().saveData(FinalyDeposit.this, Constant.NORMAL_DEP_ACC_NO, getIntent().getStringExtra("account_number"));
            Log.e("FINALYDEPOSIT",PreferenceFile.getInstance().getPreferenceData(FinalyDeposit.this,Constant.NORMAL_DEP_AMT));

        }

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = networkInfo.isConnected();
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfo.isConnected();
        Log.e("Wifi connected: ", isWifiConn+" "+networkInfo.getTypeName());
        Log.e("Mobile connected: ",isMobileConn+" ");

        NetwordDetect();

    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            Log.e("Biding","once");
        }

        this.doubleBackToExitPressedOnce = true;

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
                finish();
            }
        }, 1000);
    }


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

            TelephonyManager tMgr =  (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            operator = tMgr.getNetworkOperatorName();

        }

        if(MOBILE == true)
        {
            IPaddress = GetDeviceipMobileData();
            macAddress=getMacAddr();
            TelephonyManager tManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
            operator=tManager.getNetworkOperatorName();
        }
    }
    public String GetDeviceipWiFiData() {

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

        macAddress=  wm.getConnectionInfo().getMacAddress();
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        return ip;

    }
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    //res1.append(Integer.toHexString(b & 0xFF) + ":");
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }


    public String GetDeviceipMobileData() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                NetworkInterface networkinterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkinterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip=Formatter.formatIpAddress(inetAddress.hashCode());
                        return  ip;
//                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
        }
        return null;
    }



    private void selectImage() {
        Log.i("inside select image","done");
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(FinalyDeposit.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Log.e("take photo","yes");
                     intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery")) {
                     intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();

                }
            }
        });
        builder.show();

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, Long.toString(System.currentTimeMillis()), null);
        return Uri.parse(path);
    }
    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("inner--->", requestCode + " resultCode " + resultCode);


        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {

//                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                // tvpath.setImageBitmap(photo);
//
//                picturePath = Environment.getExternalStorageDirectory().getAbsolutePath() +"/PhysicsSketchpad";
//
//                File dir = new File(picturePath);
//                if (!dir.exists())
//                    dir.mkdirs();
//
//                file = new File(dir, "sketchpad" + ".png");
//                FileOutputStream fOut = null;
//                try {
//                    fOut = new FileOutputStream(file);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                photo.compress(Bitmap.CompressFormat.PNG, 85, fOut);
//                try {
//                    fOut.flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    fOut.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                Toast.makeText(this, picturePath, Toast.LENGTH_SHORT).show();
//
//                RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                body = MultipartBody.Part.createFormData("receipt", file.getName(), reqFile);
//
//                Log.e("file exists-->", file.exists() + " file isfile-->" + file.isFile());



                Bitmap bitmap=(Bitmap)data.getExtras().get("data");
                Uri imageUri = getImageUri(getApplicationContext(),bitmap);
                Log.e("imageURI ", String.valueOf(imageUri));
                File finalFile = new File(getRealPathFromURI(imageUri));
                Log.e("finalfile ", String.valueOf(finalFile));
                picturePath=imageUri.getPath()+".jpg";
                Log.e("PathGet ",picturePath);
                file = new File(String.valueOf(finalFile));
                Log.e("file--->", file.isFile() + " " + file.exists());
                RequestBody req = RequestBody.create(MediaType.parse("multipart/form-data"), finalFile);
                body = MultipartBody.Part.createFormData("receipt", file.getName(), req);
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                Log.e("PathGet ",picturePath);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

                Toast.makeText(this, picturePath, Toast.LENGTH_SHORT).show();
                file = new File(picturePath);
                RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                body = MultipartBody.Part.createFormData("receipt", file.getName(), reqFile);
            }
        }
    }


    @Override
    public void onClick(View view) {

        Intent intent;

        switch (view.getId()) {

            case R.id.ivarrow:
//
//                Constant.alertDialogTwoButtons(this,"Are you sure you want to cancel this deposit?");
//
////                finish();
//                break;

            case R.id.ivSelectImage:
                selectImage();
                break;

            case R.id.tvNext:


                if(transfer_type.equals("")) {
                    Constant.alertDialog(this,"Please choose transaction type.");
                } else if(ed_refrance_number.getText().toString().equals("") ){
//                } else if(ed_refrance_number.getText().toString().equals("") && picturePath.equals("")){
                    Constant.alertDialog(this,"Please enter reference code.");
                }  else if( picturePath.equals("")){
                    Constant.alertDialog(this,"Please choose reference image.");
                } else {
                    map = new HashMap<String, RequestBody>();
                    map.put("user_id", RequestBody.create(MediaType.parse("multipart/form-data"), PreferenceFile.getInstance().getPreferenceData(this, Constant.ID)));
                    map.put("deposit_id", RequestBody.create(MediaType.parse("multipart/form-data"), PreferenceFile.getInstance().getPreferenceData(this,Constant.NORMAL_DEP_ID_RECVD)));
                    map.put("deposit_type", RequestBody.create(MediaType.parse("multipart/form-data"), transfer_type));

//                    if(!ed_refrance_number.getText().toString().equals("")) {
//
//                        map.put("transaction_id", RequestBody.create(MediaType.parse("multipart/form-data"), ed_refrance_number.getText().toString()));
//                    }
//                    else {
//                        map.put("transaction_id", RequestBody.create(MediaType.parse("multipart/form-data"), ""));
//                    }

                    map.put("ip", RequestBody.create(MediaType.parse("multipart/form-data"), IPaddress));
                    map.put("network", RequestBody.create(MediaType.parse("multipart/form-data"), operator));

                    map.put("mac_address", RequestBody.create(MediaType.parse("multipart/form-data"), macAddress));
                    map.put("device_sdk", RequestBody.create(MediaType.parse("multipart/form-data"), androidSDK));
                    map.put("device_version", RequestBody.create(MediaType.parse("multipart/form-data"), androidVersion));
                    map.put("device_brand",  RequestBody.create(MediaType.parse("multipart/form-data"),androidBrand));
                    map.put("device_manufacturer", RequestBody.create(MediaType.parse("multipart/form-data"), androidManufacturer));
                    map.put("device_model", RequestBody.create(MediaType.parse("multipart/form-data"), androidModel));


                    Log.e("COMPLETE_DEPOSIT",  Constant.COMPLETE_DEPOSIT+ " "+transfer_type+" "+
                            ed_refrance_number.getText().toString());


                    if (!picturePath.equals(""))
                    {

                        RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"),file);
                        body = MultipartBody.Part.createFormData("receipt", file.getName(), reqFile);

                        if (Constant.isConnectingToInternet(FinalyDeposit.this))
                        {
                            Log.e("COMPLETE_DEPOSIT",  Constant.COMPLETE_DEPOSIT);
                            Log.e("COMPLETE_DEPOSIT","depositid"+ PreferenceFile.getInstance().getPreferenceData(this,Constant.NORMAL_DEP_ID_RECVD));

                            new Retrofit2(FinalyDeposit.this, FinalyDeposit.this, map, body,
                                    Constant.REQ_COMPLETE_DEPOSIT, Constant.COMPLETE_DEPOSIT, "2").callService(true);
                        }
                        else
                        {
                            Constant.alertDialog(FinalyDeposit.this, getResources().getString(R.string.check_connection));
                        }
                    }

                    if (picturePath.equals(""))
                    {
                        if (Constant.isConnectingToInternet(FinalyDeposit.this))
                        {
                            Log.e("COMPLETE_DEPOSIT",  Constant.COMPLETE_DEPOSIT);
                            new Retrofit2(FinalyDeposit.this,
                                    FinalyDeposit.this, map,
                                    Constant.REQ_COMPLETE_DEPOSIT, Constant.COMPLETE_DEPOSIT, "4").callService(true);
                        }
                        else
                        {
                            Constant.alertDialog(FinalyDeposit.this, getResources().getString(R.string.check_connection));
                        }
                    }

                    break;
                }

        }
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode){

            case Constant.REQ_COMPLETE_DEPOSIT:
                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    PreferenceFile.getInstance().saveData(this,Constant.COUNT_SECURITY,"1");

                    String status=result1.getString("response");
                    String message=result1.getString("message");

                    Log.e("result-->",result1.toString());

                    if(status.equals("true")){

                        PreferenceFile.getInstance().saveData(this,Constant.NORMAL_DEP_IFSC,"2");

                        Log.e("SUCCESS-->",result1.toString());

                        final Dialog dialog = new Dialog(FinalyDeposit.this);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.simple_alert);

                        int width = WindowManager.LayoutParams.MATCH_PARENT;
                        int height = WindowManager.LayoutParams.WRAP_CONTENT;
                        dialog.getWindow().setLayout(width, height);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                        params.gravity = Gravity.CENTER;
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();

                        TextView  tvText = (TextView) dialog.findViewById(R.id.tvText);
                        TextView btnok = (TextView) dialog.findViewById(R.id.btnok);

                        tvText.setText(message);

                        btnok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                finish();
                                Intent intent = new Intent(FinalyDeposit.this, Dashboard.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            }
                        });

                    }
                    else {

                        Constant.alertDialog(this,message);
                    }

                } catch (JSONException e) {

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

        }

    }
}
