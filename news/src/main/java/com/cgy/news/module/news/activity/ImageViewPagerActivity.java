package com.cgy.news.module.news.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cgy.news.R;

public class ImageViewPagerActivity extends AppCompatActivity {

    private static final String TAG = ImageViewPagerActivity.class.getSimpleName();
    public static final String IMG_URLS = "mImageUrls";
    public static final String POSITION = "position";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view_pager);
    }
}
