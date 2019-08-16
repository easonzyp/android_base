package com.develop.wiseisland.android.base;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.develop.wiseisland.android.R;
import com.develop.wiseisland.android.net.RequestResponseListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * ListView适配器基类
 * <p/>
 * Created by zyp on 2018/1/7
 */
public abstract class BaseLVAdapter extends BaseAdapter {
    protected List<BaseBean> list;
    protected BaseActivity context;
    protected BaseFragment fragment;
    protected LayoutInflater inflater;

    public static final int LIST_FIRST_POSITION = 0;

    // 刷新操作动作
    public static final int REFRESH_SUCCESS = 0;    // 更新成功
    public static final int REFRESH_ERROR = -1;     // 更新失败
    public static final int REFRESH_HEADER = 1;     // 头部刷新
    public static final int REFRESH_FOOTER = 2;     // 脚部刷新

    public static final int PAGE_COUNT = 10;        // 每次refresh更新的数目
    protected int refreshState = REFRESH_HEADER;    //当前刷新状态

    // 列表状态
    public static final int STATUS_NO_NEW_DATA = 51;    //没有最新
    public static final int STATUS_NET_ERROR = 52;      //网络错误
    public static final int STATUS_NO_MORE_DATA = 53;   //没有更多
    public static final int STATUS_LOAD_MORE = 54;      //加载更多
    public static final int STATUS_LESS_ONE_PAGE = 55;  //数据不满一页
    public static final int STATE_IDLE = 56;
    public static final int STATE_LOADING = 57;     //正在加载
    protected int adapterState = STATE_IDLE;        //当前列表状态
    public RequestResponseListener listener;

    public BaseLVAdapter(BaseActivity context, List<BaseBean> list) {
        if (list == null) {
            this.list = new ArrayList<>();
        } else {
            this.list = list;
        }
        this.context = context;
        this.inflater = LayoutInflater.from(context);

        initHttpResponseListener();
    }

    public BaseLVAdapter(BaseFragment fragment, List<BaseBean> list) {
        if (list == null)
            this.list = new ArrayList<>();
        else
            this.list = list;
        this.fragment = fragment;
        this.context = (BaseActivity) fragment.getActivity();
        this.inflater = LayoutInflater.from(context);

        initHttpResponseListener();
    }

    @Override
    public int getCount() {
        return this.list == null ? 0 : this.list.size();
    }

    @Override
    public BaseBean getItem(int position) {
        return this.list.get(position);
    }

    /**
     * 删除Item
     */
    public void deleteItem(int position) {

        if (list.size() > 0)
            this.list.remove(position);
        this.notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Activity getContext() {
        return this.context;
    }

    public BaseBean getFirst() {
        return (list == null) || list.size() == 0 ? null : this.list.get(LIST_FIRST_POSITION);
    }

    public BaseBean getLast() {
        if (list != null && list.size() > 0) {
            return this.list.get(this.list.size() - 1);
        } else
            return null;
    }

    public int getMaxId() {
        return list == null ? 0 : list.size();
    }

    final Handler resultHandler = new Handler() {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == REFRESH_SUCCESS) {
                switch (msg.arg1) {
                    case REFRESH_SUCCESS:
                        // 刷新成功
                        addHeader((List<BaseBean>) msg.obj);
                        break;
                    case REFRESH_ERROR:
                        // 刷新失败
                        if (msg.obj != null) {
                            //打印错误信息
                            Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                        }
                        finishRefresh();
                        break;
                    case REFRESH_HEADER:
                        // 下拉刷新
                        addHeader((List<BaseBean>) msg.obj);
                        break;
                    case REFRESH_FOOTER:
                        // 上拉加载
                        addFooter((List<BaseBean>) msg.obj);
                        break;
                }
            } else {
                if (msg.obj != null) {
                    //打印错误信息
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    protected void initHttpResponseListener() {
        // 重写回调方法，当请求服务后可以通知主UI刷新页面
        listener = new RequestResponseListener() {

            @Override
            public void onSuccess(Object result) {
                Message mainMsg = new Message();
                mainMsg.what = REFRESH_SUCCESS;
                mainMsg.obj = result;
                mainMsg.arg1 = refreshState;
                resultHandler.sendMessage(mainMsg);
            }

            @Override
            public void onFailure(Object errorResult) {
                Message mainMsg = Message.obtain();
                mainMsg.what = REFRESH_ERROR;
                mainMsg.obj = "连接超时，请稍后重试";
                resultHandler.sendMessage(mainMsg);
            }

        };
    }

    public void addHeader(List<BaseBean> list) {
        finishRefresh();
        if (list == null) {
            list = new ArrayList<>();
        }

        if (list.size() > 0) {
            showEmptyView(false);
            //过滤重复信息
            if (list.get(0).isOverrideEquals()) {
                for (int i = 0; i < list.size(); i++) {
                    BaseBean baseBean = list.get(i);
                    int index = this.list.indexOf(baseBean);
                    if (index != -1) {
                        this.list.set(index, baseBean);
                        list.remove(i);
                        i--;
                    }
                }
            } else {
                this.list.clear();
            }

            this.list.addAll(0, list);
            this.notifyDataSetChanged();

        } else {
            adapterState = STATUS_NO_NEW_DATA;
            showEmptyView(true);
        }
    }

    /**
     * 底部追加信息
     */
    public void addFooter(List<BaseBean> list) {
        if (list == null) {
            list = new ArrayList<>();
        }

        if (list.size() == 0) {
            finishNoMoreData();
            //没有更多数据
            adapterState = STATUS_NO_MORE_DATA;
            if (getFirst() == null) {
                //如果是第一页刷新,展示默认的空布局样式
                showEmptyView(true);
            }
        } else {
            finishLoadMore();
            //还有更多数据

            if (list.size() < PAGE_COUNT) {
                adapterState = STATUS_LESS_ONE_PAGE;
            } else {
                adapterState = STATUS_LOAD_MORE;
            }

            //对返回的内容去重
            for (int i = 0; i < list.size(); i++) {
                if (this.list.contains(list.get(i))) {
                    list.remove(i);
                    i--;
                }
            }
            //底部追加内容
            this.list.addAll(list);
        }
        // 数据修改好之后更新列表
        this.notifyDataSetChanged();

    }


    /**
     * List列表头部刷新调用的接口
     */
    public void refreshHeader() throws Exception {
    }

    /**
     * List列表加载更多调用的接口
     */
    public void refreshFooter() throws Exception {
    }

    public void doRefreshHeader() {
        // 首先判断网络状态
        if (!NetworkUtils.isConnected()) {
            Toast.makeText(context, context.getResources().getText(R.string.net_fail), Toast.LENGTH_SHORT).show();
            //隐藏刷新动画
            finishRefresh();
            adapterState = STATUS_NET_ERROR;
            //加载无网络视图

            return;
        }

        if (listener == null) {
            initHttpResponseListener();
        }

        refreshState = REFRESH_HEADER;
        try {
            refreshHeader();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void doRefreshFooter() {
        // 首先判断网络状态
        if (!NetworkUtils.isConnected()) {
            Toast.makeText(context, context.getResources().getText(R.string.net_fail), Toast.LENGTH_SHORT).show();
            //隐藏刷新动画
            finishLoadMore();
            adapterState = STATUS_NET_ERROR;
            //加载无网络视图

            return;
        }
        if (this.list == null) {
            return;
        }
        try {
            refreshState = REFRESH_FOOTER;
            refreshFooter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示无数据的空视图
     */
    protected void showEmptyView(boolean isShow) {
        View emptyView = getEmptyView();
        SmartRefreshLayout refreshLayout = getRefreshLayout();
        if (refreshLayout == null) {
            return;
        }
        if (emptyView != null) {
            if (isShow) {
                refreshLayout.removeView(emptyView);
                refreshLayout.setRefreshContent(emptyView);
            } else {
                refreshLayout.removeView(emptyView);
            }
        }
    }

    /**
     * 隐藏下拉刷新动画
     */
    public void finishRefresh() {
        SmartRefreshLayout refreshLayout = getRefreshLayout();
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.resetNoMoreData();
        }
    }

    /**
     * 隐藏上拉加载动画
     */
    public void finishLoadMore() {
        SmartRefreshLayout refreshLayout = getRefreshLayout();
        if (refreshLayout != null) {
            refreshLayout.finishLoadMore();
        }
    }

    /**
     * 显示没有更多
     */
    public void finishNoMoreData() {
        SmartRefreshLayout refreshLayout = getRefreshLayout();
        if (refreshLayout != null) {
            refreshLayout.finishLoadMoreWithNoMoreData();
        }
    }

    /**
     * 获取刷新控件
     */
    public SmartRefreshLayout getRefreshLayout() {
        if (context != null) {
            return context.getSmartRefreshLayout();
        } else if (fragment != null) {
            return fragment.getSmartRefreshLayout();
        }
        return null;
    }

    /**
     * 获取默认视图
     */
    public View getEmptyView() {
        if (context != null) {
            return context.getEmptyView();
        } else if (fragment != null) {
            return fragment.getEmptyView();
        }
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder(context, parent, getLVItemViewLayoutID());
            convertView = viewHolder.getConvertView();
        } else viewHolder = (ViewHolder) convertView.getTag();
        BaseBean t = getItem(position);
        bindView(position, t, viewHolder);
        return convertView;
    }

    public class ViewHolder {
        // 用于存储listView item的容器
        private SparseArray<View> mItemViews = new SparseArray<>();
        private View mConvertView;

        public ViewHolder(Context context, ViewGroup parent, int layoutId) {
            this.mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            this.mConvertView.setTag(this);
            this.mItemViews = new SparseArray<>();
        }

        /**
         * 获取listView中item对应的view
         */
        public View getConvertView() {
            return this.mConvertView;
        }

        /**
         * 查找View
         */
        public <T extends View> T findView(int viewPosition) {
            View view = mItemViews.get(viewPosition);
            if (view == null) {
                view = mConvertView.findViewById(viewPosition);
                mItemViews.put(viewPosition, view);
            }
            return (T) view;
        }
    }

    /**
     * 获取ListView中的item布局
     */
    public abstract int getLVItemViewLayoutID();

    /**
     * 数据绑定具体tag
     */
    public abstract void bindView(int position, BaseBean data, ViewHolder viewHolder);
}
