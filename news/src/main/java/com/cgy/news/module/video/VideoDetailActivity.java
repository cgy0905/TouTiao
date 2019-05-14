package com.cgy.news.module.video;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.cgy.news.R;
import com.cgy.news.model.entity.NewsDetail;
import com.cgy.news.module.news.activity.NewsDetailBaseActivity;
import com.cgy.news.utils.UIUtils;
import com.cgy.news.utils.VideoPathDecoder;
import com.cgy.news.widget.MyJZVideoPlayerStandard;
import com.chaychan.uikit.statusbar.Eyes;
import com.socks.library.KLog;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

public class VideoDetailActivity extends NewsDetailBaseActivity {


    @BindView(R.id.video_player)
    MyJZVideoPlayerStandard mVideoPlayer;
    @BindView(R.id.iv_back)
    ImageView mIvBack;

    private SensorManager mSensorManager;
    private Jzvd.JZAutoFullscreenListener mSensorEventListener;
    private long mProgress;
    private String mVideoUrl;

    @Override
    public void initView() {
        super.initView();
        Eyes.setStatusBarColor(this, UIUtils.getColor(android.R.color.black));
    }

    @Override
    public void initData() {
        super.initData();
        mProgress = getIntent().getLongExtra(PROGRESS, 0);
        mVideoUrl = getIntent().getStringExtra(VIDEO_URL);
    }

    @Override
    public void initListener() {
        super.initListener();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorEventListener = new Jzvd.JZAutoFullscreenListener();

        mVideoPlayer.setAllControlsVisiblity(View.GONE, View.GONE, View.VISIBLE, View.GONE, View.VISIBLE, View.VISIBLE, View.GONE);
        mVideoPlayer.titleTextView.setVisibility(View.GONE);
    }

    @Override
    protected int getViewContentViewId() {
        return R.layout.activity_video_detail;
    }

    @Override
    public void onGetNewsDetailSuccess(NewsDetail newsDetail) {
        KLog.e("onGetNewsDetailSuccess: " + newsDetail.url);
        newsDetail.content = "";
        mHeaderView.setDetail(newsDetail, () -> {
            //加载完成后,显示内容布局
            mStateView.showContent();
        });
        if (TextUtils.isEmpty(mVideoUrl)) {
            KLog.e("没有视频地址,解析视频");
            //如果列表页还没有获取到解析的视频地址,则详情页需要解析视频
            VideoPathDecoder decoder = new VideoPathDecoder() {
                @Override
                public void onSuccess(String url) {
                    KLog.e("onSuccess: " + url);
                    UIUtils.postTaskSafely(new Runnable() {
                        @Override
                        public void run() {
                            playVideo(url, newsDetail);
                        }
                    });
                }

                @Override
                public void onDecodeError(String errorMsg) {
                    UIUtils.showToast(errorMsg);
                }
            };
            decoder.decodePath(newsDetail.url);
        } else {
            //如果有视频地址,则直接播放
            KLog.e("有视频地址,则直接播放");
            playVideo(mVideoUrl, newsDetail);
        }
    }

    private void playVideo(String url, NewsDetail newsDetail) {
        mVideoPlayer.setUp(url, newsDetail.title, JzvdStd.SCREEN_WINDOW_LIST);
        mVideoPlayer.seekToInAdvance = mProgress;//设置进度
        mVideoPlayer.startVideo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorEventListener);
        Jzvd.releaseAllVideos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        postVideoEvent(true);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        postVideoEvent(true);
    }
}
