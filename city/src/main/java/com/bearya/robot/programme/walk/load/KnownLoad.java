package com.bearya.robot.programme.walk.load;

import android.util.Pair;

import com.bearya.robot.base.play.Director;
import com.bearya.robot.base.play.FacePlay;
import com.bearya.robot.base.play.FaceType;
import com.bearya.robot.base.play.LoadPlay;
import com.bearya.robot.base.play.PlayData;
import com.bearya.robot.programme.data.Command;
import com.bearya.robot.programme.entity.PatriotismContent;
import com.bearya.robot.programme.repository.CityRecordRepository;
import com.bearya.robot.programme.repository.PatriotismRepository;
import com.bearya.robot.programme.walk.load.lock.DirectorPlayLock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class KnownLoad extends StraightLoad {

    private static final Map<String, List<PlayData>> playDataMap = new HashMap<>();
    private final Random random;

    public KnownLoad(int startOid) {
        super(startOid);
        lock = new DirectorPlayLock();
        random = new Random();
        playDataMap.put(themeName(), new ArrayList<PlayData>());
    }

    @Override
    public void registerPlay() {

        LoadPlay newLoadPlay = new LoadPlay();

        List<PlayData> pairs = playDataMap.get(themeName());

        if (pairs != null && pairs.size() == 0) {
            for (PatriotismContent patriotismContent : PatriotismRepository.getInstance().getPatriotismContents(themeName())) {
                if (PatriotismRepository.getInstance().get(patriotismContent.getContentName())) {
                    for (Pair<String, String> contentPair : patriotismContent.getContent()) {
                        pairs.add(new PlayData(contentPair.first, new FacePlay(contentPair.second, FaceType.Image)));
                    }
                }
            }
        }
        if (pairs != null && pairs.size() > 0) {
            PlayData playData = pairs.remove(random.nextInt(pairs.size()));
            newLoadPlay.addLoad(playData);
            CityRecordRepository.getInstance().put(playData);
            String action = playData.sound.replaceAll("patriotism/(.*)/(.*).mp3","$1_$2");
            newLoadPlay.playAction = Command.format(Command.CITY_KNOWN, action);
        }

        Director.getInstance().register(ON_NEW_LOAD, newLoadPlay);

    }

    protected abstract String themeName();

    /**
     * 当配置更变的时候，需要清理知识地垫的内存配置
     */
    public static void clearKnownPlayData() {
        if (playDataMap.size() > 0) {
            for (List<PlayData> value : playDataMap.values()) {
                if (value.size() > 0) {
                    value.clear();
                }
            }
        }
    }

}