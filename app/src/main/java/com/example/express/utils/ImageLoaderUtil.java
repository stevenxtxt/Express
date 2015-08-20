package com.example.express.utils;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.example.express.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * ImageLoader简单工具类
 * 图片加载类
 * @author chensf5
 */
public class ImageLoaderUtil {

    private static ImageLoader imageLoader = ImageLoader.getInstance();

    private static DisplayImageOptions options;

    private static AnimateFirstDisplayListener animateFirstListener = new AnimateFirstDisplayListener();

    // SD卡挂载时，图片缓存在sd卡的路径
    public static final String SDCARD_IMG_PATH = Environment.getExternalStorageDirectory().getPath() + "/kuaidi/images/";

    // SD卡没有挂载时，图片缓存在手机缓存路径
    public static final String CACHE_IMG_PATH = Environment.getDownloadCacheDirectory().getPath() + "/kuaidi/images/";

    private String imagePath;
    private int resId;// 默认图片

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    private static ImageLoaderUtil imageLoaderUtil = new ImageLoaderUtil();

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                imageView.setImageBitmap(loadedImage);
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    public ImageLoaderUtil() {
        File sdcardImgPath = new File(SDCARD_IMG_PATH);
        if (!sdcardImgPath.exists()) { // 路径不存在
            sdcardImgPath.mkdirs();
        }
        imagePath = getImgPath();
    }

    public static ImageLoaderUtil getInstance() {
        return imageLoaderUtil;
    }

    public static String getImgPath() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return SDCARD_IMG_PATH;
        } else {
            return CACHE_IMG_PATH;
        }
    }

    public void displayImage(String uri, ImageView image) {
        // imageLoader.displayImage(uri, image, getDisplayImageOptions(),
        // animateFirstListener);
        if (getResId() == 0) {
            image.setImageResource(R.drawable.ic_launcher);
        } else {
            image.setImageResource(getResId());
            setResId(0);
        }
        // image.setImageResource(R.drawable.wb_default_photo);
        File picFile = new File(imagePath + getFileName(uri));
        if (picFile.exists()) {
            uri = addFileBegin(picFile.getAbsolutePath());
        }
        imageLoader.displayImage(uri, image, getDisplayImageOptions(), null);
        return;
    }

    public void displayImage(String uri, ImageView image, int defaultDrawableId) {
        imageLoader.displayImage(uri, image, getDisplayImageOptionsWithDefault(defaultDrawableId), null);
        return;
    }

    public void displayImage(String uri, ImageView image, DisplayImageOptions options) {
        // imageLoader.displayImage(uri, image, options, animateFirstListener);
        imageLoader.displayImage(uri, image, options, null);

        // imageLoader.displayImage(uri, imageAware)
    }

    public void loadImage(String uri, final ImageLoaderCompleteListener imageLoaderCompleteListener) {
        File picFile = new File(imagePath + getFileName(uri));
        if (picFile.exists()) {
            uri = addFileBegin(picFile.getAbsolutePath());
        }
        imageLoader.loadImage(uri, getDisplayImageOptions(), new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                imageLoaderCompleteListener.onCompleteImageLoader(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }
        });
    }

    public synchronized static DisplayImageOptions getDisplayImageOptions() {
        if (null == options) {
            options = new DisplayImageOptions.Builder()
            // .showImageOnLoading(R.drawable.ic_stub)
            // .showImageForEmptyUri(R.drawable.ic_empty)
            // .showImageOnFail(R.drawable.ic_error)
                    .cacheInMemory(true).cacheOnDisc(true)
                    // .imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    // .displayer(new FadeInBitmapDisplayer(300))
                    .build();
        }
        return options;
    }

    public static DisplayImageOptions getDisplayImageOptionsWithDefault(int defaultDrawableId) {
        if (null == options) {
            options = new DisplayImageOptions.Builder()
                    // .showImageOnLoading(R.drawable.ic_stub)
                    .showImageForEmptyUri(defaultDrawableId).showImageOnFail(defaultDrawableId).cacheInMemory(true)
                    .cacheOnDisc(true)
                    // .imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    // .displayer(new FadeInBitmapDisplayer(300))
                    .build();
        }
        return options;
    }

    public synchronized static DisplayImageOptions getDisplayImageOptions(int imageOnLoading) {
        if (null == options) {
            options = new DisplayImageOptions.Builder()
            // .showImageOnLoading(imageOnLoading)
            // .showImageForEmptyUri(R.drawable.ic_empty)
            // .showImageOnFail(R.drawable.ic_error)
                    .cacheInMemory(true).cacheOnDisc(true)
                    // .considerExifParams(true)
                    // .imageScaleType(ImageScaleType.EXACTLY)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    // .displayer(new FadeInBitmapDisplayer(300))
                    .build();
        }
        return options;
    }

    public interface ImageLoaderCompleteListener {
        public void onCompleteImageLoader(Bitmap bitmap);
    }

    // 截取文件名称
    private String getFileName(String path) {
        if (path == null) {
            return "";
        }
        int i = path.lastIndexOf("\\");
        if (i < 0) {
            i = path.lastIndexOf("/");
        }
        if (i < 0) {
            return path;
        }
        return path.substring(i + 1);
    }

    // 将文件路径前加上"file://"
    private String addFileBegin(String path) {
        if (path.startsWith("file://"))
            return path;
        return "file://" + path;
    }
}
