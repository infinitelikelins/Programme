package com.bearya.robot.programme.walk.load;

import android.util.Pair;

import com.bearya.robot.base.BaseApplication;
import com.bearya.robot.base.play.Director;
import com.bearya.robot.base.play.FacePlay;
import com.bearya.robot.base.play.FaceType;
import com.bearya.robot.base.play.LoadPlay;
import com.bearya.robot.base.play.PlayData;
import com.bearya.robot.programme.data.Tag;
import com.bearya.robot.programme.entity.ThemeEntity;
import com.bearya.robot.programme.repository.CityRecordRepository;
import com.bearya.robot.programme.repository.StationThemeRepository;
import com.bearya.robot.programme.station.StationConfigActivity;
import com.bearya.robot.programme.walk.car.LoadMgr;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class StationLoad extends XLoad {

    private final List<PlayData> stationOuterPlay = new ArrayList<>();
    private final Random random;

    public StationLoad(int startOid) {
        super(startOid);
        random = new Random();
    }

    public abstract int getStationIndex();

    @Override
    public void registerPlay() {
        LoadPlay play = new LoadPlay();
        String tag = LoadMgr.getInstance().getTag();
        PlayData data = (tag.equals(Tag.TAG_WORLD) || tag.equals(Tag.TAG_HOME) || tag.equals(Tag.TAG_LIBRARY) ||
                tag.equals(Tag.TAG_SCHOOL) || tag.equals(Tag.TAG_ZOO) || tag.equals(Tag.TAG_PARK)) ? innerPlay() : outerPlay();
        if (!data.isEmpty()) {
            play.addLoad(data);
            CityRecordRepository.getInstance().put(data);
        }
        play.addLoad(new PlayData("tts/zh/crossroads.mp3", new FacePlay("y", FaceType.Lottie)));
        Director.getInstance().register(ON_NEW_LOAD, play);
    }

    private PlayData innerPlay() {
        PlayData playData = StationConfigActivity.getLastConfigStation(BaseApplication.getInstance(), getStationIndex());
        if (!playData.isEmpty() && playData.facePlay == null) {
            playData.facePlay = new FacePlay("hg", FaceType.Lottie);
        }
        return playData;
    }

    /**
     * 外部定制主题播放的动画
     */
    private PlayData outerPlay() {
        if (stationOuterPlay.size() == 0) {
            ThemeEntity themeEntity = StationThemeRepository.getInstance().getThemeEntity(LoadMgr.getInstance().getTag());
            if (themeEntity != null) {
                List<Pair<String, String>> fetch = themeEntity.fetch(getStationIndex());
                for (Pair<String, String> entity : fetch) {
                    stationOuterPlay.add(new PlayData(entity.first, new FacePlay(entity.second, FaceType.Image)));
                }
            }
        }
        return stationOuterPlay.size() == 0 ? new PlayData() : stationOuterPlay.remove(random.nextInt(stationOuterPlay.size()));
    }

    public void clearOuterPlayData() {
        stationOuterPlay.clear();
    }

}