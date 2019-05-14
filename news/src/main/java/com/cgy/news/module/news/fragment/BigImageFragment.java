package com.cgy.news.module.news.fragment;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.cgy.news.R;
import com.cgy.news.base.BaseFragment;
import com.cgy.news.base.BasePresenter;
import com.cgy.news.utils.UIUtils;
import com.github.chrisbanes.photoview.PhotoView;
import com.sunfusheng.glideimageview.GlideImageLoader;
import com.sunfusheng.glideimageview.progress.CircleProgressView;
import com.sunfusheng.glideimageview.util.DisplayUtil;

import butterknife.BindView;

/**
 * @author cgy
 * @description
 * @date 2019/5/14 10:42
 */
public class BigImageFragment extends BaseFragment {

    public static final String IMG_URL = "imgUrl";
    @BindView(R.id.photo_view)
    PhotoView mPhotoView;
    @BindView(R.id.progress_view)
    CircleProgressView mProgressView;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_big_image;
    }

    @Override
    public void initListener() {
        mPhotoView.setOnPhotoTapListener((view, x, y) -> mActivity.finish());
    }

    @Override
    protected void loadData() {
        String imgUrl = getArguments().getString(IMG_URL);

        GlideImageLoader imageLoader = new GlideImageLoader(mPhotoView);

        imageLoader.setOnGlideImageViewListener(imgUrl, (percent, isDone, exception) -> {
            if (exception != null && !TextUtils.isEmpty(exception.getMessage())) {
                UIUtils.showToast(getString(R.string.net_error));
            }
            mProgressView.setProgress(percent);
            mProgressView.setVisibility(isDone ? View.GONE : View.VISIBLE);
        });

        RequestOptions options = imageLoader.requestOptions(R.color.placeholder_color)
                .centerCrop()
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        RequestBuilder<Drawable> requestBuilder = imageLoader.requestBuilder(imgUrl, options);
        requestBuilder.transition(DrawableTransitionOptions.withCrossFade())
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        if (resource.getIntrinsicHeight() > DisplayUtil.getScreenHeight(mActivity)) {
                            mPhotoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
                        requestBuilder.into(mPhotoView);
                    }
                });
    }

}
