package com.bearya.robot.programme.entity;

import android.util.Pair;

import com.bearya.robot.programme.walk.load.StationBlueLoad;
import com.bearya.robot.programme.walk.load.StationGreenLoad;
import com.bearya.robot.programme.walk.load.StationPinkLoad;
import com.bearya.robot.programme.walk.load.StationPurpleLoad;
import com.bearya.robot.programme.walk.load.StationRedLoad;
import com.bearya.robot.programme.walk.load.StationYellowLoad;

import java.util.ArrayList;
import java.util.List;

public class ThemeEntity {

    private final String theme;
    private final String cover;
    private final String introduceCover;
    private final String tag;

    private final List<Pair<String, String>> blueLoad;
    private final List<Pair<String, String>> redLoad;
    private final List<Pair<String, String>> greenLoad;
    private final List<Pair<String, String>> pinkLoad;
    private final List<Pair<String, String>> purpleLoad;
    private final List<Pair<String, String>> yellowLoad;

    public ThemeEntity(String theme, String tag, int blueCount, int redCount, int greenCount, int pinkCount, int purpleCount, int yellowCount) {

        this.theme = theme;
        this.tag = tag;

        cover = "file:///android_asset/theme/cover/" + tag + ".webp";
        introduceCover = "file:///android_asset/theme/introduce/" + tag + ".webp";

        blueLoad = new ArrayList<>();
        redLoad = new ArrayList<>();
        greenLoad = new ArrayList<>();
        pinkLoad = new ArrayList<>();
        purpleLoad = new ArrayList<>();
        yellowLoad = new ArrayList<>();

        generate(blueLoad, StationBlueLoad.STATION_INDEX, blueCount);
        generate(redLoad, StationRedLoad.STATION_INDEX, redCount);
        generate(greenLoad, StationGreenLoad.STATION_INDEX, greenCount);
        generate(pinkLoad, StationPinkLoad.STATION_INDEX, pinkCount);
        generate(purpleLoad, StationPurpleLoad.STATION_INDEX, purpleCount);
        generate(yellowLoad, StationYellowLoad.STATION_INDEX, yellowCount);

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

    private void generate(List<Pair<String, String>> loadData, int stationIndex, int count) {
        for (int i = 1; i <= count; i++) {
            String entity = tag + "_" + stationIndex + "_" + i;
            loadData.add(new Pair<>("theme/music/" + entity + ".mp3", "file:///android_asset/theme/face/" + entity + ".webp"));
        }
    }

    public List<Pair<String, String>> fetch(int stationIndex) {
        switch (stationIndex) {
            case StationBlueLoad.STATION_INDEX: return blueLoad;
            case StationRedLoad.STATION_INDEX: return redLoad;
            case StationGreenLoad.STATION_INDEX: return greenLoad;
            case StationPinkLoad.STATION_INDEX: return pinkLoad;
            case StationPurpleLoad.STATION_INDEX: return purpleLoad;
            case StationYellowLoad.STATION_INDEX: return yellowLoad;
            default: return new ArrayList<>();
        }
    }

}