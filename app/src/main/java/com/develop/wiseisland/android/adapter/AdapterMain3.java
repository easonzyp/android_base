package com.develop.wiseisland.android.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.develop.wiseisland.android.R;
import com.develop.wiseisland.android.api.ApiGift;
import com.develop.wiseisland.android.base.BaseActivity;
import com.develop.wiseisland.android.base.BaseBean;
import com.develop.wiseisland.android.base.BaseRVAdapter;
import com.develop.wiseisland.android.model.ModelGift;
import com.develop.wiseisland.android.net.RequestResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyp on 2018/2/10.
 */
public class AdapterMain3 extends BaseRVAdapter {

    public AdapterMain3(BaseActivity context, List<BaseBean> list) {
        super(context, list);
    }

    @Override
    public int getRVItemViewLayoutID() {
        return R.layout.item_gift_list;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(inflater.inflate(R.layout.item_gift_list, parent, false));
    }

    @Override
    protected void bindData(final BaseViewHolder holder, BaseBean data, final int position) {
        ModelGift gift = (ModelGift) data;
        TextView tv_name = holder.findView(R.id.tv_name);
        tv_name.setText(gift.getName() + "========" + position);

        tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickListener(holder.getRootView(), position);
                }
            }
        });

    }

    @Override
    public void refreshHeader() throws Exception {
        ApiGift.getGiftList(0, 10, giftListListener);
    }

    @Override
    public void refreshFooter() throws Exception {
        ApiGift.getGiftList(getMaxId(), 10, giftListListener);
    }

    protected RequestResponseListener giftListListener = new RequestResponseListener() {
        @Override
        public void onSuccess(Object result) {
            JSONObject response = (JSONObject) result;
            List<BaseBean> list = new ArrayList<>();
            try {
                JSONArray jsonArray = response.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject data = jsonArray.getJSONObject(i);
                    ModelGift gift = new ModelGift(data);
                    list.add(gift);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            listener.onSuccess(list);
        }

        @Override
        public void onFailure(Object errorResult) {
            ToastUtils.showShort("数据解析失败");
        }
    };
}