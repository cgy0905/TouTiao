package com.cgy.news.module.news.provider;

import com.cgy.news.R;
import com.cgy.news.model.entity.News;
import com.cgy.news.module.news.adapter.NewsListAdapter;
import com.cgy.news.utils.GlideUtils;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * @author cgy
 * @description 张图片布局(文章、广告)
 * @date 2019/5/10 18:03
 */
public class ThreePicNewsItemProvider extends BaseNewsItemProvider{

    public ThreePicNewsItemProvider(String channelCode) {
        super(channelCode);
    }

    @Override
    protected void setData(BaseViewHolder helper, News news) {
        //三张图片的新闻
        GlideUtils.load(mContext, news.image_list.get(0).url, helper.getView(R.id.iv_img1));
        GlideUtils.load(mContext, news.image_list.get(1).url, helper.getView(R.id.iv_img2));
        GlideUtils.load(mContext, news.image_list.get(2).url, helper.getView(R.id.iv_img3));
    }

    @Override
    public int viewType() {
        return NewsListAdapter.THREE_PICS_NEWS;
    }

    @Override
    public int layout() {
        return R.layout.item_three_pics_news;
    }
}
