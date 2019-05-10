package com.cgy.news.module.news;

import com.cgy.news.model.entity.News;

import java.util.List;

/**
 * @author cgy
 * @description 获取各种频道广告的view接口
 * @date 2019/5/9 17:49
 */
public interface INewsListView {

    void onGetNewsListSuccess(List<News> newsList, String tipInfo);

    void onError();
}
