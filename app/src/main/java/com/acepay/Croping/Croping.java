package com.acepay.Croping;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.acepay.R;
import com.isseiaoki.simplecropview.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Croping extends AppCompatActivity implements View.OnClickListener {

    CropImageView cropImageView;
    int source;
    String imagePath;
    public Toolbar toolbar;
    String oldPath;
    public TextView done, title, cancel;
    private ImageButton buttonRotateLeft, buttonRotateRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_croping);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        done = (TextView) findViewById(R.id.done);
        cancel = (TextView) findViewById(R.id.cancel);
        buttonRotateLeft=(ImageButton)findViewById(R.id.buttonRotateLeft);
        buttonRotateRight=(ImageButton)findViewById(R.id.buttonRotateRight);
        done.setOnClickListener(this);
        cancel.setOnClickListener(this);
        buttonRotateRight.setOnClickListener(this);
        buttonRotateLeft.setOnClickListener(this);

        cropImageView = (CropImageView) findViewById(R.id.cropImageView);
        cropImageView.setMinFrameSizeInDp(50);
        cancel.setVisibility(View.VISIBLE);
        done.setVisibility(View.VISIBLE);

        if (getIntent().hasExtra("RECT"))
        {
            source = 101;
            File image = new File(getIntent().getStringExtra("RECT"));
            if (image.exists()){
                Bitmap bitmap = BitmapFactory.decodeFile(getIntent().getStringExtra("RECT"));
                cropImageView.setImageBitmap(bitmap);
                cropImageView.setCropMode(CropImageView.CropMode.CUSTOM);
                cropImageView.setCustomRatio(10,6);
            }
        }
        else if (getIntent().hasExtra("PROFILE"))
        {
            source = 102;
            File image = new File(getIntent().getStringExtra("PROFILE"));
            if (image.exists())
            {
                Bitmap bitmap = BitmapFactory.decodeFile(getIntent().getStringExtra("PROFILE"));

//                cropImageView.setImageBitmap(bitmap);
                try {
                    cropImageView.setImageBitmap(modifyOrientation(bitmap,getIntent().getStringExtra("PROFILE")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cropImageView.setCropMode(CropImageView.CropMode.FREE);
            }
        }
        else if (getIntent().hasExtra("SQUARE"))
        {
            source = 103;
            oldPath=getIntent().getStringExtra("SQUARE");
            File image = new File(oldPath);
            if (image.exists())
            {

                Bitmap bitmap=BitmapFactory.decodeFile(oldPath);
                cropImageView.setImageBitmap(bitmap);

                cropImageView.setCropMode(CropImageView.CropMode.SQUARE);
                cropImageView.setCustomRatio(10,10);
            }
        }


    }

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private String saveToInternalStorage(Bitmap bitmapImage, int source) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File mypath = null;

        if (source == 101){
            mypath=new File(dir, timeStamp+"tazmeniaCover.jpeg");
        }else if (source == 102){
            mypath=new File(dir,timeStamp+"tazprofile.jpg");
        }
        else if (source==103){
            mypath=new File(dir,timeStamp+"tazprofile.jpg");
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        imagePath = mypath.getPath();
        return dir.getAbsolutePath();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.done:
                // Toast.makeText(this, "yes", Toast.LENGTH_SHORT).show();
                String path = saveToInternalStorage(cropImageView.getCroppedBitmap(), source);
                try {
                    if (imagePath != null) {
                        Intent intent = new Intent();
                        intent.putExtra("PATH", imagePath);
                        setResult(1001, intent);
                        finish();
                    }
                }
                catch (OutOfMemoryError e){
                    e.printStackTrace();
                }
                break;
            case R.id.cancel:
                finish();
                break;
            case R.id.buttonRotateLeft:
                cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D);
                break;
            case R.id.buttonRotateRight:
                cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
                break;
        }
    }

   /* public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }*/

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;

        }

        return bitmap;
    }

}
