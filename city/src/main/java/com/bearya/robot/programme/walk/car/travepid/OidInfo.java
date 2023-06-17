package com.bearya.robot.programme.walk.car.travepid;

import android.annotation.SuppressLint;
import android.graphics.Point;

import androidx.annotation.NonNull;

import com.bearya.robot.base.load.BaseLoad;
import com.bearya.robot.base.util.DebugUtil;
import com.bearya.robot.base.walk.Direct;
import com.bearya.robot.base.walk.RobotInLoadMethod;
import com.bearya.robot.programme.walk.car.LoadMgr;

@SuppressLint("DefaultLocale")
public class OidInfo {

    int oid = 0;
    int exitoid = 0;

    private BaseLoad load = null;
    private int entranceOid = 0;
    private final String name;
    private long tick = 0;
    private long newLoadTick = 0;
    private double speedCurrent = 0;
    private boolean mustBeNewOneLoad = false;
    private boolean moveToCenter = false;
    private HistoryInfo historyLast = new HistoryInfo();

    public OidInfo(String head) {
        this.name = head;
    }

    public void setValue(int value) {
        synchronized (TravelStatus.mutex) {
            if (load != null) {
                historyLast.tick = tick;
                historyLast.point = load.converToPoint(oid);
            }
            tick = System.currentTimeMillis();
            oid = value;
            BaseLoad newLoad = LoadMgr.getInstance().getLoad(value);
            if (load != newLoad) {
                load = newLoad;
            }
            speedCurrent = 0;
            if (this.load != null) {
                Point current = this.converToPoint();
                if (current != null) {
                    speedCurrent = historyLast.getSpeed(current, this.tick);
                    if (mustBeNewOneLoad) {
                        boolean isNearPoint = isNearPoint();
                        DebugUtil.debug("%s mustBeNewOneLoad NearPoint=%s diff=%d", name, String.valueOf(isNearPoint), tickDiff());
                        if (isNearPoint && this.tickDiff() < 150) {
                            newOneLoad();
                        }
                    }
                }
            }
        }
    }

    public boolean isNearPoint() {
        if (load != null && converToPoint() != null) {
            return !(speedCurrent > 100);
        }
        return true;
    }

    public long tickDiff() {
        if (tick == 0) {
            return 0;
        }
        return System.currentTimeMillis() - tick;
    }

    public int getOid() {
        return oid;
    }

    public BaseLoad getLoad() {
        return load;
    }

    public int distince_exit() {
        return load.distince(this.oid, this.exitoid);
    }

    public Point converToPoint() {
        return load.converToPoint(oid);
    }

    @NonNull
    @Override
    public String toString() {
        if (load == null) {
            return String.format("%s null", name);
        }
        Point pt = load.converToPoint(oid);
        Point ptcenter = load.converToPoint(getCenterOid());
        Point ptexit = load.converToPoint(exitoid);
        Point pten = load.converToPoint(entranceOid);
        return String.format("%s %d %.2f %d(%d,%d) %s=%d(%d,%d)->%d(%d,%d)->%d(%d,%d)"
                , name, tickDiff(), speedCurrent, oid, pt.x, pt.y
                , load.getName()
                , entranceOid, pten.x, pten.y
                , getCenterOid(), ptcenter.x, ptcenter.y
                , exitoid, ptexit.x, ptexit.y
        );
    }

    public int getExitoid() {
        return this.exitoid;
    }

    public int getEntranceoid() {
        return this.entranceOid;
    }

    //用户选择了方向
    public void setUserChoiceDirect(Direct direct) {
        DebugUtil.debug("setUserChoiceDirect %s %s %s", Thread.currentThread().getName(), this.toString(), direct.toString());
        RobotInLoadMethod method = entranceOid == 0 ? RobotInLoadMethod.USER_PUT: RobotInLoadMethod.DRIVE;
        DebugUtil.debug("setUserChoiceDirect this.entranceOid == 0 RobotInLoadMethod.USER_PUT");
        int[] oids = load.getExitOid(this.entranceOid, direct, method);
        DebugUtil.debug("setUserChoiceDirect oids.length = %d", oids.length);
        this.exitoid = oids[oids.length - 1];
        DebugUtil.debug("setUserChoiceDirect %s", toString());
    }

    public void setEnterOid(int oid) {
        this.entranceOid = oid;
        Direct direct = load.getEntranceDirect(this.entranceOid);
        DebugUtil.debug("setEnterOid direct %s", direct.name());
        int[] oids = load.getExitOid(this.entranceOid, direct, RobotInLoadMethod.DRIVE);
        if (oids.length > 0)
            exitoid = oids[oids.length - 1];
        mustBeNewOneLoad = false;
        moveToCenter = true;
        DebugUtil.debug("setEnterOid %s", this.toString());
    }

    //刚刚进入新的地垫
    public void newOneLoad() {
        entranceOid = load.getEntrance(load.getEntranceDirect(this.oid));
        setEnterOid(this.entranceOid);
        DebugUtil.debug("newOneLoad %s", this.name);
    }

    public void setMustbeNewOneLoad() {
        mustBeNewOneLoad = true;
        DebugUtil.debug("%s setMustbeNewOneLoad", this.name);
    }

    //中心点
    public int getCenterOid() {
        return load.getCenterOid();
    }


    public int getTargetOid() {
        int[] oids = new int[]{this.entranceOid, this.getCenterOid(), this.exitoid};
        int oid = load.minDistince(oids, this.oid);
        if (moveToCenter) {
            if (oid == this.getCenterOid()) {
                moveToCenter = false;
                return this.exitoid;
            } else {
                return this.getCenterOid();
            }
        }
        return this.exitoid;
    }

//    private String oidtoString(int oid) {
//        Point pt = load.converToPoint(oid);
//        return String.format("%d(%d,%d)", oid, pt.x, pt.y);
//    }

    public int getAngle_Center2Exit() {
        int ret;
        Point ptexit = load.converToPoint(exitoid);
        Point ptcenter = load.converToPoint(load.getCenterOid());
        Point pt = load.converToPoint(oid);
        Point ptstart = getFacePoint(ptexit, ptcenter);
        ret = load.getAngle(pt, ptstart, ptexit);
        DebugUtil.debug("%s(%d,%d)->Start(%d,%d)->Center(%d,%d)->Exit(%d,%d)  %d", this.name,
                pt.x, pt.y,
                ptstart.x, ptstart.y,
                ptcenter.x,ptcenter.y,
                ptexit.x, ptexit.y,
                ret);

        return ret;
    }

    public int getAngle_Enter2Center() {
        int ret;
        Point penter = load.converToPoint(this.entranceOid);
        Point ptcenter = load.converToPoint(this.getCenterOid());
        Point pt = load.converToPoint(this.getOid());
        Point ptstart = getFacePoint(ptcenter, penter);
        ret = load.getAngle(pt, ptstart, ptcenter);
        DebugUtil.debug("%s(%d,%d)->Start(%d,%d)->Center(%d,%d)->enter(%d,%d)  %d", this.name,
                pt.x, pt.y,
                ptstart.x, ptstart.y,
                ptcenter.x, ptcenter.y,
                penter.x, penter.y,
                ret);

        return ret;
    }

//    int getAngleFacePoint(int oidfrom, int oidto) {
//        Point pthead = load.converToPoint(oid);
//        Point ptto = load.converToPoint(oidto);
//        Point ptfrom = load.converToPoint(oidfrom);
//        Point ptface = getFacePoint(ptfrom, ptto);
//
//        int dis = load.distince(oid, oidfrom);
//        int ret = 0;
//        if (dis < 4) {
//            ret = 0;
//        } else {
//            ret = load.getAngle(pthead, ptfrom, ptface);
//        }
//        DebugUtil.debug("getAngleFacePoint %d %s->%s->%s : %d", dis, oidtoString(oid), oidtoString(oidfrom), oidtoString(oidto), ret);
//        return ret;
//    }

    long tick_disInObstacle = 0;

    public boolean isInObstacle(long loopTime) {
        if (tick_disInObstacle == 0) {
            tick_disInObstacle = System.currentTimeMillis();
        }

        boolean bret = true;
        if (load != null) {
            bret = load.isInObstacle(load.converToPoint(oid));
        }

        if (bret) {
            long tickDiff = System.currentTimeMillis() - tick_disInObstacle;
            DebugUtil.debug("isInObstacle %s %d", this.name, tickDiff);
            if (tickDiff() > 300 && tickDiff() < 500) {//这种情况 可能是因为没有数据没纠正 等等再确认
                DebugUtil.debug("isInObstacle %s oid tickDiff=%d return false", this.name, this.tickDiff());
                return false;
            }
            return tickDiff > loopTime;
        } else {
            tick_disInObstacle = 0;
        }
        return false;
    }

    public Point getFaceCenter2Exit() {
        Point from = load.converToPoint(this.getCenterOid());
        Point to = load.converToPoint(this.getExitoid());
        return getFacePoint(from, to);
    }

    public static Point getFacePoint(Point pt_from, Point pt_to) {
        Point facePoint;
        int dis = 50;

        int dy = pt_to.y - pt_from.y;
        int dx = pt_to.x - pt_from.x;
        if (Math.abs(dy) > Math.abs(dx)) {//Y  方向
            if (dy > 0) {
                facePoint = new Point(pt_to.x, pt_to.y + dis);
            } else {
                facePoint = new Point(pt_to.x, pt_to.y - dis);
            }
        } else {//X  方向
            if (dx > 0) {
                facePoint = new Point(pt_to.x + dis, pt_to.y);
            } else {
                facePoint = new Point(pt_to.x - dis, pt_to.y);
            }
        }
        return facePoint;
    }

    public int getFaceExit(int tail) {

        Point ptface = getFaceCenter2Exit();
        return getAngle(getOid(), tail, ptface);
    }

    public int getAngle(int head, int tail, Point ptface) {
        int r = load.getAngle(head, tail, ptface);
//        if (r == -1) {
//            return TravelStatus.ERROR_ANGLE_VALUE;
//        }
        return r;
    }

    public int getAngle(int head, int tail, int ptfaceoid) {
        return this.getAngle(head, tail, load.converToPoint(ptfaceoid));
    }

    private static class HistoryInfo {
        public Point point;
        public long tick;

        public double getSpeed(Point current, long tick) {
            if (current == null || this.point == null) return 0;
            double dis = Math.sqrt(Math.abs(Math.pow(current.x - this.point.x, 2) + Math.pow(current.y - this.point.y, 2)));
            long diff = tick - this.tick;
            return (dis * 1000) / diff;
        }
    }

}