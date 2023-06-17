package com.bearya.robot.programme.walk.load.lock;


import com.bearya.robot.base.protocol.Key;
import com.bearya.robot.base.protocol.KeyListener;
import com.bearya.robot.base.protocol.LockListener;
import com.bearya.robot.base.protocol.LockType;
import com.bearya.robot.base.walk.Direct;

public class DirectLock extends BaseLock<Direct> {
    private Direct[] supportDirects;
    private Direct entranceDirect;

    /**
     * @param directs 支持路口,Left左,Right右,Backward下,Forward上
     */
    public DirectLock(Direct... directs) {
        super(LockType.Direct, 30000);
        supportDirects = directs;
    }

    public void setEntranceDirect(Direct entranceDirect) {
        this.entranceDirect = entranceDirect;
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
                Direct targetDirect = go(entranceDirect, value);

                for (Direct d : supportDirects) {
                    if (targetDirect == d) {
                        supportTargetDirect = true;
                        break;
                    }
                }
                if (supportTargetDirect) {
                    release();
                    listener.onSuccess(targetDirect, value);
                    return true;
                } else {
                    listener.onFail(targetDirect, value);
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


    private Direct go(Direct direct, Direct action) {
        switch (direct) {
            case Backward: {
                return action;
            }
            case Forward: {
                switch (action) {
                    case Forward:
                        return Direct.Backward;
                    case Backward:
                        return Direct.Forward;
                    case Right:
                        return Direct.Left;
                    case Left:
                        return Direct.Right;
                }
            }
            break;
            case Left: {
                switch (action) {
                    case Forward:
                        return Direct.Right;
                    case Backward:
                        return Direct.Left;
                    case Right:
                        return Direct.Backward;
                    case Left:
                        return Direct.Forward;
                }
            }
            break;
            case Right: {
                switch (action) {
                    case Forward:
                        return Direct.Left;
                    case Backward:
                        return Direct.Right;
                    case Right:
                        return Direct.Forward;
                    case Left:
                        return Direct.Backward;
                }
            }
            break;
        }
        return Direct.Forward;
    }

}
