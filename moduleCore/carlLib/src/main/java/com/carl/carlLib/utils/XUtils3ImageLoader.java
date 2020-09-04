package com.carl.carlLib.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.lzy.imagepicker.loader.ImageLoader;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;

/**
 * 本工具是为了图片选则器提供图片加载框架
 */
public class XUtils3ImageLoader implements ImageLoader {
    private int loadingDrawableId;
    private int failureDrawableId;

    public XUtils3ImageLoader(int loadingDrawableId, int failureDrawableId) {
        this.loadingDrawableId = loadingDrawableId;
        this.failureDrawableId = failureDrawableId;
    }

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        ImageOptions options = new ImageOptions.Builder()//
                .setLoadingDrawableId(loadingDrawableId)//
                .setFailureDrawableId(failureDrawableId)//
                .setConfig(Bitmap.Config.RGB_565)//
                .setSize(width, height)//
                .setCrop(false)//
                .setUseMemCache(true)//
                .build();
        x.image().bind(imageView, Uri.fromFile(new File(path)).toString(), options);
    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {

    }

    @Override
    public void clearMemoryCache() {
    }
}