package com.cgy.news.model.response;

import java.util.List;

/**
 * @author cgy
 * @description
 * @date 2019/5/13 18:16
 */
public class VideoPathResponse {
    public String status;
    public List<VideoEntity> video;

    public class VideoEntity{
        public String url;
    }
}
