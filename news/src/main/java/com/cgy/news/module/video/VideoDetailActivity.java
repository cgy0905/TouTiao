package com.cgy.news.module.video;

import android.os.Bundle;

import com.cgy.news.R;
import com.cgy.news.module.news.activity.NewsDetailBaseActivity;

public class VideoDetailActivity extends NewsDetailBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
    }
}
