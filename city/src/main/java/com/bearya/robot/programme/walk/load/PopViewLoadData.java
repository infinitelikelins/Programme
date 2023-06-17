package com.bearya.robot.programme.walk.load;

import com.bearya.robot.base.play.LoadPlay;
import com.bearya.robot.programme.walk.load.lock.PopViewData;

public class PopViewLoadData {
    private final LoadPlay onNewLoadPlay;
    private final PopViewData popViewData;
    private final LoadPlay onSuccessRight;
    private final LoadPlay onSuccessWrong;
    private final LoadPlay timePlay;
    private final LoadPlay maxTimeOverPlay;

    public PopViewLoadData(LoadPlay onNewLoadPlay, PopViewData popViewData, LoadPlay onSuccessRight, LoadPlay onSuccessWrong, LoadPlay timePlay, LoadPlay maxTimeOverPlay) {
        this.onNewLoadPlay = onNewLoadPlay;
        this.popViewData = popViewData;
        this.onSuccessRight = onSuccessRight;
        this.onSuccessWrong = onSuccessWrong;
        this.timePlay = timePlay;
        this.maxTimeOverPlay = maxTimeOverPlay;
    }

    public LoadPlay getOnNewLoadPlay() {
        return onNewLoadPlay;
    }

    public PopViewData getPopViewData() {
        return popViewData;
    }

    public LoadPlay getOnSuccessRight() {
        return onSuccessRight;
    }

    public LoadPlay getOnSuccessWrong() {
        return onSuccessWrong;
    }

    public LoadPlay getTimePlay() {
        return timePlay;
    }

    public LoadPlay getMaxTimeOverPlay() {
        return maxTimeOverPlay;
    }
}
