package com.bearya.robot.programme.walk.car.travepid;

import com.bearya.robot.programme.walk.car.DriveController2;

public class ExceptionStatus implements IStatusAtion {
    private DriveController2 driveController;

    public ExceptionStatus(DriveController2 driveController2) {
        driveController = driveController2;
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {

    }

    @Override
    public void reset() {

    }
}