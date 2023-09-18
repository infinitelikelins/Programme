package com.bearya.robot.programme.entity;

import android.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class ThemeEntity implements Serializable {

    /**
     * 主题名称
     */
    private final String theme;
    /**
     * 主题封面
     */
    private final String cover;
    /**
     * 主题介绍音频
     */
    private final String audio;
    /**
     * 主题介绍页面的背景音乐
     */
    private final String bgm;
    private final String introduceCover;
    /**
     * 主题英文标签
     */
    private final String tag;

    private final HashMap<String, List<Pair<String, String>>> loads = new HashMap<>();

    public ThemeEntity(String theme, String tag, String audio,String bgm) {

        this.theme = theme;
        this.tag = tag;
        this.audio = audio;
        this.bgm = bgm;

        cover = "file:///android_asset/theme/cover/" + tag + ".webp";
        introduceCover = "file:///android_asset/theme/introduce/" + tag + ".webp";

        onBuild();

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

    public String getAudio() {
        return audio;
    }

    public String getBgm() {
        return bgm;
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
     * 修建更多的小站道路的数据
     */
    protected abstract void onBuild() ;

    public List<Pair<String, String>> fetch(int stationIndex) {
        List<Pair<String, String>> pairs = loads.get("station_" + stationIndex);
        return pairs == null ? new ArrayList<>() : pairs;
    }

}