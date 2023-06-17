package com.bearya.robot.programme.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.bearya.robot.base.ui.BaseActivity;
import com.bearya.robot.base.util.MusicUtil;
import com.bearya.robot.programme.R;
import com.bearya.robot.programme.adapter.IntroductionAdapter;
import com.bearya.robot.programme.entity.IntroductionEntity;
import com.bearya.robot.programme.repository.IntroductionRepository;

import java.util.Arrays;

public class StationThemeIntroduceActivity extends BaseActivity {

    public static void start(Context context, String introduce, String audio, String bgm) {
        context.startActivity(new Intent(context, StationThemeIntroduceActivity.class)
                .putExtra("introduce", introduce)
                .putExtra("introduceAudio", audio)
                .putExtra("introduceBgm", bgm));
    }

    private String bgm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_theme_introduce);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        String themeTagName = getIntent().getStringExtra("introduce");
        String audio = getIntent().getStringExtra("introduceAudio");
        bgm = getIntent().getStringExtra("introduceBgm");

        RecyclerView introductions = findViewById(R.id.introductions);
        IntroductionEntity[] entities = IntroductionRepository.getInstance().fetch(themeTagName);
        IntroductionAdapter adapter = new IntroductionAdapter(Arrays.asList(entities));
        adapter.setOnItemChildClickListener((adapter1, view, position) -> {

        });
        introductions.setAdapter(adapter);

        MusicUtil.playAssetsAudio(audio, mediaPlayer -> MusicUtil.playAssetsBgMusic(bgm));

    }

    @Override
    protected void onResume() {
        super.onResume();
        MusicUtil.playAssetsBgMusic(bgm);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MusicUtil.stopMusic();
        MusicUtil.stopBgMusic();
    }

}