package com.katariya.autocamera;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Created by DELL on 6/6/2017.
 */

public class Activity_Photo extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);

        getSupportActionBar().setTitle("Full photo");

        ImageView img_photo = (ImageView) findViewById(R.id.imageView_Photo);
        if (getIntent().getStringExtra("path")!=null) {
            img_photo.setImageBitmap(BitmapFactory.decodeFile(getIntent().getStringExtra("path")));
        }
    }
}
