package com.bearya.robot.programme.walk.load;

import android.graphics.Rect;

import com.bearya.robot.base.load.BaseLoad;
import com.bearya.robot.base.play.Director;
import com.bearya.robot.base.play.FacePlay;
import com.bearya.robot.base.play.FaceType;
import com.bearya.robot.base.play.LoadPlay;
import com.bearya.robot.base.play.PlayData;
import com.bearya.robot.base.protocol.DriveResult;
import com.bearya.robot.base.walk.Direct;
import com.bearya.robot.base.walk.RobotInLoadMethod;
import com.bearya.robot.programme.R;
import com.bearya.robot.programme.data.Command;

public class PatriotismEndLoad extends BaseLoad {

    public static final String NAME = "祖国万岁";
    public static final int START_OID = 57900;

    public PatriotismEndLoad() {
        super(START_OID);
        supportEntranceOid = new int[]{leftEntranceOid, topEntranceOid, rightEntranceOid, bottomEntranceOid};
        addLoadRect(new Rect(BORDER, 0, COLUMN - BORDER, ROW));
        addLoadRect(new Rect(0, BORDER, COLUMN, ROW - BORDER));
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int[] getExitOid(int entranceOid, Direct direct, RobotInLoadMethod method) {
        if (entranceOid == topEntranceOid) {
            return new int[]{getCenterOid(), getCenterOid() + ROW * 8};
        } else if (entranceOid == leftEntranceOid) {
            return new int[]{getCenterOid(), getCenterOid() + 8};
        } else if (entranceOid == rightEntranceOid) {
            return new int[]{getCenterOid(), getCenterOid() - 8};
        } else if (entranceOid == bottomEntranceOid) {
            return new int[]{getCenterOid(), getCenterOid() - ROW * 8};
        }
        return new int[]{getCenterOid()};
    }

    @Override
    public DriveResult onResult(RobotInLoadMethod method) {
        return DriveResult.PatriotismEndLoad;
    }

    @Override
    public void registerPlay() {
        LoadPlay loadPlay = new LoadPlay();
        loadPlay.addLoad(new PlayData("tts/zh/to_patriotism_end.mp3", new FacePlay(String.valueOf(R.array.patriotism_end), FaceType.Arrays)));
        loadPlay.playAction = Command.format(Command.CITY_SUCCESS_PATRIOTISM);
        Director.getInstance().register(ON_END_LOAD, loadPlay);
    }
}
