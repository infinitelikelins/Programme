package com.bearya.robot.programme.walk.load.lock;

import android.view.View;

import com.bearya.robot.base.can.Body;
import com.bearya.robot.base.can.CanManager;
import com.bearya.robot.base.walk.Direct;

public class TouchBodyKey extends DirectKey implements View.OnClickListener {

    @Override
    public void onTouchBody(Body body) {
        boolean success = false;
        if (listener != null && CanManager.getInstance().containListener(this)) {
            if (body == Body.BACK) {
                success = listener.onKey(Direct.Backward);
            } else if (body == Body.CHEST) {
                success = listener.onKey(Direct.Forward);
            } else if (body == Body.LEFT_ARM || body == Body.RIGHT_ARM) {
                success = listener.onKey(Direct.ARM);
            } else if (body == Body.LEFT_EAR || body == Body.RIGHT_EAR) {
                success = listener.onKey(Direct.EAR);
            }
        }
        if (success) {
            CanManager.getInstance().removeListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            boolean success = listener.onKey(Direct.Head);
            if (success) {
                CanManager.getInstance().removeListener(this);
            }
        }
    }

}
