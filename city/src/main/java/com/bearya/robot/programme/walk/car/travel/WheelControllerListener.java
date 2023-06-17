package com.bearya.robot.programme.walk.car.travel;

public interface WheelControllerListener {
    int getHeaderOid();
    int getTailerOid();
    void onCompleteTravelPath();
}
