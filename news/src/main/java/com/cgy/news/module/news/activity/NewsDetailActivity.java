package com.cgy.news.module.news.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cgy.news.R;
import com.cgy.news.model.entity.NewsDetail;
import com.cgy.news.utils.GlideUtils;
import com.cgy.news.utils.UIUtils;
import com.chaychan.uikit.statusbar.Eyes;
import com.socks.library.KLog;

import butterknife.BindView;
import butterknife.OnClick;

public class NewsDetailActivity extends NewsDetailBaseActivity {


    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R.id.tv_author)
    TextView mTvAuthor;
    @BindView(R.id.ll_user)
    LinearLayout mLlUser;

    @Override
    protected int getViewContentViewId() {
        return R.layout.activity_news_detail;
    }

    @Override
    public void initView() {
        super.initView();
        Eyes.setStatusBarColor(this, UIUtils.getColor(R.color.color_BDBDBD));//设置状态栏的颜色为灰色
    }

    @Override
    public void initListener() {
        super.initListener();

        int llInfoBottom = mHeaderView.mLlInfo.getBottom();
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRvComment.getLayoutManager();
        mRvComment.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                int position = layoutManager.findFirstVisibleItemPosition();
                View firstVisibleChildView = layoutManager.findViewByPosition(position);
                int itemHeight = firstVisibleChildView.getHeight();
                int scrollHeight = (position) * itemHeight - firstVisibleChildView.getTop();

                KLog.i("scrollHeight: " + scrollHeight);
                KLog.i("llInfoBottom: " + llInfoBottom);

                mLlUser.setVisibility(scrollHeight > llInfoBottom ? View.VISIBLE : View.GONE);//如果滚动超过用户信息一栏,显示标题栏中的用户头像和昵称
            }

        });

    }

    @Override
    public void onGetNewsDetailSuccess(NewsDetail newsDetail) {
        mHeaderView.setDetail(newsDetail, () -> {
            //加载完成后, 显示内容布局
            mStateView.showContent();
        });

        mLlUser.setVisibility(View.GONE);

        if (newsDetail.media_user != null) {
            GlideUtils.loadRound(this, newsDetail.media_user.avatar_url, mIvAvatar);
            mTvAuthor.setText(newsDetail.media_user.screen_name);
        }
    }

    @Override
    public void onBackPressed() {
        postVideoEvent(false);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        postVideoEvent(false);
    }
}
