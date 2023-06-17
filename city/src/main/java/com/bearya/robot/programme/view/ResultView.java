package com.bearya.robot.programme.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.bearya.robot.base.play.Director;
import com.bearya.robot.base.play.LoadPlay;
import com.bearya.robot.base.play.PlayData;
import com.bearya.robot.base.ui.view.BYGroupView;
import com.bearya.robot.base.util.AnimatorCallback;
import com.bearya.robot.programme.R;
import com.bearya.robot.programme.data.Command;

public class ResultView extends BYGroupView implements View.OnClickListener, AnimatorCallback {

    private View btnExit;
    private View btnRestart;
    private TextView tvGold;
    private TextView tvStep;
    private TextView tvGoodDone;
    private TextView tvStationLoad;
    private TextView tvKnownLoad;
    private TextView tvFlagLoad;
    private ResultListener listener;
    private View leftView;
    private View rightView;
    private View lightView;
    private View starView;
    private ValueAnimator lightAnimator;

    public ResultView(Context context, ResultListener listener, int gold, int step, int goodDone, int stationLoad, int flag, int known) {
        super(context, R.layout.result_view);

        this.listener = listener;

        tvGold.setText(String.valueOf(gold));
        tvStep.setText(String.valueOf(step));
        tvGoodDone.setText(String.valueOf(goodDone));
        tvStationLoad.setText(String.valueOf(stationLoad));
        tvFlagLoad.setText(String.valueOf(flag));
        tvKnownLoad.setText(String.valueOf(known));

        LoadPlay successPlay = new LoadPlay();

        if (gold > 50) {
            addPlay(successPlay, "tts/zh/congratulations.mp3", "tts/zh/50s.mp3", "tts/zh/coin.mp3");
        } else if (gold > 1) {
            addPlay(successPlay, "tts/zh/congratulations.mp3", "tts/zh/" + gold + ".mp3", "tts/zh/coin_count.mp3");
        } else if (gold > 0) {
            addPlay(successPlay, "tts/zh/congratulations.mp3", "tts/zh/" + gold + ".mp3", "tts/zh/coin.mp3");
        }

        if (step > 50) {
            addPlay(successPlay, "tts/zh/had_gone.mp3", "tts/zh/50s.mp3", "tts/zh/step.mp3");
        } else if (step > 1) {
            addPlay(successPlay, "tts/zh/had_gone.mp3", "tts/zh/" + step + ".mp3", "tts/zh/step_s.mp3");
        } else if (step > 0) {
            addPlay(successPlay, "tts/zh/had_gone.mp3", "tts/zh/" + step + ".mp3", "tts/zh/step.mp3");
        }

        if (goodDone > 50) {
            addPlay(successPlay, "tts/zh/had_done.mp3", "tts/zh/50.mp3", "tts/zh/good_deed.mp3");
        } else if (goodDone > 1) {
            addPlay(successPlay, "tts/zh/had_done.mp3", "tts/zh/" + goodDone + ".mp3", "tts/zh/good_deed_s.mp3");
        } else if (goodDone > 0) {
            addPlay(successPlay, "tts/zh/had_done.mp3", "tts/zh/" + goodDone + ".mp3", "tts/zh/good_deed.mp3");
        }

        if (flag > 50) {
            addPlay(successPlay, "tts/zh/flag_50.mp3");
        } else if (flag > 0) {
            addPlay(successPlay, "tts/zh/flag_" + flag + ".mp3");
        }

        if (known > 50) {
            addPlay(successPlay, "tts/zh/known_50.mp3");
        } else if (known > 0) {
            addPlay(successPlay, "tts/zh/known_" + known + ".mp3");
        }

        successPlay.playAction = Command.format(Command.CITY_RESULT_NORMAL, gold, step, goodDone, stationLoad, flag, known);

        String RESULT_SUCCESS = "RESULT_SUCCESS";
        Director.getInstance().register(RESULT_SUCCESS, successPlay);
        Director.getInstance().director(RESULT_SUCCESS, null);

    }

    private void addPlay(LoadPlay successPlay, String... sounds) {
        if (sounds.length > 0) {
            for (String sound : sounds) {
                successPlay.addLoad(new PlayData(sound));
            }
        }
    }

    @Override
    public void initSubView() {
        btnExit = findViewById(R.id.btnExit);
        btnRestart = findViewById(R.id.btnRestart);
        tvGold = findViewById(R.id.tvGold);
        tvStep = findViewById(R.id.tvStep);
        tvGoodDone = findViewById(R.id.tvGoodDone);
        tvStationLoad = findViewById(R.id.tvStationLoad);
        tvFlagLoad = findViewById(R.id.tvFlagLoad);
        tvKnownLoad = findViewById(R.id.tvKnownLoad);
        leftView = findViewById(R.id.leftView);
        rightView = findViewById(R.id.rightView);
        lightView = findViewById(R.id.ivLightView);
        starView = findViewById(R.id.ivStarView);
        btnExit.setOnClickListener(this);
        btnRestart.setOnClickListener(this);
        startAnimator();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnExit && listener != null) {
            listener.onExit();
        } else if (v.getId() == R.id.btnRestart && listener != null) {
            listener.onRestart();
        }
    }

    @Override
    public void startAnimator() {
        lightAnimator = ObjectAnimator.ofFloat(lightView, "rotation", 0, 359);
        lightAnimator.setInterpolator(new LinearInterpolator());
        lightAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        lightAnimator.setDuration(4000);
        lightAnimator.start();
        final ValueAnimator starAnimator = ValueAnimator.ofFloat(0, 1);
        starAnimator.setInterpolator(new LinearInterpolator());
        starAnimator.setDuration(200);
        starAnimator.addUpdateListener(animation -> {
            float scale = (float) animation.getAnimatedValue();
            starView.setScaleX(scale);
            starView.setScaleY(scale);
        });
        starAnimator.setRepeatCount(0);
        starAnimator.setStartDelay(10);
        starAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                starAnimator.cancel();
                playMoveAnimator();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        starAnimator.start();

    }

    private void playMoveAnimator() {
        ObjectAnimator leftAnimator = ObjectAnimator.ofFloat(leftView, "translationX", 0, -330);
        leftAnimator.setRepeatCount(0);
        leftAnimator.setStartDelay(1000);
        leftAnimator.setDuration(2000);
        leftAnimator.start();
        final ValueAnimator rightAnimator = ValueAnimator.ofFloat(0, 1);
        rightAnimator.setDuration(1500);
        rightAnimator.addUpdateListener(animation -> {
            float scale = (float) animation.getAnimatedValue();
            rightView.setScaleX(scale);
            rightView.setScaleY(scale);
        });
        rightAnimator.setRepeatCount(0);
        rightAnimator.setStartDelay(1500);
        rightAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rightAnimator.cancel();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        rightAnimator.start();
    }

    @Override
    public void release() {
        if (lightAnimator != null) {
            lightAnimator.cancel();
            lightAnimator = null;
        }
    }

    public void withShowBackBtn(View.OnClickListener onClickListener) {
        btnExit.setVisibility(GONE);
        btnRestart.setVisibility(GONE);
        findViewById(R.id.saveHistory).setVisibility(GONE);

        View btnBackHome = findViewById(R.id.btnBackHome);
        btnBackHome.setVisibility(VISIBLE);
        btnBackHome.setOnClickListener(onClickListener);
    }

    public void withShowSaveBtn(View.OnClickListener onClickListener) {
        btnExit.setVisibility(VISIBLE);
        btnRestart.setVisibility(VISIBLE);
        findViewById(R.id.btnBackHome).setVisibility(GONE);

        View saveBtn = findViewById(R.id.saveHistory);
        saveBtn.setVisibility(VISIBLE);
        saveBtn.setOnClickListener(onClickListener);
    }

    public interface ResultListener {
        void onExit();

        void onRestart();
    }

}
