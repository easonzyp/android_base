package com.develop.wiseisland.android.base;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.blankj.utilcode.util.Utils;
import com.develop.wiseisland.android.config.BaseConfig;

import java.io.File;

/**
 * Created by zyp on 2018/2/5.
 */
public class BaseApplication extends Application {
    public static String CACHE_PATH;     //缓存路劲
    public static String SAVE_IMG_PATH;     //图片保存路劲
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        BaseApplication.context = getApplicationContext();
        //初始化工具类
        Utils.init(this);
        initCachePath();
        initSaveImgPath();
    }

    public static Context getContext() {
        return context;
    }

    /**
     * 初始化缓存路径
     */
    private void initCachePath() {
        File sampleDir = new File(Environment.getExternalStorageDirectory() + File.separator +
                BaseConfig.CACHE_DIR_NAME);
        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }

        CACHE_PATH = sampleDir.getAbsolutePath();
    }

    /**
     * 初始化图片保存路径
     */
    private void initSaveImgPath() {
        SAVE_IMG_PATH = CACHE_PATH;
    }
}