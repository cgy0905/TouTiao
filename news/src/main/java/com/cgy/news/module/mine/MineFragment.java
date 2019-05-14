package com.cgy.news.module.mine;

import android.view.View;

import com.cgy.news.R;
import com.cgy.news.base.BaseFragment;
import com.cgy.news.base.BasePresenter;
import com.socks.library.KLog;

/**
 * @author cgy
 * @description
 * @date 2019/5/8 18:07
 */
public class MineFragment extends BaseFragment {
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView(View rootView) {
        KLog.i("initView");
    }

    @Override
    public void initData() {
        KLog.i("initData");
    }

    @Override
    public void initListener() {
        KLog.i("initListener");
    }

    @Override
    public void loadData() {
        KLog.i("loadData");
    }
}
