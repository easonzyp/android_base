package com.develop.wiseisland.android.base;

import java.io.Serializable;

/**
 * 对象模型基类
 *
 * Created by zyp on 2018/1/7
 */
public abstract class BaseBean implements Serializable,
        Comparable<BaseBean> {
    private static final long serialVersionUID = 1L;
    protected static final String NULL = "";

    public BaseBean() {

    }

    @Override
    public int compareTo(BaseBean another) {
        return 0;
    }

    public boolean isOverrideEquals() {
        return false;
    }

}
