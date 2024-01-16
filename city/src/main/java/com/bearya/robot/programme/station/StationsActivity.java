package com.bearya.robot.programme.station;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bearya.robot.base.play.PlayData;
import com.bearya.robot.base.ui.BaseActivity;
import com.bearya.robot.base.util.MusicUtil;
import com.bearya.robot.base.util.ResourceUtil;
import com.bearya.robot.programme.R;
import com.bearya.robot.programme.activity.CityActivity;
import com.bearya.robot.programme.activity.GameActivity;
import com.bearya.robot.programme.view.DeleteConfirmPopup;
import com.bearya.robot.base.musicplayer.AudioRecorderManager;

public class StationsActivity extends BaseActivity implements View.OnClickListener {

    public static void start(Context context) {
        context.startActivity(new Intent(context, StationsActivity.class));
    }

    public static StationLib stationLib;

    private final ImageView[] stationViewArr = new ImageView[6];

    private boolean isClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations_load);
        for (int i = 0; i < stationViewArr.length; i++) {
            ImageView view = findViewById(ResourceUtil.getId(getApplicationContext(), "btnStation" + (i + 1)));
            view.setTag(i + 1);
            view.setOnClickListener(this);
            stationViewArr[i] = view;
        }
        MusicUtil.playAssetsAudio("station/zh/station_init.mp3");
        stationLib = StationLib.getLibsFromAssets(getApplicationContext());
    }

    @Override
    public void onClick(View view) {
        if (!isClicked) {
            isClicked = true;
            int index = (Integer) view.getTag();
            Intent intent = new Intent(StationsActivity.this, StationConfigActivity.class);
            intent.putExtra("index", index);
            startActivity(intent);
        }
    }

    public void onPerformClicked(View view) {
        GameActivity.start(this);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHistory();
    }

    private void loadHistory() {
        for (ImageView iv : stationViewArr) {
            Object tag = iv.getTag();
            if (tag instanceof Integer) {
                PlayData playData = StationConfigActivity.getLastConfigStation(getApplicationContext(), (Integer) tag);
                iv.setSelected(!playData.isEmpty());
            }
        }
    }

    public void onBackClicked(View view) {
        CityActivity.start(this);
        finish();
    }

    public void onClearClicked(View view) {
        deleteStationConfig();
    }

    /**
     * 清理站点的配置
     */
    private void deleteStationConfig() {
        new DeleteConfirmPopup(this)
                .applyShowTips(getString(R.string.clear_all_station_config))
                .applyShowAudio("station/zh/station_delete_config.mp3")
                .withConfirm(v -> {
                    StationConfigActivity.clearStationCache(getApplicationContext());
                    loadHistory();
                    Toast.makeText(getApplicationContext(), getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                }, null).showPopupWindow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AudioRecorderManager.getInstance().release();
        stationLib = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MusicUtil.stopMusic();
        isClicked = false;
    }

}