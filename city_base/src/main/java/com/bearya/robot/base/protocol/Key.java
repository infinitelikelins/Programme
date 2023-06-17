package com.bearya.robot.base.protocol;

public interface Key<V> {

    void create(KeyListener<V> listener);

    void release();

    void autoUnlock(V[] value);

}
