package com.bearya.robot.programme.repository;

import android.text.TextUtils;

import com.bearya.robot.programme.entity.ThemeEntity;
import com.bearya.robot.programme.walk.load.StationBlueLoad;
import com.bearya.robot.programme.walk.load.StationGreenLoad;
import com.bearya.robot.programme.walk.load.StationPinkLoad;
import com.bearya.robot.programme.walk.load.StationPurpleLoad;
import com.bearya.robot.programme.walk.load.StationRedLoad;
import com.bearya.robot.programme.walk.load.StationYellowLoad;

import java.util.Arrays;
import java.util.List;

public class StationThemeRepository {

    private static StationThemeRepository repository;

    private StationThemeRepository() {

    }

    public static StationThemeRepository getInstance() {
        if (repository == null) {
            repository = new StationThemeRepository();
        }
        return repository;
    }

    public final List<ThemeEntity> themes = Arrays.asList(
            new ThemeEntity("红色印迹", "revolution", "theme/dub/revolution.mp3", "tts/zh/by_city_bg.mp3") {
                @Override
                protected void onBuild() {
                    generate(StationBlueLoad.STATION_INDEX, 1);
                    generate(StationRedLoad.STATION_INDEX, 1);
                    generate(StationGreenLoad.STATION_INDEX, 1);
                    generate(StationPinkLoad.STATION_INDEX, 1);
                    generate(StationPurpleLoad.STATION_INDEX, 1);
                    generate(StationYellowLoad.STATION_INDEX, 1);
                }
            },
            new ThemeEntity("绿色环保·健康生活", "protection", "theme/dub/protection.mp3", "tts/zh/by_city_bg.mp3") {
                @Override
                protected void onBuild() {
                    generate(StationBlueLoad.STATION_INDEX, 3);
                    generate(StationRedLoad.STATION_INDEX, 3);
                    generate(StationGreenLoad.STATION_INDEX, 3);
                    generate(StationPinkLoad.STATION_INDEX, 3);
                    generate(StationPurpleLoad.STATION_INDEX, 3);
                    generate(StationYellowLoad.STATION_INDEX, 3);
                }
            }
    );

    public final ThemeEntity getThemeEntity(String tag) {
        for (ThemeEntity theme : themes) {
            if (TextUtils.equals(theme.getTag(), tag))
                return theme;
        }
        return null;
    }

}