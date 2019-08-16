package com.develop.wiseisland.android.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;

import com.develop.wiseisland.android.R;
import com.develop.wiseisland.android.base.BaseApplication;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;

/**
 * 图片加载工具类
 * <p>
 * Created by zyp on 2018/2/22.
 */
public class PicassoUtils {
    /**
     * 占位图
     */
    private static int placeHolderImage = R.drawable.empty_bg;
    /**
     * 加载失败图
     */
    private static int errorImage = R.drawable.empty_bg;

    /**
     * 加载正常图片   网络图片
     */
    public static void loadNormalImage(String path, ImageView imageView) {
        Context context = BaseApplication.getContext();
        Picasso.with(context).load(path).placeholder(placeHolderImage).error(errorImage).into(imageView);
    }

    /**
     * 加载正常图片   相册图片
     */
    public static void loadNormalImage(File file, ImageView imageView) {
        Context context = BaseApplication.getContext();
        Picasso.with(context).load(file).placeholder(placeHolderImage).error(errorImage).into(imageView);
    }

    /**
     * 加载正常图片   本地图片
     */
    public static void loadNormalImage(int localImage, ImageView imageView) {
        Context context = BaseApplication.getContext();
        Picasso.with(context).load(localImage).placeholder(placeHolderImage).error(errorImage).into(imageView);
    }

    /**
     * 加载圆角图片   网络图片
     */
    public static void loadRoundImage(String path, ImageView imageView, float roundRadius) {
        Context context = BaseApplication.getContext();
        Picasso.with(context).load(path).placeholder(placeHolderImage).error(errorImage).transform(new RoundTransform(roundRadius)).into(imageView);
    }

    /**
     * 加载圆角图片   相册图片
     */
    public static void loadRoundImage(File file, ImageView imageView, float roundRadius) {
        Context context = BaseApplication.getContext();
        Picasso.with(context).load(file).placeholder(placeHolderImage).error(errorImage).transform(new RoundTransform(roundRadius)).into(imageView);
    }

    /**
     * 加载圆角图片   本地图片
     */
    public static void loadRoundImage(int localImage, ImageView imageView, float roundRadius) {
        Context context = BaseApplication.getContext();
        Picasso.with(context).load(localImage).placeholder(placeHolderImage).error(errorImage).transform(new RoundTransform(roundRadius)).into(imageView);
    }

    /**
     * 加载圆形图片   网络图片
     */
    public static void loadCircleImage(String path, ImageView imageView) {
        Context context = BaseApplication.getContext();
        Picasso.with(context).load(path).placeholder(placeHolderImage).error(errorImage).transform(new CircleTransform()).into(imageView);
    }

    /**
     * 加载圆角图片   相册图片
     */
    public static void loadCircleImage(File file, ImageView imageView) {
        Context context = BaseApplication.getContext();
        Picasso.with(context).load(file).placeholder(placeHolderImage).error(errorImage).transform(new CircleTransform()).into(imageView);
    }

    /**
     * 加载圆形图片   本地图片
     */
    public static void loadCircleImage(int localImage, ImageView imageView) {
        Context context = BaseApplication.getContext();
        Picasso.with(context).load(localImage).placeholder(placeHolderImage).error(errorImage).transform(new CircleTransform()).into(imageView);
    }

    /**
     * 设置圆形头像
     */
    public static class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

    /**
     * 绘制圆角
     */
    public static class RoundTransform implements Transformation {
        private float radius;

        public RoundTransform(float radius) {
            this.radius = radius;
        }

        @Override
        public String key() {
            return "round";
        }

        @Override
        public Bitmap transform(Bitmap bitmap) {
            int size = Math.min(bitmap.getWidth(), bitmap.getHeight());

            int x = (bitmap.getWidth() - size) / 2;
            int y = (bitmap.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(bitmap, x, y, size, size);
            if (squaredBitmap != bitmap) {
                bitmap.recycle();
            }
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, radius, radius, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            squaredBitmap.recycle();
            return output;
        }
    }
}