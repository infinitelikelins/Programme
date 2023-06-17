package com.bearya.robot.programme.walk.load.lock;


public class PopViewItemData {
    private final int imageRes;//显示图片
    private final String animatorMp3;//执行动画时并列播放的Mp3
    private final String selectedMp3;//选择时播放的Mp3
    private final int selectedDrawableId;//选中时弹出的动画
    private final PopViewResult result;//选中返回的结果,包含是否是正确答案和紧接着交给Director执行的动画Key

    public PopViewItemData(int imageRes, String animatorMp3, int selectedDrawableId,String selectMp3, PopViewResult result) {
        this.imageRes = imageRes;
        this.animatorMp3 = animatorMp3;
        this.selectedDrawableId = selectedDrawableId;
        this.result = result;
        this.selectedMp3 = selectMp3;
    }

    public int getImageRes() {
        return imageRes;
    }

    public String getAnimatorMp3() {
        return animatorMp3;
    }

    public int getSelectedDrawableId() {
        return selectedDrawableId;
    }

    public PopViewResult getResult() {
        return result;
    }

    public String getSelectedMp3() {
        return selectedMp3;
    }
}
