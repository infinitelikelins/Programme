package com.bearya.robot.base.can;

public class TouchHandle {
    private int during;
    private long mLastTouchTime = 0;

    public TouchHandle(){
        this(500);
    }
    public TouchHandle(int during){
        this.during = during;
    }

    public boolean touch() {
        long now = System.currentTimeMillis();
        boolean moreDuring = (now-mLastTouchTime) >during;
        mLastTouchTime = now;
        return moreDuring;
    }
}
