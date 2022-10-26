package com.acepay;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.acepay.Croping.Croping;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class BankDetailsVerification extends ToolabarActivity implements View.OnClickListener , RetrofitResponse {
    LinearLayout lnBank,lnAdhar,lnPan;
    TextView txName,tvTitle;
    Spinner spAccounttype;
    ImageView ivImageSelect,imgProfile;
    TextView txNext,txstatus;
    EditText edIfcs,edbackname,edbranch,edhaldername,edAccountNumber,edConfirmAccount;
    String path="",imagepath="";
    File file;
    Intent CamIntent,GalIntent,CropIntent;
    Uri uri;
    RequestBody req;
    ScrollView scrollView;
    Bitmap bitmap;
    int flag=0;
    Context context;
    boolean doubleBackToExitPressedOnce = false;
    private File profileIMG;
    String result;
    MultipartBody.Part body;
    String[] defaultaccount = {"Select account type","Saving Account","Current Account"};
    private HashMap<String, RequestBody> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details_verification);
//        setContentView(R.layout.bcc);
        lnBank= (LinearLayout) findViewById(R.id.lnBank);
        lnAdhar= (LinearLayout) findViewById(R.id.lnAdhar);
        lnPan= (LinearLayout) findViewById(R.id.lnPan);
        txName= (TextView) findViewById(R.id.txName);
        tvTitle= (TextView) findViewById(R.id.tvTitle);
        context=this;

        edbackname= (EditText) findViewById(R.id.edbackname);
        edIfcs= (EditText) findViewById(R.id.edIfcs);
        spAccounttype= (Spinner) findViewById(R.id.spAccounttype);
        edbranch= (EditText) findViewById(R.id.edbranch);
        edhaldername= (EditText) findViewById(R.id.edhaldername);
        txNext= (TextView) findViewById(R.id.txNext);
        txstatus= (TextView) findViewById(R.id.txstatus);
        ivImageSelect= (ImageView) findViewById(R.id.ivImageSelect);
        imgProfile= (ImageView) findViewById(R.id.imgProfile);
        edAccountNumber= (EditText) findViewById(R.id.edAccountNumber);
        edConfirmAccount= (EditText) findViewById(R.id.edConfirmAccount);

      //  edIfcs.setAllCaps(true);
        edIfcs.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

//        ivarrow= (ImageView) findViewById(R.id.ivarrow);
//
        spAccounttype.setAdapter(new ArrayAdapter<String>(this,R.layout.new_text,defaultaccount));
        spAccounttype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.gradient_1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        PreferenceFile.getInstance().saveData(this,Constant.ACCOUNT_TYPE,"Saving Account");

        callBankDetailSetup();



    }


    public void callBankDetailSetup()
    {
        if (PreferenceFile.getInstance().getPreferenceData(this, Constant.VERIFY_BANK).equals("Yes")) {

            txstatus.setText("Approved");
            txstatus.setTextColor(Color.parseColor("#2fd49e"));
            txstatus.setVisibility(View.VISIBLE);
            flag=1;
            edAccountNumber.setEnabled(false);
            edbackname.setEnabled(false);
            edbranch.setEnabled(false);
            edConfirmAccount.setEnabled(false);
            edIfcs.setEnabled(false);
            edhaldername.setEnabled(false);
            spAccounttype.setEnabled(false);

            tvTitle.setText("Bank details");

            edAccountNumber.setTextColor(getResources().getColor(R.color.gradient_1));
            edbackname.setTextColor(getResources().getColor(R.color.gradient_1));
            edbranch.setTextColor(getResources().getColor(R.color.gradient_1));
            edConfirmAccount.setTextColor(getResources().getColor(R.color.gradient_1));
            edIfcs.setTextColor(getResources().getColor(R.color.gradient_1));
            edhaldername.setTextColor(getResources().getColor(R.color.gradient_1));

            edIfcs.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.IFSC));
            edbackname.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.BANK_NAME));
            edbranch.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.BRANCH));
            edbranch.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.BRANCH));
            edhaldername.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_HOLDER));
            edAccountNumber.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_NUMBER));
            edConfirmAccount.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_NUMBER));

            Log.e("user bank-->",Constant.BANK_IMAGE+PreferenceFile.getInstance().getPreferenceData(this,Constant.PASSBOOK_IMAGE));
            Picasso.with(this)
                    .load(Constant.BANK_IMAGE+PreferenceFile.getInstance().getPreferenceData(this,Constant.PASSBOOK_IMAGE)).resize(800,800).placeholder(getResources().getDrawable(R.drawable.placeholder))
                    .into(imgProfile);

            if(PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_TYPE).equals("Saving Account")){
                spAccounttype.setSelection(1);
            }
            else {
                spAccounttype.setSelection(2);
            }


        }

       else  if (PreferenceFile.getInstance().getPreferenceData(this, Constant.VERIFY_BANK).equals("No"))
        {
            txstatus.setText("Rejected");
            txstatus.setTextColor(Color.parseColor("#d40e0e"));
            txstatus.setVisibility(View.VISIBLE);
            flag=0;
            edAccountNumber.setEnabled(true);
            edbackname.setEnabled(true);
            edbranch.setEnabled(true);
            edConfirmAccount.setEnabled(true);
            edIfcs.setEnabled(true);
            edhaldername.setEnabled(true);
            spAccounttype.setEnabled(true);

            tvTitle.setText("Bank details");

            edAccountNumber.setTextColor(getResources().getColor(R.color.gradient_1));
            edbackname.setTextColor(getResources().getColor(R.color.gradient_1));
            edbranch.setTextColor(getResources().getColor(R.color.gradient_1));
            edConfirmAccount.setTextColor(getResources().getColor(R.color.gradient_1));
            edIfcs.setTextColor(getResources().getColor(R.color.gradient_1));
            edhaldername.setTextColor(getResources().getColor(R.color.gradient_1));
        }


        else{
            tvTitle.setText("Enter your Bank details");
        }

        if(PreferenceFile.getInstance().getPreferenceData(this,Constant.BANK_NAME)!=null){

            if(PreferenceFile.getInstance().getPreferenceData(this,Constant.VERIFY_BANK).equals("Pending")){

                txstatus.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.VERIFY_BANK));

                txstatus.setVisibility(View.VISIBLE);
                txstatus.setText("Pending");
                txstatus.setTextColor(Color.parseColor("#d40e0e"));
                txstatus.setVisibility(View.VISIBLE);
                flag=1;
                edAccountNumber.setEnabled(false);
                edbackname.setEnabled(false);
                edbranch.setEnabled(false);
                edConfirmAccount.setEnabled(false);
                edIfcs.setEnabled(false);
                edhaldername.setEnabled(false);
                spAccounttype.setEnabled(false);

                edAccountNumber.setTextColor(getResources().getColor(R.color.gradient_1));
                edbackname.setTextColor(getResources().getColor(R.color.gradient_1));
                edbranch.setTextColor(getResources().getColor(R.color.gradient_1));
                edConfirmAccount.setTextColor(getResources().getColor(R.color.gradient_1));
                edIfcs.setTextColor(getResources().getColor(R.color.gradient_1));
                edhaldername.setTextColor(getResources().getColor(R.color.gradient_1));


                edIfcs.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.IFSC));
                edbackname.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.BANK_NAME));
                edbranch.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.BRANCH));
                edbranch.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.BRANCH));
                edhaldername.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_HOLDER));
                edAccountNumber.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_NUMBER));
                edConfirmAccount.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_NUMBER));

                Log.e("user bank-->",Constant.BANK_IMAGE+PreferenceFile.getInstance().getPreferenceData(this,Constant.PASSBOOK_IMAGE));
                Picasso.with(this)
                        .load(Constant.BANK_IMAGE+PreferenceFile.getInstance().getPreferenceData(this,Constant.PASSBOOK_IMAGE)).resize(800,800).placeholder(getResources().getDrawable(R.drawable.placeholder))
                        .into(imgProfile);

                if(PreferenceFile.getInstance().getPreferenceData(this,Constant.ACCOUNT_TYPE).equals("Saving Account")){
                    spAccounttype.setSelection(1);
                }
                else {
                    spAccounttype.setSelection(2);
                }
            }


        }

        lnBank.setBackground(getResources().getDrawable(R.drawable.pan_primary_center));
        lnAdhar.setBackground(getResources().getDrawable(R.drawable.grey_center));
        lnPan.setBackground(getResources().getDrawable(R.drawable.pan_grey_one));

        lnBank.setOnClickListener(this);
        lnAdhar.setOnClickListener(this);
        lnPan.setOnClickListener(this);
        txNext.setOnClickListener(this);
        ivImageSelect.setOnClickListener(this);

        txName.setText("Account Verification");

        edIfcs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (edIfcs.getText().toString().length() == 11)     //size as per your requirement
                {
                    Constant.hideKeyboard(context,edIfcs);
                    Log.e("call service-->","yes");
                    call();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void call() {

        Log.e("hit-->","yes");
        new Retrofit2(this, BankDetailsVerification.this, Constant.REQ_IFSC_API,
                Constant.IFSC_API+edIfcs.getText().toString(),1).callService(true);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()){

            case R.id.lnAdhar:
                if (PreferenceFile.getInstance().getPreferenceData(this, Constant.VERIFY_Adhaar).equals("Yes")) {
                    tvTitle.setText("Aadhaar details");
                }else{
                    tvTitle.setText("Enter your Aadhaar details");

                }
                lnBank.setBackground(getResources().getDrawable(R.drawable.grey_center));
                lnAdhar.setBackground(getResources().getDrawable(R.drawable.pan_primary_right));
                lnPan.setBackground(getResources().getDrawable(R.drawable.pan_grey_one));
                finish();
                 intent=new Intent(BankDetailsVerification.this,AdhaarCardVerification.class);
                 startActivity(intent);
                break;

            case R.id.ivImageSelect:
               // selectImage();

                if(flag==0) {
                    popup();
                }

                break;

            case R.id.lnPan:

                if (PreferenceFile.getInstance().getPreferenceData(this, Constant.VERIFY_PAN).equals("Yes")) {
                    tvTitle.setText("Pan Card details");
                }else{
                    tvTitle.setText("Enter your Pan Card details");
                }
                lnBank.setBackground(getResources().getDrawable(R.drawable.grey_center));
                lnAdhar.setBackground(getResources().getDrawable(R.drawable.grey_center));
                lnPan.setBackground(getResources().getDrawable(R.drawable.pan_primary_left));
                finish();
                 intent=new Intent(BankDetailsVerification.this,PanCardVerification.class);
                 startActivity(intent);
                break;

            case R.id.lnBank:
                if (PreferenceFile.getInstance().getPreferenceData(this, Constant.VERIFY_BANK).equals("Yes")) {

                    tvTitle.setText("Bank details");
                }else{
                    tvTitle.setText("Enter your Bank details");

                }
                lnBank.setBackground(getResources().getDrawable(R.drawable.pan_primary_center));
                lnAdhar.setBackground(getResources().getDrawable(R.drawable.grey_center));
                lnPan.setBackground(getResources().getDrawable(R.drawable.pan_grey_one));

                finish();
                 intent=new Intent(BankDetailsVerification.this,BankDetailsVerification.class);
                startActivity(intent);
                break;

            case R.id.ivarrow:
                Constant.hideKeyboard(this,v);
                 intent=new Intent(BankDetailsVerification.this,Dashboard.class);
                startActivity(intent);
                break;

            case R.id.txNext:

                Constant.hideKeyboard(this,v);

                if(PreferenceFile.getInstance().getPreferenceData(this,Constant.BANK_NAME)==null) {

                    Log.e("click-->","yes");

                    if (verfication()){

                        map = new HashMap<String, RequestBody>();
                        map.put("user_id", RequestBody.create(MediaType.parse("multipart/form-data"), PreferenceFile.getInstance().getPreferenceData(this, Constant.ID)));
                        map.put("bank_name", RequestBody.create(MediaType.parse("multipart/form-data"), edbackname.getText().toString()));
                        map.put("ifsc", RequestBody.create(MediaType.parse("multipart/form-data"), edIfcs.getText().toString()));
                        map.put("branch", RequestBody.create(MediaType.parse("multipart/form-data"), edbranch.getText().toString()));
                        map.put("account_holder", RequestBody.create(MediaType.parse("multipart/form-data"), edhaldername.getText().toString()));
                        map.put("account_number", RequestBody.create(MediaType.parse("multipart/form-data"), edAccountNumber.getText().toString()));

                        String account = "";
                        if (spAccounttype.getSelectedItem().toString().equals("Saving Account")) {
                            account = "Saving";
                        }
                        if (spAccounttype.getSelectedItem().toString().equals("Current Account")) {
                            account = "Current";
                        }
                        map.put("account_type", RequestBody.create(MediaType.parse("multipart/form-data"), account));

                        if (Constant.isConnectingToInternet(BankDetailsVerification.this)) {
                            Log.e("connect--->", "2");
                            new Retrofit2(this, BankDetailsVerification.this,
                                    map, body, Constant.REQ_BANK_VERIFICATION, Constant.BANK_VERIFICATION, "2").callService(true);
                        }
                    }
               }

               else {
                    if (PreferenceFile.getInstance().getPreferenceData(this, Constant.VERIFY_BANK).equals("No"))
                    {
                        if (verfication()){

                            map = new HashMap<String, RequestBody>();
                            map.put("user_id", RequestBody.create(MediaType.parse("multipart/form-data"), PreferenceFile.getInstance().getPreferenceData(this, Constant.ID)));
                            map.put("bank_name", RequestBody.create(MediaType.parse("multipart/form-data"), edbackname.getText().toString()));
                            map.put("ifsc", RequestBody.create(MediaType.parse("multipart/form-data"), edIfcs.getText().toString()));
                            map.put("branch", RequestBody.create(MediaType.parse("multipart/form-data"), edbranch.getText().toString()));
                            map.put("account_holder", RequestBody.create(MediaType.parse("multipart/form-data"), edhaldername.getText().toString()));
                            map.put("account_number", RequestBody.create(MediaType.parse("multipart/form-data"), edAccountNumber.getText().toString()));

                            String account = "";
                            if (spAccounttype.getSelectedItem().toString().equals("Saving Account")) {
                                account = "Saving";
                            }
                            if (spAccounttype.getSelectedItem().toString().equals("Current Account")) {
                                account = "Current";
                            }
                            map.put("account_type", RequestBody.create(MediaType.parse("multipart/form-data"), account));

                            if (Constant.isConnectingToInternet(BankDetailsVerification.this)) {
                                Log.e("connect--->", "2");
                                new Retrofit2(this, BankDetailsVerification.this, map, body, Constant.REQ_BANK_VERIFICATION, Constant.BANK_VERIFICATION, "2").callService(true);
                            }
                        }
                    }
                }
                break;
        }

    }

    public void ClickImageFromCamera() {

        CamIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        file = new File(Environment.getExternalStorageDirectory(),
                "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        uri = Uri.fromFile(file);

        CamIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);

        CamIntent.putExtra("return-data", true);

        startActivityForResult(CamIntent, 0);

    }

    @Override
    protected void onResume() {
        super.onResume();

        callStateService();

    }

    private void callStateService() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, BankDetailsVerification.this, Constant.REQ_Dashboard_Refresh, Constant.Dashboard_Refresh+PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    public void popup()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View diaview = inflater.inflate(R.layout.choose_image, null);
        dialogBuilder.setView(diaview);
        final ImageView cancal = (ImageView) diaview.findViewById(R.id.camera);
        final ImageView gallery = (ImageView) diaview.findViewById(R.id.gallery);
        final AlertDialog dialog = dialogBuilder.create();
        cancal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    try {

                        startActivityForResult(cameraIntent, 33);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                dialog.dismiss();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, 111);
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    public void GetImageFromGallery(){

        GalIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), 2);

    }


    public void saveImage(Bitmap bitmap) {
        File storagepath = new File(Environment.getExternalStorageDirectory() + "/photos/");
        storagepath.mkdirs();

        File image = new File(storagepath, Long.toString(System.currentTimeMillis()) + ".jpg");

        try {
            FileOutputStream outputStream = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ImageCropFunction() {

        // Image Crop Code
        try {
            CropIntent = new Intent("com.android.camera.action.CROP");

            CropIntent.setDataAndType(uri, "image/*");

            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, 1);

        } catch (ActivityNotFoundException e) {

        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {

            ImageCropFunction();

        }
        else if (requestCode == 2) {

            if (data != null) {

                uri = data.getData();

                ImageCropFunction();
            }
        }
        else if (requestCode == 1)
        {
             *//*Gallery/Camera Cropped Image*//*

            if (data != null) {

                Bundle bundle = data.getExtras();

                Bitmap bitmap = bundle.getParcelable("data");

                imgProfile.setImageBitmap(bitmap);

                imagepath="1";

                bitmap = Bitmap.createScaledBitmap(bitmap, 800, 800, false);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, bytes);
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                file = new File(dir, System.currentTimeMillis() + ".png");
                if (file.exists()) {
                    file.delete();
                }
                file = new File(dir, System.currentTimeMillis() + ".png");
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(file.getPath());
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, out); // bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Log.e("file--->", file.isFile() + " " + file.exists());
                req = RequestBody.create(MediaType.parse("image"), file);
                body = MultipartBody.Part.createFormData("passbook_image", file.getName(), req);
            }
        }
    }*/


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
    private void cropImage(String path)
    {
        Intent intent = new Intent(this, Croping.class);
        intent.putExtra("PROFILE", path);
        startActivityForResult(intent, 301);
    }

    public String getPath(Uri uri)
    {
        if( uri == null )
        {
            return null;
        }

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        return uri.getPath();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 33 && resultCode == RESULT_OK)
        {
            // TODO:  image from Camera
            bitmap=(Bitmap)data.getExtras().get("data");
            Uri imageUri = getImageUri(getApplicationContext(),bitmap);
            File finalFile = new File(getRealPathFromURI(imageUri));
            imagepath=imageUri.getPath()+Long.toString(System.currentTimeMillis())+".jpg";
            imgProfile.setImageBitmap(bitmap);
            Log.e("PathGet ",imagepath);
            file = new File(imagepath);
            Log.e("file--->", file.isFile() + " " + file.exists());
            req = RequestBody.create(MediaType.parse("image"), finalFile);
            body = MultipartBody.Part.createFormData("image", file.getName(), req);
           // cropImage(imageUri.getPath());
        }
        else if (requestCode == 111 && resultCode == RESULT_OK && data != null)
        {
            /*Gallery Image*/
            Uri uri = data.getData();
            path = getPath(uri);
            imagepath = getPath(uri);
            Log.e("PathGet ",path);


                //   imagepath = data.getExtras().get("PATH").toString();
                file = new File(imagepath);
                Picasso.with(context).load(file).placeholder(R.drawable.placeholder).
                        into(imgProfile);

                Log.e("file--->", file.isFile() + " " + file.exists());
                req = RequestBody.create(MediaType.parse("image"), file);
                body = MultipartBody.Part.createFormData("passbook_image", file.getName(), req);


           // cropImage(path);
        }
        else if (requestCode == 301 && resultCode==1001)
        {
             /*Gallery/Camera Cropped Image*/
            imagepath = data.getExtras().get("PATH").toString();

            file=new File(imagepath);
            Picasso.with(context).load(file).placeholder(R.drawable.placeholder).
                    into(imgProfile);

            Log.e("file--->",file.isFile()+" "+file.exists());
            req= RequestBody.create(MediaType.parse("image"), file);
            body= MultipartBody.Part.createFormData("passbook_image", file.getName(), req);
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {

            ImageCropFunction();

        }
        else if (requestCode == 2) {

            if (data != null) {

                uri = data.getData();

                ImageCropFunction();
            }
        }
        else if (requestCode == 1)
        {
             *//*Gallery/Camera Cropped Image*//*

            if (data != null) {

                Bundle bundle = data.getExtras();

                Bitmap bitmap = bundle.getParcelable("data");

                imgProfile.setImageBitmap(bitmap);

                imagepath="1";

                bitmap = Bitmap.createScaledBitmap(bitmap, 800, 800, false);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, bytes);
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                file = new File(dir, System.currentTimeMillis() + ".png");
                if (file.exists()) {
                    file.delete();
                }
                file = new File(dir, System.currentTimeMillis() + ".png");
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(file.getPath());
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, out); // bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Log.e("file--->", file.isFile() + " " + file.exists());
                req = RequestBody.create(MediaType.parse("image"), file);
                body = MultipartBody.Part.createFormData("passbook_image", file.getName(), req);
            }
        }
    }*/


   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case 1:
                if (resultCode == RESULT_OK) {

                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                    imgProfile.setImageBitmap(bitmap);

                    path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "mower";

                    File dir = new File(path);

                    if (!dir.exists())
                    {
                        dir.mkdirs();
                    }

                    file1 = new File(dir, System.currentTimeMillis() + ".png");
                    if (file1.exists()) {
                        file1.delete();
                    }

                    FileOutputStream fileOutputStream = null;
                    try {
                        fileOutputStream = new FileOutputStream(file1);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    bitmap.compress(Bitmap.CompressFormat.PNG, 85, fileOutputStream);

                    try {
                        fileOutputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Log.e("file-->",file1.exists()+"");
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
                    part = MultipartBody.Part.createFormData("passbook_image", file1.getName(), requestBody);

                }

                break;

            case 2:

                if (resultCode == RESULT_OK) {

                    Uri uri = data.getData();
                    String filepath[] = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, filepath, null, null, null);
                    cursor.moveToFirst();
                    int colIndex = cursor.getColumnIndex(filepath[0]);
                    path = cursor.getString(colIndex);
                    Bitmap bitmapFactory = (BitmapFactory.decodeFile(path));

                    imgProfile.setImageBitmap(bitmapFactory);

                    file1 = new File(path);

                    Log.e("file-->",file1.exists()+"");

                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
                    part = MultipartBody.Part.createFormData("passbook_image", file1.getName(), requestBody);

                }

                break;
        }
    }*/

    private void selectImage() {


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 2);
        }
        else {
            final CharSequence[] items = {"Take Photo", "Choose From Gallery", "Cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Photo !");

            builder.setItems(items, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (items[which].equals("Take Photo")) {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 1);

                    }
                    if (items[which].equals("Choose From Gallery")) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, 2);
                    }
                    if (items[which].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }

    }

    public boolean verfication() {
        Log.e("image--<>",imagepath);

        if(imagepath.equals("")){

            Constant.alertDialog(this,getString(R.string.please_select_chequebook_image));
            return false;
        }

       else if(edIfcs.getText().toString().equals("")){

           Constant.alertDialog(this,getString(R.string.please_enter_ifsc));
            return false;

        }
        else if(edIfcs.getText().toString().length()<11){

           Constant.alertDialog(this,getString(R.string.please_enter_valid_ifsc));
            return false;

        }
        else if(edbackname.getText().toString().equals("")){

            Constant.alertDialog(this,getString(R.string.please_enter_bank_name));
             return false;
        }

        else if(edbranch.getText().toString().equals("")){

            Constant.alertDialog(this,getString(R.string.please_enter_branch_name));
            return false;
        }

        else if(spAccounttype.getSelectedItem().toString().equals("Select account type")){

            Constant.alertDialog(this,getString(R.string.please_select_account_type));
            return false;
        }

        else if(edhaldername.getText().toString().trim().equals("")){

            Constant.alertDialog(this,getString(R.string.Please_enter_account_holder_name));
            return false;
        }

        else if(edAccountNumber.getText().toString().equals("")){

            Constant.alertDialog(this,getString(R.string.Please_enter_account_number));
            return false;
        }
        else if(edAccountNumber.getText().toString().length()<10  || edAccountNumber.getText().toString().length()>16){

            Constant.alertDialog(this,getString(R.string.Please_enter_correct_account_number));
            return false;
        }
        else if(edConfirmAccount.getText().toString().equals("")){

            Constant.alertDialog(this,getString(R.string.Please_enter_confirm_account_number));
            return false;
        }

        else if(!edConfirmAccount.getText().toString().equals(edAccountNumber.getText().toString())){

            Constant.alertDialog(this,getString(R.string.account_number_not_match));
            return false;
        }

        return true;
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode){

            case Constant.REQ_Dashboard_Refresh:

                if (response.isSuccessful()) {

                    try {
                        JSONObject result1 = new JSONObject(response.body().string());
                        Log.e("req_sign_up--->", "yes");
                        Log.e("resultttt-->", result1.toString());
                        String status = result1.getString("response");
                        String message = result1.getString("message");

                        if (status.equals("true")){

                            PreferenceFile.getInstance().saveData(this,Constant.COUNT_SECURITY,"1");
                            JSONObject result = result1.getJSONObject("data");

                            Log.e("status--->", status + "");
                            Log.e("id-->", result.getString("id"));

                            PreferenceFile.getInstance().saveData(this, Constant.ID, result.getString("id"));
                            PreferenceFile.getInstance().saveData(this, Constant.phone, result.getString("phone"));
                            PreferenceFile.getInstance().saveData(this, Constant.secure_pin, result.getString("secure_pin"));
                            PreferenceFile.getInstance().saveData(this, Constant.Inr_Amount, result.getString("inr_amount"));
                            PreferenceFile.getInstance().saveData(this, Constant.BTC_amount, result.getString("btc_amount"));

                            PreferenceFile.getInstance().saveData(this, Constant.Courtry_id, result.getString("country"));
                            PreferenceFile.getInstance().saveData(this, Constant.REFERCODE, result.getString("refer_code"));

                            Double bit=Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.BTC_amount));
                            Double finacal = Double.parseDouble(BigDecimal.valueOf(bit).toPlainString());
                            Double inr= Double.parseDouble(PreferenceFile.getInstance().getPreferenceData(this,Constant.Inr_Amount));
                            BigDecimal ff = new BigDecimal(inr);




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
                            }

                            PreferenceFile.getInstance().saveData(this,Constant.COUNT_SECURITY,"1");

                            callBankDetailSetup();

                        }
                        else
                        {
                            Constant.alertWithIntent(this,"Account Blocked",BlockScreen.class);

                        }

                    } catch (JSONException e) {
                        Log.e("exception-->", e.toString());
                    }
                    catch (IOException e) {

                    }
                }
                break;

            case Constant.REQ_IFSC_API:

                Log.e("result-->","ok");

                try {
                    JSONObject result1 = new JSONObject(response.body().string());

                    Log.e("resultresult1-->",result1.toString());

                    edbackname.setText(result1.getString("BANK"));
                    edbranch.setText(result1.getString("BRANCH"));
                   // edhaldername.setText(result1.getString("BRANCH"));


                }
                catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case Constant.REQ_BANK_VERIFICATION:

                Log.e("result-->","ok");

                try {
                    JSONObject result1 = new JSONObject(response.body().string());

                    Log.e("resultresult1-->",result1.toString());

                    String status=result1.getString("response");
                    String message=result1.getString("message");

                    if(status.equals("true")){

                        if(PreferenceFile.getInstance().getPreferenceData(this,Constant.VERIFY_BANK).equals("Pending")){

                            txstatus.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.VERIFY_BANK));
                            txstatus.setTextColor(Color.parseColor("#d40e0e"));
                            txstatus.setVisibility(View.VISIBLE);
                        }

                        JSONObject data=result1.getJSONObject("data");


                        final Dialog dialog = new Dialog(BankDetailsVerification.this);
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
                                Intent intent=new Intent(BankDetailsVerification.this,AdhaarCardVerification.class);
                                startActivity(intent);
                            }
                        });



                    }else {

                        Constant.alertDialog(this,message);
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


