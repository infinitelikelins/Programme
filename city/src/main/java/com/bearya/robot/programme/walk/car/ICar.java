package com.bearya.robot.programme.walk.car;

import com.bearya.robot.base.protocol.DriveResult;
import com.bearya.robot.base.walk.Direct;
import com.bearya.robot.base.walk.LoadEntrance;

public interface ICar {

    enum DriveException {
        NoEntry,
        /**
         * 走出道路(小贝未检测到OID)
         */
        OutOfLoad,
        /**
         * 走到马路牙子(避障区)
         */
        InObstacle,
        /**
         * 将小贝放置在两张地垫上,注意不是小贝自己走到两张地垫上
         */
        PutRobotInTowLoad,
    }

    interface DriveListener {

        void onException(DriveException exception, Object param);

        /**
         * 驾驶结果
         */
        void onDriveResult(DriveResult result);

    }

    /**
     * 按指定跳线行驶
     */
    void drive();

}