package com.bearya.robot.programme.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bearya.robot.base.ui.BaseActivity;
import com.bearya.robot.base.util.MusicUtil;
import com.bearya.robot.programme.R;
import com.bearya.robot.programme.adapter.IntroductionAdapter;
import com.bearya.robot.programme.entity.IntroductionEntity;
import com.bearya.robot.programme.entity.ThemeEntity;
import com.bearya.robot.programme.repository.IntroductionRepository;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.Arrays;

public class StationThemeIntroduceActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {

    public static void start(Context context, ThemeEntity item) {
        context.startActivity(new Intent(context, StationThemeIntroduceActivity.class)
                .putExtra("name", item.getTheme())
                .putExtra("bgm", item.getBgm())
                .putExtra("audio", item.getAudio())
                .putExtra("tag", item.getTag()));
    }

    private String bgm;

    private IntroductionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_theme_introduce);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        String themeName = getIntent().getStringExtra("name");
        String audio = getIntent().getStringExtra("audio");
        String tag = getIntent().getStringExtra("tag");
        bgm = getIntent().getStringExtra("bgm");

        TextView themeTextView = findViewById(R.id.theme_name);
        themeTextView.setText(" \" " + themeName + " \" 主题介绍");

        RecyclerView introductions = findViewById(R.id.introductions);
        IntroductionEntity[] entities = IntroductionRepository.getInstance().fetch(tag);
        adapter = new IntroductionAdapter(Arrays.asList(entities));
        adapter.setOnItemClickListener(this);
        introductions.setAdapter(adapter);

        if (!TextUtils.isEmpty(audio)) {
            MusicUtil.playAssetsAudio(audio, mediaPlayer -> MusicUtil.playAssetsBgMusic(bgm));
        }

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

    @Override
    public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
        IntroductionEntity item = adapter.getItem(position);
        if (item != null) {
            switch (item.getType()) {
                case IntroductionEntity.AUDIO:
                    if (!TextUtils.isEmpty(item.getFileDir())) {
                        MusicUtil.playAssetsAudio(item.getFileDir(), mp -> MusicUtil.playAssetsBgMusic(bgm));
                    }
                    break;
                case IntroductionEntity.VIDEO:
                    VideoActivity.start(this, item.getFileDir());
                    break;
            }
        }
    }

}