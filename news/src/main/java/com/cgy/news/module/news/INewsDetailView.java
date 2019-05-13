package com.cgy.news.module.news;

import com.cgy.news.model.entity.NewsDetail;
import com.cgy.news.model.response.CommentResponse;

/**
 * @author cgy
 * @description 获取各种频道广告的view回调接口
 * @date 2019/5/13 10:13
 */
public interface INewsDetailView {

    void onGetNewsDetailSuccess(NewsDetail newsDetail);

    void onGetCommentSuccess(CommentResponse response);

    void onError();
}
