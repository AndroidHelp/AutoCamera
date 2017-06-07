package com.katariya.autocamera;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.katariya.autocameralib.AutoFitTextureView;
import com.katariya.autocameralib.CameraController;
import com.katariya.autocameralib.CameraResult;

import java.io.File;

/**
 * Created by Hemant Katariya on 6/6/2017.
 */

public class Activity_SecondaryCamera extends AppCompatActivity implements CameraResult, View.OnClickListener {

    private TextView txt_timer;
    private AutoFitTextureView textureView;
    private CameraController cameraController;
    private ImageView img_photo,img_front;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final int cameraType = 0; //0 for back camera and 1 for front camera
    private static final int cameraPhotoWidth = 640; // captured photo width
    private static final int cameraPhotoHeight= 480; // captured photo width
    private static final int countdownTimer = 5000; //countdownTimer in miliseconds
    private String FOLDERNAME = "CustomCameraAPI2";  // foldername which will have taken photos
    private String PHOTONAME = "katariya";      // photoname with current timestamp
    private String imagePathPhoto = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary_camera);

        getSupportActionBar().setTitle("Secondary Camera");

        textureView = (AutoFitTextureView) findViewById(R.id.texture);
        assert textureView != null;
        img_photo = (ImageView) findViewById(R.id.imageView_Photo);
        img_photo.setOnClickListener(this);
        img_front = (ImageView)  findViewById(R.id.imageView_RearCamera);
        img_front.setOnClickListener(this);
        txt_timer = (TextView) findViewById(R.id.textView_Timer);
        cameraController = new CameraController(Activity_SecondaryCamera.this, textureView, this, cameraType);

        img_front.setClickable(false);
        new CountDownTimer(countdownTimer, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txt_timer.setTextSize(50);
                txt_timer.setText("" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                txt_timer.setText("done!");
                cameraController.takePicture(cameraPhotoWidth, cameraPhotoHeight, FOLDERNAME, PHOTONAME);
            }
        }.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(Activity_SecondaryCamera.this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }
            else
            {
                Toast.makeText(this, "granted"+requestCode, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void success(final String imagePath, File imageFile) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txt_timer.setTextSize(15);
                txt_timer.setText(imagePath);
                imagePathPhoto = imagePath;
                img_photo.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                img_front.setClickable(true);
            }
        });

  //      Toast.makeText(this, imagePath, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void failure(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txt_timer.setTextSize(20);
                txt_timer.setText(error);
                img_front.setClickable(true);
            }
        });
 //       Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imageView_RearCamera:
                startActivity(new Intent(Activity_SecondaryCamera.this, Activity_RearCamera.class));
                finish();
                break;
            case R.id.imageView_Photo:
                if (!imagePathPhoto.isEmpty()) {
                    Intent intent = new Intent(Activity_SecondaryCamera.this, Activity_Photo.class);
                    intent.putExtra("path", imagePathPhoto);
                    startActivity(intent);
                }
                break;
        }
    }
}