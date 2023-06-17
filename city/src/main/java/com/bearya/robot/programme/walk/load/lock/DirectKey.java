package com.bearya.robot.programme.walk.load.lock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.bearya.robot.base.BaseApplication;
import com.bearya.robot.base.can.Body;
import com.bearya.robot.base.can.CanDataListener;
import com.bearya.robot.base.can.CanManager;
import com.bearya.robot.base.protocol.Key;
import com.bearya.robot.base.protocol.KeyListener;
import com.bearya.robot.base.walk.Direct;

import java.util.Random;

public class DirectKey implements Key<Direct>, CanDataListener {

    // private static final String LEFT_EAR_PRESS_ACTION = "com.reach.receiver.key2.high"; // 收到触摸左耳按下广播
    private static final String LEFT_EAR_UP_ACTION = "com.reach.receiver.key2.low"; // 收到触摸左耳抬起广播
    // private static final String RIGHT_EAR_PRESS_ACTION = "com.reach.receiver.key1.high"; // 收到触摸右耳按下广播
    private static final String RIGHT_EAR_UP_ACTION = "com.reach.receiver.key1.low"; // 收到触摸右耳抬起广播

    protected KeyListener<Direct> listener;
    private boolean mReceiverTag = false;
    /**
     * 处理头部,双耳的触摸广播
     */
    private final BroadcastReceiver mTouchBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (RIGHT_EAR_UP_ACTION.equals(action)) {
                onTouchBody(Body.LEFT_EAR);
            } else if (LEFT_EAR_UP_ACTION.equals(action)) {
                onTouchBody(Body.RIGHT_EAR);
            }
        }
    };

    @Override
    public void create(KeyListener<Direct> listener) {
        this.listener = listener;
        CanManager.getInstance().addListener(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(LEFT_EAR_UP_ACTION);
        filter.addAction(RIGHT_EAR_UP_ACTION);
        mReceiverTag = !mReceiverTag;
        BaseApplication.getInstance().registerReceiver(mTouchBroadcast, filter);
    }

    @Override
    public void release() {
        CanManager.getInstance().removeListener(this);
        listener = null;
        if (mReceiverTag) {
            mReceiverTag = false;
            BaseApplication.getInstance().unregisterReceiver(mTouchBroadcast);
        }
    }

    @Override
    public void autoUnlock(Direct[] value) {
        Random random = new Random();
        int index = random.nextInt(value.length);
        Direct direct = value[index];
        if (listener != null) {
            listener.onKey(direct);
        }
    }


    @Override
    public void onFrontOid(int oid) {

    }

    @Override
    public void onBackOid(int oid) {

    }

    @Override
    public void onTouchBody(Body body) {
        boolean success = false;
        if (listener != null && CanManager.getInstance().containListener(this)) {
            if (body == Body.BACK) {
                success = listener.onKey(Direct.Backward);
            } else if (body == Body.CHEST) {
                success = listener.onKey(Direct.Forward);
            } else if (body == Body.LEFT_ARM || body == Body.LEFT_EAR) {
                success = listener.onKey(Direct.Left);
            } else if (body == Body.RIGHT_ARM || body == Body.RIGHT_EAR) {
                success = listener.onKey(Direct.Right);
            }
        }
        if (success) {
            CanManager.getInstance().removeListener(this);
        }
    }

}
