package com.cgy.news.module.news.provider;

import com.cgy.news.R;
import com.cgy.news.model.entity.News;
import com.cgy.news.module.news.adapter.NewsListAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * @author cgy
 * @description 纯文本新闻
 * @date 2019/5/10 17:04
 */
public class TextNewsItemProvider extends BaseNewsItemProvider{

    public TextNewsItemProvider(String channelCode) {
        super(channelCode);
    }

    @Override
    protected void setData(BaseViewHolder helper, News news) {
        //由于文本消息的逻辑目前已经在基类中封装,所以此处无须写
        //定义此类事提供文本消息的ItemProvider
    }

    @Override
    public int viewType() {
        return NewsListAdapter.TEXT_NEWS;
    }

    @Override
    public int layout() {
        return R.layout.item_text_news;
    }
}
