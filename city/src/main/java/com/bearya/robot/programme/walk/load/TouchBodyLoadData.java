package com.bearya.robot.programme.walk.load;

import com.bearya.robot.base.play.LoadPlay;
import com.bearya.robot.base.walk.Direct;

public class TouchBodyLoadData {

    private final LoadPlay onNewLoadPlay;
    private final LoadPlay onSuccessPlay;
    private final LoadPlay onFailPlay;
    private final Direct direct;
    private final LoadPlay onDelayPlay;

    public TouchBodyLoadData(LoadPlay onNewLoadPlay, LoadPlay onSuccessPlay, LoadPlay onFailPlay, LoadPlay delay, Direct direct) {
        this.onNewLoadPlay = onNewLoadPlay;
        this.onSuccessPlay = onSuccessPlay;
        this.onFailPlay = onFailPlay;
        this.onDelayPlay = delay;
        this.direct = direct;
    }

    public LoadPlay getOnNewLoadPlay() {
        return onNewLoadPlay;
    }

    public LoadPlay getOnSuccessPlay() {
        return onSuccessPlay;
    }

    public LoadPlay getOnFailPlay() {
        return onFailPlay;
    }

    public Direct getDirect() {
        return direct;
    }

    public LoadPlay getDelay() {
        return onDelayPlay;
    }
}
