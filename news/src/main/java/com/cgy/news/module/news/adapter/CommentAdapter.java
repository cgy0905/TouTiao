package com.cgy.news.module.news.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.cgy.news.R;
import com.cgy.news.model.entity.CommentData;
import com.cgy.news.utils.GlideUtils;
import com.cgy.news.utils.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author cgy
 * @description 新闻详情页评论的适配器
 * @date 2019/5/13 10:55
 */
public class CommentAdapter extends BaseQuickAdapter<CommentData, BaseViewHolder> {

    private Context mContext;

    public CommentAdapter(Context context, int layoutResId, @Nullable List<CommentData> data) {
        super(layoutResId, data);
        mContext = context;
    }


    @Override
    protected void convert(BaseViewHolder helper, CommentData commentData) {
        GlideUtils.loadRound(mContext, commentData.comment.user_profile_image_url, helper.getView(R.id.iv_avatar));
        helper.setText(R.id.tv_name, commentData.comment.user_name)
                .setText(R.id.tv_like_count, String.valueOf(commentData.comment.digg_count))
                .setText(R.id.tv_content, commentData.comment.text)
                .setText(R.id.tv_time, TimeUtils.getShortTime(commentData.comment.create_time * 1000));
    }
}
