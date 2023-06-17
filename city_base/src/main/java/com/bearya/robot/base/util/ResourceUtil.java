package com.bearya.robot.base.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.bearya.robot.base.BaseApplication;

import java.io.InputStream;

public class ResourceUtil {
    private static final String RES_ID = "id";
    private static final String RES_STRING = "string";
    private static final String RES_DRAWBLE = "drawable";
    private static final String RES_MIPMAP = "mipmap";
    private static final String RES_LAYOUT = "layout";
    private static final String RES_STYLE = "style";
    private static final String RES_COLOR = "color";
    private static final String RES_DIMEN = "dimen";
    private static final String RES_ANIM = "anim";
    private static final String RES_MENU = "menu";
    private static final String RES_RAW = "raw";
    public static final String ASSETS = "assets://";
    public static final String HTTP = "http://";

    public static Bitmap createBitmap(String url) {
        if (url.contains("sdcard/")) {
            return BitmapFactory.decodeFile(url);
        } else {
            InputStream inputStream = getRawInputStream(url);
            if (inputStream != null) {
                return BitmapFactory.decodeStream(inputStream);
            }
            try {
                int resId = getMipmapId(url);
                if (resId > 0) {
                    return BitmapFactory.decodeResource(BaseApplication.getInstance().getResources(), resId);
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static InputStream getRawInputStream(String name) {
        int resId = ResourceUtil.getRawIdByName(name);
        if (resId > 0) {
            return BaseApplication.getInstance().getResources().openRawResource(resId);
        }
        return null;
    }

    public static String getRawResPath(String name) {
        return "android.resource://" + BaseApplication.getInstance().getPackageName() + "/" + getRawIdByName(name);
    }

    public static int getRawIdByName(String name) {
        return getResId(BaseApplication.getInstance(), name, RES_RAW);
    }

    public static int getMipmapId(String name) {
        return getResId(BaseApplication.getInstance(), name, RES_MIPMAP);
    }

    /**
     * 获取资源文件的id
     */
    public static int getId(Context context, String resName) {
        return getResId(context, resName, RES_ID);
    }

    /**
     * 获取资源文件string的id
     */
    public static int getStringId(Context context, String resName) {
        return getResId(context, resName, RES_STRING);
    }

    /**
     * 获取资源文件drable的id
     */
    public static int getDrawableId(String resName) {
        return getResId(BaseApplication.getInstance(), resName, RES_DRAWBLE);
    }

    /**
     * 获取资源文件layout的id
     */
    public static int getLayoutId(Context context, String resName) {
        return getResId(context, resName, RES_LAYOUT);
    }

    /**
     * 获取资源文件style的id
     */
    public static int getStyleId(Context context, String resName) {
        return getResId(context, resName, RES_STYLE);
    }

    /**
     * 获取资源文件color的id
     */
    public static int getColorId(Context context, String resName) {
        return getResId(context, resName, RES_COLOR);
    }

    /**
     * 获取资源文件dimen的id
     */
    public static int getDimenId(Context context, String resName) {
        return getResId(context, resName, RES_DIMEN);
    }

    /**
     * 获取资源文件ainm的id
     */
    public static int getAnimId(Context context, String resName) {
        return getResId(context, resName, RES_ANIM);
    }

    /**
     * 获取资源文件menu的id
     */
    public static int getMenuId(Context context, String resName) {
        return getResId(context, resName, RES_MENU);
    }

    /**
     * 获取资源文件ID
     */
    public static int getResId(Context context, String resName, String defType) {
        return context.getResources().getIdentifier(resName, defType, context.getPackageName());
    }

    public static Uri getRawUri(Context context, String resName, String ext) {
        return Uri.parse(String.format("android.resource://%s/raw/%s.%s", context.getPackageName(), resName, ext));
    }

    public static Uri getRawPngUri(Context context, String resName) {
        return Uri.parse(String.format("android.resource://%s/raw/%s.%s", context.getPackageName(), resName, "png"));
    }

    public static Uri getRawJpgUri(Context context, String resName) {
        return Uri.parse(String.format("android.resource://%s/raw/%s.%s", context.getPackageName(), resName, "jpg"));
    }

    public static Uri getRawImageUri(Context context, String resName) {
        Uri uri = null;
        try {
            uri = getRawJpgUri(context, resName);
        } catch (Exception e) {
            try {
                uri = getRawPngUri(context, resName);
            } catch (Exception ee) {

            }
        }
        return uri;
    }

    public static String getRawUriString(String resName) {
        return String.format("android.resource://%s/raw/%s.%s", BaseApplication.getInstance(), resName, "jpg");
    }
}
