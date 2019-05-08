package com.cgy.news.module;

import android.content.Intent;

import com.cgy.news.module.main.view.MainActivity;
import com.cgy.news.R;
import com.cgy.news.base.BaseActivity;
import com.cgy.news.base.BasePresenter;
import com.cgy.news.utils.UIUtils;
import com.chaychan.uikit.statusbar.Eyes;

public class SplashActivity extends BaseActivity {


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public boolean enableSlideClose() {
        return false;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        Eyes.translucentStatusBar(this, false);
        UIUtils.postTaskDelay(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, 2000);
    }
}
