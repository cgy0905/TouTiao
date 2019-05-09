package com.cgy.news.module.news.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.cgy.news.model.entity.Channel;
import com.cgy.news.module.news.NewsListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cgy
 * @description
 * @date 2019/5/9 11:05
 */
public class ChannelPagerAdapter extends FragmentStatePagerAdapter {

    private List<NewsListFragment> mFragments;
    private List<Channel> mChannels;
    public ChannelPagerAdapter(List<NewsListFragment> fragmentList, List<Channel> channelList, FragmentManager fm) {
        super(fm);
        mFragments = fragmentList != null ? fragmentList : new ArrayList<>();
        mChannels = channelList != null ? channelList : new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mChannels.get(position).title;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
