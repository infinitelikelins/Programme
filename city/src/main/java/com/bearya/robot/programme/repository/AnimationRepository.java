package com.bearya.robot.programme.repository;

import com.bearya.robot.base.util.DebugUtil;
import com.bearya.robot.programme.repository.animations.AnimationType;
import com.bearya.robot.programme.repository.animations.CivilizationAnimation;
import com.bearya.robot.programme.repository.animations.LovingHeartAnimation;
import com.bearya.robot.programme.repository.animations.ProtectionAnimation;
import com.bearya.robot.programme.repository.animations.SafeAnimation;
import com.bearya.robot.programme.walk.load.PopViewLoadData;
import com.bearya.robot.programme.walk.load.TouchBodyLoadData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AnimationRepository {

    private static AnimationRepository animationRepository = null;

    public static AnimationRepository getInstance() {
        if (animationRepository == null)
            animationRepository = new AnimationRepository();
        return animationRepository;
    }

    private final Random random;

    private final List<PopViewLoadData> safeAnimationList;
    private final List<PopViewLoadData> protectionAnimationList;
    private final List<PopViewLoadData> civilizationAnimationList;
    private final List<TouchBodyLoadData> lovingHeartAnimateList;

    private final Map<String, List<PopViewLoadData>> unUsedPopViewMap = new HashMap<>();
    private final Map<String, List<TouchBodyLoadData>> unUsedLoadPlayMap = new HashMap<>();

    private AnimationRepository() {
        random = new Random();
        safeAnimationList = new SafeAnimation().generateAnimation();
        protectionAnimationList = new ProtectionAnimation().generateAnimation();
        civilizationAnimationList = new CivilizationAnimation().generateAnimation();
        lovingHeartAnimateList = new LovingHeartAnimation().generateAnimation();
    }

    private List<PopViewLoadData> getPopViewAnimate(AnimationType type) {
        switch (type) {
            case SecurityGuards: return safeAnimationList; // 安全卫士
            case EnvironmentalTalent: return protectionAnimationList; // 环保达人
            case PacesetterCivilization: return civilizationAnimationList; // 文明标兵
            default: return new ArrayList<>();
        }
    }

    private List<TouchBodyLoadData> getTouchBodyAnimate(AnimationType type) {
        if (type == AnimationType.LovingHeart) return lovingHeartAnimateList; // 爱心小天使
        return new ArrayList<>();
    }

    public PopViewLoadData getPopViewDataByLoad(AnimationType type) {
        String key = type.name();
        DebugUtil.info("获取 PopViewDataByLoadData : %s", key);
        List<PopViewLoadData> unUsedPopViewList = unUsedPopViewMap.get(key);
        if (unUsedPopViewList == null || unUsedPopViewList.size() == 0) {
            unUsedPopViewList = new ArrayList<>(getPopViewAnimate(type));
            unUsedPopViewMap.put(key, unUsedPopViewList);
        }
        return unUsedPopViewList.remove(random.nextInt(unUsedPopViewList.size()));
    }

    public TouchBodyLoadData getTouchBodyDataByLoad(AnimationType type) {
        String key = type.name();
        List<TouchBodyLoadData> unUsedLoadPlays = unUsedLoadPlayMap.get(key);
        if (unUsedLoadPlays == null || unUsedLoadPlays.size() == 0) {
            unUsedLoadPlays = new ArrayList<>(getTouchBodyAnimate(type));
            unUsedLoadPlayMap.put(key, unUsedLoadPlays);
        }
        return unUsedLoadPlays.remove(random.nextInt(unUsedLoadPlays.size()));
    }

    public void clear() {
        unUsedPopViewMap.clear();
        unUsedLoadPlayMap.clear();
    }

}