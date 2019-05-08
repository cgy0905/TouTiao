package com.cgy.news.model.event;

import com.chaychan.library.BottomBarItem;

/**
 * @author cgy
 * @description 下拉刷新事件
 * @date 2019/5/8 18:24
 */
public class TabRefreshEvent {

    /**
     * 频道
     */
    private String channelCode;
    private BottomBarItem bottomBarItem;
    private boolean isHomeTab;

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public BottomBarItem getBottomBarItem() {
        return bottomBarItem;
    }

    public void setBottomBarItem(BottomBarItem bottomBarItem) {
        this.bottomBarItem = bottomBarItem;
    }

    public boolean isHomeTab() {
        return isHomeTab;
    }

    public void setHomeTab(boolean homeTab) {
        isHomeTab = homeTab;
    }
}
