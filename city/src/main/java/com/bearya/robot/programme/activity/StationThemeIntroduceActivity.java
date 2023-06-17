package com.bearya.robot.programme.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;

import com.bearya.robot.base.ui.BaseActivity;
import com.bearya.robot.base.util.MusicUtil;
import com.bearya.robot.programme.R;
import com.bumptech.glide.Glide;

public class StationThemeIntroduceActivity extends BaseActivity {

    public static void start(Context context, String introduceCover, String audio, String bgm) {
        context.startActivity(new Intent(context, StationThemeIntroduceActivity.class)
                .putExtra("introduceCover", introduceCover)
                .putExtra("introduceAudio", audio)
                .putExtra("introduceBgm", bgm));
    }

    private String bgm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_theme_introduce);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        String theme = getIntent().getStringExtra("introduceCover");
        String audio = getIntent().getStringExtra("introduceAudio");
        bgm = getIntent().getStringExtra("introduceBgm");

        AppCompatImageView imageView = findViewById(R.id.introduce);

        Glide.with(this).load(theme).into(imageView);

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