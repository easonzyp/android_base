package com.develop.wiseisland.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.develop.wiseisland.android.base.BaseActivity;
import com.develop.wiseisland.android.base.BaseApplication;
import com.develop.wiseisland.android.base.BaseFragment;
import com.develop.wiseisland.android.config.ImageCutConfig;
import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by zyp on 2018/2/23.
 * <p>
 * 选择本地图片工具类
 */
public class SelectPicUtils {
    private static SelectPicUtils instance;

    public static final int GET_BY_ALBUM_SINGLE = 801;      //从相册获取 单选
    public static final int GET_BY_ALBUM_MULTI = 802;       //从相册获取 多选
    public static final int CROP = 803;                     //裁剪
    private static ImageCaptureManager captureManager;
    private static int aspectX = 1;
    private static int aspectY = 1;
    private static int SELECT_WIDTH = 300;
    private static int SELECT_HEIGHT = 300;

    private final static int DEFAULT_WIDTH = 300;
    private final static int DEFAULT_HEIGHT_1 = 300;
    private final static int DEFAULT_HEIGHT_2 = 150;
    private final static int DEFAULT_HEIGHT_3 = 200;
    private final static int DEFAULT_HEIGHT_4 = 600;

    private static boolean isCrop = false;
    private String imagePath;

    public static SelectPicUtils getInstance() {
        if (instance == null) {
            instance = new SelectPicUtils();
        }
        return instance;
    }

    /**
     * 从相册获取图片
     *
     * @param isShowCarema 是否显示拍照， 默认false
     * @param para         裁剪比例
     * @param multiple     图片大小倍数
     */
    public static void getByAlbumSingle(Activity act, boolean isShowCarema, int para, int multiple) {
        getImageRatio(para, multiple);
        PhotoPickerIntent intent = new PhotoPickerIntent(BaseApplication.getContext());
        intent.setSelectModel(SelectModel.SINGLE);
        intent.setShowCarema(isShowCarema);
        act.startActivityForResult(intent, GET_BY_ALBUM_SINGLE);
    }

    /**
     * 从相册获取图片
     *
     * @param isShowCarema 是否显示拍照， 默认false
     */
    public static void getByAlbumSingle(Activity act, boolean isShowCarema) {
        PhotoPickerIntent intent = new PhotoPickerIntent(BaseApplication.getContext());
        intent.setSelectModel(SelectModel.SINGLE);
        intent.setShowCarema(isShowCarema);
        act.startActivityForResult(intent, GET_BY_ALBUM_SINGLE);
    }

    /**
     * 从相册获取图片 多选
     *
     * @param isShowCarema 是否显示拍照， 默认false
     */
    public static void getByAlbumMulti(Activity act, boolean isShowCarema) {
        PhotoPickerIntent intent = new PhotoPickerIntent(BaseApplication.getContext());
        intent.setSelectModel(SelectModel.MULTI);
        intent.setShowCarema(isShowCarema);
        act.startActivityForResult(intent, GET_BY_ALBUM_MULTI);
    }

    /**
     * 通过拍照获取图片
     */
    public static void getByCamera(Activity act, int para, int multiple) {
        getImageRatio(para, multiple);
        try {
            if (captureManager == null) {
                captureManager = new ImageCaptureManager(BaseApplication.getContext());
            }
            Intent intent = captureManager.dispatchTakePictureIntent();
            act.startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
        } catch (IOException e) {
            ToastUtils.showShort("请选择相机");
            e.printStackTrace();
        }
    }

    /**
     * 处理获取的图片，注意判断空指针
     */
    public ArrayList<String> onActivityResult(BaseActivity act, int requestCode, int resultCode, Intent data) {
        ArrayList<String> imageList = new ArrayList<>();
        if (resultCode == Activity.RESULT_OK) {
            Uri uri;
            switch (requestCode) {
                case GET_BY_ALBUM_SINGLE:   //单选
                    if (isCrop) {
                        //需要裁剪
                        imagePath = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT).get(0);
                        uri = Uri.fromFile(new File(imagePath));
                        act.startActivityForResult(crop(uri), CROP);
                    } else {
                        imageList = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    }
                    break;
                case GET_BY_ALBUM_MULTI:    //多选
                    imageList = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    break;
                case ImageCaptureManager.REQUEST_TAKE_PHOTO:    //拍照
                    if (captureManager.getCurrentPhotoPath() != null) {
                        captureManager.galleryAddPic();
                        // 照片地址
                        imagePath = captureManager.getCurrentPhotoPath();
                        if (isCrop) {
                            uri = Uri.fromFile(new File(imagePath));
                            act.startActivityForResult(crop(uri), CROP);
                        } else {
                            imageList.add(imagePath);
                        }
                    }
                    break;
                case CROP:  //裁剪
                    imageList.add(imagePath);
                    break;
            }
        }
        return imageList;
    }

    /**
     * 处理获取的图片，注意判断空指针
     */
    public ArrayList<String> onActivityResult(BaseFragment fragment, int requestCode, int resultCode, Intent data) {
        ArrayList<String> imageList = new ArrayList<>();
        if (resultCode == Activity.RESULT_OK) {
            Uri uri;
            switch (requestCode) {
                case GET_BY_ALBUM_SINGLE:   //单选
                    if (isCrop) {
                        //需要裁剪
                        imagePath = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT).get(0);
                        uri = Uri.fromFile(new File(imagePath));
                        fragment.startActivityForResult(crop(uri), CROP);
                    } else {
                        imageList = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    }
                    break;
                case GET_BY_ALBUM_MULTI:    //多选
                    imageList = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    break;
                case ImageCaptureManager.REQUEST_TAKE_PHOTO:    //拍照
                    if (captureManager.getCurrentPhotoPath() != null) {
                        captureManager.galleryAddPic();
                        // 照片地址
                        imagePath = captureManager.getCurrentPhotoPath();
                        if (isCrop) {
                            uri = Uri.fromFile(new File(imagePath));
                            fragment.startActivityForResult(crop(uri), CROP);
                        } else {
                            imageList.add(imagePath);
                        }
                    }
                    break;
                case CROP:  //裁剪
                    imageList.add(imagePath);
                    break;
            }
        }
        return imageList;
    }

    /**
     * 裁剪
     */
    public Intent crop(Uri uri) {
        imagePath = getImagePath(BaseApplication.getContext(), null);
        Intent intent = new Intent("com.android.camera.action.CROP");
        // 照片URL地址
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra("crop", "true");
        if (isCrop) {
            intent.putExtra("aspectX", aspectX);
            intent.putExtra("aspectY", aspectY);

            intent.putExtra("outputX", SELECT_WIDTH);
            intent.putExtra("outputY", SELECT_HEIGHT);
            intent.putExtra("scale", true);
            intent.putExtra("scaleUpIfNeeded", true);
        }
        File imageFile = new File(imagePath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        // 不启用人脸识别
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);

        return intent;
    }

    public static String getImagePath(Context context, String fileName) {
        String path;

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = context.getFilesDir().getAbsolutePath();
        } else {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/";
        }

        if (!path.endsWith("/")) {
            path += "/";
        }

        if (TextUtils.isEmpty(fileName)) {
            path += "ifd.jpg";
        } else {
            path += (fileName + ".jpg");
        }
        return path;
    }

    public static void getImageRatio(int para, int multiple) {
        if (multiple < 1) {
            multiple = 1;
        } else if (multiple > 3) {
            multiple = 3;
        }
        switch (para) {
            case ImageCutConfig.IMAGE_RATIO_ONE_TO_ONE:
                aspectX = 1;
                aspectY = 1;
                SELECT_WIDTH = DEFAULT_WIDTH * multiple;
                SELECT_HEIGHT = DEFAULT_HEIGHT_1 * multiple;
                isCrop = true;
                break;
            case ImageCutConfig.IMAGE_RATIO_TWO_TO_ONE:
                aspectX = 2;
                aspectY = 1;
                SELECT_WIDTH = DEFAULT_WIDTH * multiple;
                SELECT_HEIGHT = DEFAULT_HEIGHT_2 * multiple;
                isCrop = true;
                break;
            case ImageCutConfig.IMAGE_RATIO_THREE_TO_TWO:
                aspectX = 3;
                aspectY = 2;
                SELECT_WIDTH = DEFAULT_WIDTH * multiple;
                SELECT_HEIGHT = DEFAULT_HEIGHT_3 * multiple;
                isCrop = true;
                break;
            case ImageCutConfig.IMAGE_RATIO_ONE_TO_TWO:
                aspectX = 1;
                aspectY = 2;
                SELECT_WIDTH = DEFAULT_WIDTH * multiple;
                SELECT_HEIGHT = DEFAULT_HEIGHT_4 * multiple;
                isCrop = true;
                break;
            default:
                aspectX = 1;
                aspectY = 1;
                SELECT_WIDTH = DEFAULT_WIDTH * multiple;
                SELECT_HEIGHT = DEFAULT_HEIGHT_1 * multiple;
                isCrop = false;
                break;
        }
    }
}