package com.bearya.robot.base.walk;

/**
 * Created by yexifeng on 2019/3/5.
 */

public interface ISection {
    boolean in(int value);
    int getStart();
    void release();
}
