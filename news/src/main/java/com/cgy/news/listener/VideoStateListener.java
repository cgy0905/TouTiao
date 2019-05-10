package com.cgy.news.listener;

/**
 * @author cgy
 * @description
 * @date 2019/5/9 18:41
 */
public interface VideoStateListener {

    void onStateNormal();

    void onPreparing();

    void onStartClick();

    void onStart();

    void onPlaying();

    void onPause();

    void onProgressChanged(int progress);

    void onComplete();

    void onTouch();

    void onStartDismissControlViewTimer();
}
