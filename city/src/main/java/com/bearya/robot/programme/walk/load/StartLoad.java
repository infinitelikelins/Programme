package com.bearya.robot.programme.walk.load;

import android.graphics.Rect;

import com.bearya.robot.base.load.BaseLoad;
import com.bearya.robot.base.walk.Direct;
import com.bearya.robot.base.walk.RobotInLoadMethod;

public class StartLoad extends BaseLoad {

    public static final int START_OID = 25576;//启动点码
    public static final String NAME = "起点";

    public StartLoad() {
        super(START_OID);
        supportEntranceOid = new int[]{topEntranceOid};
        closeEntranceOid = new int[]{leftEntranceOid, rightEntranceOid, bottomEntranceOid};
        addLoadRect(new Rect(BORDER, 0, COLUMN - BORDER, ROW));
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int[] getExitOid(int entranceOid, Direct direct, RobotInLoadMethod method) {
        return method == RobotInLoadMethod.USER_PUT ? new int[]{topEntranceOid + COLUMN * 3, topEntranceOid} :
                new int[]{getCenterOid(), getCenterOid() + COLUMN * 4, topEntranceOid};
    }

    @Override
    public void registerPlay() {

    }

}