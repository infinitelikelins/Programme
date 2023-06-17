package com.bearya.robot.base.walk;

/**
 * Created by yexifeng on 17/11/13.
 * 区间值
 */

public class Section implements ISection{
    /**
     * 开始值(包含)
     *
     */
    private int start;
    /**
     * 结束值(包含)
     */
    private int end;

    public Section(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    @Override
    public void release() {

    }

    public int getEnd() {
        return end;
    }

    public boolean in(int value){
        return value>=start && value<=end;
    }
}
