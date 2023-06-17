package com.bearya.robot.base.protocol;

import com.bearya.robot.base.walk.Direct;

public interface ILock<V> {
    void unLock(Key<V> key,LockListener<V> listener);
    LockType getType();
    void release();
    V[] getValues();
    void setValues(Direct... objects);
}