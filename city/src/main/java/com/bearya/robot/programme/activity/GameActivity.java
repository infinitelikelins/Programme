package com.bearya.robot.programme.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bearya.robot.base.BaseApplication;
import com.bearya.robot.base.load.BaseLoad;
import com.bearya.robot.base.play.Director;
import com.bearya.robot.base.play.FacePlay;
import com.bearya.robot.base.play.FaceType;
import com.bearya.robot.base.play.LoadPlay;
import com.bearya.robot.base.play.PlayData;
import com.bearya.robot.base.protocol.DriveResult;
import com.bearya.robot.base.ui.BaseActivity;
import com.bearya.robot.base.util.AnimatorCallback;
import com.bearya.robot.base.util.CodeUtils;
import com.bearya.robot.base.util.DebugUtil;
import com.bearya.robot.base.util.MusicUtil;
import com.bearya.robot.base.walk.InObstacleReason;
import com.bearya.robot.programme.R;
import com.bearya.robot.programme.data.Command;
import com.bearya.robot.programme.data.Tag;
import com.bearya.robot.programme.repository.CityRecordRepository;
import com.bearya.robot.programme.view.ExceptionView;
import com.bearya.robot.programme.view.LoadPopView;
import com.bearya.robot.programme.view.ResultView;
import com.bearya.robot.programme.view.SaveGamePopup;
import com.bearya.robot.programme.walk.car.ICar;
import com.bearya.robot.programme.walk.car.LoadMgr;
import com.bearya.robot.programme.walk.car.RobotCar;

public class GameActivity extends BaseActivity {

    private static final String EXCEPTION_VIEW_TAG = "exception_view_tag";
    private static final String RESULT_VIEW_TAG = "result_view_tag";

    public static void start(Context context) {
        context.startActivity(new Intent(context, GameActivity.class));
    }

    private LottieAnimationView lottieView;
    private RobotCar robotCar;

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            BaseApplication.getInstance().getHandler().removeCallbacks(this);
            MusicUtil.playAssetsAudio("tts/zh/retry_40s.mp3");
        }
    };

    private final Runnable maxTimeRunnable = new Runnable() {
        @Override
        public void run() {
            BaseApplication.getInstance().getHandler().removeCallbacks(this);
            MusicUtil.playAssetsAudio("tts/zh/retry_60s.mp3");
        }
    };

    private void releaseTimeOut() {
        DebugUtil.debug("Baselock==============> %s", "移除超时倒计时");
        BaseApplication.getInstance().getHandler().removeCallbacks(task);
        BaseApplication.getInstance().getHandler().removeCallbacks(maxTimeRunnable);
    }

    private final ICar.DriveListener driveListener = new ICar.DriveListener() {

        @Override
        public void onException(final ICar.DriveException exception, final Object param) {
            BaseApplication.getInstance().getHandler().postDelayed(task, 40000);
            BaseApplication.getInstance().getHandler().postDelayed(maxTimeRunnable, 60000);
            runOnUiThread(() -> {
                removeView(LoadPopView.class.getSimpleName());
                String mp3 = null;
                String towMp3 = null;
                int layoutId = R.layout.exception_view;
                String actionID = "";
                switch (exception) {
                    case NoEntry:
                        towMp3 = "tts/zh/mission_error_5.mp3";
                        actionID = Command.CITY_EXCEPT_NOENTRY;
                        break;
                    case OutOfLoad:
                        mp3 = "tts/zh/mission_fail_1.mp3";
                        actionID = Command.CITY_EXCEPT_OUTOFLOAD;
                        DebugUtil.debug("走出道路");
                        break;
                    case InObstacle: {
                        InObstacleReason reason = (InObstacleReason) param;
                        switch (reason) {
                            case DriveInObstacle:
                                layoutId = R.layout.obstacle_layout;
                                actionID = Command.CITY_EXCEPT_DRIVEINOBSTACLE;
                                mp3 = "tts/zh/mission_fail_3.mp3";
                                break;
                            case UserPutInObstacle:
                                layoutId = R.layout.obstacle_layout;
                                actionID = Command.CITY_EXCEPT_PUTINOBSTACLE;
                                mp3 = "tts/zh/mission_fail_3.mp3";
                                break;
                            case LoadConnectException:
                                mp3 = "tts/zh/load_connect_exception.mp3";
                                actionID = Command.CITY_EXCEPT_LOADCONNECT;
                                break;
                            case HardwareException:
                                mp3 = "tts/zh/hardware_exception.mp3";
                                actionID = Command.CITY_EXCEPT_HARDWARE;
                                break;
                        }
                        break;
                    }
                    case PutRobotInTowLoad:
                        mp3 = "tts/zh/mission_fail_4.mp3";
                        actionID = Command.CITY_EXCEPT_INTOWLOAD;
                        break;
                }
                removeAnimatorView(EXCEPTION_VIEW_TAG);
                final LoadPopView view = (LoadPopView) getViewByTag(LoadPopView.class.getSimpleName());
                if (view != null) {
                    view.release();
                    removeView(LoadPopView.class.getSimpleName());
                }
                // TODO: 2019-05-24 避障UI需要改动 layout需灵活设置
                ExceptionView exceptionView = new ExceptionView(GameActivity.this, new ExceptionView.ExceptionListener() {
                    @Override
                    public void onExit() {
                        releaseTimeOut();
                        removeAnimatorView(EXCEPTION_VIEW_TAG);
                        onRestartClicked(null);
                    }

                    @Override
                    public void onFix() {
                        releaseTimeOut();
                        robotCar.drive();
                        lottieView.setImageResource(R.mipmap.act_load_bg);
                        removeAnimatorView(EXCEPTION_VIEW_TAG);
                    }
                }, mp3, towMp3, layoutId);

                BaseApplication.sendAction(Command.format(actionID));
                addView(exceptionView, EXCEPTION_VIEW_TAG);
            });

        }

        @Override
        public void onDriveResult(DriveResult result) {
            runOnUiThread(this::showGameSuccess);
        }

        private void showGameSuccess() {
            MusicUtil.playAssetsAudio("tts/zh/game_success.mp3");
            removeAnimatorView(RESULT_VIEW_TAG);
            int gold = LoadMgr.getInstance().getGoldNumber();
            int step = LoadMgr.getInstance().getStepNumber();
            int goodDone = LoadMgr.getInstance().getGoodDoneNumber();
            int stationLoad = LoadMgr.getInstance().getStationLoadNumber();
            int flag = LoadMgr.getInstance().getFlagLoadNumber();
            int known = LoadMgr.getInstance().getKnownLoadNumber();

            SharedPreferences sharedPreferences = getSharedPreferences("record", Context.MODE_PRIVATE);
            sharedPreferences.edit()
                    .putInt("gold", gold)
                    .putInt("step", step)
                    .putInt("goodDone", goodDone)
                    .putInt("stationLoad", stationLoad)
                    .putInt("flag", flag)
                    .putInt("known", known)
                    .apply();

            ResultView exceptionView = new ResultView(GameActivity.this, new ResultView.ResultListener() {
                @Override
                public void onExit() {
                    removeAnimatorView(RESULT_VIEW_TAG);
                    CityRecordRepository.getInstance().clearSavePlayDataMemory();
                    BaseApplication.getInstance().release();
                }

                @Override
                public void onRestart() {
                    CityRecordRepository.getInstance().clearSavePlayDataMemory();
                    jump();
                    removeAnimatorView(RESULT_VIEW_TAG);
                }
            }, gold, step, goodDone, stationLoad, flag, known);
            exceptionView.withShowSaveBtn(v -> {
                if (v.isEnabled()) {
                    v.setEnabled(false);
                    saveGameInfo();
                } else {
                    Toast.makeText(GameActivity.this, getString(R.string.save_success_again), Toast.LENGTH_SHORT).show();
                }
            });
            addView(exceptionView, RESULT_VIEW_TAG);
        }

        private void removeAnimatorView(String tag) {
            View view = getViewByTag(tag);
            if (view instanceof AnimatorCallback) {
                AnimatorCallback animatorCallback = (AnimatorCallback) view;
                animatorCallback.release();
            }
            removeView(tag);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        lottieView = findViewById(R.id.lottieView);
        lottieView.loop(true);

        Director.getInstance().setView(lottieView);

        robotCar = new RobotCar(driveListener);

        playStartLoadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }

    private void release() {
        MusicUtil.stopBgMusic();
        Director.getInstance().release();
        robotCar.release();
        DebugUtil.info("游戏场景资源应该被释放");
    }

    private void playStartLoadData() {
        LoadPlay newLoadPlay = new LoadPlay();
        PlayData playData = new PlayData();
        String tag = LoadMgr.getInstance().getTag();
        if (tag != null && (tag.equals(Tag.TAG_WORLD) || tag.equals(Tag.TAG_HOME) || tag.equals(Tag.TAG_LIBRARY) ||
                tag.equals(Tag.TAG_SCHOOL) || tag.equals(Tag.TAG_ZOO) || tag.equals(Tag.TAG_PARK) || tag.equals(Tag.TAG_PATRIOTISM))) {

            String mp3 = null;
            switch (tag) {
                case Tag.TAG_HOME:
                    mp3 = CodeUtils.oneOf("tts/zh/s_go_home1.mp3", "tts/zh/s_go_home2.mp3", "tts/zh/s_go_home3.mp3");
                    break;
                case Tag.TAG_LIBRARY:
                    mp3 = CodeUtils.oneOf("tts/zh/s_go_library1.mp3", "tts/zh/s_go_library2.mp3", "tts/zh/s_go_library3.mp3");
                    break;
                case Tag.TAG_ZOO:
                    mp3 = CodeUtils.oneOf("tts/zh/s_go_zoo1.mp3", "tts/zh/s_go_zoo2.mp3", "tts/zh/s_go_zoo3.mp3");
                    break;
                case Tag.TAG_WORLD:
                    mp3 = CodeUtils.oneOf("station/zh/station_go.mp3");
                    break;
                case Tag.TAG_PARK:
                    mp3 = CodeUtils.oneOf("tts/zh/s_go_amu_pa1.mp3", "tts/zh/s_go_amu_pa2.mp3", "tts/zh/s_go_amu_pa3.mp3");
                    break;
                case Tag.TAG_SCHOOL:
                    mp3 = CodeUtils.oneOf("tts/zh/s_go_school1.mp3", "tts/zh/s_go_school2.mp3", "tts/zh/s_go_school3.mp3");
                    break;
                case Tag.TAG_PATRIOTISM:
                    mp3 = CodeUtils.oneOf("tts/zh/s_go_patriotism1.mp3", "tts/zh/s_go_patriotism2.mp3", "tts/zh/s_go_patriotism3.mp3",
                            "tts/zh/s_go_patriotism4.mp3", "tts/zh/s_go_patriotism5.mp3", "tts/zh/s_go_patriotism6.mp3");
                    break;
            }
            if (mp3 != null) {
                playData.facePlay = new FacePlay("hg", FaceType.Lottie);
                playData.sound = mp3;
                newLoadPlay.playAction = Command.format(Command.CITY_GO, mp3.replaceAll("tts/zh/(.*).mp3", "$1"));
            }
        } else {
            playData.facePlay = new FacePlay("hg", FaceType.Lottie);
            playData.sound = "theme/music/" + tag + "_start.mp3";
        }
        newLoadPlay.addLoad(playData);
        Director.getInstance().register(BaseLoad.ON_START_LOAD, newLoadPlay);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Director.getInstance().director(BaseLoad.ON_START_LOAD, () -> robotCar.drive());
    }

    public void onRestartClicked(View view) {
        release();
        finish();
    }

    private void jump() {
        release();
        LoadingActivity.start(this);
        finish();
    }

    public void setScreenClickListener(View.OnClickListener screenClickListener) {
        lottieView.setOnClickListener(screenClickListener);
    }

    private void saveGameInfo() {
        final long saveId = CityRecordRepository.getInstance().save();
        SaveGamePopup popup = new SaveGamePopup(this, saveId);
        popup.withConfirm(v -> TellStoryActivity.start(GameActivity.this, CityRecordRepository.getInstance().getItem(String.valueOf(saveId))), null);
        popup.showPopupWindow();
    }

}