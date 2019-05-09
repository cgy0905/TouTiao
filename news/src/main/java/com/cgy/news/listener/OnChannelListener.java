package com.cgy.news.listener;

/**
 * @author cgy
 * @description
 * @date 2019/5/9 14:45
 */
public interface OnChannelListener {
    void onItemMove(int startPos, int endPos);
    void onMoveToMyChannel(int startPos, int endPos);
    void onMoveToOtherChannel(int startPos, int endPos);
}
