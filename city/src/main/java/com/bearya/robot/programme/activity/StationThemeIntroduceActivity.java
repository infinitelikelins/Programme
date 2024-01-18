package com.bearya.robot.programme.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.recyclerview.widget.RecyclerView;

import com.bearya.robot.base.ui.BaseActivity;
import com.bearya.robot.base.util.MusicUtil;
import com.bearya.robot.programme.R;
import com.bearya.robot.programme.adapter.IntroductionAdapter;
import com.bearya.robot.programme.entity.IntroductionEntity;
import com.bearya.robot.programme.entity.ThemeEntity;
import com.bearya.robot.programme.repository.IntroductionRepository;

import java.util.Arrays;

public class StationThemeIntroduceActivity extends BaseActivity {

    public static void start(Context context, ThemeEntity item) {
        context.startActivity(new Intent(context, StationThemeIntroduceActivity.class)
                .putExtra("bgm", item.getBgm())
                .putExtra("audio", item.getAudio())
                .putExtra("tag", item.getTag()));
    }

    private String bgm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_theme_introduce);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        String audio = getIntent().getStringExtra("audio");
        String tag = getIntent().getStringExtra("tag");
        bgm = getIntent().getStringExtra("bgm");

        RecyclerView introductions = findViewById(R.id.introductions);
        IntroductionEntity[] entities = IntroductionRepository.getInstance().fetch(tag);
        IntroductionAdapter adapter = new IntroductionAdapter(Arrays.asList(entities));
        introductions.setAdapter(adapter);

        if (!TextUtils.isEmpty(audio)) {
            MusicUtil.playAssetsAudio(audio, mediaPlayer -> MusicUtil.playAssetsBgMusic(bgm));
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        MusicUtil.stopMusic();
    }
}