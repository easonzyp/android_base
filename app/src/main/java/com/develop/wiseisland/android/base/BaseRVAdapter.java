package com.develop.wiseisland.android.base;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.develop.wiseisland.android.R;
import com.develop.wiseisland.android.net.RequestResponseListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView适配器基类
 * <p/>
 * Created by zyp on 2018/2/10.
 */
public abstract class BaseRVAdapter extends RecyclerView.Adapter<BaseRVAdapter.BaseViewHolder> {
    private BaseActivity context;
    private BaseFragment fragment;
    public LayoutInflater inflater;
    private List<BaseBean> list;
    protected OnItemClickListener onItemClickListener;//单击事件
    protected OnItemLongClickListener onItemLongClickListener;//长按单击事件
    private boolean clickFlag = true;//单击事件和长单击事件的屏蔽标识

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


    public BaseRVAdapter(BaseActivity context, List<BaseBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);

        initHttpResponseListener();
    }

    public BaseRVAdapter(BaseFragment fragment, List<BaseBean> list) {
        this.fragment = fragment;
        this.list = list;
        this.inflater = LayoutInflater.from(context);

        initHttpResponseListener();
    }

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
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new BaseViewHolder(inflater.inflate(getRVItemViewLayoutID(), parent, false));
    }

    public abstract int getRVItemViewLayoutID();

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        bindData(holder, list.get(position), position);
    }

    public BaseBean getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public BaseBean getFirst() {
        return (list == null) || list.size() == 0 ? null : this.list.get(LIST_FIRST_POSITION);
    }

    public int getMaxId() {
        return (list == null) || list.size() == 0 ? 0 : this.list.size();
    }

    protected abstract void bindData(BaseViewHolder holder, BaseBean data, int position);

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener {
        void onItemClickListener(View v, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClickListener(View v, int position);
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        private SparseArray<View> views;

        public BaseViewHolder(View view) {
            super(view);
            this.views = new SparseArray<>();
        }

        public <T extends View> T findView(int viewId) {
            View view = views.get(viewId);
            if (view == null) {
                view = itemView.findViewById(viewId);
                views.put(viewId, view);
            }
            return (T) view;
        }

        public View getRootView() {
            return itemView;
        }
    }
}


