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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.acepay.Croping.Croping;
import com.acepay.Model.CountryModel;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class AdhaarCardVerification extends ToolabarActivity implements View.OnClickListener,
        RetrofitResponse {
    LinearLayout lnBank,lnAdhar,lnPan;
    TextView txName,txNext,txstatus,tvTitle;
    ImageView ivEdit,imgProfile,ivback,imback;
    String path="",Country_id;
    EditText edAccountHolder,edAddress1,edAddress2,edcity,edLandmark;
    File file1,file;
    Spinner spCountry,spState,spcity;
    ArrayList<CountryModel>Countrys;
    ArrayList<String> CountryList_name;
    ArrayList<CountryModel>States;
    ArrayList<String> State_name;
    boolean doubleBackToExitPressedOnce = false;
    private HashMap<String, RequestBody> map;
    String state_id;
    String city_id;
    Intent CamIntent,GalIntent,CropIntent;
    ArrayList<String> city_name;
    int image_flag=0;
    RequestBody req;
    Uri uri;
    private File profileIMG;
    Bitmap bitmap;
    int flaag=0;
    ArrayList<CountryModel>City;
    MultipartBody.Part body,body1;
    String imagepath="",backpath="";
    Context context;
    String[] defaultCity = {"Select city"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.bcc);
        setContentView(R.layout.activity_adhaar_card_verification2);
        lnBank= (LinearLayout) findViewById(R.id.lnBank);
        lnAdhar= (LinearLayout) findViewById(R.id.lnAdhar);
        lnPan= (LinearLayout) findViewById(R.id.lnPan);
        txName= (TextView) findViewById(R.id.txName);
        txstatus= (TextView) findViewById(R.id.txstatus);
        tvTitle= (TextView) findViewById(R.id.tvTitle);
        City=new ArrayList<>();
        city_name=new ArrayList<>();
        context=this;
        ivback= (ImageView) findViewById(R.id.ivback);
//        ivarrow= (ImageView) findViewById(R.id.ivarrow);
        ivEdit= (ImageView) findViewById(R.id.ivEdit);
        imgProfile= (ImageView) findViewById(R.id.imgProfile);
        imback= (ImageView) findViewById(R.id.imback);
        edAccountHolder= (EditText) findViewById(R.id.edAccountHolder);
        edAddress2= (EditText) findViewById(R.id.edAddress2);
        edAddress1= (EditText) findViewById(R.id.edAddress1);
//        edcity= (EditText) findViewById(R.id.edcity);
        edLandmark= (EditText) findViewById(R.id.edLandmark);
        txNext= (TextView) findViewById(R.id.txNext);
//        spCountry= (Spinner) findViewById(R.id.spCountry);
        spState= (Spinner) findViewById(R.id.spState);
        spcity= (Spinner) findViewById(R.id.spcity);

        lnBank.setBackground(getResources().getDrawable(R.drawable.grey_center));
        lnAdhar.setBackground(getResources().getDrawable(R.drawable.pan_primary_right));
        lnPan.setBackground(getResources().getDrawable(R.drawable.pan_grey_one));
        txName.setText("Account Verification");
        lnBank.setOnClickListener(this);
        lnAdhar.setOnClickListener(this);
        lnPan.setOnClickListener(this);
//        ivarrow.setOnClickListener(this);
        ivEdit.setOnClickListener(this);
        txNext.setOnClickListener(this);
        ivback.setOnClickListener(this);


        callStateService();
        Countrys=new ArrayList<>();
        CountryList_name=new ArrayList<>();
        States=new ArrayList<>();
        State_name=new ArrayList<>();



        spcity.setAdapter(new ArrayAdapter<String>(this, R.layout.new_text,defaultCity));

        spcity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.gradient_1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.gradient_1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        callAAdharSetup();



    }


    public void callAAdharSetup()
    {
        if (PreferenceFile.getInstance().getPreferenceData(this, Constant.VERIFY_Adhaar).equals("Yes")) {

            flaag=1;
            txstatus.setText("Approved");
            txstatus.setTextColor(Color.parseColor("#2fd49e"));
            txstatus.setVisibility(View.VISIBLE);
            edAccountHolder.setEnabled(false);
            edAddress1.setEnabled(false);
            edAddress2.setEnabled(false);

            edLandmark.setEnabled(false);
            spcity.setEnabled(false);
//            spCountry.setEnabled(false);
            spState.setEnabled(false);

            edAccountHolder.setTextColor(getResources().getColor(R.color.gradient_1));
            edAddress1.setTextColor(getResources().getColor(R.color.gradient_1));
            edAddress2.setTextColor(getResources().getColor(R.color.gradient_1));
            edLandmark.setTextColor(getResources().getColor(R.color.gradient_1));
            tvTitle.setText("Aadhaar details");

            edAccountHolder.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Adhaar_number));
            edAddress1.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Adhaar_line1));
            edAddress2.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Adhaar_line2));
            edLandmark.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.LANDMARK));
//            edcity.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Aadhar_city));

            Picasso.with(this)
                    .load(Constant.Adhar_image+PreferenceFile.getInstance().getPreferenceData(this,Constant.Adhaar_image)).resize(800,600).placeholder(getResources().getDrawable(R.drawable.placeholder))
                    .into(imgProfile);

            Picasso.with(this)
                    .load(Constant.Adhar_image+PreferenceFile.getInstance().getPreferenceData(this,Constant.Adhaar_image_back)).resize(800,600).placeholder(getResources().getDrawable(R.drawable.placeholder))
                    .into(imback);

        }
        else  if (PreferenceFile.getInstance().getPreferenceData(this, Constant.VERIFY_Adhaar).equals("No"))
        {
            flaag=0;
            txstatus.setText("Rejected");
            txstatus.setTextColor(Color.parseColor("#d40e0e"));
            txstatus.setVisibility(View.VISIBLE);
            edAccountHolder.setEnabled(true);
            edAddress1.setEnabled(true);
            edAddress2.setEnabled(true);

            edLandmark.setEnabled(true);
            spcity.setEnabled(true);
//            spCountry.setEnabled(false);
            spState.setEnabled(true);

            edAccountHolder.setTextColor(getResources().getColor(R.color.gradient_1));
            edAddress1.setTextColor(getResources().getColor(R.color.gradient_1));
            edAddress2.setTextColor(getResources().getColor(R.color.gradient_1));
            edLandmark.setTextColor(getResources().getColor(R.color.gradient_1));
            tvTitle.setText("Aadhaar details");
        }
        else{
            tvTitle.setText("Enter your Aadhaar details");

        }

        if(PreferenceFile.getInstance().getPreferenceData(this,Constant.Adhaar_number)!=null){

            if(PreferenceFile.getInstance().getPreferenceData(this,Constant.VERIFY_Adhaar).equals("Pending")){

                txstatus.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.VERIFY_Adhaar));
                txstatus.setTextColor(Color.parseColor("#d40e0e"));
                txstatus.setVisibility(View.VISIBLE);
                flaag=1;
                txstatus.setText("Pending");
                txstatus.setTextColor(Color.parseColor("#d40e0e"));
                txstatus.setVisibility(View.VISIBLE);
                edAccountHolder.setEnabled(false);
                edAddress1.setEnabled(false);
                edAddress2.setEnabled(false);

                edLandmark.setEnabled(false);
                spcity.setEnabled(false);
//                spCountry.setEnabled(false);
                spState.setEnabled(false);

                edAccountHolder.setTextColor(getResources().getColor(R.color.gradient_1));
                edAddress1.setTextColor(getResources().getColor(R.color.gradient_1));
                edAddress2.setTextColor(getResources().getColor(R.color.gradient_1));
                edLandmark.setTextColor(getResources().getColor(R.color.gradient_1));

                edAccountHolder.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Adhaar_number));
                edAddress1.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Adhaar_line1));
                edAddress2.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Adhaar_line2));
                edLandmark.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.LANDMARK));
//            edcity.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Aadhar_city));

                Picasso.with(this)
                        .load(Constant.Adhar_image+PreferenceFile.getInstance().getPreferenceData(this,Constant.Adhaar_image)).resize(800,600).placeholder(getResources().getDrawable(R.drawable.placeholder))
                        .into(imgProfile);

                Picasso.with(this)
                        .load(Constant.Adhar_image+PreferenceFile.getInstance().getPreferenceData(this,Constant.Adhaar_image_back)).resize(800,600).placeholder(getResources().getDrawable(R.drawable.placeholder))
                        .into(imback);

            }


        }
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
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

    public void setAdapter(){

//        spCountry.setAdapter(new ArrayAdapter<String>(this, R.layout.new_text,CountryList_name));
//
//        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                if (spCountry.getSelectedItem().equals("Select Country")) {
//
//                }
//
//                if (!spCountry.getSelectedItem().equals("Select Country")){
//
//                    Country_id = Countrys.get(spCountry.getSelectedItemPosition() - 1).getCountry_id();
//
//                    Log.e("india-->",Country_id);
//
//                    callStateService();
//
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        newserice();
    }

    private void newserice() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, AdhaarCardVerification.this, Constant.REQ_Dashboard_Refresh, Constant.Dashboard_Refresh+PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    private void callStateService() {

        Log.e("callstate-->","yes");
        Log.e("callstate-->",PreferenceFile.getInstance().getPreferenceData(this,Constant.State_id));

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, AdhaarCardVerification.this, Constant.REQ_State, Constant.State+ PreferenceFile.getInstance().getPreferenceData(this,Constant.Country_id)).callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }


    private void callService() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, AdhaarCardVerification.this, Constant.REQ_Country, Constant.Country).callService(true);
        } else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
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
                intent=new Intent(AdhaarCardVerification.this,AdhaarCardVerification.class);
                startActivity(intent);
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
                intent=new Intent(AdhaarCardVerification.this,PanCardVerification.class);
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
                intent=new Intent(AdhaarCardVerification.this,BankDetailsVerification.class);
                startActivity(intent);
                break;

//            case R.id.ivarrow:
//                Constant.hideKeyboard(this,v);
//                finish();
//                break;

            case R.id.txNext:

                Constant.hideKeyboard(this,v);

                if(PreferenceFile.getInstance().getPreferenceData(this,Constant.Adhaar_number)==null) {

                    if (verification()) {
                        Log.e("fist if verificatio","true");
                        map = new HashMap<String, RequestBody>();
                        map.put("user_id", RequestBody.create(MediaType.parse("multipart/form-data"), PreferenceFile.getInstance().getPreferenceData(this, Constant.ID)));
                        map.put("aadhar_number", RequestBody.create(MediaType.parse("multipart/form-data"), edAccountHolder.getText().toString()));
                        map.put("line1", RequestBody.create(MediaType.parse("multipart/form-data"), edAddress1.getText().toString()));
                        map.put("line2", RequestBody.create(MediaType.parse("multipart/form-data"), edAddress2.getText().toString()));
                        map.put("city", RequestBody.create(MediaType.parse("multipart/form-data"),city_id));
                        map.put("state", RequestBody.create(MediaType.parse("multipart/form-data"), state_id));
                        map.put("landmark", RequestBody.create(MediaType.parse("multipart/form-data"), edLandmark.getText().toString()));

                        if (Constant.isConnectingToInternet(AdhaarCardVerification.this)) {
                            Log.e("connect--->", "2");
                            new Retrofit2(this, AdhaarCardVerification.this, map, body, body1, Constant.REQ_Aadhar_verify, Constant.Aadhar_verify, "5").callService(true);
                        }
                    }
                }
                else
                    {
                        //todo in case of rejected status
                    if (PreferenceFile.getInstance().getPreferenceData(this, Constant.VERIFY_Adhaar).equals("No"))
                    {
                        if (verification()) {
                            map = new HashMap<String, RequestBody>();
                            map.put("user_id", RequestBody.create(MediaType.parse("multipart/form-data"), PreferenceFile.getInstance().getPreferenceData(this, Constant.ID)));
                            map.put("aadhar_number", RequestBody.create(MediaType.parse("multipart/form-data"), edAccountHolder.getText().toString()));
                            map.put("line1", RequestBody.create(MediaType.parse("multipart/form-data"), edAddress1.getText().toString()));
                            map.put("line2", RequestBody.create(MediaType.parse("multipart/form-data"), edAddress2.getText().toString()));
                            map.put("city", RequestBody.create(MediaType.parse("multipart/form-data"),city_id));
                            map.put("state", RequestBody.create(MediaType.parse("multipart/form-data"), state_id));
                            map.put("landmark", RequestBody.create(MediaType.parse("multipart/form-data"), edLandmark.getText().toString()));

                            if (Constant.isConnectingToInternet(AdhaarCardVerification.this)) {
                                Log.e("connect--->", "3");
                                new Retrofit2(this, AdhaarCardVerification.this, map, body, body1, Constant.REQ_Aadhar_verify, Constant.Aadhar_verify, "5").callService(true);
                            }
                        }

                    }
                }

                break;

            case R.id.ivEdit:
                image_flag=1;
                //selectImage();
                if(flaag==0) {
                    popup();
                }
                break;

            case R.id.ivback:
                image_flag=2;
                if(flaag==0) {
                    popup();
                }
              //  selectImage();
               // popup();
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

    public void GetImageFromGallery(){

        GalIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(Intent.createChooser(GalIntent, "Select Image From Gallery"), 2);

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
//                        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//                        profileIMG = new File(dir, Long.toString(System.currentTimeMillis()) + ".jpg");
//                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(profileIMG));
                        startActivityForResult(cameraIntent, 33);
//                        saveImage(bitmap);
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

    private void cropImage(String path)
    {
        Intent intent = new Intent(this, Croping.class);
        intent.putExtra("RECT", path);
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

    public void ImageCropFunction() {

        // Image Crop Code
        try {
            CropIntent = new Intent("com.android.camera.action.CROP");

            CropIntent.setDataAndType(uri, "image/*");

            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 800);
            CropIntent.putExtra("outputY", 640);
            CropIntent.putExtra("aspectX", 5);
            CropIntent.putExtra("aspectY", 3);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, 1);

        } catch (ActivityNotFoundException e) {

        }
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



            if(image_flag==1) {
                imagepath=imageUri.getPath()+Long.toString(System.currentTimeMillis())+".jpg";
                Log.e("PathGet-1 ",imagepath);
                imgProfile.setImageBitmap(bitmap);
                //   imagepath = data.getExtras().get("PATH").toString();
                file = new File(imagepath);

                Log.e("file--->", file.isFile() + " " + file.exists());
                req = RequestBody.create(MediaType.parse("image"), finalFile);
                body = MultipartBody.Part.createFormData("aadhar", file.getName(), req);
            }
            if(image_flag==2){

                backpath = imageUri.getPath()+Long.toString(System.currentTimeMillis())+".jpg";
                Log.e("PathGet-2 ",imagepath);
                imback.setImageBitmap(bitmap);
                // backpath = data.getExtras().get("PATH").toString();
                file = new File(backpath);

                Log.e("file-->",file.exists()+"");
                Log.e("file--->", file.isFile() + " " + file.exists());
                req = RequestBody.create(MediaType.parse("image"), finalFile);
                body1 = MultipartBody.Part.createFormData("back", file.getName(), req);

            }
          //  cropImage(imageUri.getPath());
        }
        else if (requestCode == 111 && resultCode == RESULT_OK && data != null)
        {
            /*Gallery Image*/
            Uri uri = data.getData();
         //   path = getPath(uri);
            imagepath = getPath(uri);
            Log.e("PathGet ",imagepath);

            if(image_flag==1) {
                imagepath = getPath(uri);

                //   imagepath = data.getExtras().get("PATH").toString();
                file = new File(imagepath);
                Picasso.with(context).load(file).placeholder(R.drawable.placeholder).
                        into(imgProfile);

                Log.e("file--->", file.isFile() + " " + file.exists());
                req = RequestBody.create(MediaType.parse("image"), file);
                body = MultipartBody.Part.createFormData("aadhar", file.getName(), req);
            }
            if(image_flag==2){

                backpath = getPath(uri);

               // backpath = data.getExtras().get("PATH").toString();
                file = new File(backpath);

                Log.e("file-->",file.exists()+"");

                Picasso.with(context).load(file).placeholder(R.drawable.placeholder).
                        into(imback);

                Log.e("file--->", file.isFile() + " " + file.exists());
                req = RequestBody.create(MediaType.parse("image"), file);
                body1 = MultipartBody.Part.createFormData("back", file.getName(), req);

            }
          //  cropImage(path);
        }
        else if (requestCode == 301 && resultCode==1001)
        {
             /*Gallery/Camera Cropped Image*/

            if(image_flag==1) {
                imagepath = data.getExtras().get("PATH").toString();
                file = new File(imagepath);
                Picasso.with(context).load(file).placeholder(R.drawable.placeholder).
                        into(imgProfile);

                Log.e("file--->", file.isFile() + " " + file.exists());
                req = RequestBody.create(MediaType.parse("image"), file);
                body = MultipartBody.Part.createFormData("aadhar", file.getName(), req);
            }
            if(image_flag==2){


                backpath = data.getExtras().get("PATH").toString();
                file = new File(backpath);

                Log.e("file-->",file.exists()+"");

                Picasso.with(context).load(file).placeholder(R.drawable.placeholder).
                        into(imback);

                Log.e("file--->", file.isFile() + " " + file.exists());
                req = RequestBody.create(MediaType.parse("image"), file);
                body1 = MultipartBody.Part.createFormData("back", file.getName(), req);

            }
        }
    }

    private void selectImage() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 2);
        } else {
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

    public void setStateAdapter() {

        spState.setAdapter(new ArrayAdapter<String>(this, R.layout.new_text,State_name));

        if(PreferenceFile.getInstance().getPreferenceData(this,Constant.Aadhar_state)!=null) {


            //for rejected
          if (!(PreferenceFile.getInstance().getPreferenceData(this, Constant.VERIFY_Adhaar).equals("No")))
          {

              for (int j = 0; j < States.size(); j++) {

                  Log.e("c-->", PreferenceFile.getInstance().getPreferenceData(this, Constant.Aadhar_state));
                  Log.e("country-->", States.get(j).getCountry_name());

                  if (States.get(j).getCountry_name().equalsIgnoreCase(PreferenceFile.getInstance().getPreferenceData(this, Constant.Aadhar_state))) {
                      Log.e("inside-->", "yes");
                      spState.setSelection(j + 1);

                  }

              }
          }


        }

        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.gradient_1));

                if (spState.getSelectedItem().equals("Select state")) {
                    city_name.clear();
                    City.clear();
                    spcity.setAdapter(new ArrayAdapter<String>(AdhaarCardVerification.this,R.layout.new_text, defaultCity));
                }

                if (!spState.getSelectedItem().equals("Select state")){

                    state_id = States.get(spState.getSelectedItemPosition() - 1).getCountry_id();
                    callcity(state_id);
                    Log.e("state--->",state_id);

                    // callStateService();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public boolean verification(){

        if(imagepath.equals("")){

            Constant.alertDialog(this,getString(R.string.Please_pic_aadhar_image));
            return false;
        }

        if(backpath.equals("")){

            Constant.alertDialog(this,getString(R.string.Please_pic_back_aadhar_image));
            return false;
        }
        else if(edAccountHolder.getText().toString().equals("")){

            Constant.alertDialog(this,getString(R.string.Please_enter_adhar_number));
            return false;
        }
        else if(edAccountHolder.getText().toString().length()<12){

            Constant.alertDialog(this,getString(R.string.Please_enter_valid_adhar_number));
            return false;
        }

        else if(edAddress1.getText().toString().trim().equals("")){

            Constant.alertDialog(this,getString(R.string.Please_enter_address_one));
            return false;
        }

        else if(edAddress2.getText().toString().trim().equals("")){

            Constant.alertDialog(this,getString(R.string.Please_enter_address_two));
            return false;
        }

        else if(edAddress2.getText().toString().trim().equals("")){

            Constant.alertDialog(this,getString(R.string.Please_enter_address_two));
            return false;
        }
        else if(edLandmark.getText().toString().trim().equals("")){

            Constant.alertDialog(this,getString(R.string.Please_enter_landmark));
            return false;
        }

        else if(spState.getSelectedItem().toString().equals("Select state")){

            Constant.alertDialog(this,getString(R.string.please_select_state));
            return false;
        }

        if(spcity.getSelectedItem().toString().equals("Select city")){

            Constant.alertDialog(this,getString(R.string.please_select_city));

            return false;
        }

        return true;
    }

    public void callcity(String val){

        Log.e("callservice-->","yes");

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, AdhaarCardVerification.this, Constant.REQ_City, Constant.City+val).callService(true);
        } else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }

    public void setCityAdapter() {

        Log.e("cityState-->",City.size()+" ");

        spcity.setAdapter(new ArrayAdapter<String>(this, R.layout.new_text,city_name));

        if (!(PreferenceFile.getInstance().getPreferenceData(this, Constant.VERIFY_Adhaar).equals("No")))
        {
            for(int x=0;x<City.size();x++) {

                Log.e("aadarcity-->",PreferenceFile.getInstance().getPreferenceData(this, Constant.Aadhar_city)+" city-->"+City.get(x).getCountry_name());

                if (City.get(x).getCountry_id().equalsIgnoreCase(PreferenceFile.getInstance().getPreferenceData(this, Constant.Aadhar_city))) {
                    Log.e("inside-->", "yes");
                    spcity.setSelection(x + 1);
                }
            }
        }



        spcity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.gradient_1));

                if (spcity.getSelectedItem().equals("Select city")) {

                }

                if (!spcity.getSelectedItem().equals("Select city")){

                    city_id = City.get(spcity.getSelectedItemPosition() - 1).getCountry_id();

                    Log.e("india-->",city_id);

                    //callStateService();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode) {

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
                            Log.e("againcall--->","yes");
                            BigDecimal ff = new BigDecimal(inr);
                            Log.e("newcal-->","d -->"+ ff);


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

                            callAAdharSetup();

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


            case Constant.REQ_Country:

                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    String status = result1.getString("response");
                    String message = result1.getString("message");

                    if (status.equals("true")) {

                        Countrys.clear();
                        CountryList_name.clear();
                        CountryList_name.add(0, "Select Country");

                        JSONArray data = result1.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject CountryObj = data.getJSONObject(i);
                            CountryModel country = new CountryModel();
                            country.setCountry_id(CountryObj.getString("id"));
                            country.setCountry_name(CountryObj.getString("name"));
                            CountryList_name.add(CountryObj.getString("name"));
                            Countrys.add(country);

                            setAdapter();
                        }

                    } else {

                        Constant.alertDialog(this, message);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            case Constant.REQ_City:

                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    String status=result1.getString("response");
                    String message=result1.getString("message");

                    if(status.equals("true")){

                        City.clear();
                        city_name.clear();
                        city_name.add(0, "Select city");

                        JSONArray data=result1.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++){
                            JSONObject CountryObj = data.getJSONObject(i);
                            CountryModel country = new CountryModel();
                            country.setCountry_id(CountryObj.getString("id"));
                            country.setCountry_name(CountryObj.getString("name"));
                            city_name.add(CountryObj.getString("name"));
                            City.add(country);

                            setCityAdapter();
                        }
                        Log.e("inside-->",city_name.size()+"");

                    }else {

                        Constant.alertDialog(this,message);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;


            case Constant.REQ_State:

                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    String status=result1.getString("response");
                    String message=result1.getString("message");

                    if(status.equals("true")){

                        callcity(PreferenceFile.getInstance().getPreferenceData(this,Constant.Aadhar_city));

                        States.clear();
                        State_name.clear();
                        State_name.add(0, "Select state");

                        JSONArray data=result1.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++){
                            JSONObject CountryObj = data.getJSONObject(i);
                            CountryModel country = new CountryModel();
                            country.setCountry_id(CountryObj.getString("id"));
                            country.setCountry_name(CountryObj.getString("name"));
                            State_name.add(CountryObj.getString("name"));
                            States.add(country);

                            setStateAdapter();
                        }

                    }else {

                        Constant.alertDialog(this,message);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;


            case Constant.REQ_Aadhar_verify:

                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    String status = result1.getString("response");
                    String message = result1.getString("message");

                    if (status.equals("true")) {

                        if(PreferenceFile.getInstance().getPreferenceData(this,Constant.VERIFY_Adhaar).equals("Pending")){

                            txstatus.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.VERIFY_Adhaar));
                            txstatus.setTextColor(Color.parseColor("#d40e0e"));
                            txstatus.setVisibility(View.VISIBLE);
                        }

                        //Constant.alertWithIntent(this, message, Dashboard.class);
                        PreferenceFile.getInstance().saveData(this,Constant.AadharVerification,"1");


                        final Dialog dialog = new Dialog(AdhaarCardVerification.this);
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
                                Intent intent=new Intent(AdhaarCardVerification.this,Dashboard.class);
                                startActivity(intent);
                            }
                        });









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
