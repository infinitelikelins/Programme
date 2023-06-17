package com.bearya.robot.base.car;

import com.bearya.robot.base.walk.Direct;
import com.bearya.robot.base.load.BaseLoad;

public class RobotInLoadDirect {
    private Direct faceDirect;
    private Direct nearDirect;
    private Direct entranceDirect;

    public void update(BaseLoad load,int headOid, int tailOid) {

        //计算前后读头 各自距离地垫出口的距离
        int headDisLeftEntrance = load.distince(headOid,load.getLeftEntranceOid());
        int headDisRightEntrance = load.distince(headOid,load.getRightEntranceOid());
        int headDisTopEntrance = load.distince(headOid,load.getTopEntranceOid());
        int headDisBottomEntrance = load.distince(headOid,load.getBottomEntranceOid());
        int tailDisLeftEntrance = load.distince(tailOid,load.getLeftEntranceOid());
        int tailDisRightEntrance = load.distince(tailOid,load.getRightEntranceOid());
        int tailDisTopEntrance = load.distince(tailOid,load.getTopEntranceOid());
        int tailDisBottomEntrance = load.distince(tailOid,load.getBottomEntranceOid());

        //前读头
        int hltr = headDisLeftEntrance+tailDisRightEntrance;
        int hrtl = headDisRightEntrance+tailDisLeftEntrance;
        int httb = headDisTopEntrance+tailDisBottomEntrance;
        int hbtt = headDisBottomEntrance+tailDisTopEntrance;

        //计算最短距离
        int min1 = Math.min(hltr,hrtl);
        int min2 = Math.min(httb,hbtt);
        int min = Math.min(min1,min2);
        if(min == hltr){
            faceDirect = Direct.Left;
            if(httb<hbtt){
                nearDirect = Direct.Forward;
            }else{
                nearDirect = Direct.Backward;
            }
            entranceDirect = Direct.Right;
        }else if(min == hrtl){
            faceDirect = Direct.Right;
            if(httb<hbtt){
                nearDirect = Direct.Forward;
            }else{
                nearDirect = Direct.Backward;
            }
            entranceDirect = Direct.Left;
        }else if(min == httb){
            faceDirect = Direct.Forward;
            if(hltr<hrtl){
                nearDirect = Direct.Left;
            }else{
                nearDirect = Direct.Right;
            }
            entranceDirect = Direct.Backward;
        }else{
            faceDirect = Direct.Backward;
            if(hltr<hrtl){
                nearDirect = Direct.Left;
            }else{
                nearDirect = Direct.Right;
            }
            entranceDirect = Direct.Forward;
        }
    }

    public Direct getFaceDirect() {
        return faceDirect;
    }

    public Direct getNearDirect() {
        return nearDirect;
    }

    public Direct getEntranceDirect(){
        return entranceDirect;
    }
}
