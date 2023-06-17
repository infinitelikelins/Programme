package com.bearya.robot.programme.walk.load.lock;

import com.bearya.robot.base.protocol.Key;
import com.bearya.robot.base.protocol.KeyListener;


public class PopViewKey implements Key<PopViewResult>, PopViewCallback {
    private KeyListener<PopViewResult> listener;

    @Override
    public void create(KeyListener<PopViewResult> listener) {
        this.listener = listener;
    }

    @Override
    public void release() {
        listener = null;
    }

    @Override
    public void autoUnlock(PopViewResult[] value) {

    }

    @Override
    public void onSelectView(PopViewResult result) {
        if (listener != null) {
            listener.onKey(result);
        }
    }

}