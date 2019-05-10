package com.cgy.news.api;

import com.cgy.news.model.entity.VideoModel;
import com.cgy.news.model.response.NewsResponse;
import com.cgy.news.model.response.ResultResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by cgy on 2019/5/8 15:03 .
 * 网络请求的service
 */
public interface ApiService {
    String GET_ARTICLE_LIST = "api/news/feed/v62/?refer=1&count=20&loc_mode=4&device_id=34960436458&iid=13136511752";
    String GET_COMMENT_LIST = "article/v2/tab_comments/";


    /**
     * 获取新闻列表
     * @param category  频道
     * @param lastTime
     * @param currentTime
     * @return
     */
    @GET(GET_ARTICLE_LIST)
    Observable<NewsResponse> getNewsList(@Query("category") String category, @Query("min_behot_time") long lastTime, @Query("last_refresh_sub_entrance_interval") long currentTime);


    /**
     * 获取视频页的html代码
     */
    @GET
    Observable<String> getVideoHtml(@Url String url);

    /**
     * 获取视频数据json
     * @param url
     * @return
     */
    @GET
    Observable<ResultResponse<VideoModel>> getVideoData(@Url String url);
}
