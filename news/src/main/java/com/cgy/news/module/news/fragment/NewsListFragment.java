package com.cgy.news.module.news.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.cgy.news.R;
import com.cgy.news.base.BaseFragment;
import com.cgy.news.constants.Constant;
import com.cgy.news.model.entity.News;
import com.cgy.news.model.entity.NewsRecord;
import com.cgy.news.model.entity.VideoEntity;
import com.cgy.news.model.event.DetailCloseEvent;
import com.cgy.news.model.event.TabRefreshCompletedEvent;
import com.cgy.news.model.event.TabRefreshEvent;
import com.cgy.news.module.main.WebViewActivity;
import com.cgy.news.module.news.INewsListView;
import com.cgy.news.module.news.activity.NewsDetailActivity;
import com.cgy.news.module.news.NewsListPresenter;
import com.cgy.news.module.news.activity.NewsDetailBaseActivity;
import com.cgy.news.module.news.adapter.NewsListAdapter;
import com.cgy.news.module.news.adapter.VideoListAdapter;
import com.cgy.news.module.video.VideoDetailActivity;
import com.cgy.news.utils.ListUtils;
import com.cgy.news.utils.MyJZVideoPlayerStandard;
import com.cgy.news.utils.NetWorkUtils;
import com.cgy.news.utils.NewsRecordHelper;
import com.cgy.news.utils.UIUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaychan.library.BottomBarItem;
import com.chaychan.uikit.TipView;
import com.chaychan.uikit.powerfulrecyclerview.PowerfulRecyclerView;
import com.chaychan.uikit.refreshlayout.BGANormalRefreshViewHolder;
import com.chaychan.uikit.refreshlayout.BGARefreshLayout;
import com.google.gson.Gson;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.jzvd.JZMediaManager;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdMgr;

/**
 * @author cgy
 * @description     展示每个频道新闻列表的fragment
 * @date 2019/5/9 11:04
 */
public class NewsListFragment extends BaseFragment<NewsListPresenter> implements INewsListView,
        BGARefreshLayout.BGARefreshLayoutDelegate, BaseQuickAdapter.RequestLoadMoreListener {

    private static final String TAG = NewsListFragment.class.getSimpleName();

    @BindView(R.id.tip_view)
    TipView mTipView;
    @BindView(R.id.rv_news)
    PowerfulRecyclerView mRvNews;
    @BindView(R.id.fl_content)
    FrameLayout mFlContent;
    @BindView(R.id.refresh_layout)
    BGARefreshLayout mRefreshLayout;

    private String mChannelCode;
    private boolean isVideoList;

    /**
     * 是否是推荐频道
     */
    private boolean isRecommendChannel;
    private List<News> mNewsList = new ArrayList<>();
    protected BaseQuickAdapter mNewsAdapter;

    /**
     * 是否是点击底部标签进行刷新的标识
     */
    private boolean isClickTabRefreshing;
    private RotateAnimation mRotateAnimation;
    private Gson mGson = new Gson();

    //新闻纪录
    private NewsRecord mNewsRecord;

    //用于标记是否是首页的底部刷新,如果是加载成功后发送完成的事件
    private boolean isHomeTabRefresh;

    @Override
    protected NewsListPresenter createPresenter() {
        return new NewsListPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_news_list;
    }

    @Override
    public View getStateViewRoot() {
        return mFlContent;
    }

    @Override
    public void initView(View rootView) {
        mRefreshLayout.setDelegate(this);
        mRvNews.setLayoutManager(new GridLayoutManager(mActivity, 1));
        //设置下拉刷新和下拉加载更多的风格 参数1：应用程序上下文 参数2：是否具有上拉加载更多功能
        BGANormalRefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(mActivity, false);
        //设置下拉刷新
        refreshViewHolder.setRefreshViewBackgroundColorRes(R.color.color_F3F5F4);//背景色
        refreshViewHolder.setPullDownRefreshText(UIUtils.getString(R.string.refresh_pull_down_text));//下拉的提示文字
        refreshViewHolder.setReleaseRefreshText(UIUtils.getString(R.string.refresh_release_text));//松开的提示文字
        refreshViewHolder.setRefreshingText(UIUtils.getString(R.string.refresh_ing_text));//刷新中的提示文字
    }

    @Override
    public void initData() {
        mChannelCode = getArguments().getString(Constant.CHANNEL_CODE);
        isVideoList = getArguments().getBoolean(Constant.IS_VIDEO_LIST, false);

        String[] channelCodes = UIUtils.getStringArr(R.array.channel_code);
        isRecommendChannel = mChannelCode.equals(channelCodes[0]);//是否是推荐频道
    }

    @Override
    public void initListener() {
        if (isVideoList) {
            //如果是视频列表
            mNewsAdapter = new VideoListAdapter(mNewsList);
        } else {
            //其他新闻列表
            mNewsAdapter = new NewsListAdapter(mChannelCode, mNewsList);
        }
        mRvNews.setAdapter(mNewsAdapter);

        mNewsAdapter.setOnItemClickListener((baseQuickAdapter, view, position) -> {
            News news = mNewsList.get(position);

            String itemId = news.item_id;
            StringBuffer urlSb = new StringBuffer("http://m.toutiao.com/i");
            urlSb.append(itemId).append("/info/");
            String url = urlSb.toString(); //http://m.toutiao.com/i6412427713050575361/info/
            Intent intent = null;
            if (news.has_video) {
                //视频
                intent = new Intent(mActivity, VideoDetailActivity.class);
                if (JZMediaManager.instance() != null && JzvdMgr.getCurrentJzvd() != null) {
                    //传递进度
                    long progress = JZMediaManager.getCurrentPosition();
                    if (progress != 0) {
                        intent.putExtra(VideoDetailActivity.PROGRESS, progress);
                    }
                    VideoEntity videoDetailInfo = news.video_detail_info;
                    String videoUrl = "";
                    if (videoDetailInfo != null && !TextUtils.isEmpty(videoDetailInfo.parse_video_url)) {
                        videoUrl = videoDetailInfo.parse_video_url;
                    }
                    intent.putExtra(VideoDetailActivity.VIDEO_URL, videoUrl);
                }
            } else {
                //非视频新闻
                if (news.article_type == 1) {
                    //如果article_type为1, 则是使用WebViewActivity打开
                    intent = new Intent(mActivity, WebViewActivity.class);
                    intent.putExtra(WebViewActivity.URL, news.article_url);
                    startActivity(intent);
                }
                //其他新闻
                intent = new Intent(mActivity, NewsDetailActivity.class);
            }

            intent.putExtra(NewsDetailBaseActivity.CHANNEL_CODE, mChannelCode);
            intent.putExtra(NewsDetailBaseActivity.POSITION, position);

            intent.putExtra(NewsDetailBaseActivity.DETAIL_URL, url);
            intent.putExtra(NewsDetailBaseActivity.GROUP_ID, news.group_id);
            intent.putExtra(NewsDetailBaseActivity.ITEM_ID, itemId);

            startActivity(intent);

        });

        mNewsAdapter.setEnableLoadMore(true);
        mNewsAdapter.setOnLoadMoreListener(this, mRvNews);
        if (isVideoList) {
            //如果是视频列表,监听滚动
            mRvNews.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
                @Override
                public void onChildViewAttachedToWindow(@NonNull View view) {

                }

                @Override
                public void onChildViewDetachedFromWindow(@NonNull View view) {
                    MyJZVideoPlayerStandard jzvd = view.findViewById(R.id.video_player);
                    if (jzvd != null && jzvd.jzDataSource != null && jzvd.jzDataSource.containsTheUrl(JZMediaManager.getCurrentUrl())) {
                        Jzvd currentJzvd = JzvdMgr.getCurrentJzvd();
                        if (currentJzvd != null && currentJzvd.currentScreen != Jzvd.SCREEN_WINDOW_FULLSCREEN) {
                            Jzvd.releaseAllVideos();
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void loadData() {
        mStateView.showLoading();

        //查找该频道的最后一组纪录
        mNewsRecord = NewsRecordHelper.getLastNewsRecord(mChannelCode);
        if (mNewsRecord == null) {
            //找不到记录,拉取网络数据
            mNewsRecord = new NewsRecord();//创建一个没有数据的对象
            mPresenter.getNewsList(mChannelCode);
            return;
        }

        //找到最后一组记录,转换成新闻集合并展示
        List<News> newsList = NewsRecordHelper.convertToNewsList(mNewsRecord.getJson());
        mNewsList.addAll(newsList);//添加到集合中
        mNewsAdapter.notifyDataSetChanged();//刷新adapter

        mStateView.showContent();//显示内容

        //判断时间是否超过10分钟,如果是则自动刷新
        if (mNewsRecord.getTime() - System.currentTimeMillis() == 10 * 60 * 1000) {
            mRefreshLayout.beginRefreshing();
        }

    }

    @Override
    public void onGetNewsListSuccess(List<News> newsList, String tipInfo) {
        mRefreshLayout.endRefreshing();//加载完毕后,在UI线程结束下拉刷新
        if (isHomeTabRefresh) {
            postRefreshCompletedEvent();//发送加载完成的事件
        }

        //如果是第一次获取数据
        if (ListUtils.isEmpty(mNewsList)) {
            if (ListUtils.isEmpty(newsList)) {
                //获取不到数据,显示空布局
                mStateView.showEmpty();
                return;
            }
            mStateView.showContent();//显示内容
        }

        if (ListUtils.isEmpty(newsList)) {
            //已经获取不到新闻了,处理出现获取不到新闻的情况
            UIUtils.showToast(UIUtils.getString(R.string.no_news_now));
            return;
        }

        if (TextUtils.isEmpty(newsList.get(0).title)) {
            //由于汽车、体育等频道第一条属于导航的内容,所以第一条没有标题,则移除
            newsList.remove(0);

        }

        dealRepeat(newsList);//处理新闻重复问题

        mNewsList.addAll(0, newsList);
        mNewsAdapter.notifyDataSetChanged();

        mTipView.show(tipInfo);

        //保存到数据库
        NewsRecordHelper.save(mChannelCode, mGson.toJson(newsList));
    }

    /**
     * 处理置顶新闻和广告重复
     */
    private void dealRepeat(List<News> newsList) {
        if (isRecommendChannel && !ListUtils.isEmpty(mNewsList)) {
            //如果是推荐频道并且数据列表已经有数据,处理置顶新闻或广告重复的问题
            mNewsList.remove(0);//由于第一条新闻是重复的,移除原有的第一条
            //新闻列表通常第4个是广告, 除了第一次有广告,再次获取的都移除广告
            if (newsList.size() >= 4) {
                News fourthNews = newsList.get(3);
                //如果列表第4个和原有列表第4个新闻都是广告,并且id一致,移除
                if (fourthNews.tag.equals(Constant.ARTICLE_GENRE_AD)) {
                    newsList.remove(fourthNews);
                }
            }
        }
    }

    @Override
    public void onError() {
        mTipView.show();//弹出提示

        if (ListUtils.isEmpty(mNewsList)) {
            //如果一开始进入没有数据
            mStateView.showEmpty();//显示重试的布局
        }

        //收起刷新
        if (mRefreshLayout.getCurrentRefreshStatus() == BGARefreshLayout.RefreshStatus.REFRESHING) {
            mRefreshLayout.endRefreshing();
        }
        postRefreshCompletedEvent();//发送加载完成的事件
    }

    private void postRefreshCompletedEvent() {
        if (isClickTabRefreshing) {
            //如果是点击底部刷新获取到数据的,发送加载完成的事件
            EventBus.getDefault().post(new TabRefreshCompletedEvent());
            isClickTabRefreshing = false;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        //BaseRecyclerViewAdapterHelper的加载更多
        if (mNewsRecord.getPage() == 0 || mNewsRecord.getPage() == 1) {
            //如果记录的页数为0(即是创建空的记录) 获取页数为1(即已经是第一条记录)
            //mRefreshLayout.endLoadMore();//结束加载更多
            mNewsAdapter.loadMoreEnd();
            return;
        }

        NewsRecord preNewsRecord = NewsRecordHelper.getPreNewsRecord(mChannelCode, mNewsRecord.getPage());
        if (preNewsRecord == null) {
            // mRefreshLayout.endLoadingMore();//结束加载更多
            mNewsAdapter.loadMoreEnd();
            return;
        }

        mNewsRecord = preNewsRecord;

        long startTime = System.currentTimeMillis();

        List<News> newsList = NewsRecordHelper.convertToNewsList(mNewsRecord.getJson());

        if (isRecommendChannel) {
            //如果是推荐频道
            newsList.remove(0);//移除第一个,因为第一个是置顶新闻,重复
        }

        KLog.e(newsList);

        long endTime = System.currentTimeMillis();

        //由于是读取数据库,如果耗时不足1s,则1s后才收起加载更多
        if (endTime - startTime <= 1000) {
            UIUtils.postTaskDelay(new Runnable() {
                @Override
                public void run() {
                    mNewsAdapter.loadMoreComplete();
                    mNewsList.addAll(newsList);//添加到集合下面
                    mNewsAdapter.notifyDataSetChanged();//刷新adapter
                }
            }, (int) (1000 - (endTime - startTime)));
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        if (!NetWorkUtils.isNetworkAvailable(mActivity)) {
            //网络不可用弹出提示
            mTipView.show();
            if (mRefreshLayout.getCurrentRefreshStatus() == BGARefreshLayout.RefreshStatus.REFRESHING) {
                mRefreshLayout.endRefreshing();
            }
            return;
        }
        mPresenter.getNewsList(mChannelCode);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        //BGARefresh的加载更多,不处理,使用到的是BaseRecyclerViewAdapterHelper的加载更多
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(TabRefreshEvent event) {
        if (event.getChannelCode().equals(mChannelCode) && mRefreshLayout.getCurrentRefreshStatus() != BGARefreshLayout.RefreshStatus.REFRESHING) {
            //如果和当前的频道码一致并且不是刷新中,进行下拉刷新
            if (!NetWorkUtils.isNetworkAvailable(mActivity)) {
                //网络不可用提示
                mTipView.show();
                return;
            }

            isClickTabRefreshing = true;

            if (event.isHomeTab()) {
                //如果页签是首页,则换成就加载的图标并执行动画
                BottomBarItem bottomBarItem = event.getBottomBarItem();
                bottomBarItem.setIconSelectedResourceId(R.mipmap.tab_loading);//更换成加载图标
                bottomBarItem.setStatus(true);

                //播放旋转动画
                if (mRotateAnimation == null) {
                    mRotateAnimation = new RotateAnimation(0, 360,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                            0.5f);
                    mRotateAnimation.setDuration(800);
                    mRotateAnimation.setRepeatCount(-1);
                }
                ImageView bottomImageView = bottomBarItem.getImageView();
                bottomImageView.setAnimation(mRotateAnimation);
                bottomImageView.startAnimation(mRotateAnimation);//播放旋转动画
            }

            isHomeTabRefresh = event.isHomeTab();//是否是首页

            mRvNews.scrollToPosition(0);//滚动到顶部
            mRefreshLayout.beginRefreshing();//开始下拉刷新
        }
    }

    /**
     * 详情页关闭后传递过来的事件,更新评论数播放进度等
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDetailCloseEvent(DetailCloseEvent event) {
        if (!event.getChannelCode().equals(mChannelCode)) {
            //如果频道不一致 不用处理
            return;
        }

        int position = event.getPosition();
        int commentCount = event.getCommentCount();

        News news = mNewsList.get(position);
        news.comment_count = commentCount;

        if (news.video_detail_info != null) {
            //如果有视频
            long progress = event.getProgress();
            news.video_detail_info .progress = progress;
        }

        //刷新adapter
        mNewsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        registerEventBus(NewsListFragment.this);
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterEventBus(NewsListFragment.this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        KLog.e("onDestroy" + mChannelCode);
    }
}