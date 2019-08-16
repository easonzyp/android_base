package com.develop.wiseisland.android.net;

import android.content.Context;
import android.util.Log;

import com.develop.wiseisland.android.R;
import com.develop.wiseisland.android.base.BaseApplication;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * http请求工具类
 * <p>
 * Created by zyp on 2018/2/2.
 */
public class OkHttpClient {

    //更换接口只需要改变这部分
    private static String API_URL = "";
    private static String HOST = "";

    private static String MOD_ACT = "?mod=%s&act=%s";
    public static String TOKEN;
    public static String TOKEN_SECRET;

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_DELETE = "DELETE";

    /**
     * get请求
     */
    public static void okHttpGet(String url, CallBackListener callBack) {
        okHttpGet(url, null, null, callBack);
    }

    /**
     * get请求，可以传递参数
     */
    public static void okHttpGet(String url, Map<String, String> paramsMap, CallBackListener callBack) {
        okHttpGet(url, paramsMap, null, callBack);
    }

    /**
     * get请求，可以传递参数
     */
    public static void okHttpGet(String[] mod_act, Map<String, String> paramsMap, CallBackListener callBack) {
        okHttpGet(getAbsoluteApiUrl(mod_act[0], mod_act[1]), paramsMap, null, callBack);
    }

    /**
     * get请求，可以传递参数
     */
    public static void okHttpGet(String url, Map<String, String> paramsMap, Map<String, String> headerMap, CallBackListener callBack) {
        Log.d("OkHttpClient GET ", url + "&" + paramsMap);
        new RequestUtil(METHOD_GET, url, paramsMap, headerMap, callBack).execute();
    }

    /**
     * post请求
     */
    public static void okHttpPost(String url, CallBackListener callBack) {
        okHttpPost(url, null, callBack);
    }

    /**
     * post请求，可以传递参数
     */
    public static void okHttpPost(String[] mod_act, Map<String, String> paramsMap, CallBackListener callBack) {
        okHttpPost(getAbsoluteApiUrl(mod_act[0], mod_act[1]), paramsMap, null, callBack);
    }

    /**
     * post请求，可以传递参数
     */
    public static void okHttpPost(String url, Map<String, String> paramsMap, CallBackListener callBack) {
        okHttpPost(url, paramsMap, null, callBack);
    }

    /**
     * post请求，可以传递参数
     */
    public static void okHttpPost(String url, Map<String, String> paramsMap, Map<String, String> headerMap, CallBackListener callBack) {
        Log.d("OkHttpClient POST ", url + "&" + paramsMap);
        new RequestUtil(METHOD_POST, url, paramsMap, headerMap, callBack).execute();
    }

    /**
     * post请求
     */
    public static void okHttpPut(String url, CallBackListener callBack) {
        okHttpPut(url, null, callBack);
    }

    /**
     * post请求，可以传递参数
     */
    public static void okHttpPut(String url, Map<String, String> paramsMap, CallBackListener callBack) {
        okHttpPut(url, paramsMap, null, callBack);
    }

    /**
     * post请求，可以传递参数
     */
    public static void okHttpPut(String url, Map<String, String> paramsMap, Map<String, String> headerMap, CallBackListener callBack) {
        new RequestUtil(METHOD_PUT, url, paramsMap, headerMap, callBack).execute();
    }

    /**
     * post请求，可以传递参数
     */
    public static void okHttpPostJson(String url, String jsonStr, CallBackListener callBack) {
        okHttpPostJson(url, jsonStr, null, callBack);
    }

    /**
     * post请求，可以传递参数
     */
    public static void okHttpPostJson(String url, String jsonStr, Map<String, String> headerMap, CallBackListener callBack) {
        new RequestUtil(METHOD_POST, url, jsonStr, headerMap, callBack).execute();
    }

    /**
     * post请求，上传单个文件
     *
     * @param url：url
     * @param file：File对象
     * @param fileKey：上传参数时file对应的键
     * @param fileType：File类型，是image，video，audio，file
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，这两个方法都执行在UI线程。还可以重写onProgress方法，得到上传进度
     */
    public static void okHttpUploadFile(String url, File file, String fileKey, String fileType, CallBackListener callBack) {
        okHttpUploadFile(url, file, fileKey, fileType, null, callBack);
    }

    /**
     * post请求，上传单个文件
     *
     * @param url：url
     * @param file：File对象
     * @param fileKey：上传参数时file对应的键
     * @param fileType：File类型，是image，video，audio，file
     * @param paramsMap：map集合，封装键值对参数
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，这两个方法都执行在UI线程。还可以重写onProgress方法，得到上传进度
     */
    public static void okHttpUploadFile(String url, File file, String fileKey, String fileType, Map<String, String> paramsMap, CallBackListener callBack) {
        okHttpUploadFile(url, file, fileKey, fileType, paramsMap, null, callBack);
    }

    /**
     * post请求，上传单个文件
     *
     * @param url：url
     * @param file：File对象
     * @param fileKey：上传参数时file对应的键
     * @param fileType：File类型，是image，video，audio，file
     * @param paramsMap：map集合，封装键值对参数
     * @param headerMap：map集合，封装请求头键值对
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，这两个方法都执行在UI线程。还可以重写onProgress方法，得到上传进度
     */
    public static void okHttpUploadFile(String url, File file, String fileKey, String fileType, Map<String, String> paramsMap, Map<String, String> headerMap, CallBackListener callBack) {
        new RequestUtil(METHOD_POST, url, paramsMap, file, fileKey, fileType, headerMap, callBack).execute();
    }

    /**
     * post请求，上传多个文件，以list集合的形式
     *
     * @param url：url
     * @param fileList：集合元素是File对象
     * @param fileKey：上传参数时fileList对应的键
     * @param fileType：File类型，是image，video，audio，file
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，这两个方法都执行在UI线程。
     */
    public static void okHttpUploadListFile(String url, List<File> fileList, String fileKey, String fileType, CallBackListener callBack) {
        okHttpUploadListFile(url, null, fileList, fileKey, fileType, callBack);
    }

    /**
     * post请求，上传多个文件，以list集合的形式
     *
     * @param url：url
     * @param fileList：集合元素是File对象
     * @param fileKey：上传参数时fileList对应的键
     * @param fileType：File类型，是image，video，audio，file
     * @param paramsMap：map集合，封装键值对参数
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，这两个方法都执行在UI线程。
     */
    public static void okHttpUploadListFile(String url, Map<String, String> paramsMap, List<File> fileList, String fileKey, String fileType, CallBackListener callBack) {
        okHttpUploadListFile(url, paramsMap, fileList, fileKey, fileType, null, callBack);
    }

    /**
     * post请求，上传多个文件，以list集合的形式
     *
     * @param url：url
     * @param fileList：集合元素是File对象
     * @param fileKey：上传参数时fileList对应的键
     * @param fileType：File类型，是image，video，audio，file
     * @param paramsMap：map集合，封装键值对参数
     * @param headerMap：map集合，封装请求头键值对
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，这两个方法都执行在UI线程。
     */
    public static void okHttpUploadListFile(String url, Map<String, String> paramsMap, List<File> fileList, String fileKey, String fileType, Map<String, String> headerMap, CallBackListener callBack) {
        new RequestUtil(METHOD_POST, url, paramsMap, fileList, fileKey, fileType, headerMap, callBack).execute();
    }

    /**
     * post请求，上传多个文件，以map集合的形式
     *
     * @param url：url
     * @param fileMap：集合key是File对象对应的键，集合value是File对象
     * @param fileType：File类型，是image，video，audio，file
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，这两个方法都执行在UI线程。
     */
    public static void okHttpUploadMapFile(String url, Map<String, File> fileMap, String fileType, CallBackListener callBack) {
        okHttpUploadMapFile(url, fileMap, fileType, null, callBack);
    }

    /**
     * post请求，上传多个文件，以map集合的形式
     *
     * @param url：url
     * @param fileMap：集合key是File对象对应的键，集合value是File对象
     * @param fileType：File类型，是image，video，audio，file
     * @param paramsMap：map集合，封装键值对参数
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，这两个方法都执行在UI线程。
     */
    public static void okHttpUploadMapFile(String url, Map<String, File> fileMap, String fileType, Map<String, String> paramsMap, CallBackListener callBack) {
        okHttpUploadMapFile(url, fileMap, fileType, paramsMap, null, callBack);
    }

    /**
     * post请求，上传多个文件，以map集合的形式
     *
     * @param url：url
     * @param fileMap：集合key是File对象对应的键，集合value是File对象
     * @param fileType：File类型，是image，video，audio，file
     * @param paramsMap：map集合，封装键值对参数
     * @param headerMap：map集合，封装请求头键值对
     * @param callBack：回调接口，onFailure方法在请求失败时调用，onResponse方法在请求成功后调用，这两个方法都执行在UI线程。
     */
    public static void okHttpUploadMapFile(String url, Map<String, File> fileMap, String fileType, Map<String, String> paramsMap, Map<String, String> headerMap, CallBackListener callBack) {
        new RequestUtil(METHOD_POST, url, paramsMap, fileMap, fileType, headerMap, callBack).execute();
    }

    /**
     * 下载文件,不带参数
     */
    public static void okHttpDownloadFile(String url, CallBackListener.CallBackFile callBack) {
        okHttpDownloadFile(url, null, callBack);
    }

    /**
     * 下载文件,带参数
     */
    public static void okHttpDownloadFile(String url, Map<String, String> paramsMap, CallBackListener.CallBackFile callBack) {
        okHttpGet(url, paramsMap, null, callBack);
    }

    /**
     * 加载图片
     */
    public static void okHttpGetBitmap(String url, CallBackListener.CallBackBitmap callBack) {
        okHttpGetBitmap(url, null, callBack);
    }

    /**
     * 加载图片，带参数
     */
    public static void okHttpGetBitmap(String url, Map<String, String> paramsMap, CallBackListener.CallBackBitmap callBack) {
        okHttpGet(url, paramsMap, null, callBack);
    }

    public static String getAbsoluteApiUrl(String mode, String act) {
        Context context = BaseApplication.getContext();
        HOST = context.getResources().getString(R.string.sys_host);
        API_URL = context.getResources().getString(R.string.sys_api_full_url);
        String mod_act = String.format(MOD_ACT, mode, act);
        String url = API_URL + mod_act;
        // 拼接版本信息
        url += "&app_version=1.0.0";
        url += "&app_type=1";
        //拼接用户token认证信息
//        url += "&oauth_token=" + TOKEN;
//        url += "&oauth_token_secret=" + TOKEN_SECRET;
        url += "&oauth_token=7f0af7cdc17beec971c6fe0012220d44";
        url += "&oauth_token_secret=461a4b8aa70ac88b2e2afdd2199116a0";
//        String r = StringRandom.getStringRandom(10); //10位大小写英文字母数字组合
//        String t = System.currentTimeMillis() / 1000 + ""; //当前时间的时间戳
//        String k = "nlp20170531@hi135faf5z21a65!";
//        String sign = StringToMD5.getMD5Str(t + "&" + r + "&" + k);
//        url += "&r=" + r;
//        url += "&t=" + t;
//        url += "&sign=" + sign;
        return url;
    }
}