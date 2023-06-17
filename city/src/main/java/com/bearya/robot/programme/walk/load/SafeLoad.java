package com.bearya.robot.programme.walk.load;

import com.bearya.robot.base.play.Director;
import com.bearya.robot.base.play.LoadPlay;
import com.bearya.robot.base.util.DebugUtil;
import com.bearya.robot.programme.repository.AnimationRepository;
import com.bearya.robot.programme.repository.animations.AnimationType;
import com.bearya.robot.programme.walk.load.lock.PopViewLock;

/**
 * 安全卫士
 */
public class SafeLoad extends StraightLoad {
    public static final int START_OID = 9100;
    public static final String NAME = "安全卫士";

    public SafeLoad() {
        super(START_OID);
        lock = new PopViewLock();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void registerPlay() {
        DebugUtil.info("注册了 安全卫士 动画事件, 将会 生成 新的 动画事件");
        PopViewLoadData popViewLoadData = AnimationRepository.getInstance().getPopViewDataByLoad(AnimationType.SecurityGuards);
        ((PopViewLock) lock).setData(popViewLoadData.getPopViewData());
        Director.getInstance().register(ON_NEW_LOAD, new LoadPlay(popViewLoadData.getOnNewLoadPlay()));
        Director.getInstance().register(ON_UNLOCK_SUCCESS_RIGHT, new LoadPlay(popViewLoadData.getOnSuccessRight()));
        Director.getInstance().register(ON_UNLOCK_SUCCESS_WRONG, new LoadPlay(popViewLoadData.getOnSuccessWrong()));
        Director.getInstance().register(ON_LOCK_TIMEOUT, new LoadPlay(popViewLoadData.getTimePlay()));
        Director.getInstance().register(ON_LOCK_MAX_TIMEOUT, new LoadPlay(popViewLoadData.getMaxTimeOverPlay()));
    }
}

