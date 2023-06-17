package com.bearya.robot.programme.walk.load.lock;

import com.bearya.robot.base.protocol.Key;
import com.bearya.robot.base.protocol.KeyListener;
import com.bearya.robot.base.protocol.LockListener;
import com.bearya.robot.base.protocol.LockType;
import com.bearya.robot.base.walk.Direct;

public class TouchBodyLock extends BaseLock<Direct> {
    private Direct[] supportDirects;

    public TouchBodyLock() {
        super(LockType.TouchBody, 30000);
    }

    @Override
    public void unLock(Key<Direct> key, final LockListener<Direct> l) {
        this.listener = l;
        if (listener != null) {
            listener.onLocking();
        }
        registerTimeout();
        key.create(new KeyListener<Direct>() {
            @Override
            public boolean onKey(Direct value) {
                boolean supportTargetDirect = false;
                for (Direct d : supportDirects) {
                    if (value == d) {
                        supportTargetDirect = true;
                        break;
                    }
                }
                if (supportTargetDirect) {
                    release();
                    listener.onSuccess(null, null);
                    return true;
                } else {
                    listener.onFail(null, null);
                    return false;
                }
            }

            @Override
            public void delay(Direct value, int second) {

            }
        });
    }

    @Override
    public Direct[] getValues() {
        return supportDirects;
    }

    @Override
    public void setValues(Direct... supportDirects) {
        this.supportDirects = supportDirects;
    }

}
