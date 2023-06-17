package com.bearya.robot.programme;

import com.bearya.robot.base.BaseApplication;
import com.bearya.robot.base.load.ILoadMgr;
import com.bearya.robot.base.util.DebugUtil;
import com.bearya.robot.programme.repository.AnimationRepository;
import com.bearya.robot.programme.repository.CityRecordRepository;
import com.bearya.robot.programme.repository.PatriotismRepository;
import com.bearya.robot.programme.walk.car.LoadMgr;

public class CityApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        PatriotismRepository.getInstance().init();
        DebugUtil.setDebugMode(BuildConfig.DEBUG);
    }

    @Override
    public void release() {
        super.release();
        CityRecordRepository.getInstance().release();
        AnimationRepository.getInstance().clear();
        PatriotismRepository.getInstance().release();
        LoadMgr.getInstance().release();
    }

    @Override
    public ILoadMgr getLoadMgr() {
        return LoadMgr.getInstance();
    }

}