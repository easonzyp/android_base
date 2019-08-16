package com.develop.wiseisland.android.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * Fragment基类
 * <p>
 * Created by zyp on 2018/2/6.
 */
public abstract class BaseFragment extends Fragment {
    protected Activity mActivity;

    /**
     * 获得全局的，防止使用getActivity()为空
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initParams();
        View view = LayoutInflater.from(mActivity)
                .inflate(getLayoutId(), container, false);
        initView(view, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initClick();
    }

    /**
     * 接收页面传递的参数
     */
    protected void initParams() {
    }

    /**
     * onCreateView中需要的layoutID
     */
    protected abstract int getLayoutId();

    /**
     * 初始化view
     */
    protected abstract void initView(View view, Bundle savedInstanceState);

    /**
     * 加载数据
     */
    protected abstract void initData();

    /**
     * 点击事件
     */
    protected void initClick() {
    }

    public SmartRefreshLayout getSmartRefreshLayout() {
        return null;
    }

    public View getEmptyView() {
        return null;
    }
}