package com.bearya.robot.programme.view;

import android.content.Context;
import android.view.View;

import com.bearya.robot.programme.R;

public class TellStoryPopup extends AbsBasePopup {

    public TellStoryPopup(Context context) {
        super(context);
    }

    @Override
    protected int inflateLayoutId() {
        return R.layout.popup_story_finish;
    }

    @Override
    protected void onViewInflated() {
        setWidth(850);
    }

    public final void withConfirm(View.OnClickListener showAgain, View.OnClickListener cancelListener) {
        withClick(R.id.cancel, cancelListener);
        withClick(R.id.show_again, showAgain);
    }

}