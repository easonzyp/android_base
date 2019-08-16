package com.develop.wiseisland.android.utils;

import android.content.Context;
import android.widget.ImageView;

import com.youth.banner.loader.ImageLoader;

/**
 * Created by zyp on 2018/3/5.
 */
public class BannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        PicassoUtils.loadNormalImage((String) path, imageView);
    }
}