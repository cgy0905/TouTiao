package com.cgy.news.module.news;

import com.cgy.news.api.SubscriberCallBack;
import com.cgy.news.base.BasePresenter;
import com.cgy.news.constants.Constant;
import com.cgy.news.model.entity.NewsDetail;
import com.cgy.news.model.response.CommentResponse;
import com.socks.library.KLog;

import rx.Subscriber;

/**
 * @author cgy
 * @description
 * @date 2019/5/13 10:15
 */
public class NewsDetailPresenter extends BasePresenter<INewsDetailView> {

    public NewsDetailPresenter(INewsDetailView view) {
        super(view);
    }

    public void getNewsDetail(String url) {
        addSubscription(mApiService.getNewsDetail(url), new SubscriberCallBack<NewsDetail>() {

            @Override
            protected void onSuccess(NewsDetail response) {
                mView.onGetNewsDetailSuccess(response);
            }

            @Override
            protected void onError() {
                mView.onError();
            }
        });
    }

    public void getComment(String groupId, String itemId, int pageNow) {
        int offset = (pageNow - 1) * Constant.COMMENT_PAGE_SIZE;
        addSubscription(mApiService.getComment(groupId, itemId, offset + "", String.valueOf(Constant.COMMENT_PAGE_SIZE)), new Subscriber<CommentResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                KLog.e(e.getLocalizedMessage());
                mView.onError();
            }

            @Override
            public void onNext(CommentResponse response) {
                mView.onGetCommentSuccess(response);

            }
        });
    }
}
