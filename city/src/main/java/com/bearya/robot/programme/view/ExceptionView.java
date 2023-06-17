package com.bearya.robot.programme.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.bearya.robot.base.ui.view.BYGroupView;
import com.bearya.robot.base.util.AnimatorCallback;
import com.bearya.robot.base.util.MusicUtil;
import com.bearya.robot.programme.R;

public class ExceptionView extends BYGroupView implements View.OnClickListener, AnimatorCallback {

    public interface ExceptionListener {
        void onExit();
        void onFix();
    }

    private Animator animator;
    private View leftEyeView;
    private View rightEyeView;
    private ExceptionListener listener;
    private ValueAnimator valueAnimator;
    private boolean isClicked;

    public ExceptionView(Context context, ExceptionListener listener, String mp3, final String towMp3, int layoutId) {
        super(context, layoutId);
        this.listener = listener;
        MusicUtil.playAssetsAudio(mp3, mp -> {
            if (towMp3 != null) {
                MusicUtil.playAssetsAudio(towMp3, mp1 -> MusicUtil.playAssetsBgMusic("tts/zh/game_fail.mp3"));
            } else {
                MusicUtil.playAssetsBgMusic("tts/zh/game_fail.mp3");
            }
        });
    }

    @Override
    public void initSubView() {
        findViewById(R.id.btnExit).setOnClickListener(this);
        findViewById(R.id.btnFix).setOnClickListener(this);
        leftEyeView = findViewById(R.id.leftEyeView);
        rightEyeView = findViewById(R.id.rightEyeView);
        startAnimator();
    }

    @Override
    public void onClick(View v) {
        if (!isClicked) {
            isClicked = true;
            MusicUtil.stopMusic();
            if (v.getId() == R.id.btnExit) {
                if (listener != null) {
                    listener.onExit();
                }
            } else if (v.getId() == R.id.btnFix) {
                if (listener != null) {
                    listener.onFix();
                }
            }
        }
    }

    public void startAnimator() {
        if (leftEyeView != null && rightEyeView != null) {
            valueAnimator = ValueAnimator.ofInt(0, 359);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.setDuration(800);
            valueAnimator.addUpdateListener(animation -> {
                int rotation = (int) animation.getAnimatedValue();
                leftEyeView.setRotation(rotation);
                rightEyeView.setRotation(rotation);
            });
            valueAnimator.setRepeatMode(ValueAnimator.RESTART);
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.start();
        }

    }

    public void release() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator = null;
        }
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
    }


}
