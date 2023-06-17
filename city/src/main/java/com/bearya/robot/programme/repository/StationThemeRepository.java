package com.bearya.robot.programme.repository;

import android.text.TextUtils;

import com.bearya.robot.programme.entity.ThemeEntity;

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
            new ThemeEntity("红色印迹", "revolution", 1, 1, 1, 1, 1, 1),
            new ThemeEntity("绿色环保·健康生活", "protection", 3, 3, 3, 3, 3, 3)
    );

    public final ThemeEntity getThemeEntity(String tag) {
        for (ThemeEntity theme : themes) {
            if (TextUtils.equals(theme.getTag() ,tag))
                return theme;
        }
        return null;
    }

}