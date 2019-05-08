package com.cgy.news.app;


import com.cgy.news.BuildConfig;
import com.cgy.news.app.base.BaseApp;
import com.github.anzewei.parallaxbacklayout.ParallaxHelper;
import com.socks.library.KLog;

import org.litepal.LitePalApplication;

/**
 * @author cgy
 * @description: Application类
 * @date 2019/5/8  15:44
 */

public class MyApp extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();

        //**************************************相关第三方SDK的初始化等操作*************************************************
        KLog.init(BuildConfig.DEBUG);//初始化KLog
        LitePalApplication.initialize(getApplicationContext());//初始化litePal

        registerActivityLifecycleCallbacks(ParallaxHelper.getInstance());
    }
}
