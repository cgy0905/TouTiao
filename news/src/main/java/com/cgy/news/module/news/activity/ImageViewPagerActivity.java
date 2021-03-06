package com.cgy.news.module.news.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.cgy.news.R;
import com.cgy.news.base.BaseActivity;
import com.cgy.news.base.BasePresenter;
import com.cgy.news.listener.PermissionListener;
import com.cgy.news.module.news.fragment.BigImageFragment;
import com.cgy.news.utils.FileUtils;
import com.cgy.news.utils.UIUtils;
import com.chaychan.uikit.statusbar.Eyes;
import com.socks.library.KLog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ImageViewPagerActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = ImageViewPagerActivity.class.getSimpleName();
    public static final String IMG_URLS = "mImageUrls";
    public static final String POSITION = "position";


    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.tv_indicator)
    TextView mTvIndicator;
    @BindView(R.id.tv_save)
    TextView mTvSave;

    private List<String> mImageUrls = new ArrayList<>();
    private List<BigImageFragment> mFragments = new ArrayList<>();
    private int mCurrentPosition;
    private Map<Integer, Boolean> mDownloadingFlagMap = new HashMap<>();//用户保存对应位置图片是否在下载的标识


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_image_view_pager;
    }

    @Override
    public void initView() {
        Eyes.translucentStatusBar(this);
    }

    @Override
    public void initData() {
        mImageUrls = getIntent().getStringArrayListExtra(IMG_URLS);
        int position = getIntent().getIntExtra(POSITION, 0);
        mCurrentPosition = position;

        for (int i = 0; i < mImageUrls.size(); i++) {
            String url = mImageUrls.get(i);
            BigImageFragment imageFragment = new BigImageFragment();

            Bundle bundle = new Bundle();
            bundle.putString(BigImageFragment.IMG_URL, url);
            imageFragment.setArguments(bundle);

            mFragments.add(imageFragment);//添加到fragment集合中
            mDownloadingFlagMap.put(i, false);//初始化map,一开始全部的值都为false
        }

        mViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(this);

        mViewPager.setCurrentItem(mCurrentPosition);//设置当前所在位置
        setIndicator(mCurrentPosition);//设置当前位置指示
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPosition = position;
        //页面变化时,设置当前的指示
        setIndicator(mCurrentPosition);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setIndicator(int position) {
        mTvIndicator.setText(position + 1 + "/" + mImageUrls.size());//设置当前的指示
    }

    @OnClick(R.id.tv_save)
    public void onViewClicked() {
        requestRuntimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
            @Override
            public void onGranted() {
                //保存图片
                downloadImg();
            }

            @Override
            public void onDenied(List<String> deniedPermissions) {
                UIUtils.showToast(getString(R.string.write_storage_permission_deny));
            }
        });
    }

    private void downloadImg() {
        String imgUrl = mImageUrls.get(mCurrentPosition);
        Boolean isDownloading = mDownloadingFlagMap.get(mCurrentPosition);
        if (!isDownloading) {
            //如果不是正在下载,则开始下载
            mDownloadingFlagMap.put(mCurrentPosition, true);//更新标识为下载中
            new DownloadImgTask(mCurrentPosition).execute(imgUrl);

        }
    }

    class DownloadImgTask extends AsyncTask<String, Integer, Void> {

        private int mPosition;

        public DownloadImgTask(int position) {
            mPosition = position;
        }

        @Override
        protected Void doInBackground(String... params) {
            String imgUrl = params[0];
            File file = null;
            try {
                FutureTarget<File> future = Glide
                        .with(ImageViewPagerActivity.this)
                        .load(imgUrl)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                file = future.get();

                String filePath = file.getAbsolutePath();

                String destFileName = System.currentTimeMillis() + FileUtils.getImageFileExt(filePath);
                File destFile = new File(FileUtils.getDir(""), destFileName);

                FileUtils.copy(file, destFile);//保存图片

                //最后通知图库更新
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(destFile.getPath()))));
            } catch (Exception e) {
                KLog.e(TAG, e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mDownloadingFlagMap.put(mPosition, false);//下载完成后,更改对应的flag
            UIUtils.showToast("保存成功，图片所在文件夹：SD卡根路径/TouTiao");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            KLog.i(TAG, "progress: " + values[0]);
        }
    }

    class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
