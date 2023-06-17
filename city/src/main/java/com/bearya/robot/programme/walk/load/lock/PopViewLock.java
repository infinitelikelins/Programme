package com.bearya.robot.programme.walk.load.lock;


import com.bearya.robot.base.protocol.Key;
import com.bearya.robot.base.protocol.KeyListener;
import com.bearya.robot.base.protocol.LockListener;
import com.bearya.robot.base.protocol.LockType;
import com.bearya.robot.base.util.DebugUtil;

public class PopViewLock extends BaseLock<PopViewResult> {
    private PopViewData data;

    public PopViewLock() {
        super(LockType.PopView, 30000);
    }

    public void setData(PopViewData data) {
        this.data = data;
    }

    @Override
    public void unLock(final Key<PopViewResult> key, LockListener<PopViewResult> l) {
        listener = l;
        if (listener != null) {
            listener.onLocking();
        }
        DebugUtil.debug("进入PopViewLock ========> unlock");
        registerTimeout();

        key.create(new KeyListener<PopViewResult>() {
            @Override
            public boolean onKey(PopViewResult value) {
                release();
                if (listener != null) {
                    listener.onSuccess(value, null);
                }
                return true;
            }

            @Override
            public void delay(PopViewResult value, int second) {

            }
        });
    }

    @Override
    public PopViewResult[] getValues() {
        return null;
    }

    public PopViewData getData() {
        return data;
    }
}
