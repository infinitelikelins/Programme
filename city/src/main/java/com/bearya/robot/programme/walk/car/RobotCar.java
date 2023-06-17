package com.bearya.robot.programme.walk.car;

import com.bearya.actionlib.utils.RobotActionManager;
import com.bearya.robot.base.can.Body;
import com.bearya.robot.base.can.CanDataListener;
import com.bearya.robot.base.can.CanManager;
import com.bearya.robot.base.util.DebugUtil;
import com.bearya.robot.base.util.RobotOidReaderRater;

public class RobotCar implements ICar, CanDataListener {

    private final DriveController2 driveController;

    private final RobotOidReaderRater robotOidReaderRater;

    public RobotCar(DriveListener listener) {
        driveController = new DriveController2();
        driveController.setListener(listener);
        robotOidReaderRater = new RobotOidReaderRater(2, new RobotOidReaderRater.OidReaderEmptyListener() {
            @Override
            public void onHeadEmpty() {
                onTailEmpty();
            }

            @Override
            public void onTailEmpty() {
                driveController.onOidEmpty();
            }

            @Override
            public void onTowEmpty() {//两读头数据都为空
                driveController.towOidEmpty();
            }

            @Override
            public void onFull() {
                driveController.oidFull();
            }
        });
        CanManager.getInstance().addListener(this);
    }

    @Override
    public void drive() {
        DebugUtil.info("现在小贝应该是开始运动了");
        driveController.setDrive(true);
        robotOidReaderRater.start();
    }

    @Override
    public void onFrontOid(int oid) {
        robotOidReaderRater.addHeadOid();
        if (LoadMgr.getInstance().inLoad(oid)) {
            driveController.setHeadOid(oid);
        }
    }

    @Override
    public void onBackOid(int oid) {
        robotOidReaderRater.addTailOid();
        if (LoadMgr.getInstance().inLoad(oid)) {
            driveController.setTailOid(oid);
        }
    }

    @Override
    public void onTouchBody(Body body) {

    }

    public void release() {
        driveController.release();
        robotOidReaderRater.release();
        DebugUtil.debug("Robot release reset");
        RobotActionManager.reset();
        CanManager.getInstance().removeListener(this);
    }

}