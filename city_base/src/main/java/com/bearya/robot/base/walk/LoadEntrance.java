package com.bearya.robot.base.walk;

import com.bearya.robot.base.BaseApplication;
import com.bearya.robot.base.load.BaseLoad;

import java.util.Locale;

public class LoadEntrance {
    private String loadName;
    private int entranceOid;
    private int exitOid;
    private Direct userChoiceDirect;

    public LoadEntrance(String loadName, int entranceOid, int exitOid) {
        this.loadName = loadName;
        this.entranceOid = entranceOid;
        this.exitOid = exitOid;
    }

    public LoadEntrance() {

    }

    public void setLoad(BaseLoad load) {
        this.loadName = load.getName();
    }

    public void setEntranceOid(int entranceOid) {
        this.entranceOid = entranceOid;
    }

    public BaseLoad getLoad() {
        return BaseApplication.getInstance().getLoadMgr().getLoad(loadName);
    }

    public int getEntranceOid() {
        return entranceOid;
    }

    private int[] getExitOid(Direct userChoiceDirect, RobotInLoadMethod method) {
        return getLoad().getExitOid(entranceOid, userChoiceDirect, method);
    }

    @Override
    public String toString() {
        return String.format(Locale.CHINA, "%s 入口%d", loadName, entranceOid);
    }

    public void setUserChoiceDirect(Direct userChoiceDirect) {
        this.userChoiceDirect = userChoiceDirect;
    }

    public int[] computeExitPath(RobotInLoadMethod method) {
        int oids[] = getExitOid(userChoiceDirect, method);
        userChoiceDirect = null;
        return oids;
    }

    public void setExitOid(int exitOid) {
        this.exitOid = exitOid;
    }

    public LoadEntrance newInstance() {
        return new LoadEntrance(loadName, entranceOid, exitOid);
    }

    public int getExitOid() {
        return exitOid;
    }

}
