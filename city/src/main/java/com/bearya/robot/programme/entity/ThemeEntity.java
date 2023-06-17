package com.bearya.robot.programme.entity;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThemeEntity {

    private final String theme;
    private final String cover;
    private final String introduceCover;
    private final String tag;

    private final Map<String, List<Pair<String, String>>> loads = new HashMap<>();

    public ThemeEntity(String theme, String tag) {

        this.theme = theme;
        this.tag = tag;

        cover = "file:///android_asset/theme/cover/" + tag + ".webp";
        introduceCover = "file:///android_asset/theme/introduce/" + tag + ".webp";

    }

    public String getTag() {
        return tag;
    }

    public String getTheme() {
        return theme;
    }

    public String getCover() {
        return cover;
    }

    public String getIntroduceCover() {
        return introduceCover;
    }

    protected void generate(int stationIndex, int count) {
        List<Pair<String, String>> loadData = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            String entity = tag + "_" + stationIndex + "_" + i;
            loadData.add(new Pair<>("theme/music/" + entity + ".mp3", "file:///android_asset/theme/face/" + entity + ".webp"));
        }
        loads.put("station_" + stationIndex, loadData);
    }

    /**
     * 这里可以更多的小站数据
     */
    protected void moreStation() {

    }

    public List<Pair<String, String>> fetch(int stationIndex) {
        List<Pair<String, String>> pairs = loads.get("station_" + stationIndex);
        return pairs == null ? new ArrayList<>() : pairs;
    }

}