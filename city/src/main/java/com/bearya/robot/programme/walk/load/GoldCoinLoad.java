package com.bearya.robot.programme.walk.load;

import com.bearya.robot.base.play.Director;
import com.bearya.robot.base.play.FacePlay;
import com.bearya.robot.base.play.FaceType;
import com.bearya.robot.base.play.LoadPlay;
import com.bearya.robot.base.play.PlayData;
import com.bearya.robot.programme.R;
import com.bearya.robot.programme.data.Command;
import com.bearya.robot.programme.walk.car.LoadMgr;
import com.bearya.robot.programme.walk.load.lock.DirectorPlayLock;

import java.util.Locale;

/**
 * 金币
 */
public class GoldCoinLoad extends StraightLoad {

    public static final int START_OID = 7300;
    public static final String NAME = "金币";

    public GoldCoinLoad() {
        super(START_OID);
        lock = new DirectorPlayLock();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void registerPlay() {
        LoadPlay unlockSuccessPlay = new LoadPlay();

        unlockSuccessPlay.addLoad(new PlayData("tts/zh/gold_effect.mp3", new FacePlay(String.valueOf(R.array.gold_coin), FaceType.Arrays)));
        unlockSuccessPlay.addLoad(new PlayData("tts/zh/pick_up_coin1.mp3"));
        int gold = LoadMgr.getInstance().getGoldNumber();
        unlockSuccessPlay.addLoad(new PlayData(String.format(Locale.CHINA, "tts/zh/%d.mp3", Math.min(gold, 50))));
        unlockSuccessPlay.addLoad(new PlayData(gold > 1 ? "tts/zh/pick_up_coins2.mp3" : "tts/zh/pick_up_coin2.mp3"));

        unlockSuccessPlay.playAction = Command.format(Command.CITY_GOLD, Math.min(gold, 50));

        Director.getInstance().register(ON_NEW_LOAD, unlockSuccessPlay);
    }
}