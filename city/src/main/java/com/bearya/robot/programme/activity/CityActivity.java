package com.bearya.robot.programme.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bearya.actionlib.utils.RobotActionManager;
import com.bearya.robot.base.BaseApplication;
import com.bearya.robot.base.can.Body;
import com.bearya.robot.base.can.CanDataListener;
import com.bearya.robot.base.can.CanManager;
import com.bearya.robot.base.play.AnimationsContainer;
import com.bearya.robot.base.play.FacePlay;
import com.bearya.robot.base.play.FaceType;
import com.bearya.robot.base.ui.BaseActivity;
import com.bearya.robot.base.util.MusicUtil;
import com.bearya.robot.base.util.ResourceUtil;
import com.bearya.robot.base.util.RobotOidReaderRater;
import com.bearya.robot.programme.R;
import com.bearya.robot.programme.data.Command;
import com.bearya.robot.programme.station.StationsActivity;
import com.bearya.robot.programme.walk.car.LoadMgr;

import java.util.List;

public class CityActivity extends BaseActivity implements View.OnClickListener, CanDataListener {

    public static void start(Context context) {
        context.startActivity(new Intent(context, CityActivity.class));
    }

    private RobotOidReaderRater robotOidReaderRater;
    private boolean twoReaderEmpty;
    private boolean isPerform;
    private boolean isRobotNot;
    private List<AnimationsContainer.FramesSequenceAnimation> animations;
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            MusicUtil.playAssetsAudio("tts/zh/s_delay_20s.mp3", mp -> {
                if (task != null) {
                    BaseApplication.getInstance().getHandler().postDelayed(task, 20000);
                }
            });
        }
    };

    private ImageView stationEntryView;
    private ImageView patriotismEntryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beiya_city);
        initCan();
        for (int i = 1; i <= 6; i++) {
            View view = findViewById(ResourceUtil.getId(getApplicationContext(), "view" + i));
            if (view != null) {
                view.setOnClickListener(this);
            }
        }
        stationEntryView = findViewById(R.id.stationEntryView);
        stationEntryView.setOnClickListener(v -> {
            LoadMgr.getInstance().setTag((String) v.getTag());
            StationsActivity.start(this);
            release();
        });
        patriotismEntryView = findViewById(R.id.patriotismEntryView);
        if (BaseApplication.isEnglish) {
            patriotismEntryView.setVisibility(View.GONE);
        } else {
            patriotismEntryView.setVisibility(View.VISIBLE);
            patriotismEntryView.setOnClickListener(v -> {
                LoadMgr.getInstance().setTag((String) v.getTag());
                PatriotismConfigActivity.start(this);
                release();
            });
        }
        findViewById(R.id.theme_more).setOnClickListener(v -> {
            StationThemeActivity.start(this);
            release();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        BaseApplication.sendAction(Command.format(Command.CITY_THEME));
        BaseApplication.getInstance().getHandler().postDelayed(() -> MusicUtil.playAssetsAudio("tts/zh/wel_to_by.mp3", mp -> MusicUtil.playAssetsBgMusic("tts/zh/by_city_bg.mp3")), 500);
    }

    @Override
    public void onClick(View v) {
        if (isPerform) {
            return;
        }
        animationStop();
        isPerform = true;
        MusicUtil.stopBgMusic();
        if (!twoReaderEmpty) {
            LoadMgr.getInstance().setTag((String) v.getTag());
            startGame();
        } else {
            isPerform = false;
            isRobotNot = true;
            MusicUtil.playAssetsAudio("tts/zh/put_me_startpoint.mp3");
            BaseApplication.getInstance().moveALittle(true);
        }
    }

    private void initReader() {
        robotOidReaderRater = new RobotOidReaderRater(10, new RobotOidReaderRater.OidReaderEmptyListener() {
            @Override
            public void onHeadEmpty() {

            }

            @Override
            public void onTailEmpty() {

            }

            @Override
            public void onTowEmpty() {//两读头数据都为空
                twoReaderEmpty = true;
            }

            @Override
            public void onFull() {
                twoReaderEmpty = false;
                if (isRobotNot) {
                    startGame();
                    isRobotNot = false;
                }
            }
        });
    }

    private void initCan() {
        CanManager.getInstance().addListener(this);
        RobotActionManager.reset();
        initReader();
    }

    @Override
    public void onFrontOid(int oid) {
        if (robotOidReaderRater != null) {
            robotOidReaderRater.addHeadOid();
        }
    }

    @Override
    public void onBackOid(int oid) {
        if (robotOidReaderRater != null) {
            robotOidReaderRater.addTailOid();
        }
    }

    @Override
    public void onTouchBody(Body body) {

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        BaseApplication.getInstance().getHandler().postDelayed(task, 20000);
        startAnimation(new FacePlay(String.valueOf(R.array.station_entry), FaceType.Arrays), stationEntryView);
        if (!BaseApplication.isEnglish) {
            startAnimation(new FacePlay(String.valueOf(R.array.patriotism_entry), FaceType.Arrays), patriotismEntryView);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        animationStop();
    }

    private void animationStop() {
        if (animations != null) {
            for (AnimationsContainer.FramesSequenceAnimation animation : animations) {
                animation.stop();
                animation.release();
            }
            animations.clear();
            animations = null;
        }
    }

    private void startGame() {
        GameActivity.start(this);
        release();
    }

    private void release() {
        BaseApplication.getInstance().getHandler().removeCallbacks(task);
        task = null;
        MusicUtil.stopMusic();
        MusicUtil.stopBgMusic();
        CanManager.getInstance().removeListener(this);
        if (robotOidReaderRater != null) {
            robotOidReaderRater.release();
        }
        finish();
    }

    public void onBackClicked(View view) {
        release();
    }

    @Override
    public void onBackPressed() {
        onBackClicked(null);
    }

    private void startAnimation(FacePlay facePlay, ImageView view) {
        int array = Integer.parseInt(facePlay.getFace());
        AnimationsContainer.FramesSequenceAnimation animation = AnimationsContainer.getInstance(array, 200).createProgressDialogAnim(view);
        animation.setRepair(true);
        animation.setOnAnimStopListener(null);
        animation.start();
    }

}