package com.cgy.news.model.event;

/**
 * @author cgy
 * @description
 * @date 2019/5/10 11:31
 */
public class DetailCloseEvent {

    private String channelCode;
    private int position;
    private long progress;
    private int commentCount;

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
