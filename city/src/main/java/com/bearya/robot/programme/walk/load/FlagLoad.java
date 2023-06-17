package com.bearya.robot.programme.walk.load;

import com.bearya.robot.base.play.Director;
import com.bearya.robot.base.play.FacePlay;
import com.bearya.robot.base.play.FaceType;
import com.bearya.robot.base.play.LoadPlay;
import com.bearya.robot.base.play.PlayData;
import com.bearya.robot.programme.R;
import com.bearya.robot.programme.data.Command;
import com.bearya.robot.programme.walk.load.lock.DirectorPlayLock;

public class FlagLoad extends StraightLoad {

    public static final int START_OID = 50700;
    public static final String NAME = "红旗";

    private final String[] sounds = new String[]{
            "flag_effect_1", "flag_effect_2", "flag_effect_3",
            "flag_effect_4", "flag_effect_5"
    };

    private int soundPlayIndex = 0;

    public FlagLoad() {
        super(START_OID);
        lock = new DirectorPlayLock();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void registerPlay() {
        LoadPlay flagPlay = new LoadPlay();
        String sound = sounds[soundRandom()];
        flagPlay.addLoad(new PlayData("tts/zh/" + sound + ".mp3", new FacePlay(String.valueOf(R.array.flag), FaceType.Arrays)));
        flagPlay.playAction = Command.format(Command.CITY_FLAG, sound);
        Director.getInstance().register(ON_NEW_LOAD, flagPlay);
    }

    private int soundRandom() {
        if (soundPlayIndex >= sounds.length) {
            soundPlayIndex = 0;
        }
        return soundPlayIndex++;
    }

}