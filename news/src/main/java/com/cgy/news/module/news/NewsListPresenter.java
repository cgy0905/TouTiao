package com.cgy.news.module.news;

import com.cgy.news.base.BasePresenter;
import com.cgy.news.model.entity.News;
import com.cgy.news.model.entity.NewsData;
import com.cgy.news.model.response.NewsResponse;
import com.cgy.news.utils.ListUtils;
import com.cgy.news.utils.PreUtils;
import com.google.gson.Gson;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * @author cgy
 * @description     新闻列表的presenter
 * @date 2019/5/9 18:00
 */
public class NewsListPresenter extends BasePresenter<INewsListView> {

    private long lastTime;

    public NewsListPresenter(INewsListView view) {
        super(view);
    }

    public void getNewsList(String channelCode) {
        lastTime = PreUtils.getLong(channelCode, 0);//读取对应频道下最后一次刷新的时间戳
        if (lastTime == 0) {
            //如果是空,则是从来没有刷新过, 使用当前时间戳
            lastTime = System.currentTimeMillis() / 1000;
        }

        addSubscription(mApiService.getNewsList(channelCode, lastTime, System.currentTimeMillis() / 1000), new Subscriber<NewsResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                KLog.e(e.getLocalizedMessage());
                mView.onError();
            }

            @Override
            public void onNext(NewsResponse response) {
                lastTime = System.currentTimeMillis() / 1000;
                PreUtils.putLong(channelCode, lastTime);//保存刷新的时间戳

                List<NewsData> data = response.data;
                List<News> newsList = new ArrayList<>();
                if (!ListUtils.isEmpty(data)) {
                    for (NewsData newsData : data) {
                        News news = new Gson().fromJson(newsData.content, News.class);
                        newsList.add(news);

                    }
                }
                KLog.e(newsList);
                mView.onGetNewsListSuccess(newsList, response.tips.display_info);
            }

        });

    }
}
