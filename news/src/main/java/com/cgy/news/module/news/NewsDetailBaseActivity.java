package com.cgy.news.module.news;

import com.cgy.news.base.BaseActivity;
import com.cgy.news.base.BasePresenter;

/**
 * @author cgy
 * @description
 * @date 2019/5/10 10:16
 */
public class NewsDetailBaseActivity extends BaseActivity {

    public static final String CHANNEL_CODE = "channelCode";
    public static final String VIDEO_URL = "videoUrl";
    public static final String PROGRESS = "progress";
    public static final String POSITION = "position";
    public static final String DETAIL_URL = "detailUrl";
    public static final String GROUP_ID = "groupId";
    public static final String ITEM_ID = "itemId";

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return 0;
    }
}
