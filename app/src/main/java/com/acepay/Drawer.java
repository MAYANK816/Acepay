package com.acepay;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.acepay.Croping.Croping;
import com.acepay.QuickHelp.DashBoardHelp;
import com.acepay.Util.Constant;
import com.acepay.Util.PreferenceFile;
import com.acepay.Util.Retrofit2;
import com.acepay.Util.RetrofitResponse;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;


public class Drawer extends AppCompatActivity implements View.OnClickListener, RetrofitResponse
{

    boolean doubleBackToExitPressedOnce = false;
    TextView tvheadername,tvNumber,txName,tvName;
    String picturePath="";
    Intent CamIntent,GalIntent,CropIntent;
    Uri uri;
    File file;
    private File profileIMG;
    LinearLayout tvViewProfile;
    Bitmap bitmap;
    MultipartBody.Part body;
    private HashMap<String, RequestBody> map;

    public View view;
    public FrameLayout frame;
    public static DrawerLayout drawer;
    public Toolbar toolbar;
    public RelativeLayout rlMain;
    public TextView tvTitle;
    public NavigationView navigationView;
    public ActionBarDrawerToggle drawerToggle;
    ImageView ivCross,imgProfile,ivIvg;
    Intent intent;
    LinearLayout llDashboard, llVer, llBitcoinAddressBook, llBitcoinAddress, llStatements, llRefer,
            llRateChart, llSync, llNoti, llAbout, llQuick,llSettings,llmessage,lllogout;

    @Override
    public void setContentView(int layoutResID) {

        view = getLayoutInflater().inflate(R.layout.activity_drawer, null);
        frame = (FrameLayout) view.findViewById(R.id.frame);
        getLayoutInflater().inflate(layoutResID, frame, true);

        super.setContentView(view);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        rlMain = (RelativeLayout) findViewById(R.id.rlMain);
//        navigationView.inflateMenu(R.menu.drawer_menu);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txName = (TextView) findViewById(R.id.txName);
        tvName = (TextView) findViewById(R.id.tvName);
        toolbar.setNavigationIcon(R.drawable.menu);

        setSupportActionBar(toolbar);

        map = new HashMap<String, RequestBody>();

        toolbar.setTitle("");
        toolbar.setSubtitle("");


        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu();
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                supportInvalidateOptionsMenu();
            }
        };

        drawerToggle.setDrawerIndicatorEnabled(false);
        drawer.setDrawerListener(drawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle.syncState();

        drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        InitializeView();

        if(PreferenceFile.getInstance().getPreferenceData(this,Constant.Username)!=null){
            tvName.setText(PreferenceFile.getInstance().getPreferenceData(this,Constant.Username)+"");
        }
        if(PreferenceFile.getInstance().getPreferenceData(this,Constant.Image)!=null){

            Log.e("imagepath--->", Constant.ImagePath+PreferenceFile.getInstance().getPreferenceData(this,Constant.Image));

            Picasso.with(this)
                    .load(Constant.ImagePath+PreferenceFile.getInstance().getPreferenceData(this,Constant.Image)).
                    resize(400,400).placeholder(getResources().getDrawable(R.drawable.logo_ace))
                    .into(imgProfile);
        }

      //  tvViewProfile.setOnClickListener(this);

    }

    private void InitializeView() {

        llmessage=(LinearLayout) findViewById(R.id.llmessage);
        llDashboard = (LinearLayout) findViewById(R.id.llDashboard);
        llVer = (LinearLayout) findViewById(R.id.llVer);
//        llBitcoinAddress = (LinearLayout) findViewById(R.id.llBitcoinAddress);
        llBitcoinAddressBook = (LinearLayout) findViewById(R.id.llBitcoinAddressBook);
        llStatements = (LinearLayout) findViewById(R.id.llStatements);
        llRateChart = (LinearLayout) findViewById(R.id.llRateChart);
        lllogout=(LinearLayout) findViewById(R.id.lllogout);

        llRefer = (LinearLayout) findViewById(R.id.llRefer);
//        llSync = (LinearLayout) findViewById(R.id.llSync);
        llQuick = (LinearLayout) findViewById(R.id.llQuick);
        llAbout = (LinearLayout) findViewById(R.id.llAbout);
        llNoti = (LinearLayout) findViewById(R.id.llNoti);
        llSettings = (LinearLayout) findViewById(R.id.llSettings);


        ivCross = (ImageView) findViewById(R.id.ivCross);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);


        llDashboard.setOnClickListener(this);
        llmessage.setOnClickListener(this);
        llVer.setOnClickListener(this);
//        llBitcoinAddress.setOnClickListener(this);
        llBitcoinAddressBook.setOnClickListener(this);
        llStatements.setOnClickListener(this);
        llRateChart.setOnClickListener(this);
        llRefer.setOnClickListener(this);
//        llSync.setOnClickListener(this);
        llQuick.setOnClickListener(this);
        llAbout.setOnClickListener(this);
        llNoti.setOnClickListener(this);
        ivCross.setOnClickListener(this);
        llSettings.setOnClickListener(this);
        imgProfile.setOnClickListener(this);
        lllogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        drawer.closeDrawers();

        switch (v.getId())
        {

            case R.id.ivCross:
                drawer.closeDrawers();
                break;
            case R.id.llmessage:
                    intent = new Intent(Drawer.this, NotificationPage.class);
                    startActivity(intent);
                break;
            case R.id.lllogout:
                PreferenceFile.getInstance().saveData(Drawer.this,Constant.ID,null);
                Intent intent = new Intent(this, AfterSplash.class);
                startActivity(intent);
                finishAffinity();
                break;

            case R.id.imgProfile:
                intent = new Intent(this, ViewProfile.class);
                startActivity(intent);
                break;

            case R.id.llDashboard:
                Dashboard.flag=1;
                finish();
                intent = new Intent(Drawer.this, Dashboard.class);
                startActivity(intent);
                break;

            case R.id.llVer:
                intent = new Intent(Drawer.this, PanCardVerification.class);
                startActivity(intent);
                break;

//            case R.id.llBitcoinAddress:
//                intent = new Intent(Drawer.this, BitcoinAddress.class);
//                intent.putExtra("key","address");
//                startActivity(intent);
//                break;

            case R.id.llBitcoinAddressBook:
                intent = new Intent(Drawer.this, BitcoinAddressAddedList.class);
                intent.putExtra("key","drawer");
                startActivity(intent);
                break;

            case R.id.llSettings:
                intent = new Intent(Drawer.this, Setting.class);
                startActivity(intent);
                break;

            case R.id.llStatements:
                intent = new Intent(Drawer.this, Statements.class);
                intent.putExtra("key","statement");
                startActivity(intent);
                break;

            case R.id.llRateChart:
//                intent = new Intent(Drawer.this, RateChart.class);
//                startActivity(intent);
                break;

//            case R.id.llSync:
////                intent = new Intent(Drawer.this,SinkPage.class);
////                startActivity(intent);
//                break;

            case R.id.llQuick:
                intent = new Intent(Drawer.this,newSupportEmail.class);
                startActivity(intent);
                break;

            case R.id.llAbout:
                intent = new Intent(Drawer.this,AboutUS.class);
                startActivity(intent);
                break;

            case R.id.llRefer:
                intent = new Intent(Drawer.this, ReferalCode.class);
                startActivity(intent);
                break;

            case R.id.llNoti:
                intent = new Intent(Drawer.this, NotificationPage.class);
                startActivity(intent);
                break;

//            case R.id.quick_help:
//
//                intent = new Intent(Drawer.this, FirstToolTip.class);
//                startActivity(intent);


        }

    }








    public void popup()
    {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View diaview = inflater.inflate(R.layout.choose_image, null);
        dialogBuilder.setView(diaview);
        final ImageView cancal = (ImageView) diaview.findViewById(R.id.camera);
        final ImageView gallery = (ImageView) diaview.findViewById(R.id.gallery);
        final android.app.AlertDialog dialog = dialogBuilder.create();
        cancal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null)
                {
                    try {
                        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        profileIMG = new File(dir, Long.toString(System.currentTimeMillis()) + ".jpg");
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(profileIMG));
                        startActivityForResult(cameraIntent, 33);
                        saveImage(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

    public void ClickImageFromCamera() {

        CamIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        file = new File(Environment.getExternalStorageDirectory(),
                "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        uri = Uri.fromFile(file);

        CamIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);

        CamIntent.putExtra("return-data", true);

        startActivityForResult(CamIntent, 0);

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
        intent.putExtra("SQUARE", path);
        startActivityForResult(intent, 301);
    }

    public void ImageCropFunction() {

        // Image Crop Code
        try {
            CropIntent = new Intent("com.android.camera.action.CROP");

            CropIntent.setDataAndType(uri, "image/*");

            CropIntent.putExtra("crop", "true");
            CropIntent.putExtra("outputX", 1080);
            CropIntent.putExtra("outputY", 1080);
            CropIntent.putExtra("aspectX", 4);
            CropIntent.putExtra("aspectY", 4);
            CropIntent.putExtra("scaleUpIfNeeded", true);
            CropIntent.putExtra("return-data", true);

            startActivityForResult(CropIntent, 1);

        } catch (ActivityNotFoundException e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 33 && resultCode == RESULT_OK) {
                // TODO:  image from Camera
                Uri imageUri = Uri.parse(profileIMG.getPath());
                picturePath = imageUri.getPath();
                cropImage(imageUri.getPath());
            } else if (requestCode == 111 && resultCode == RESULT_OK && data != null) {
                /*Gallery Image*/
                Uri uri = data.getData();
                picturePath = getPath(uri);

                file = new File(picturePath);
                Picasso.with(this).load(file).placeholder(R.drawable.placeholder).
                        into(imgProfile);

                Log.e("file--->", file.isFile() + " " + file.exists());

                if (file.exists()) {
                    RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
                    map.put("user_id", RequestBody.create(MediaType.parse("multipart/form-data"), PreferenceFile.getInstance().getPreferenceData(this, Constant.ID)));
                    if (Constant.isConnectingToInternet(Drawer.this)) {
                        new Retrofit2(Drawer.this, Drawer.this, map, body, Constant.REQ_EDIT_PROFILE, Constant.EDIT_PROFILE, "2").callService(true);

                    } else {

                        Constant.alertDialog(Drawer.this, getResources().getString(R.string.check_connection));
                    }
                }


                // cropImage(picturePath);
            } else if (requestCode == 301 && resultCode == 1001) {
                /*Gallery/Camera Cropped Image*/

                picturePath = data.getExtras().get("PATH").toString();
                file = new File(picturePath);

                Log.e("file-->", file.exists() + "");

                Picasso.with(this).load(file).placeholder(R.drawable.placeholder).
                        into(imgProfile);

                Log.e("file--->", file.isFile() + " " + file.exists());
         /*   RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
*/
                if (file.exists()) {
                    RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
                    map.put("user_id", RequestBody.create(MediaType.parse("multipart/form-data"), PreferenceFile.getInstance().getPreferenceData(this, Constant.ID)));
                    if (Constant.isConnectingToInternet(Drawer.this)) {
                        new Retrofit2(Drawer.this, Drawer.this, map, body, Constant.REQ_EDIT_PROFILE, Constant.EDIT_PROFILE, "2").callService(true);

                    } else {

                        Constant.alertDialog(Drawer.this, getResources().getString(R.string.check_connection));
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public String getPath(Uri uri) {
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



    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        Log.e("on service-->","drawer");
        switch (requestCode) {

            case Constant.REQ_EDIT_PROFILE:
                if (response.isSuccessful()) {

                    try {

                        JSONObject result = new JSONObject(response.body().string());

                        String status = result.getString("response");
                        String message = result.getString("message");
                        if (status.equals("true")) {

                            JSONObject data=result.getJSONObject("data");

                            PreferenceFile.getInstance().saveData(this, Constant.Image, data.getString("image"));
                            Constant.alertDialog(this, message);
                            drawer.closeDrawers();
                        } else {
                            drawer.closeDrawers();
                            Constant.alertDialog(this, message);
                            drawer.closeDrawers();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {

                    }
                }
                else {
                    Constant.alertDialog(this, getResources().getString(R.string.try_again));
                }

                break;
            }


        }

}
