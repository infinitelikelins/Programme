package com.bearya.robot.base.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class GlideUtil {

    public static void setRoundImage(Context mContext, int path, ImageView view) {
        Glide.with(mContext).load(path).crossFade().into(view);
    }

}
