package com.bearya.robot.programme.walk.car;

import android.os.SystemClock;

public abstract class Engine implements Runnable {

    private boolean isLaunch = true;
    private boolean isRunning = false;
    public static final int F = 10;//每秒执行次数
    public static final int F_T = 1000 / F;//每次执行时间
    private Thread carEngine = null;

    @Override
    public void run() {
        while (isLaunch) {
            if (isRunning) {
                update();
                SystemClock.sleep(F_T);
            }
        }
    }

    public void start() {
        isLaunch = true;
        isRunning = true;
        if (carEngine == null) {
            carEngine = new Thread(this, "Car-Engine");
            carEngine.start();
        }
    }

    public void resume() {
        isRunning = true;
    }

    public void pause() {
        isRunning = false;
    }

    public void stop() {
        isLaunch = false;
        isRunning = false;
        if (carEngine != null) {
            carEngine.interrupt();
            carEngine = null;
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public abstract void update();
}
