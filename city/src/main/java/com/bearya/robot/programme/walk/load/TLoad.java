package com.bearya.robot.programme.walk.load;

import android.graphics.Rect;

import com.bearya.robot.base.load.BaseLoad;
import com.bearya.robot.base.play.Director;
import com.bearya.robot.base.play.FacePlay;
import com.bearya.robot.base.play.FaceType;
import com.bearya.robot.base.play.LoadPlay;
import com.bearya.robot.base.play.PlayData;
import com.bearya.robot.base.walk.Direct;
import com.bearya.robot.base.walk.RobotInLoadMethod;
import com.bearya.robot.programme.data.Command;
import com.bearya.robot.programme.walk.load.lock.DirectLock;

public class TLoad extends BaseLoad {
    public static final int START_OID = 4600;
    public static final String NAME = "T字路";

    public TLoad() {
        super(START_OID);
        supportEntranceOid = new int[]{leftEntranceOid, rightEntranceOid, bottomEntranceOid};
        closeEntranceOid = new int[]{topEntranceOid};
        addLoadRect(new Rect(0, BORDER, COLUMN, ROW - BORDER));
        addLoadRect(new Rect(BORDER, ROW - BORDER, COLUMN - BORDER, ROW));
        lock = new DirectLock(Direct.Left, Direct.Right, Direct.Backward);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int[] getExitOid(int entranceOid, Direct direct, RobotInLoadMethod method) {
        if (method == RobotInLoadMethod.USER_PUT) {
            if (getRobotInLoadDirect().getFaceDirect() == Direct.Backward) {
                entranceOid = topEntranceOid;
            } else if (getRobotInLoadDirect().getFaceDirect() == Direct.Left) {
                entranceOid = rightEntranceOid;
            } else if (getRobotInLoadDirect().getFaceDirect() == Direct.Right) {
                entranceOid = leftEntranceOid;
            } else if (getRobotInLoadDirect().getFaceDirect() == Direct.Forward) {
                entranceOid = bottomEntranceOid;
            }
        }
        switch (direct) {
            case Forward:
                if (entranceOid == leftEntranceOid) {
                    return new int[]{getCenterOid(), rightEntranceOid};
                } else if (entranceOid == rightEntranceOid) {
                    return new int[]{getCenterOid(), leftEntranceOid};
                } else if (entranceOid == topEntranceOid) {
                    return new int[]{getCenterOid(), bottomEntranceOid};
                }
                break;
            case Backward:
                if (entranceOid == leftEntranceOid) {
                    return new int[]{getCenterOid(), leftEntranceOid};
                } else if (entranceOid == bottomEntranceOid) {
                    return new int[]{getCenterOid(), bottomEntranceOid};
                } else if (entranceOid == rightEntranceOid) {
                    return new int[]{getCenterOid(), rightEntranceOid};
                }
                break;
            case Left:
                if (entranceOid == bottomEntranceOid) {
                    return new int[]{getCenterOid(), leftEntranceOid};
                } else if (entranceOid == rightEntranceOid) {
                    return new int[]{getCenterOid(), bottomEntranceOid};
                } else if (entranceOid == topEntranceOid) {
                    return new int[]{getCenterOid(), rightEntranceOid};
                }
                break;
            case Right:
                if (entranceOid == leftEntranceOid) {
                    return new int[]{getCenterOid(), bottomEntranceOid};
                } else if (entranceOid == bottomEntranceOid) {
                    return new int[]{getCenterOid(), rightEntranceOid};
                } else if (entranceOid == topEntranceOid) {
                    return new int[]{getCenterOid(), leftEntranceOid};
                }
                break;
        }
        return new int[0];
    }

    @Override
    public void registerPlay() {
        registerOnNewLoadPlay();
        registerOnUnlockSuccessPlay();
        registerOnUnlockFailPlay();
        registerOnTimeoutPlay();
        registerOnMaxTimeOverPlay();
    }

    private void registerOnUnlockSuccessPlay() {
        LoadPlay unlockSuccessPlay = new LoadPlay();
        unlockSuccessPlay.addLoad(new PlayData("tts/zh/%s.mp3", new FacePlay("kx", FaceType.Lottie)));
        unlockSuccessPlay.playAction = Command.format(Command.CITY_ROAD_SELECT_OK);
        Director.getInstance().register(ON_UNLOCK_SUCCESS, unlockSuccessPlay);
    }

    private void registerOnUnlockFailPlay() {
        LoadPlay unlockSuccessPlay = new LoadPlay();
        unlockSuccessPlay.addLoad(new PlayData("tts/zh/%s.mp3", new FacePlay("ng", FaceType.Lottie)));
        unlockSuccessPlay.playAction = Command.format(Command.CITY_ROAD_SELECT_ERROR);
        Director.getInstance().register(ON_UNLOCK_FAIL, unlockSuccessPlay);
    }

    private void registerOnNewLoadPlay() {
        LoadPlay newLoadPlay = new LoadPlay();
        newLoadPlay.addLoad(new PlayData("tts/zh/crossroads.mp3", new FacePlay("y", FaceType.Lottie)));
        newLoadPlay.playAction = Command.format(Command.CITY_ROAD_T);
        Director.getInstance().register(ON_NEW_LOAD, newLoadPlay);
    }

    private void registerOnTimeoutPlay() {
        LoadPlay newLoadPlay = new LoadPlay();
        newLoadPlay.addLoad(new PlayData("tts/zh/timeout_tip.mp3", new FacePlay("zm", FaceType.Lottie)));
        newLoadPlay.playAction = Command.format(Command.CITY_LOCK_TIMEOUT);
        Director.getInstance().register(ON_LOCK_TIMEOUT, newLoadPlay);
    }

    private void registerOnMaxTimeOverPlay() {
        LoadPlay unlockSuccessPlay = new LoadPlay();
        unlockSuccessPlay.addLoad(new PlayData("tts/zh/max_time_over.mp3", new FacePlay("axy", FaceType.Lottie)));
        unlockSuccessPlay.playAction = Command.format(Command.CITY_LOCK_MAX_TIMEOUT);
        Director.getInstance().register(ON_LOCK_MAX_TIMEOUT, unlockSuccessPlay);
    }

}
