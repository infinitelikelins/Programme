package com.bearya.robot.base.protocol;

public interface KeyListener<V> {
    boolean onKey(V value);
    void delay(V value,int second);
}
