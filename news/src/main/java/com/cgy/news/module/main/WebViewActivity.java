package com.cgy.news.module.main;

import com.cgy.news.base.BaseActivity;
import com.cgy.news.base.BasePresenter;

public class WebViewActivity extends BaseActivity {
    public static final String URL = "url";

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return 0;
    }
}
