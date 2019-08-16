package com.develop.wiseisland.android.api;

import com.develop.wiseisland.android.net.CallBackListener;
import com.develop.wiseisland.android.net.OkHttpClient;
import com.develop.wiseisland.android.net.RequestResponseListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by zyp on 2018/2/5.
 */
public class ApiGift {
    private final static String MOD_NAME = "Gift";
    private final static String GET_GIFT_LIST = "get_list";
    private final static String GET_GIFT_INFO = "get_info";

    public static void getGiftList(int max_id, int count, final RequestResponseListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("count", "" + count);
        params.put("max_id", "" + max_id);
        OkHttpClient.okHttpPost(new String[]{MOD_NAME, GET_GIFT_LIST}, params, new CallBackListener.CallBackJsonObject() {
            @Override
            public void onFailure(Call call, Exception e) {

            }

            @Override
            public void onResponse(JSONObject response) {
                if (listener != null) {
                    listener.onSuccess(response);
                }
            }
        });
    }

    public static void getGiftInfo(String id, CallBackListener<JSONObject> listener) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("id", id);

        OkHttpClient.okHttpPost(new String[]{MOD_NAME, GET_GIFT_INFO}, paramsMap, listener);
    }
}