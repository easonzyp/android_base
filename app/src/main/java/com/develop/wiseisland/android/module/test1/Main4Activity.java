package com.develop.wiseisland.android.module.test1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.develop.wiseisland.android.R;
import com.develop.wiseisland.android.base.BaseActivity;
import com.develop.wiseisland.android.config.ImageCutConfig;
import com.develop.wiseisland.android.ui.WarpLinearLayout;
import com.develop.wiseisland.android.utils.PicassoUtils;
import com.develop.wiseisland.android.utils.SelectPicUtils;

import java.io.File;
import java.util.List;

public class Main4Activity extends BaseActivity {

    Button btn_camera;
    Button btn_photo_single;
    Button btn_photo_multi;
    WarpLinearLayout wll_image_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initClick();
    }

    public void initView() {
        btn_camera = findView(R.id.btn_camera);
        btn_photo_single = findView(R.id.btn_photo_single);
        btn_photo_multi = findView(R.id.btn_photo_multi);
        wll_image_view = findView(R.id.wll_image_view);
    }

    public void initClick() {
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectPicUtils.getByCamera(Main4Activity.this, ImageCutConfig.IMAGE_RATIO_NO, 0);
            }
        });

        btn_photo_single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectPicUtils.getByAlbumSingle(Main4Activity.this, false);
            }
        });

        btn_photo_multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectPicUtils.getByAlbumMulti(Main4Activity.this, false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        List<String> list = SelectPicUtils.getInstance().onActivityResult(this, requestCode, resultCode, data);
        if (list != null && list.size() > 0) {
            showImage(list);
        }
    }

    public void showImage(List<String> list) {
        wll_image_view.removeAllViews();
        int imageWidth = wll_image_view.getWidth() / 4 - 10;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.width = imageWidth;
        layoutParams.height = imageWidth;
        wll_image_view.setVertical_Space(10);
        wll_image_view.setHorizontal_Space(10);
        for (int i = 0; i < list.size(); i++) {
            String path = list.get(i);
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(layoutParams);
            File file = new File(path);
            PicassoUtils.loadRoundImage(file, imageView, 10);
            wll_image_view.addView(imageView);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main4;
    }
}
