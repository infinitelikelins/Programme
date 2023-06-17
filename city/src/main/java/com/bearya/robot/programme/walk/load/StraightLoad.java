package com.bearya.robot.programme.walk.load;

import android.graphics.Rect;

import com.bearya.robot.base.load.BaseLoad;
import com.bearya.robot.base.util.DebugUtil;
import com.bearya.robot.base.walk.Direct;
import com.bearya.robot.base.walk.RobotInLoadMethod;

public class StraightLoad extends BaseLoad {

    public static final int START_OID = 1900;
    public static final String NAME = "直行";

    public StraightLoad() {
        this(START_OID);
    }

    public StraightLoad(int startOid) {
        super(startOid);
        supportEntranceOid = new int[]{topEntranceOid, bottomEntranceOid};
        closeEntranceOid = new int[]{leftEntranceOid, rightEntranceOid};
        addLoadRect(new Rect(BORDER, 0, COLUMN - BORDER, ROW));
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int[] getExitOid(int entranceOid, Direct direct, RobotInLoadMethod method) {
        if (method == RobotInLoadMethod.USER_PUT) {
            switch (getRobotInLoadDirect().getFaceDirect()) {
                case Forward:
                    return new int[]{topEntranceOid};
                case Backward:
                    return new int[]{bottomEntranceOid};
                default:
                    if (getRobotInLoadDirect().getNearDirect() == Direct.Forward) {
                        return new int[]{topEntranceOid};
                    } else {
                        return new int[]{bottomEntranceOid};
                    }
            }
        } else {
            if(entranceOid == leftEntranceOid || entranceOid == rightEntranceOid){
                if(distince(entranceOid,topEntranceOid) <distince(entranceOid,bottomEntranceOid)){
                    entranceOid = topEntranceOid;
                }else{
                    entranceOid = bottomEntranceOid;
                }
            }
            DebugUtil.debug("entranceOid=========>%d ,topEntranceOid ==========>%d , bottomEntranceOid =========>%d , leftEntranceOid ======>%d , rightEntranceOid =====> %d",
                    entranceOid, topEntranceOid, bottomEntranceOid, leftEntranceOid, rightEntranceOid);
            if (entranceOid == topEntranceOid) {
                return new int[]{getCenterOid(), bottomEntranceOid - 4 * ROW, bottomEntranceOid};
            } else if (entranceOid == bottomEntranceOid) {
                return new int[]{getCenterOid(), topEntranceOid + 4 * ROW, topEntranceOid};
            }
            return null;
        }
    }

    @Override
    public void registerPlay() {

    }

}