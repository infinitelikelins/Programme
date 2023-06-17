package com.bearya.robot.programme.walk.load;

import com.bearya.robot.base.play.Director;
import com.bearya.robot.base.play.LoadPlay;
import com.bearya.robot.programme.repository.AnimationRepository;
import com.bearya.robot.programme.repository.animations.AnimationType;
import com.bearya.robot.programme.walk.load.lock.TouchBodyLock;

/**
 * 爱心小天使
 */
public class LovingHeartLoad extends StraightLoad {

    public static final int START_OID = 10000;
    public static final String NAME = "爱心小天使";
    private TouchBodyLoadData touchBodyLoadData;

    public LovingHeartLoad() {
        super(START_OID);
        lock = new TouchBodyLock();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void registerPlay() {
        touchBodyLoadData = AnimationRepository.getInstance().getTouchBodyDataByLoad(AnimationType.LovingHeart);
        lock.setValues(touchBodyLoadData.getDirect());
        registerLoadPlay();
    }

    public void registerLoadPlay() {
        Director.getInstance().register(ON_NEW_LOAD, new LoadPlay(touchBodyLoadData.getOnNewLoadPlay()));
        Director.getInstance().register(ON_UNLOCK_SUCCESS, new LoadPlay(touchBodyLoadData.getOnSuccessPlay()));
        Director.getInstance().register(ON_UNLOCK_FAIL, new LoadPlay(touchBodyLoadData.getOnFailPlay()));
        Director.getInstance().register(ON_LOCK_MAX_TIMEOUT, new LoadPlay(touchBodyLoadData.getDelay()));
    }

}
