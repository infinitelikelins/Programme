package com.bearya.robot.programme.walk.car.travepid;

import com.bearya.actionlib.utils.RobotActionManager;
import com.bearya.robot.base.util.DebugUtil;
import com.bearya.robot.base.walk.InObstacleReason;
import com.bearya.robot.programme.walk.car.DriveController2;
import com.bearya.robot.programme.walk.car.ICar;

public class StartStatus implements IStatusAtion {
    private DriveController2 driveController;
    private OidInfo headInfo;
    private OidInfo tailInfo;
    private long lastMoveTimeStamp = 0;

    public StartStatus(DriveController2 driveController2) {
        driveController = driveController2;
        headInfo = driveController.headInfo;
        tailInfo = driveController.tailInfo;
    }

    @Override
    public void start() {
        driveController.currentLoad = null;
       // LoadEntrance.
    }
    private int validCount = 0;
    private void waitoid() {

        if (System.currentTimeMillis() - lastMoveTimeStamp > 300) {
            lastMoveTimeStamp = System.currentTimeMillis();
            DebugUtil.debug("waitoid %d", validCount);
            validCount++;
            if (validCount % 5 == 0) {
                RobotActionManager.goAhead(10, 10, "");
            } else if (validCount% 5 == 1) {
                RobotActionManager.turnRight(10, 10, "");
            } else if (validCount% 5 == 2) {
                RobotActionManager.turnLeft(10, 10, "");
            } else if (validCount% 5 == 3) {
                RobotActionManager.turnLeft(10, 10, "");
            } else if (validCount% 5 == 4) {
                RobotActionManager.turnRight(10, 10, "");
            }
        }
    }

    @Override
    public void update() {

        if (headInfo.getLoad() == null || tailInfo.getLoad() == null) {
            if (validCount > 10) {
                driveController.exception(ICar.DriveException.OutOfLoad, null);
            } else {
                waitoid();
            }
            return;
        }
        validCount = 0;
        long tickOutOfLoad = 3000;
        if (headInfo.tickDiff() > tickOutOfLoad && tailInfo.tickDiff() > tickOutOfLoad) {
            DebugUtil.debug("OutOfLoad");
            driveController.exception(ICar.DriveException.OutOfLoad, null);
        } else if (headInfo.getLoad() == null || tailInfo.getLoad() == null) {
            DebugUtil.debug("headInfo null");
        } else if (headInfo.isInObstacle(300)) {
            DebugUtil.debug("headInfo isInObstacle");
            driveController.exception(ICar.DriveException.InObstacle, InObstacleReason.UserPutInObstacle);
        } else if (tailInfo.isInObstacle(800)) {
            DebugUtil.debug("headInfo isInObstacle");
            driveController.exception(ICar.DriveException.InObstacle, InObstacleReason.UserPutInObstacle);
        } else if (this.driveController.isInOneLoad()) {
            driveController.updateRobotInLoadDirect();
            driveController.onNewLoad(headInfo.getLoad());
        }
    }


    @Override
    public void reset() {

    }
}
