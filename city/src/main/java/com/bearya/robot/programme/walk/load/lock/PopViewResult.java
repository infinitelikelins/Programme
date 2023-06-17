package com.bearya.robot.programme.walk.load.lock;

public class PopViewResult {
    private final boolean resultRight;
    private final String resultPlayKey;

    public PopViewResult(boolean isRight, String resultPlayKey) {
        this.resultRight = isRight;
        this.resultPlayKey = resultPlayKey;
    }

    public boolean isResult() {
        return resultRight;
    }

    public String getResultPlayKey() {
        return resultPlayKey;
    }
}
