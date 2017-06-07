package com.katariya.autocamera;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.katariya.autocameralib.AutoFitTextureView;
import com.katariya.autocameralib.CameraController;
import com.katariya.autocameralib.CameraResult;

import java.io.File;

/**
 * Created by Hemant Katariya on 6/6/2017.
 */

public class Activity_RearCamera extends AppCompatActivity implements CameraResult, View.OnClickListener {
    private TextView txt_timer;
    private AutoFitTextureView textureView;
    private CameraController cameraController;
    private ImageView img_photo, img_front;
    private static final int cameraType = 1; //0 for back camera and 1 for front camera
    private static final int cameraPhotoWidth = 640; // captured photo width
    private static final int cameraPhotoHeight = 480; // captured photo width
    private static final int countdownTimer = 5000; //countdownTimer in miliseconds
    private String FOLDERNAME = "CustomCameraAPI2";  // foldername which will have taken photos
    private String PHOTONAME = "katariya";      // photoname with current timestamp
    private String imagePathPhoto = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rear_camera);

        getSupportActionBar().setTitle("Primary Camera");

        textureView = (AutoFitTextureView) findViewById(R.id.texture);
        assert textureView != null;
        img_photo = (ImageView) findViewById(R.id.imageView_Photo);
        img_photo.setOnClickListener(this);
        img_front = (ImageView) findViewById(R.id.imageView_SecondaryCamera);
        img_front.setOnClickListener(this);
        txt_timer = (TextView) findViewById(R.id.textView_Timer);
        cameraController = new CameraController(Activity_RearCamera.this, textureView, this, cameraType);

        img_front.setClickable(true);
        new CountDownTimer(countdownTimer, 1000) {

            public void onTick(long millisUntilFinished) {
                txt_timer.setTextSize(50);
                txt_timer.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                txt_timer.setText("done!");
                cameraController.takePicture(cameraPhotoWidth, cameraPhotoHeight, FOLDERNAME, PHOTONAME);
            }
        }.start();
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

    }

    @Override
    public void failure(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txt_timer.setTextSize(15);
                txt_timer.setText(error);
                img_front.setClickable(true);

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_SecondaryCamera:
                startActivity(new Intent(Activity_RearCamera.this, Activity_SecondaryCamera.class));
                finish();
                break;
            case R.id.imageView_Photo:
                if (!imagePathPhoto.isEmpty()) {
                    Intent intent = new Intent(Activity_RearCamera.this, Activity_Photo.class);
                    intent.putExtra("path", imagePathPhoto);
                    startActivity(intent);
                }
                break;
        }
    }
}
