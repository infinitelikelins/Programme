package com.bearya.robot.base.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bearya.robot.base.BaseApplication;
import com.bearya.robot.base.R;
import com.bearya.robot.base.ui.view.RippleImageView;
import com.bearya.robot.base.util.DeviceUtil;
import com.bearya.robot.base.util.MusicUtil;

import java.util.Locale;

public abstract class BaseLauncherActivity extends BaseActivity implements View.OnClickListener {

    private RippleImageView rippleImageView;
    private Handler mHandler = new Handler();
    private ObjectAnimator translationXAnimator;
    private ObjectAnimator rotationAnimator;
    private ObjectAnimator scaleXAnimator;
    private ObjectAnimator scaleYAnimator;

    private Runnable stopWaveAnimationRunnable = new Runnable() {
        @Override
        public void run() {
            rippleImageView.stopWaveAnimation();
        }
    };

    private boolean flag;
    private LauncherData launcherData;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launcherData = getLauncherData();
        setContentView(R.layout.activity_launch);
        View contentLayout = findViewById(R.id.content_layout);
        contentLayout.setOnClickListener(this);
        findViewById(R.id.record).setOnClickListener(this);
        if (BaseApplication.isEnglish) {
            findViewById(R.id.casting).setVisibility(View.GONE);
        } else {
            findViewById(R.id.casting).setOnClickListener(this);
        }
        findViewById(R.id.my_working).setOnClickListener(this);
        ImageView mFingerView = findViewById(R.id.fingerView);
        rippleImageView = findViewById(R.id.rippleImageView);
        translationXAnimator = ObjectAnimator.ofFloat(mFingerView, "translationX", 0, -60);
        translationXAnimator.setDuration(1000);
        translationXAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        translationXAnimator.setRepeatMode(ObjectAnimator.REVERSE);

        rotationAnimator = ObjectAnimator.ofFloat(mFingerView, "rotationX", 0, 30);
        rotationAnimator.setDuration(1000);
        rotationAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        rotationAnimator.setRepeatMode(ObjectAnimator.REVERSE);

        scaleXAnimator = ObjectAnimator.ofFloat(mFingerView, "scaleX", 1.0f, 0.8f);
        scaleXAnimator.setDuration(1000);
        scaleXAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        scaleXAnimator.setRepeatMode(ObjectAnimator.REVERSE);

        scaleYAnimator = ObjectAnimator.ofFloat(mFingerView, "scaleY", 1.0f, 0.8f);
        scaleYAnimator.setDuration(1000);
        scaleYAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        scaleYAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        scaleXAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                flag = !flag;
                if (flag) {
                    rippleImageView.startWaveAnimation();
                    mHandler.postDelayed(stopWaveAnimationRunnable, 1000);
                }
            }


        });

        contentLayout.setBackgroundResource(launcherData.bg);
        TextView tvVersion = findViewById(R.id.tvVersion);
        tvVersion.setText(String.format("V %s", DeviceUtil.getVersionName(getApplicationContext())));
    }

    @Override
    protected void onResume() {
        super.onResume();

        translationXAnimator.start();
        rotationAnimator.start();
        scaleXAnimator.start();
        scaleYAnimator.start();

        BaseApplication.getInstance().getHandler().postDelayed(() -> MusicUtil.playAssetsAudio(launcherData.bgMp3, mp -> MusicUtil.playAssetsBgMusic(launcherData.tipMp3)), 500);

    }

    @Override
    protected void onPause() {
        super.onPause();
        translationXAnimator.pause();
        rotationAnimator.pause();
        scaleXAnimator.pause();
        scaleYAnimator.pause();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.content_layout) {
            startActivity(new Intent(this, launcherData.jumpToActivity));
            release();
        } else if (i == R.id.record) {
            startHistoryRecord();
        } else if (i == R.id.casting) {
            startBluetoothCasting();
        } else if (i == R.id.my_working) {
            startWork();
        }

    }

    protected abstract void startWork();

    protected abstract void startHistoryRecord();

    protected abstract void startBluetoothCasting();

    private void release() {
        translationXAnimator.cancel();
        rotationAnimator.cancel();
        scaleXAnimator.cancel();
        scaleYAnimator.cancel();
        stopWaveAnimationRunnable.run();
        mHandler.removeCallbacks(stopWaveAnimationRunnable);
        MusicUtil.stopBgMusic();
        MusicUtil.stopMusic();
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public void onBackClicked(View view) {
        BaseApplication.getInstance().release();
        release();
    }

    protected abstract LauncherData getLauncherData();

}