package com.bearya.robot.programme.walk.load.lock;


public class PopViewData {
    private final PopViewItemData leftViewData;
    private final PopViewItemData rightViewData;
    private final String initMp3;
    private PopViewCallback callback;


    public PopViewData(String initMp3,PopViewItemData leftViewData, PopViewItemData rightViewData) {
        this.leftViewData = leftViewData;
        this.rightViewData = rightViewData;
        this.initMp3 = initMp3;
    }

    public void setCallback(PopViewCallback callback) {
        this.callback = callback;
    }


    public PopViewItemData getLeftViewData() {
        return leftViewData;
    }

    public PopViewItemData getRightViewData() {
        return rightViewData;
    }

    public String getInitMp3() {
        return initMp3;
    }

    public PopViewCallback getCallback() {
        return callback;
    }
}
