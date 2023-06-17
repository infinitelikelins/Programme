package com.bearya.robot.programme.walk.load;

import android.graphics.Rect;

import com.bearya.robot.base.load.BaseLoad;
import com.bearya.robot.base.walk.Direct;
import com.bearya.robot.base.walk.RobotInLoadMethod;

public class TurnLoad extends BaseLoad {

    public static final int START_OID = 2800;
    public static final String NAME = "转弯路";

    public TurnLoad() {
        super(START_OID);
        supportEntranceOid = new int[]{rightEntranceOid,bottomEntranceOid};
        closeEntranceOid = new int[]{leftEntranceOid,topEntranceOid};
        addLoadRect(new Rect(BORDER,BORDER,COLUMN,ROW-BORDER));
        addLoadRect(new Rect(BORDER,ROW-BORDER,COLUMN-BORDER,ROW));
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int[] getExitOid(int entranceOid, Direct direct, RobotInLoadMethod method) {
        if (method == RobotInLoadMethod.USER_PUT) {
            Direct faceDirect = getRobotInLoadDirect().getFaceDirect();
            if (faceDirect == Direct.Forward || faceDirect == Direct.Right) {
                return new int[]{rightEntranceOid};
            }
            return new int[]{bottomEntranceOid};
        } else {
            if (entranceOid == rightEntranceOid) {
                return new int[]{3385, 3269, bottomEntranceOid};
            } else if (entranceOid == bottomEntranceOid) {
                return new int[]{3269, 3385, rightEntranceOid};
            }
            return new int[0];
        }
    }

    @Override
    public void registerPlay() {

    }
}
