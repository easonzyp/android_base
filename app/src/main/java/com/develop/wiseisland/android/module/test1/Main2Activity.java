package com.develop.wiseisland.android.module.test1;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.develop.wiseisland.android.R;
import com.develop.wiseisland.android.adapter.AdapterGiftList;
import com.develop.wiseisland.android.base.BaseActivity;
import com.develop.wiseisland.android.base.BaseBean;
import com.develop.wiseisland.android.base.BaseLVAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

public class Main2Activity extends BaseActivity{

    private SmartRefreshLayout refreshLayout;
    private ListView lv_list;
    private BaseLVAdapter adapter;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initData() {
        adapter.doRefreshHeader();
    }

    public void initView() {
        emptyView = inflater.inflate(R.layout.layout_list_empty, null);
        refreshLayout = findView(R.id.refreshLayout);
        lv_list = findView(R.id.lv_list);
        refreshLayout.setEnableAutoLoadmore(false);
        adapter = new AdapterGiftList(this, new ArrayList<BaseBean>());
        lv_list.setAdapter(adapter);
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

    @Override
    public SmartRefreshLayout getSmartRefreshLayout() {
        return refreshLayout;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main2;
    }

    @Override
    public View getEmptyView() {
        return emptyView;
    }
}
