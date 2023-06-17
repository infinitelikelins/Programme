package com.bearya.robot.base.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

public abstract class BYGroupView extends FrameLayout {

    protected Context mContext;

    public BYGroupView(Context context, int layoutResId) {
        this(context,null,layoutResId);
    }

    public BYGroupView(Context context, @Nullable AttributeSet attrs, int layoutResId) {
        this(context, attrs,0,layoutResId);
    }

    public BYGroupView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int layoutResId) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater.from(context).inflate(layoutResId, this, true);
        initSubView();
    }

    public abstract void initSubView();

}
