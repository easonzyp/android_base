package com.develop.wiseisland.android.module.test1;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.develop.wiseisland.android.R;
import com.develop.wiseisland.android.adapter.AdapterViewPager;
import com.develop.wiseisland.android.base.BaseActivity;

public class Main6Activity extends BaseActivity {
    private TabLayout tl_tab;
    private ViewPager vp_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initFragment();
    }

    private void initView() {
        tl_tab = findView(R.id.tl_tab);
        vp_list = findView(R.id.vp_list);
    }

    private void initFragment() {
        AdapterViewPager viewPager = new AdapterViewPager(getSupportFragmentManager());
        MainFragment fragment1 = new MainFragment();
        MainFragment fragment2 = new MainFragment();
        MainFragment fragment3 = new MainFragment();
        MainFragment fragment4 = new MainFragment();
        MainFragment fragment5 = new MainFragment();
        viewPager.addFragment(fragment1, "推荐");
        viewPager.addFragment(fragment2, "热点");
        viewPager.addFragment(fragment3, "体育");
        viewPager.addFragment(fragment4, "军事");
        viewPager.addFragment(fragment5, "NBA");


        vp_list.setAdapter(viewPager);
        tl_tab.setupWithViewPager(vp_list);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main6;
    }
}
