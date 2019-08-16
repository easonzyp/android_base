package com.develop.wiseisland.android.model;

import com.develop.wiseisland.android.base.BaseBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zyp on 2018/2/7.
 */
public class ModelGift extends BaseBean {

    private String name ;
    private String desc;

    public ModelGift(JSONObject data){
        try {
            this.name = data.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            this.desc = data.getString("brief");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}