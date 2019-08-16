package com.develop.wiseisland.android.adapter;

import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.develop.wiseisland.android.R;
import com.develop.wiseisland.android.api.ApiGift;
import com.develop.wiseisland.android.base.BaseActivity;
import com.develop.wiseisland.android.base.BaseBean;
import com.develop.wiseisland.android.base.BaseLVAdapter;
import com.develop.wiseisland.android.model.ModelGift;
import com.develop.wiseisland.android.net.RequestResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyp on 2018/2/7.
 */
public class AdapterGiftList extends BaseLVAdapter {

    public AdapterGiftList(BaseActivity context, List<BaseBean> list) {
        super(context, list);
    }

    @Override
    public int getLVItemViewLayoutID() {
        return R.layout.item_gift_list;
    }

    @Override
    public void bindView(int position, BaseBean data, BaseLVAdapter.ViewHolder viewHolder) {
        ModelGift gift = (ModelGift) data;
        TextView tv_name = viewHolder.findView(R.id.tv_name);
        tv_name.setText(gift.getName() + "------" + position);
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
            List<ModelGift> list = new ArrayList<>();
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
            ToastUtils.showShort("数据解析失败,请重试");
        }
    };
}