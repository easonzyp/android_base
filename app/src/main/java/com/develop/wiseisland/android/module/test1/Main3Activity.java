package com.develop.wiseisland.android.module.test1;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.develop.wiseisland.android.R;
import com.develop.wiseisland.android.adapter.AdapterMain3;
import com.develop.wiseisland.android.base.BaseActivity;
import com.develop.wiseisland.android.base.BaseBean;
import com.develop.wiseisland.android.base.BaseRVAdapter;
import com.develop.wiseisland.android.model.ModelGift;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

public class Main3Activity extends BaseActivity {

    private SmartRefreshLayout refreshLayout;
    private RecyclerView rv_list;
    private BaseRVAdapter adapter;
    private View emptyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initView() {
        emptyView = inflater.inflate(R.layout.layout_list_empty, null);
        refreshLayout = findView(R.id.refreshLayout);
        rv_list = findView(R.id.rv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Main3Activity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new AdapterMain3(Main3Activity.this, new ArrayList<BaseBean>());
        rv_list.setLayoutManager(layoutManager);
        rv_list.setAdapter(adapter);
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
        adapter.setOnItemClickListener(new BaseRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                ModelGift gift = (ModelGift) adapter.getItem(position);
                Toast.makeText(Main3Activity.this, gift.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main3;
    }

    @Override
    public View getEmptyView() {
        return emptyView;
    }

    @Override
    public SmartRefreshLayout getSmartRefreshLayout() {
        return refreshLayout;
    }
}
