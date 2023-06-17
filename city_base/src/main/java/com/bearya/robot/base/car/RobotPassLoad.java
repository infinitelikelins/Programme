package com.bearya.robot.base.car;

import com.bearya.robot.base.load.BaseLoad;

import java.util.ArrayList;
import java.util.List;

public class RobotPassLoad {
    private BaseLoad preLoad;//进入之前的地垫
    private List<Integer> headReaderPassEntranceOidList = new ArrayList<>();//前读头经过的锚点
    private List<Integer> tailReaderPassEntranceOidList = new ArrayList<>();//后读头经过的锚点

    public void clear(){
        headReaderPassEntranceOidList.clear();
        tailReaderPassEntranceOidList.clear();
        preLoad = null;
    }

    public void setPreLoad(BaseLoad preLoad) {
        this.preLoad = preLoad;
    }

    public void addInfo(boolean isHeadReader, int entranceOid) {
        if(isHeadReader){
            headReaderPassEntranceOidList.add(entranceOid);
        }else{
            tailReaderPassEntranceOidList.add(entranceOid);
        }
    }


    /**
     *
     * @return true 表示从某张地垫开车进入的, false表示用户放置在地垫上的
     */
    public boolean isDriveInLoad(){
        return preLoad!=null && headReaderPassEntranceOidList.size()>0 && tailReaderPassEntranceOidList.size()>0;
    }
}
