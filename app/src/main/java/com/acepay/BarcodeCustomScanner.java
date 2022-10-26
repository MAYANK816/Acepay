package com.acepay;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class BarcodeCustomScanner extends AppCompatActivity {

    SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    ImageView btnGallery;
    private Uri imageUri;
    private BarcodeDetector detector;
//    MarshmallowPermissions marshmallowPermissions;
    private static final int CAMERA_REQUEST = 101;
    private static final String TAG = "API123";
    private static final String SAVED_INSTANCE_URI = "uri";
    private static final String SAVED_INSTANCE_RESULT = "result";
    private final int GALERY_CODE = 8;
    private final int BARCODE = 12;
    TextView txtResultBody;
    String intentData = "";
    ObjectAnimator animator;
    View scannerLayout;
    View scannerBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        animation();

        if (savedInstanceState != null)
        {
            if (imageUri != null)
            {
                imageUri = Uri.parse(savedInstanceState.getString(SAVED_INSTANCE_URI));
                txtResultBody.setText("abcd"+savedInstanceState.getString(SAVED_INSTANCE_RESULT));
            }
        }

        detector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                .build();

        if (!detector.isOperational()) {
            txtResultBody.setText("Detector initialisation failed");
            return;
        }
    }

    public void animation()
    {
        scannerLayout = findViewById(R.id.scannerLayout);
        scannerBar = findViewById(R.id.scannerBar);
        surfaceView = (SurfaceView) findViewById(R.id.cameraView);
        btnGallery = (ImageView) findViewById(R.id.btnGallery);
        txtResultBody = (TextView) findViewById(R.id.txtResultsBody);

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            ActivityCompat.requestPermissions(BarcodeCustomScanner.this, new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
            }
        });

        animator = null;

        ViewTreeObserver vto = scannerLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                scannerLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    scannerLayout.getViewTreeObserver().
                            removeGlobalOnLayoutListener(this);

                }
                else {
                    scannerLayout.getViewTreeObserver().
                            removeOnGlobalLayoutListener(this);
                }

                float destination = (float)(scannerLayout.getY() +
                        scannerLayout.getHeight());

                animator = ObjectAnimator.ofFloat(scannerBar, "translationY",
                        scannerLayout.getY(),
                        destination);

                animator.setRepeatMode(ValueAnimator.REVERSE);
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(3000);
                animator.start();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    takeBarcodePicture();
                    //   ImageSelection();

                } else {
//                    Toast.makeText(getApplicationContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GALERY_CODE && resultCode == RESULT_OK) {
            launchMediaScanIntent();
            try {
                Uri uri = data.getData();
                String path = "";
                String filePath[] = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, filePath, null, null, null);
                if (cursor == null) {
                    path = uri.getPath();

                } else {

                    cursor.moveToFirst();

                    int colIndex = cursor.getColumnIndex(filePath[0]);
                    path = cursor.getString(colIndex);
                }
                Bitmap bitmap = BitmapFactory.decodeFile(path);


                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                imageUri = getImageUri(getApplicationContext(), bitmap);

                bitmap = decodeBitmapUri(this, imageUri);
                Barcode code = null;

                if (detector.isOperational() && bitmap != null) {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<Barcode> barcodes = detector.detect(frame);
                    for (int index = 0; index < barcodes.size(); index++) {
                        code = barcodes.valueAt(index);
                        txtResultBody.setText("edfg"+txtResultBody.getText() + "\n" + code.displayValue + "\n");
                        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onActivityResult: "+code.toString()+"aaa"+code.displayValue+"aaa" );
                        int type = barcodes.valueAt(index).valueFormat;
                        switch (type) {
                            case Barcode.CONTACT_INFO:
                                Log.i(TAG, code.contactInfo.title);
                                break;
                            case Barcode.EMAIL:
                                Log.i(TAG, code.displayValue);
                                break;
                            case Barcode.ISBN:
                                Log.i(TAG, code.rawValue);
                                break;
                            case Barcode.PHONE:
                                Log.i(TAG, code.phone.number);
                                break;
                            case Barcode.PRODUCT:
                                Log.i(TAG, code.rawValue);
                                break;
                            case Barcode.SMS:
                                Log.i(TAG, code.sms.message);
                                break;
                            case Barcode.TEXT:
                                Log.i(TAG, code.displayValue);
                                break;
                            case Barcode.URL:
                                Log.i(TAG, "url: " + code.displayValue);
                                break;
                            case Barcode.WIFI:
                                Log.i(TAG, code.wifi.ssid);
                                break;
                            case Barcode.GEO:
                                Log.i(TAG, code.geoPoint.lat + ":" + code.geoPoint.lng);
                                break;
                            case Barcode.CALENDAR_EVENT:
                                Log.i(TAG, code.calendarEvent.description);
                                break;
                            case Barcode.DRIVER_LICENSE:
                                Log.i(TAG, code.driverLicense.licenseNumber);
                                break;
                            default:
                                Log.i(TAG, code.rawValue);
                                break;
                        }

                    }

                    Log.e(TAG, "onActivityResult: gallery scan code"+code.displayValue+"    "+code.rawValue);
                    animator.pause();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("resultDisplayValue",code.displayValue);
                    returnIntent.putExtra("resultBody",code.toString());
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                    if (barcodes.size() == 0) {
//                        txtResultBody.setText("No barcode could be detected. Please try again.");
                    }
                } else {
//                    txtResultBody.setText("Detector initialisation failed");
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to load Image Barcode", Toast.LENGTH_SHORT)
                        .show();
                Log.e(TAG, e.toString());
            }
        }
    }

    private void takeBarcodePicture() {

        Log.e("InsideBarcode Add","inside");
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALERY_CODE);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (imageUri != null) {
            outState.putString(SAVED_INSTANCE_URI, imageUri.toString());
            outState.putString(SAVED_INSTANCE_RESULT, txtResultBody.getText().toString()+"ijk");
        }
        super.onSaveInstanceState(outState);
    }

    private void launchMediaScanIntent() {
        Log.e("insideLAunchScan","bar");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private Bitmap decodeBitmapUri(Context ctx, Uri uri) throws FileNotFoundException {
        int targetW = 600;
        int targetH = 600;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(ctx.getContentResolver()
                .openInputStream(uri), null, bmOptions);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage,
                "**IMG_", "BarCodeImage");
        return Uri.parse(path);
    }

    private void initialiseDetectorsAndSources() {

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(BarcodeCustomScanner.this,
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                       try {
                           cameraSource.start(surfaceView.getHolder());
                       }catch (Exception e){
                           e.printStackTrace();
                       }
                    } else {
                        Log.e("InsideElseCamera ","bar");
                        ActivityCompat.requestPermissions(BarcodeCustomScanner.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
//                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                Log.e("barcodesValue bar ",barcodes.toString());

                if (barcodes.size() != 0) {

                    Log.e("BarcodeSize bar","if");

                    txtResultBody.post(new Runnable() {

                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                txtResultBody.removeCallbacks(null);
                                intentData = barcodes.valueAt(0).email.address;
                                Log.e("IntentData bar if",intentData);
                                txtResultBody.setText("123"+intentData);
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("resultDisplayValue",barcodes.valueAt(0).email);
//                                returnIntent.putExtra("resultBody",code.toString());
                                setResult(Activity.RESULT_OK,returnIntent);
                                finish();
                             } else {
                                 intentData = barcodes.valueAt(0).displayValue;
                                Log.e("IntentData bar else",intentData);

                                txtResultBody.setText("456"+intentData);
                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("resultDisplayValue",intentData);
//                                returnIntent.putExtra("resultBody",code.toString());
                                setResult(Activity.RESULT_OK,returnIntent);
                                finish();


                            }
                        }
                    });

                }
                else
                {
                    Log.e("BarcodeSize bar","else");

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

}
