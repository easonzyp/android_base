package com.develop.wiseisland.android.net;

/**
 * Created by zyp on 2018/1/7 0012.
 */
public abstract class RequestResponseListener {
    //该方法用于非执行网络请求的回调
    public abstract void onSuccess(Object result);

    public abstract void onFailure(Object errorResult);
}
