package com.bearya.robot.programme.view;

import android.content.Context;

import androidx.appcompat.widget.AppCompatImageView;

import com.bearya.robot.base.BaseApplication;
import com.bearya.robot.base.ui.BaseActivity;
import com.bearya.robot.base.ui.view.BYGroupView;
import com.bearya.robot.base.util.AnimationUtil;
import com.bearya.robot.base.util.GlideUtil;
import com.bearya.robot.base.util.MusicUtil;
import com.bearya.robot.programme.R;
import com.bearya.robot.programme.data.Command;
import com.bearya.robot.programme.repository.animations.LoadAnimation;
import com.bearya.robot.programme.walk.load.lock.BaseLock;
import com.bearya.robot.programme.walk.load.lock.PopViewCallback;
import com.bearya.robot.programme.walk.load.lock.PopViewData;
import com.bearya.robot.programme.walk.load.lock.PopViewItemData;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class LoadPopView extends BYGroupView {
    private AppCompatImageView leftView;
    private AppCompatImageView rightView;
    private AppCompatImageView centerView;
    private BaseLock baseLock;

    public LoadPopView(Context context) {
        super(context, R.layout.load_pop_view);
    }

    @Override
    public void initSubView() {
        leftView = findViewById(R.id.leftView);
        rightView = findViewById(R.id.rightView);
        centerView = findViewById(R.id.centerView);
    }

    public void playByData(final PopViewData data) {
        GlideUtil.setRoundImage(getContext(), data.getLeftViewData().getImageRes(), leftView);
        GlideUtil.setRoundImage(getContext(), data.getRightViewData().getImageRes(), rightView);
        MusicUtil.playAssetsAudio(data.getInitMp3(), mp -> {
            MusicUtil.playAssetsAudio(data.getLeftViewData().getAnimatorMp3(), mp1 -> {
                MusicUtil.playAssetsAudio(data.getRightViewData().getAnimatorMp3());
                AnimationUtil.startShakeByPropertyAnim(rightView, 0.9f, 1.1f, 10f, 1000);
            });
            AnimationUtil.startShakeByPropertyAnim(leftView, 0.9f, 1.1f, 10f, 1000);
        });
        leftView.setOnClickListener(v -> {
            leftView.setOnClickListener(null);
            rightView.setOnClickListener(null);
            centerView.setVisibility(VISIBLE);
            centerView.setImageResource(data.getLeftViewData().getSelectedDrawableId());
            performClicked(data.getCallback(), data.getLeftViewData());
        });
        rightView.setOnClickListener(v -> {
            leftView.setOnClickListener(null);
            rightView.setOnClickListener(null);
            centerView.setVisibility(VISIBLE);
            centerView.setImageResource(data.getRightViewData().getSelectedDrawableId());
            performClicked(data.getCallback(), data.getRightViewData());
        });
    }

    private void performClicked(final PopViewCallback callback, final PopViewItemData result) {
        if (baseLock != null) {
            baseLock.release();
        }

        BaseApplication.sendAction(Command.format(getCmdByTts(result.getSelectedMp3())));

        MusicUtil.playAssetsAudio(result.getSelectedMp3(), mp -> {
            centerView.setVisibility(GONE);
            if (activity != null) {
                activity.removeView(LoadPopView.class.getSimpleName());
                activity = null;
            }
            Observable.timer(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
                if (callback != null) {
                    callback.onSelectView(result.getResult());
                }
            });
        });
    }

    private BaseActivity activity;

    public void addToActivity(BaseActivity activity) {
        this.activity = activity;
        activity.addView(this, LoadPopView.class.getSimpleName());
    }

    public void release() {
        leftView = null;
        rightView = null;
        centerView = null;
        if (activity != null) {
            activity = null;
        }
    }

    public void setLock(BaseLock baseLock) {
        this.baseLock = baseLock;
    }

    /**
     * 根据tts获取投屏码
     */
    private String getCmdByTts(String tts) {
        if (LoadAnimation.TTS_RIGHT_1.equals(tts)) {
            return Command.CITY_ANSER_OK_1;
        } else if (LoadAnimation.TTS_RIGHT_2.equals(tts)) {
            return Command.CITY_ANSER_OK_2;
        } else if (LoadAnimation.TTS_RIGHT_3.equals(tts)) {
            return Command.CITY_ANSER_OK_3;
        } else if (LoadAnimation.TTS_WRONG_1.equals(tts)) {
            return Command.CITY_ANSER_ERROR_1;
        } else if (LoadAnimation.TTS_WRONG_2.equals(tts)) {
            return Command.CITY_ANSER_ERROR_2;
        }
        return "";
    }
}
