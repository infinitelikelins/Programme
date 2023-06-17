package com.bearya.robot.base.play;


import java.util.ArrayList;
import java.util.List;

public class LoadPlay {
    private final List<PlayData> playDataList;

    public String playAction;

    public LoadPlay() {
        playDataList = new ArrayList<>();
    }

    public LoadPlay(LoadPlay data) {
        playDataList = new ArrayList<>();
        playDataList.addAll(data.playDataList);
        playAction = data.playAction;
    }

    public void addLoad(PlayData data) {
        playDataList.add(data);
    }

    public PlayData getPlay() {
        return !isComplete() ? playDataList.remove(0) : null;
    }

    public boolean isComplete() {
        return playDataList.isEmpty();
    }

}