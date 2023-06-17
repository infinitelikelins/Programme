package com.bearya.robot.programme.walk.car.travepid;

import com.bearya.actionlib.utils.RobotActionManager;
import com.bearya.robot.base.util.DebugUtil;
import com.bearya.robot.base.walk.InObstacleReason;
import com.bearya.robot.base.walk.RobotInLoadMethod;
import com.bearya.robot.programme.walk.car.DriveController2;
import com.bearya.robot.programme.walk.car.ICar;
import com.bearya.robot.programme.walk.load.StartLoad;
import com.bearya.robot.programme.walk.load.TLoad;
import com.bearya.robot.programme.walk.load.XLoad;

import java.util.LinkedList;

public class TravelStatus implements IStatusAtion {

    public static final Object mutex = new Object();
    public static final int ERROR_ANGLE_VALUE = -99999;

    private OidInfo headInfo;
    private OidInfo tailInfo;

    private static final long tickOutOfLoad = 1000 * 2;
    private static final long tickWaitTick = 250;
    private boolean isInTowLoading = false;
    private int validCount = 0;
    private long lastMoveTimeStamp = 0;

    private final DrivePid pidOnlyHead = new DrivePid(3.23, 0.0012, 0.02, 90, "onlyhead");
    private final DrivePid pidOnlyTail = new DrivePid(1.21, 0.0089, 0.02, 40, "onlytail");
    private final DrivePid pidHeadAndTail = new DrivePid(4.23, 0.01, 0.03, 90, "headandtail");

    private DrivePid pidCurrent = pidHeadAndTail;
    private long pidTick = 0;
    private final DriveController2 driveController;

    private final tickDiff tickHeadTailFace = new tickDiff("tick_head_tail_face");
    private final tickDiff tickTowLoad = new tickDiff("tick_tow_load");
    private final tickDiff tickArrivedExiting = new tickDiff("tick_Arrived_Exiting");

    private static class tickDiff {
        private boolean isFlag = false;
        private final String name;

        public tickDiff(String name) {
            this.name = name;
        }

        public long tick;

        public void refrush() {
            tick = System.currentTimeMillis();
        }

        public long diff() {
            return System.currentTimeMillis() - tick;
        }

        public void checkAndStart() {
            if (tick == 0) {
                refrush();
            }
        }

        public void reset() {
            tick = 0;
        }

        @Override
        public String toString() {
            return String.format("%s %d %s", name, diff(), isFlag);
        }

        public void setFlag() {
            isFlag = true;
            refrush();
        }

        public void cleanFlag() {
            isFlag = false;
            reset();
        }

        public boolean flagTime(int time) {
            if (isFlag) {
                return diff() > time;
            }
            return false;
        }
    }


    public TravelStatus(DriveController2 driveController) {
        this.driveController = driveController;
        this.headInfo = driveController.headInfo;
        this.tailInfo = driveController.tailInfo;
    }

    private void setPid(DrivePid pid) {
        if (pidCurrent != pid) {
            if (System.currentTimeMillis() - pidTick > 500) {
                if (pidOnlyHead != pidHeadAndTail) {
                    pidCurrent.reset();
                }
            }
            pidCurrent = pid;
            DebugUtil.debug("setPid %s", pidCurrent.getName());
            pidTick = System.currentTimeMillis();
        }
    }

    private void waitoid() {

        if (System.currentTimeMillis() - lastMoveTimeStamp > 300) {
            lastMoveTimeStamp = System.currentTimeMillis();
            DebugUtil.debug("waitoid %d", validCount);
            validCount++;
            if (validCount == 1) {
                RobotActionManager.goAhead(10, 10, "");
            } else if (validCount == 2) {
                RobotActionManager.turnRight(10, 10, "");
            } else if (validCount == 3) {
                RobotActionManager.turnLeft(10, 10, "");
            } else if (validCount == 4) {
                RobotActionManager.turnLeft(10, 10, "");
            } else if (validCount == 5) {
                RobotActionManager.turnRight(10, 10, "");
                validCount = 0;
            }
        }
    }


    public InObstacleReason getInObstacleReason() {
        RobotInLoadMethod r = driveController.getRobotInLoadMethod();
        if (r == RobotInLoadMethod.DRIVE) {
            return InObstacleReason.DriveInObstacle;
        } else {
            return InObstacleReason.UserPutInObstacle;
        }
    }

    private int getOnlyHead() {
        int angle;
        setPid(pidOnlyHead);
        if (headInfo.getCenterOid() == headInfo.getTargetOid()) {
            //前读头---->入口点----> 中心点
            angle = headInfo.getAngle_Enter2Center();
            DebugUtil.debug("head->enter->center %d", angle);
        } else {
            //前读头---->中心点----> 出口
            angle = headInfo.getAngle_Center2Exit();
            DebugUtil.debug("head->center->exit %d", angle);
        }
        return angle;
    }

    private int getOnlyTail() {
        int angle;

        //后读头---->中心点----> 出口
        setPid(pidOnlyTail);
        angle = tailInfo.getAngle_Center2Exit();
        DebugUtil.debug("tail->center->exit %d", angle);
        return angle;
    }


    private void setArriveTargetState() {
        DebugUtil.debug("travel arrive %s", headInfo);
        if (!headInfo.getLoad().getName().equals(StartLoad.NAME)) {
            driveController.setArriveTargetState();
        }
    }

    int checkcount = -1;
    LinkedList<String> loadshistory = new LinkedList<>();

    //判断两个起点间的数量  不符合要求的话  可能就是转弯了
    private boolean checkloadlist() {
        int lastflag = -1;
        int i = loadshistory.size() - 1;
        for (; i >= 0; i--) {
            if (loadshistory.get(i).equals(StartLoad.NAME)) {
                if (lastflag == -1) {
                    lastflag = i;
                } else {
                    int size = lastflag - i;
                    if (checkcount == -1) {
                        checkcount = size;
                        DebugUtil.debug("checkloadlist setcheckcount=%d", checkcount);
                    }
                    DebugUtil.debug("checkloadlist checkcount=%d  %d-%d=%d", checkcount, lastflag, i, size);
                    if (checkcount != size) {
                        checkcount = -1;
                        return false;
                    }
                    break;
                }
            }
        }
        return true;
    }

    private void onNewLoad() {
        if (tailInfo.tickDiff() > 200) {
            headInfo.newOneLoad();
            tailInfo.setEnterOid(headInfo.getEntranceoid());
        } else {
            tailInfo.newOneLoad();
            headInfo.setEnterOid(headInfo.getEntranceoid());
        }
        DebugUtil.debug("travel newLoad %s", headInfo);

        //重新开始一组参数
        pidHeadAndTail.reset();

        driveController.onNewLoad(headInfo.getLoad());
        tickArrivedExiting.cleanFlag();
        if (loadshistory.size() > 20) {
            loadshistory.removeFirst();
        }
        loadshistory.add(headInfo.getLoad().getName());
        if (!checkloadlist()) {
            DebugUtil.debug("==========> error  loadshistory");
            for (String n : loadshistory) {
                DebugUtil.debug("==========> error  %s", n);
            }
            //System.exit(-2);
        }
    }

    private int getAngle() {
        int angle = ERROR_ANGLE_VALUE;
        //stayInPlaceWatcher.setLocate(headinfo.getOid(), tailinfo.getOid());

        //前读头异常,只用后读头的算法
        if (headInfo.tickDiff() > 150) {
            angle = getOnlyTail();
        }
        //后读头异常,只用前读头的算法
        else if (tailInfo.tickDiff() > 300) {
            angle = getOnlyHead();
        } else {
            //前后都读头数据合理
            boolean oneLoad = driveController.isInOneLoad();
            if (oneLoad) {//在同一地垫上
                boolean setnewload = false;
                if (tickArrivedExiting.flagTime(500)) {//一段时间都还没进入新地垫
                    DebugUtil.debug("setnewload %s", tickArrivedExiting.toString());
                    onNewLoad();
                    setnewload = true;
                }
                if (isInTowLoading) {
                    //后读头进入一个新地垫, 机器人都在同一个地垫上了
                    DebugUtil.debug("setnewload isinTowLoading true ---> false");
                    if(!setnewload ){
                        onNewLoad();
                    }
                    isInTowLoading = false;
                }
                tickTowLoad.reset();

                int dis = headInfo.distince_exit();
                if (dis < 3) {//开始调整姿态
                    angle = headInfo.getFaceExit(tailInfo.getOid());
                    if (angleNearZero(angle)) {
                        //角度调整完毕  使用后读头数据走
                        DebugUtil.debug("onlytai distince=%d angle=%d", dis, angle);
                        angle = getOnlyTail();
                    } else {
                        setPid(pidHeadAndTail);
                        DebugUtil.debug("head->tail->face distince=%d angle=%d", dis, angle);
                    }

                    if (tickHeadTailFace.diff() > 1500) {//超过1500 ms  判断为新的一次调整
                        setArriveTargetState();
                    } else {
                        DebugUtil.debug("%s", tickHeadTailFace.toString());
                    }
                    tickHeadTailFace.refrush();
                    tickArrivedExiting.setFlag();

                } else if (dis < 50) {
                    setPid(pidHeadAndTail);
                    int targetid = headInfo.getTargetOid();
                    /*
                    if ( targetid == headinfo.getCenterOid() ) {
                        angle = headinfo.getAngle(headinfo.getOid(), tailinfo.getOid(), targetid);
                        DebugUtil.debug("head->tail->center distince=%d angle=%d", dis, angle);
                    }
                    else */
                    {
                        angle = headInfo.getAngle(headInfo.getOid(), tailInfo.getOid(), headInfo.getExitoid());
                        DebugUtil.debug("head->tail->exit distince=%d angle=%d", dis, angle);
                    }
                } else {
                    //距离异常
                    angle = ERROR_ANGLE_VALUE;
                    DebugUtil.debug("error distince=%d angle=%d reset", dis, angle);
                    headInfo.newOneLoad();
                    tailInfo.newOneLoad();
                    angle = getOnlyTail();
                }

            } else {//在两个地垫上,切换过程中
                long tick_inTowLoad_diff;
                tickTowLoad.checkAndStart();
                tick_inTowLoad_diff = tickTowLoad.diff();
                if (!isInTowLoading) {
                    if (tick_inTowLoad_diff > 250) {
                        //前读头进入一个新地垫
                        headInfo.setMustbeNewOneLoad();
                        //headinfo.newOneLoad();
                        //两个地垫之间
                        isInTowLoading = true;
                    }
                }

                if (!headInfo.isNearPoint()) {
                    angle = getOnlyTail();
                } else if (headInfo.getLoad() instanceof XLoad ||
                        headInfo.getLoad() instanceof TLoad) {
                    angle = getOnlyTail();
                } else {
                    angle = getOnlyHead();
                }
                DebugUtil.debug("isinTowLoading=%s %d", String.valueOf(isInTowLoading), tick_inTowLoad_diff);
            }
        }
        return angle;
    }

    protected boolean angleNearZero(int angle) {
        int allowErrorAngle = 4;
        return (angle >= 0 && angle <= allowErrorAngle) || (angle <= 360 && angle >= (360 - allowErrorAngle));
    }


    @Override
    public void start() {

    }

    @Override
    public void update() {

        if (!driveController.isMoving) {
            return;
        }

        if (headInfo.getLoad() == null || tailInfo.getLoad() == null) {
            if (validCount > 10) {
                driveController.exception(ICar.DriveException.OutOfLoad, null);
            } else {
                waitoid();
            }
            return;
        }
        DebugUtil.debug("update %s %s", headInfo.toString(), tailInfo.toString());
        //前读头长时间异常
        if (headInfo.tickDiff() > 500 && headInfo.tickDiff() < 1500) {
            DebugUtil.debug("headinfo error!!!!!");
            waitoid();
            return;
        }
        if (headInfo.tickDiff() > tickOutOfLoad && tailInfo.tickDiff() > tickOutOfLoad) {
            DebugUtil.debug("OutOfLoad");
            driveController.exception(ICar.DriveException.OutOfLoad, null);
            return;
        }
        if (headInfo.isInObstacle(300)) {
            DebugUtil.debug("headinfo isInObstacle");
            driveController.exception(ICar.DriveException.InObstacle, getInObstacleReason());
            return;
        }
        if (tailInfo.isInObstacle(800)) {
            DebugUtil.debug("tailinfo isInObstacle");
            driveController.exception(ICar.DriveException.InObstacle, getInObstacleReason());
            return;
        }


        //前后读头异常
        if (headInfo.tickDiff() > tickWaitTick && tailInfo.tickDiff() > tickWaitTick) {
            waitoid();
            return;
        }

        validCount = 0;
        int angle = ERROR_ANGLE_VALUE;
        synchronized (mutex) {
            try {
                angle = getAngle();
            } catch (Exception ex) {
                DebugUtil.debug(ex.getMessage());
            }
        }

        if (driveController.isMoving) {
            if (angle == ERROR_ANGLE_VALUE) {
                DebugUtil.debug("angle == error_angle_value");
            } else {
                pidCurrent._loc_accel(angle);
                goWithSpeed(pidCurrent.getleft(), pidCurrent.getright());
            }
        } else {
            DebugUtil.debug("TravelStatus .isMoving=false");
        }

    }

    @Override
    public void reset() {
        RobotActionManager.stopWheel();
    }

    public void goWithSpeed(int ls, int rs) {
        if (ls == 0 && rs == 0) {
            RobotActionManager.stopWheel();
            return;
        }
        if (ls >= 0 && rs >= 0) {
            DebugUtil.debug("前进:LS=%d,RS=%d", ls, rs);
            RobotActionManager.send(0x40, 0x1, ls, rs, "编程地垫游戏");
        } else if (ls < 0 && rs >= 0) {
            DebugUtil.debug("左转:LS=%d,RS=%d", ls, rs);
            RobotActionManager.send(0x40, 0x3, -ls, rs, "编程地垫游戏");
        } else if (ls >= 0 && rs < 0) {
            DebugUtil.debug("右转:LS=%d,RS=%d", ls, rs);
            RobotActionManager.send(0x40, 0x4, ls, -rs, "编程地垫游戏");
        }
    }

}
