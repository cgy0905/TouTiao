package com.cgy.news.module.main;

import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cgy.news.R;
import com.cgy.news.base.BaseActivity;
import com.cgy.news.base.BasePresenter;
import com.cgy.news.utils.UIUtils;
import com.chaychan.uikit.statusbar.Eyes;

import butterknife.BindView;
import butterknife.OnClick;

public class WebViewActivity extends BaseActivity {
    public static final String URL = "url";
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_author)
    TextView mTvAuthor;
    @BindView(R.id.pb_loading)
    ProgressBar mPbLoading;
    @BindView(R.id.webView)
    WebView mWebView;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_web_view;
    }

    @Override
    public void initView() {
        Eyes.setStatusBarColor(this, UIUtils.getColor(R.color.color_BDBDBD));
    }

    @Override
    public void initData() {
        String url = getIntent().getStringExtra(URL);
        mWebView.loadUrl(url);
    }

    @Override
    public void initListener() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mPbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mPbLoading.setVisibility(View.GONE);
            }
        });

        mWebView.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) { //表示按返回键
                    mWebView.goBack();//后退
                    return true;    //已处理

                }
            }
            return false;
        });
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
