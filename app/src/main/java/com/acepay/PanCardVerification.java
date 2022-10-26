package com.acepay;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class PanCardVerification extends ToolabarActivity implements View.OnClickListener, RetrofitResponse {

//    ImageView ivarrow;
    LinearLayout lnBank,lnAdhar,lnPan;
    TextView txName,txSubmit,txstatus,tvTitle;
    ImageView imgProfile,ivImageSelect;
    String path="";
   // LinearLayout lnrl,lnlayer;
    File file1,file;
    Intent CamIntent,GalIntent,CropIntent;
    Uri uri;
    boolean doubleBackToExitPressedOnce = false;
    Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
    MultipartBody.Part body;
    private File profileIMG;
    Context context;
    EditText edUsername,edPanCardNumber,edBirthdate,edlast;
    Spinner spGender;
    int flaag=0;
    MultipartBody.Part part;
    String imagepath="";
    private HashMap<String, RequestBody> map;
    DatePickerDialog datePickerDialog;
    DateFormat dateFormatter;
    RequestBody req;
    private Bitmap bitmap;
    Intent cameraIntent;
    String[] genderlist = {"Select Gender", "Female", "Male"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.bcc);
        setContentView(R.layout.activity_adhaar_card_verification);
//        ivarrow= (ImageView) findViewById(R.id.ivarrow);
        lnBank= (LinearLayout) findViewById(R.id.lnBank);
       // lnrl= (LinearLayout) findViewById(R.id.lnrl);
        lnAdhar= (LinearLayout) findViewById(R.id.lnAdhar);
        lnPan= (LinearLayout) findViewById(R.id.lnPan);
       // lnlayer= (LinearLayout) findViewById(R.id.lnlayer);
        txName= (TextView) findViewById(R.id.txName);
        txstatus= (TextView) findViewById(R.id.txstatus);
        tvTitle= (TextView) findViewById(R.id.tvTitle);

        imgProfile= (ImageView) findViewById(R.id.imgProfile);
        ivImageSelect= (ImageView) findViewById(R.id.ivImageSelect);
        edUsername= (EditText) findViewById(R.id.edUsername);
        edlast= (EditText) findViewById(R.id.edlast);
        edPanCardNumber= (EditText) findViewById(R.id.edPanCardNumber);
        edBirthdate= (EditText) findViewById(R.id.edBirthdate);
        spGender= (Spinner) findViewById(R.id.spGender);
        txSubmit= (TextView) findViewById(R.id.txSubmit);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        edPanCardNumber.setFilters(new InputFilter[]{new InputFilter.AllCaps()});



        lnBank.setBackground(getResources().getDrawable(R.drawable.grey_center));
        lnAdhar.setBackground(getResources().getDrawable(R.drawable.grey_center));
        lnPan.setBackground(getResources().getDrawable(R.drawable.pan_primary_left));

        spGender.setAdapter(new ArrayAdapter<String>(this, R.layout.new_text,genderlist));

        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.gradient_1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        callSetPanConditions();

    }


    public void callSetPanConditions()
    {
        if (PreferenceFile.getInstance().getPreferenceData(this, Constant.VERIFY_PAN).equals("Yes"))
        {
            txstatus.setText("Approved");
            flaag=1;
            txstatus.setTextColor(Color.parseColor("#2fd49e"));
            txstatus.setVisibility(View.VISIBLE);
            edUsername.setEnabled(false);
            edPanCardNumber.setEnabled(false);
            edBirthdate.setEnabled(false);
            edlast.setEnabled(false);
            spGender.setEnabled(false);
            edUsername.setTextColor(getResources().getColor(R.color.gradient_1));
            edPanCardNumber.setTextColor(getResources().getColor(R.color.gradient_1));
            edBirthdate.setTextColor(getResources().getColor(R.color.gradient_1));
            tvTitle.setText("Pan Card details");
            edlast.setTextColor(getResources().getColor(R.color.gradient_1));


            edPanCardNumber.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Pan_number));
            edUsername.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Pan_name));
            edlast.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Pan_last));
            edBirthdate.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Pan_dob));

            Log.e("gander-->",PreferenceFile.getInstance().getPreferenceData(this,Constant.Pan_gender));


            Picasso.with(this)
                    .load(Constant.PAN_IMAGE+PreferenceFile.getInstance().getPreferenceData(this,Constant.Pan_image)).resize(800,600).placeholder(getResources().getDrawable(R.drawable.placeholder))
                    .into(imgProfile);

            if(PreferenceFile.getInstance().getPreferenceData(this,Constant.Pan_gender).equalsIgnoreCase("Male")){
                Log.e("gander-->","yes");
                spGender.setSelection(2);
            }
            else {
                Log.e("gander-->","no");
                spGender.setSelection(1);
            }

        }
        else if (PreferenceFile.getInstance().getPreferenceData(this, Constant.VERIFY_PAN).equals("No"))
        {
            flaag=0;
            txstatus.setText("Rejected");
            txstatus.setTextColor(Color.parseColor("#d40e0e"));
            txstatus.setVisibility(View.VISIBLE);
            edUsername.setEnabled(true);
            edPanCardNumber.setEnabled(true);
            edBirthdate.setEnabled(true);
            edlast.setEnabled(true);
            spGender.setEnabled(true);
            edUsername.setTextColor(getResources().getColor(R.color.gradient_1));
            edPanCardNumber.setTextColor(getResources().getColor(R.color.gradient_1));
            edBirthdate.setTextColor(getResources().getColor(R.color.gradient_1));
            tvTitle.setText("Pan Card details");
            edlast.setTextColor(getResources().getColor(R.color.gradient_1));

            edUsername.setText("");
            edPanCardNumber.setText("");
            edBirthdate.setText("");
            edlast.setText("");
        }

        else{
            tvTitle.setText("Enter your Pan Card details");
        }

        if(PreferenceFile.getInstance().getPreferenceData(this,Constant.Pan_name)!=null) {



            if(PreferenceFile.getInstance().getPreferenceData(this,Constant.VERIFY_PAN).equals("Pending"))
            {
                txstatus.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.VERIFY_PAN));
                txstatus.setVisibility(View.VISIBLE);
                txstatus.setText("Pending");

                flaag=1;
                txstatus.setTextColor(Color.parseColor("#d40e0e"));
                txstatus.setVisibility(View.VISIBLE);
                edUsername.setEnabled(false);
                edPanCardNumber.setEnabled(false);
                edBirthdate.setEnabled(false);
                edlast.setEnabled(false);
                spGender.setEnabled(false);
                edUsername.setTextColor(getResources().getColor(R.color.gradient_1));
                edPanCardNumber.setTextColor(getResources().getColor(R.color.gradient_1));
                edBirthdate.setTextColor(getResources().getColor(R.color.gradient_1));
                edlast.setTextColor(getResources().getColor(R.color.gradient_1));

                if(PreferenceFile.getInstance().getPreferenceData(this,Constant.Pan_gender).equalsIgnoreCase("Male")){
                    Log.e("gander-->","yes");
                    spGender.setSelection(2);
                }
                else {
                    Log.e("gander-->","no");
                    spGender.setSelection(1);
                }


                edPanCardNumber.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Pan_number));
                edUsername.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Pan_name));
                edlast.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Pan_last));
                edBirthdate.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Pan_dob));

                Log.e("gander-->",PreferenceFile.getInstance().getPreferenceData(this,Constant.Pan_gender));


                Picasso.with(this)
                        .load(Constant.PAN_IMAGE+PreferenceFile.getInstance().getPreferenceData(this,Constant.Pan_image)).resize(800,600).placeholder(getResources().getDrawable(R.drawable.placeholder))
                        .into(imgProfile);

            }


        }

        lnBank.setOnClickListener(this);
        txSubmit.setOnClickListener(this);
        lnAdhar.setOnClickListener(this);
//        ivarrow.setOnClickListener(this);
        ivImageSelect.setOnClickListener(this);
        edBirthdate.setOnClickListener(this);

        txName.setText("Account Verification");

        // keyboard();

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
        Cursor cursor = managedQuery(uri, projection, null,
                null, null);
        if( cursor != null )
        {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        else
        {
            Log.e("InsideElse ","else");
        }

        return uri.getPath();
    }


    public void popup()
    {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View diaview = inflater.inflate(R.layout.choose_image, null);
        dialogBuilder.setView(diaview);
        final ImageView camera = (ImageView) diaview.findViewById(R.id.camera);
        final ImageView gallery = (ImageView) diaview.findViewById(R.id.gallery);
        final android.app.AlertDialog dialog = dialogBuilder.create();

        camera.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
               
            //Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    try {

                        startActivityForResult(cameraIntent, 33);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                dialog.dismiss();


//                //ClickImageFromCamera();
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (cameraIntent.resolveActivity(getPackageManager()) != null)
//                {
//                    try {
//                        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//                        profileIMG = new File(dir, Long.toString(System.currentTimeMillis()) + ".jpg");
//                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(profileIMG));
//                        startActivityForResult(cameraIntent, 33);
//                        saveImage(bitmap);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                dialog.dismiss();
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

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()){

           case  R.id.txSubmit:

               Constant.hideKeyboard(this,v);

               if(PreferenceFile.getInstance().getPreferenceData(this,Constant.Pan_name)==null){

                   Log.e("If first","If");
               if(verification()){

                   Log.e("Verification first","If");

                   map = new HashMap<String, RequestBody>();

                   map.put("user_id", RequestBody.create(MediaType.parse("multipart/form-data"), PreferenceFile.getInstance().getPreferenceData(this, Constant.ID)));
                   map.put("name", RequestBody.create(MediaType.parse("multipart/form-data"), edUsername.getText().toString()));
                   map.put("last_name", RequestBody.create(MediaType.parse("multipart/form-data"), edlast.getText().toString()));
                   map.put("pan_number", RequestBody.create(MediaType.parse("multipart/form-data"), edPanCardNumber.getText().toString()));
                   map.put("dob", RequestBody.create(MediaType.parse("multipart/form-data"), edBirthdate.getText().toString()));
                   map.put("gender", RequestBody.create(MediaType.parse("multipart/form-data"), spGender.getSelectedItem().toString()));

                   Log.e("param-->",
                           PreferenceFile.getInstance().getPreferenceData(this, Constant.ID) + " " +
                                   edUsername.getText().toString() + " " + edPanCardNumber.getText().toString() + " " + edBirthdate.getText().toString() + " " + spGender.getSelectedItem().toString());

                   if (Constant.isConnectingToInternet(PanCardVerification.this)) {
                       Log.e("connect--->", "2");
                       new Retrofit2(this, PanCardVerification.this, map, body,
                               Constant.REQ_PAN_CARD_VERIFY, Constant.PAN_CARD_VERIFY, "2").callService(true);
                   }
               }
        }
        else {
                   Log.e("If first","Elseeeee");

                   //todo in case of reject
                   if (PreferenceFile.getInstance().getPreferenceData(this, Constant.VERIFY_PAN).equals("No"))
                   {
                       Log.e("If 2nd","If");

                       if(verification()){
                           Log.e("Verification 2nd","If");

                           map = new HashMap<String, RequestBody>();

                           map.put("user_id", RequestBody.create(MediaType.parse("multipart/form-data"), PreferenceFile.getInstance().getPreferenceData(this, Constant.ID)));
                           map.put("name", RequestBody.create(MediaType.parse("multipart/form-data"), edUsername.getText().toString()));
                           map.put("last_name", RequestBody.create(MediaType.parse("multipart/form-data"), edlast.getText().toString()));
                           map.put("pan_number", RequestBody.create(MediaType.parse("multipart/form-data"), edPanCardNumber.getText().toString()));
                           map.put("dob", RequestBody.create(MediaType.parse("multipart/form-data"), edBirthdate.getText().toString()));
                           map.put("gender", RequestBody.create(MediaType.parse("multipart/form-data"), spGender.getSelectedItem().toString()));

                           Log.e("paramResubmit-->", PreferenceFile.getInstance().getPreferenceData(this, Constant.ID) + " " + edUsername.getText().toString() + " " + edPanCardNumber.getText().toString() + " " + edBirthdate.getText().toString() + " " + spGender.getSelectedItem().toString());

                           if (Constant.isConnectingToInternet(PanCardVerification.this)) {
                               Log.e("connect--->", "2");
                               new Retrofit2(this, PanCardVerification.this, map, body,
                                       Constant.REQ_PAN_CARD_VERIFY, Constant.PAN_CARD_VERIFY, "2").callService(true);
                           }
                       }
                   }
               }
            break;

            case  R.id.ivarrow:
                Constant.hideKeyboard(this,v);
                finish();

            break;

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

                intent=new Intent(PanCardVerification.this,AdhaarCardVerification.class);
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
                intent=new Intent(PanCardVerification.this,PanCardVerification.class);
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
                intent=new Intent(PanCardVerification.this,BankDetailsVerification.class);
                startActivity(intent);
                break;

            case R.id.ivImageSelect:
                //selectImage();

                if(flaag==0) {
                    popup();
                }
                break;

            case R.id.edBirthdate:
                date();

                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        callStateService();
    }

    public boolean verification() {

        Matcher matcher = pattern.matcher(edPanCardNumber.getText().toString());
// Check if pattern matches

        Log.e("verification--->",imagepath);

        if(imagepath.equals("")){

            Constant.alertDialog(this,getString(R.string.please_select_pan_card_image));

            return false;
        }

        if(edPanCardNumber.getText().toString().equals("")){

            Constant.alertDialog(this,getString(R.string.please_enter_pan_card_no));
            return false;
        }

        if (!matcher.matches()) {
            Log.e("Matching","Yes");
            Constant.alertDialog(this,getString(R.string.please_enter_valid_pan_card_no));
            return false;
        }

        if(edUsername.getText().toString().equals("")){

            Constant.alertDialog(this,getString(R.string.please_enter_first_name));
            return false;
        }
        if(edlast.getText().toString().equals("")){

            Constant.alertDialog(this,getString(R.string.please_enter_last_name));
            return false;
        }

        if(edBirthdate.getText().toString().equals("")){

            Constant.alertDialog(this,getString(R.string.please_select_dob));
            return false;
        }
        if(spGender.getSelectedItem().toString().equals("Select gender")){

            Constant.alertDialog(this,getString(R.string.please_select_gender));

            return false;
        }

        return true;
    }

    private void callStateService() {

        if (Constant.isConnectingToInternet(this)) {
            new Retrofit2(this, PanCardVerification.this, Constant.REQ_Dashboard_Refresh,
                    Constant.Dashboard_Refresh+PreferenceFile.getInstance().getPreferenceData(this,Constant.ID)).callService(true);
        }
        else {
            Constant.alertDialog(this, getResources().getString(R.string.check_connection));
        }
    }


    private void date() {

        final Calendar newCalendar = Calendar.getInstance();
        final Date curDate = new Date();

        dateFormatter.format(curDate);
        datePickerDialog = new DatePickerDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar userAge = new GregorianCalendar(year,monthOfYear,dayOfMonth);
                Calendar minAdultAge = new GregorianCalendar();
                minAdultAge.add(Calendar.YEAR, -18);
                if (minAdultAge.before(userAge)) {
                    Constant.alertDialog(PanCardVerification.this,getString(R.string.please_select_valid_date));
                }
                else {
                    edBirthdate.setText(dateFormatter.format(userAge.getTime()));
                }

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        datePickerDialog.show();
    }


    private void selectImage() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 2);
        }
        else
        {
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
        // Uri photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", createImageFile());
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
        }
        else if (requestCode == 111 && resultCode == RESULT_OK && data != null)
        {
            /*Gallery Image*/
            Uri uri = data.getData();
//            path = getPath(uri);
            imagepath = getPath(uri);

            Log.e("PathGet ",path);
                //   imagepath = data.getExtras().get("PATH").toString();
                file = new File(imagepath);
                Picasso.with(context).load(file).placeholder(R.drawable.placeholder).
                        into(imgProfile);
                Log.e("file--->", file.isFile() + " " + file.exists());
                req = RequestBody.create(MediaType.parse("image"), file);
                body = MultipartBody.Part.createFormData("image", file.getName(), req);
//            cropImage(path);
        } else if (requestCode == 301 && resultCode==1001)
        {
             /*Gallery/Camera Cropped Image*/
            imagepath = data.getExtras().get("PATH").toString();

            Log.e("priyaimagepath--->",imagepath);
            file=new File(imagepath);
            Picasso.with(context).load(file).placeholder(R.drawable.placeholder).
                    into(imgProfile);

            Log.e("file--->",file.isFile()+" "+file.exists());
            req= RequestBody.create(MediaType.parse("image"), file);
            body= MultipartBody.Part.createFormData("image", file.getName(), req);
        }
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
                            Log.e("againcall--->","yes");
                            BigDecimal ff = new BigDecimal(inr);
                            Log.e("newcal-->","d -->"+ ff);

                           // tvwallet.setText(String.format("%.2f", ff)+" "+PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));
                            // tvwallet.setText(formatter.format(ff) + " " + PreferenceFile.getInstance().getPreferenceData(this, Constant.Currency_Symbol));



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
                            }

                            PreferenceFile.getInstance().saveData(this,Constant.COUNT_SECURITY,"1");

                            callSetPanConditions();


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

            case Constant.REQ_PAN_CARD_VERIFY:

                try {
                    JSONObject result1 = new JSONObject(response.body().string());
                    Log.e("result1-->",result1.toString());
                    String status=result1.getString("response");
                    String message=result1.getString("message");

                    if(status.equals("true")){

                        if(PreferenceFile.getInstance().getPreferenceData(this,Constant.VERIFY_PAN).equals("Pending")){

                            txstatus.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.VERIFY_PAN));
                            txstatus.setTextColor(Color.parseColor("#d40e0e"));
                            txstatus.setVisibility(View.VISIBLE);
                        }

                        PreferenceFile.getInstance().saveData(this,Constant.PanVerification,"1");


                        final Dialog dialog = new Dialog(PanCardVerification.this);
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
                                Intent intent=new Intent(PanCardVerification.this,BankDetailsVerification.class);
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
