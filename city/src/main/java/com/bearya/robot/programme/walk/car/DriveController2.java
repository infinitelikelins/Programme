package com.bearya.robot.programme.walk.car;

import com.bearya.actionlib.utils.RobotActionManager;
import com.bearya.robot.base.BaseApplication;
import com.bearya.robot.base.load.BaseLoad;
import com.bearya.robot.base.play.AnimationsContainer;
import com.bearya.robot.base.play.Director;
import com.bearya.robot.base.protocol.DriveResult;
import com.bearya.robot.base.protocol.Key;
import com.bearya.robot.base.util.DebugUtil;
import com.bearya.robot.base.util.MusicUtil;
import com.bearya.robot.base.walk.Direct;
import com.bearya.robot.base.walk.LoadEntrance;
import com.bearya.robot.base.walk.RobotInLoadMethod;
import com.bearya.robot.programme.CityApplication;
import com.bearya.robot.programme.data.Command;
import com.bearya.robot.programme.walk.car.travepid.ExceptionStatus;
import com.bearya.robot.programme.walk.car.travepid.IStatusAtion;
import com.bearya.robot.programme.walk.car.travepid.OidInfo;
import com.bearya.robot.programme.walk.car.travepid.StartStatus;
import com.bearya.robot.programme.walk.car.travepid.TravelStatus;
import com.bearya.robot.programme.walk.car.travepid.UnLockingStatus;

/**
 * 1.前读头进入新地垫
 */
public class DriveController2 {

    private final Engine engine;

    public ICar.DriveListener mListener;
    public boolean isMoving;
    private Key mKey = null;
    public OidInfo headInfo = new OidInfo("head");
    public OidInfo tailInfo = new OidInfo("tail");
    public BaseLoad currentLoad;
    private IStatusAtion mStatus = null;
    private final IStatusAtion statusTravel;
    private final IStatusAtion statusException;
    private final IStatusAtion statusUnLocking;
    private final IStatusAtion statusStart;

    public DriveController2() {
        statusTravel = new TravelStatus(this);
        statusException = new ExceptionStatus(this);
        statusUnLocking = new UnLockingStatus(this);
        statusStart = new StartStatus(this);

        currentLoad = null;
        isMoving = true;
        engine = new Engine() {
            @Override
            public void update() {
                mStatus.update();
            }
        };
    }

    public boolean isInOneLoad() {
        int distance = -1;
        boolean headnear = headInfo.isNearPoint();
        boolean ret = false;
        if (tailInfo.getLoad() == headInfo.getLoad()) {
            distance = tailInfo.getLoad().distince(headInfo.getOid(), tailInfo.getOid());
            if (distance > 6 && distance < 10) { //前后读头在同一张地垫上
                if (headnear) {
                    ret = true;
                }
            }
        }
        DebugUtil.debug("isInOneLoad=%s %d headnear=%s", String.valueOf(ret), distance, String.valueOf(headnear));
        return ret;
    }

    public void towOidEmpty() {

    }

    public void onOidEmpty() {

    }

    public void oidFull() {

    }

    public void onNewLoad(BaseLoad load) {
        Director.getInstance().reset();
        LoadMgr.getInstance().addHistory(new LoadEntrance(headInfo.getLoad().getName(), headInfo.getEntranceoid(), headInfo.getExitoid()));
        currentLoad = load;
        if (currentLoad.hasLock()) {
            stopMove();
            setStatus(statusUnLocking);
        } else {
            setComputeExitPathState("newLoad not lock");
        }
    }

    public void setComputeExitPathState(String newLoad_not_lock) {
        if (!isMoving) {
            moving();
        }
        setStatus(statusTravel);
        DebugUtil.debug("setComputeExitPathState %s", newLoad_not_lock);
    }

    private void setExitLoadState() {
        if (!isMoving) {
            moving();
        }
        setStatus(statusTravel);
        DebugUtil.debug("setExitLoadState");
    }

    public RobotInLoadMethod getRobotInLoadMethod() {
        return currentLoad == null ? RobotInLoadMethod.USER_PUT : RobotInLoadMethod.DRIVE;
    }

    public void setArriveTargetState() {
        DebugUtil.debug("setArriveTargetState");

        BaseLoad load = headInfo.getLoad();
        final DriveResult result = load.onResult(getRobotInLoadMethod());
        if (result == DriveResult.EndLoad || result == DriveResult.PatriotismEndLoad) {
            pause();
            load.registerPlay();
            Director.getInstance().director(BaseLoad.ON_END_LOAD, () -> {
                if (mListener != null) {
                    mListener.onDriveResult(result);
                }
            });
        } else {
            setExitLoadState();
        }
    }

    private void setStatus(IStatusAtion s) {
        if (mStatus != s) {
            if (mStatus != null) {
                mStatus.reset();
                DebugUtil.debug("reset %s", mStatus.getClass().getSimpleName());
            }
            mStatus = s;
            mStatus.start();
            DebugUtil.debug("start %s", mStatus.getClass().getSimpleName());
        }
    }

    public void setListener(ICar.DriveListener listener) {
        this.mListener = listener;
    }

    public void updateRobotInLoadDirect() {
        BaseLoad load = headInfo.getLoad();
        load.updateRobotInLoadDirect(headInfo.getOid(), tailInfo.getOid());
        Direct d = load.getRobotInLoadDirect().getEntranceDirect();
        int enid = load.getEntrance(d);
        headInfo.setEnterOid(enid);
        tailInfo.setEnterOid(enid);
        DebugUtil.debug("updateRobotInLoadDirect Direct %s setEnterOid %d", d.name(), enid);
    }

    /**
     * 用户选择好了出口
     */
    public void onUserChoiceMutilExitDirect(Direct direct) {
        DebugUtil.debug("onUserChoiceMutilExitDirect");
        updateRobotInLoadDirect();
        try {
            headInfo.setUserChoiceDirect(direct);
            tailInfo.setUserChoiceDirect(direct);
        } catch (Exception ex) {
            DebugUtil.debug("%s", ex.getMessage());
        }

        setComputeExitPathState("onUserChoiceMutilExitDirect");
    }

    public void setDrive(boolean drive) {
        if (drive) {
            DebugUtil.debug("引擎启动 运行中%s", engine.isRunning() ? "是" : "否");
            MusicUtil.playTravelBgMusic();
            RobotActionManager.handShake(80);
            setStatus(statusStart);
            if (!engine.isRunning()) {
                engine.start();
            }
        } else {
            stopMove();
            engine.stop();
            RobotActionManager.reset();
        }
    }

    public void setHeadOid(int oid) {
        headInfo.setValue(oid);
    }

    public void setTailOid(int oid) {
        tailInfo.setValue(oid);
    }

    public void release() {
        setDrive(false);
        mListener = null;
        MusicUtil.stopMusic();
        stopBgMusic();
        if (currentLoad != null) {
            currentLoad.release();
        }
        LoadMgr.getInstance().clear();
        Director.getInstance().reset();
    }

    public BaseLoad getCurrentLoad() {
        return currentLoad;
    }

    private void moving() {
        DebugUtil.debug("moving");
        isMoving = true;
        BaseApplication.sendAction(Command.format(Command.CITY_MOVE));

        Director.getInstance().playMovingEmotion();
        MusicUtil.playTravelBgMusic();
        RobotActionManager.handShake(80);
    }

    public void stopMove() {
        DebugUtil.debug("stopMove");
        isMoving = false;
        stopBgMusic();
        RobotActionManager.reset();
    }

    private void stopBgMusic() {
        MusicUtil.stopBgMusic();
    }

    public void exception(ICar.DriveException exception, Object param) {
        setStatus(statusException);
        BaseLoad load = getCurrentLoad();
        if (load != null) {
            if (load.getLock() != null) {
                load.getLock().release();
            }
        }
        if (mKey != null) {
            mKey.release();
            mKey = null;
        }
        CityApplication.getInstance().getHandler().post(new Runnable() {
            @Override
            public void run() {
                if (AnimationsContainer.getInstance() != null) {
                    if (AnimationsContainer.getInstance().getProgressDialogAnim() != null) {
                        AnimationsContainer.getInstance().getProgressDialogAnim().release();
                    }
                }
            }
        });

        Director.getInstance().reset();

        pause();
        if (mListener != null) {
            mListener.onException(exception, param);
        }
    }

    private void pause() {
        BaseLoad load = getCurrentLoad();
        if (load != null) {
            load.release();
        }
        setDrive(false);
        stopMove();
    }
}
