package com.bearya.robot.base.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.bearya.robot.base.BaseApplication;
import com.bearya.robot.base.util.DebugUtil;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

public abstract class BaseActivity extends AppCompatActivity {

    private static final Set<BaseActivity> mActivityList = new LinkedHashSet<>();

    public static void finishAllActivity() {
        for (Activity activity : mActivityList) {
            if (activity != null) {
                activity.finish();
            }
        }
    }

    private ViewGroup mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(BaseApplication.isEnglish ? Locale.ENGLISH : Locale.CHINESE);
        resources.updateConfiguration(configuration, displayMetrics);
        DebugUtil.error("%s onCreate", getClass().getSimpleName());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRootView = getRootView();
        mActivityList.add(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mActivityList.remove(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DebugUtil.debug("%s onDestroy", getClass().getSimpleName());
    }

    public static BaseActivity getTopActivity() {
        if (mActivityList.size() > 0) {
            for (BaseActivity baseActivity : mActivityList) {
                return baseActivity;
            }
        }
        return null;
    }

    private ViewGroup getRootView() {
        try {
            return findViewById(android.R.id.content);
        } catch (Exception ignore) {
            return null;
        }
    }

    public void addView(View view, Object tag) {
        if (view != null) {
            addView(view, tag, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    protected void addView(View view, Object tag, ViewGroup.LayoutParams params) {
        if (mRootView != null && !isExist(tag)) {
            view.setTag(tag);
            mRootView.addView(view, params);
        }
    }

    protected boolean isExist(Object tag) {
        return mRootView.findViewWithTag(tag) != null;
    }

    public View getViewByTag(Object tag) {
        return mRootView.findViewWithTag(tag);
    }

    public void removeView(Object tag) {
        if (isExist(tag)) {
            View view = mRootView.findViewWithTag(tag);
            mRootView.removeView(view);
        }
    }

}