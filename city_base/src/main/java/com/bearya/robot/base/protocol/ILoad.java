package com.bearya.robot.base.protocol;

import com.bearya.robot.base.walk.ISection;
import com.bearya.robot.base.walk.RobotInLoadMethod;

public interface ILoad {


    /**
     * 名称
     */
    String getName();

    /**
     * 获取OID范围
     */
    ISection getOidSection();

    /**
     * 宽
     */
    int getWidth();

    /**
     * 高
     */
    int getHeight();

    /**
     * 中点
     */
    int getCenterOid();

    ILock getLock();

    void release();

    /**
     * 走到地垫上就会得到一个结果
     */
    DriveResult onResult(RobotInLoadMethod method);

}
