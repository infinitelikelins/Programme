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

public class XLoad extends BaseLoad {
    public static final int START_OID = 3700;
    public static final String NAME = "十字路";

    public XLoad() {
        this(START_OID);
    }

    public XLoad(int startOid) {
        super(startOid);
        supportEntranceOid = new int[]{leftEntranceOid, topEntranceOid, rightEntranceOid, bottomEntranceOid};
        addLoadRect(new Rect(BORDER, 0, COLUMN - BORDER, ROW));
        addLoadRect(new Rect(0, BORDER, COLUMN, ROW - BORDER));
        lock = new DirectLock(Direct.values());
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
                if (entranceOid == topEntranceOid) {
                    return new int[]{getCenterOid(), bottomEntranceOid};
                } else if (entranceOid == bottomEntranceOid) {
                    return new int[]{getCenterOid(), topEntranceOid};
                } else if (entranceOid == leftEntranceOid) {
                    return new int[]{getCenterOid(), rightEntranceOid};
                } else if (entranceOid == rightEntranceOid) {
                    return new int[]{getCenterOid(), leftEntranceOid};
                }
                break;
            case Backward:
                if (entranceOid == topEntranceOid) {
                    return new int[]{getCenterOid(), topEntranceOid};
                } else if (entranceOid == bottomEntranceOid) {
                    return new int[]{getCenterOid(), bottomEntranceOid};
                } else if (entranceOid == leftEntranceOid) {
                    return new int[]{getCenterOid(), leftEntranceOid};
                } else if (entranceOid == rightEntranceOid) {
                    return new int[]{getCenterOid(), rightEntranceOid};
                }
                break;
            case Left:
                if (entranceOid == topEntranceOid) {
                    return new int[]{getCenterOid(), rightEntranceOid};
                } else if (entranceOid == bottomEntranceOid) {
                    return new int[]{getCenterOid(), leftEntranceOid};
                } else if (entranceOid == leftEntranceOid) {
                    return new int[]{getCenterOid(), topEntranceOid};
                } else if (entranceOid == rightEntranceOid) {
                    return new int[]{getCenterOid(), bottomEntranceOid};
                }
                break;
            case Right:
                if (entranceOid == topEntranceOid) {
                    return new int[]{getCenterOid(), leftEntranceOid};
                } else if (entranceOid == bottomEntranceOid) {
                    return new int[]{getCenterOid(), rightEntranceOid};
                } else if (entranceOid == leftEntranceOid) {
                    return new int[]{getCenterOid(), bottomEntranceOid};
                } else if (entranceOid == rightEntranceOid) {
                    return new int[]{getCenterOid(), topEntranceOid};
                }
                break;
        }
        return null;
    }

    @Override
    public void registerPlay() {
        registerOnNewLoadPlay();
        registerOnUnlockSuccessPlay();
        registerOnTimeoutPlay();
        registerOnMaxTimeOverPlay();
    }

    private void registerOnUnlockSuccessPlay() {
        LoadPlay unlockSuccessPlay = new LoadPlay();
        unlockSuccessPlay.addLoad(new PlayData("tts/zh/%s.mp3", new FacePlay("kx", FaceType.Lottie)));
        unlockSuccessPlay.playAction = Command.format(Command.CITY_ROAD_SELECT_OK);
        Director.getInstance().register(ON_UNLOCK_SUCCESS, unlockSuccessPlay);
    }

    private void registerOnNewLoadPlay() {
        LoadPlay newLoadPlay = new LoadPlay();
        newLoadPlay.addLoad(new PlayData("tts/zh/crossroads.mp3", new FacePlay("y", FaceType.Lottie)));
        newLoadPlay.playAction = Command.format(Command.CITY_ROAD_X);
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
