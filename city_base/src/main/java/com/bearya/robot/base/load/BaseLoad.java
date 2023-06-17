package com.bearya.robot.base.load;

import android.graphics.Point;
import android.graphics.Rect;

import com.bearya.robot.base.car.RobotInLoadDirect;
import com.bearya.robot.base.car.RobotPassLoad;
import com.bearya.robot.base.protocol.DriveResult;
import com.bearya.robot.base.protocol.ILoad;
import com.bearya.robot.base.protocol.ILock;
import com.bearya.robot.base.protocol.IntMap;
import com.bearya.robot.base.walk.Direct;
import com.bearya.robot.base.walk.ISection;
import com.bearya.robot.base.walk.RobotInLoadMethod;
import com.bearya.robot.base.walk.Section;
import com.bearya.robot.base.walk.SectionArea;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseLoad<V> extends IntMap implements ILoad {

    public static final int COLUMN = 30;
    public static final int ROW = 30;
    public static final int BORDER = 5;

    public static final String ON_END_LOAD = "on_end_load";
    public static final String ON_START_LOAD = "on_start_load";
    public static final String ON_NEW_LOAD = "on_new_load";
    public static final String ON_UNLOCK_SUCCESS = "on_unlock_success";
    public static final String ON_UNLOCK_SUCCESS_RIGHT = "on_unlock_success_right";
    public static final String ON_UNLOCK_SUCCESS_WRONG = "on_unlock_success_wrong";
    public static final String ON_UNLOCK_FAIL = "on_unlock_fail";
    public static final String ON_LOCK_TIMEOUT = "on_lock_timeout";
    public static final String ON_LOCK_MAX_TIMEOUT = "on_lock_max_timeout";

    protected ISection section;
    protected int[] entranceOid;//四个入口OId
    protected int leftEntranceOid;//左入口OId
    protected int topEntranceOid;//上入口OId
    protected int rightEntranceOid;//右入口OId
    protected int bottomEntranceOid;//下入口OId
    protected int[] supportEntranceOid;//支持入口OId
    protected int[] closeEntranceOid;//封闭入口OId
    protected List<Rect> rectList = new ArrayList<>();//道路矩阵
    protected ILock<V> lock;
    private int centerOid;
    private RobotPassLoad robotPassLoad = new RobotPassLoad();//机器人在道路上经过的入口信息
    private RobotInLoadDirect robotInLoadDirect = new RobotInLoadDirect();

    public BaseLoad(int startOid) {
        section = new Section(startOid, startOid + COLUMN * ROW - 1);
        init();
    }

    public BaseLoad(int maxColumn, int startOid) {
        section = new SectionArea(maxColumn, startOid, COLUMN, ROW);
        init();
    }

    private void init() {
        int entranceOidBorder = 2;
        leftEntranceOid = converToOid(new Point(entranceOidBorder, ROW / 2));
        topEntranceOid = converToOid(new Point(COLUMN / 2, entranceOidBorder));
        rightEntranceOid = converToOid(new Point(COLUMN - entranceOidBorder, ROW / 2));
        bottomEntranceOid = converToOid(new Point(COLUMN / 2, ROW - entranceOidBorder));
        entranceOid = new int[]{leftEntranceOid, topEntranceOid, rightEntranceOid, bottomEntranceOid};
        centerOid = converToOid(new Point(COLUMN / 2, ROW / 2));
        closeEntranceOid = new int[]{};
    }

    @Override
    public int getWidth() {
        return COLUMN;
    }

    @Override
    public int getHeight() {
        return ROW;
    }

    @Override
    public ISection getOidSection() {
        return section;
    }

    @Override
    public int getRow() {
        return getHeight();
    }

    @Override
    public int getColumn() {
        return getWidth();
    }

    @Override
    public int getStartOid() {
        return section.getStart();
    }

    /**
     * 引方法专门针对起点地垫设计的
     */
    public abstract int[] getExitOid(int entranceOid, Direct direct, RobotInLoadMethod method);

    @Override
    public ILock<V> getLock() {
        return lock;
    }

    @Override
    public int getCenterOid() {
        return centerOid;
    }

    public Direct getEntranceDirect(int oid) {
        if (oid == topEntranceOid) {
            return Direct.Forward;
        } else if (oid == bottomEntranceOid) {
            return Direct.Backward;
        } else if (oid == leftEntranceOid) {
            return Direct.Left;
        } else if (oid == rightEntranceOid) {
            return Direct.Right;
        }
        //如果没有锚点OID则找距离最近的那个
        int minDisOid = minDistince(supportEntranceOid, oid);
        return getEntranceDirect(minDisOid);
    }

    public int getEntrance(Direct direct) {
        if (direct == Direct.Backward) {
            return bottomEntranceOid;
        } else if (direct == Direct.Forward) {
            return topEntranceOid;
        } else if (direct == Direct.Right) {
            return rightEntranceOid;
        } else if (direct == Direct.Left) {
            return leftEntranceOid;
        }
        return 0;
    }

    @Override
    public void release() {
        if (lock != null) {
            lock.release();
        }
    }

    protected void addLoadRect(Rect rect) {
        rectList.add(rect);
    }

    /**
     * 某点是否在避障区内
     */
    public boolean isInObstacle(Point point) {
        boolean inLoad = false;
        for (Rect rect : rectList) {
            boolean rectCheck = rect.left < rect.right && rect.top < rect.bottom;// check for empty first
            boolean inRect = point.x >= rect.left && point.x <= rect.right && point.y >= rect.top && point.y <= rect.bottom;
            inLoad = (rectCheck && inRect) || inLoad;
        }
        return !inLoad;
    }

    public boolean hasLock() {
        return lock != null;
    }

    public void recordEntranceInfo(boolean isHeadReader, int entranceOid) {
        robotPassLoad.addInfo(isHeadReader, entranceOid);
    }

    @Override
    public DriveResult onResult(RobotInLoadMethod method) {
        return null;
    }

    /**
     * 更新设备在地垫上的位置信息
     * 计算最近的出口位置方向
     */
    public void updateRobotInLoadDirect(int headOid, int tailOid) {
        robotInLoadDirect.update(this, headOid, tailOid);
    }

    public int getLeftEntranceOid() {
        return leftEntranceOid;
    }

    public int getTopEntranceOid() {
        return topEntranceOid;
    }

    public int getRightEntranceOid() {
        return rightEntranceOid;
    }

    public int getBottomEntranceOid() {
        return bottomEntranceOid;
    }

    public RobotInLoadDirect getRobotInLoadDirect() {
        return robotInLoadDirect;
    }

    public abstract void registerPlay();
}
