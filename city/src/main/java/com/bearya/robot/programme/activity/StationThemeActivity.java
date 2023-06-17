package com.bearya.robot.programme.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bearya.robot.base.ui.BaseActivity;
import com.bearya.robot.base.util.MusicUtil;
import com.bearya.robot.programme.R;
import com.bearya.robot.programme.adapter.ThemeAdapter;
import com.bearya.robot.programme.entity.ThemeEntity;
import com.bearya.robot.programme.repository.StationThemeRepository;
import com.bearya.robot.programme.walk.car.LoadMgr;
import com.chad.library.adapter.base.BaseQuickAdapter;

public class StationThemeActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    public static void start(Context context) {
        context.startActivity(new Intent(context, StationThemeActivity.class));
    }

    private ThemeAdapter themeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_theme);

        findViewById(R.id.btnBack).setOnClickListener(v -> {
            CityActivity.start(this);
            finish();
        });

        RecyclerView themeRecyclerView = findViewById(R.id.themes);
        themeRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        themeAdapter = new ThemeAdapter(StationThemeRepository.getInstance().themes);
        themeAdapter.setOnItemChildClickListener(this);
        themeAdapter.bindToRecyclerView(themeRecyclerView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MusicUtil.playAssetsBgMusic("tts/zh/by_city_bg.mp3");
    }

    @Override
    protected void onStop() {
        super.onStop();
        MusicUtil.stopBgMusic();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        ThemeEntity item = themeAdapter.getItem(position);
        assert item != null;
        int viewId = view.getId();
        if (viewId == R.id.travelGo) {
            MusicUtil.stopBgMusic();
            LoadMgr.getInstance().clearStationLoadPlayData();
            LoadMgr.getInstance().setTag(item.getTag());
            GameActivity.start(this);
            finish();
        } else if (viewId == R.id.cover_over) {
            StationThemeIntroduceActivity.start(this, item.getIntroduceCover());
        }
    }

}