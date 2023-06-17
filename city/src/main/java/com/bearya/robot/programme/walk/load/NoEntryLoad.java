package com.bearya.robot.programme.walk.load;

import com.bearya.robot.base.play.Director;
import com.bearya.robot.base.play.FacePlay;
import com.bearya.robot.base.play.FaceType;
import com.bearya.robot.base.play.LoadPlay;
import com.bearya.robot.base.play.PlayData;
import com.bearya.robot.base.walk.Direct;
import com.bearya.robot.base.walk.RobotInLoadMethod;
import com.bearya.robot.programme.R;
import com.bearya.robot.programme.data.Command;
import com.bearya.robot.programme.walk.load.lock.DirectorPlayLock;

/**
 * 禁止通行
 */
public class NoEntryLoad extends XLoad {

    public static final int START_OID = 6400;
    public static final String NAME = "禁止通行";

    public NoEntryLoad() {
        super(START_OID);
        lock = new DirectorPlayLock();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int[] getExitOid(int entranceOid, Direct direct, RobotInLoadMethod method) {
        int dis = ROW * 8;
        if (entranceOid == topEntranceOid) {
            return new int[]{getCenterOid(), getCenterOid() + dis};
        } else if (entranceOid == bottomEntranceOid) {
            return new int[]{getCenterOid(), getCenterOid() - dis};
        } else if (entranceOid == leftEntranceOid) {
            return new int[]{getCenterOid(), getCenterOid() + 8};
        } else if (entranceOid == rightEntranceOid) {
            return new int[]{getCenterOid(), getCenterOid() - 8};
        }
        return null;
    }

    @Override
    public void registerPlay() {
        LoadPlay newLoadPlay = new LoadPlay();
        PlayData playData = new PlayData();
        playData.facePlay = new FacePlay(String.valueOf(R.mipmap.no_entry), FaceType.Image);
        playData.sound = "tts/zh/mission_fail_5.mp3";
        newLoadPlay.addLoad(playData);
        newLoadPlay.playAction = Command.format(Command.CITY_ROAD_NO);
        Director.getInstance().register(ON_NEW_LOAD, newLoadPlay);
    }

}