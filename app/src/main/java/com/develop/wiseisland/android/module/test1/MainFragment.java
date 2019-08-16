package com.develop.wiseisland.android.module.test1;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.develop.wiseisland.android.R;
import com.develop.wiseisland.android.base.BaseFragment;

/**
 * Created by zyp on 2018/2/6.
 */
public class MainFragment extends BaseFragment {

    private Main6Activity main6Activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.main6Activity = (Main6Activity) context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }
}