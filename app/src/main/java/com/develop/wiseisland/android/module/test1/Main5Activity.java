package com.develop.wiseisland.android.module.test1;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.develop.wiseisland.android.R;
import com.develop.wiseisland.android.adapter.AdapterMain3;
import com.develop.wiseisland.android.base.BaseActivity;
import com.develop.wiseisland.android.base.BaseBean;
import com.develop.wiseisland.android.utils.BannerImageLoader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;

public class Main5Activity extends BaseActivity {

    private SmartRefreshLayout refreshLayout;
    private Banner banner;
    private RecyclerView rc_list;
    private AdapterMain3 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initClick();
        initBanner();
    }

    public void initData() {
        adapter.doRefreshHeader();
    }

    public void initClick() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                adapter.doRefreshHeader();
            }
        });

        refreshLayout.setOnLoadmoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                adapter.doRefreshFooter();
            }
        });
    }

    public void initView() {
        banner = findView(R.id.banner);
        refreshLayout = findView(R.id.refreshLayout);
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setEnableAutoLoadmore(false);
        rc_list = findView(R.id.rc_list);
        adapter = new AdapterMain3(Main5Activity.this, new ArrayList<BaseBean>());
        rc_list.setLayoutManager(new LinearLayoutManager(Main5Activity.this));
        rc_list.setAdapter(adapter);
    }

    public void initBanner() {
        //放图片地址的集合
        ArrayList<String> list_path = new ArrayList<>();
        //放标题的集合
        ArrayList<String> list_title = new ArrayList<>();

        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg");
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2e7vsaj30ci08cglz.jpg");

        list_title.add("好好学习");
        list_title.add("天天向上");
        list_title.add("热爱劳动");
        list_title.add("不搞对象");

        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                .setImageLoader(new BannerImageLoader())
                .setImages(list_path)
                .setBannerTitles(list_title)
                .setDelayTime(3000)
                .isAutoPlay(true)
                .setBannerAnimation(Transformer.Default)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main5;
    }

    @Override
    public SmartRefreshLayout getSmartRefreshLayout() {
        return refreshLayout;
    }
}
