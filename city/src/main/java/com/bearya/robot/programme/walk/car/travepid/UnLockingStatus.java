package com.bearya.robot.programme.walk.car.travepid;

import android.view.View;

import com.bearya.robot.base.BaseApplication;
import com.bearya.robot.base.load.BaseLoad;
import com.bearya.robot.base.play.Director;
import com.bearya.robot.base.protocol.ILock;
import com.bearya.robot.base.protocol.Key;
import com.bearya.robot.base.protocol.LockListener;
import com.bearya.robot.base.protocol.LockType;
import com.bearya.robot.base.ui.BaseActivity;
import com.bearya.robot.base.util.DebugUtil;
import com.bearya.robot.base.walk.Direct;
import com.bearya.robot.programme.CityApplication;
import com.bearya.robot.programme.activity.GameActivity;
import com.bearya.robot.programme.view.LoadPopView;
import com.bearya.robot.programme.walk.car.DriveController2;
import com.bearya.robot.programme.walk.car.ICar;
import com.bearya.robot.programme.walk.car.LoadMgr;
import com.bearya.robot.programme.walk.load.LovingHeartLoad;
import com.bearya.robot.programme.walk.load.NoEntryLoad;
import com.bearya.robot.programme.walk.load.lock.BaseLock;
import com.bearya.robot.programme.walk.load.lock.DirectKey;
import com.bearya.robot.programme.walk.load.lock.DirectLock;
import com.bearya.robot.programme.walk.load.lock.PopViewKey;
import com.bearya.robot.programme.walk.load.lock.PopViewLock;
import com.bearya.robot.programme.walk.load.lock.PopViewResult;
import com.bearya.robot.programme.walk.load.lock.TouchBodyKey;
import com.bearya.robot.programme.walk.load.lock.TouchBodyLock;

/**
 * 等待解锁状态
 */
public class UnLockingStatus implements IStatusAtion {

    private int unlockFailTimes;
    private boolean perform;
    private final DriveController2 driveController;

    private Key mKey = null;

    public UnLockingStatus(DriveController2 driveController2) {
        driveController = driveController2;
    }

    protected synchronized void doPerform() {
        perform = true;
    }

    @Override
    public void start() {
        perform = false;
        driveController.stopMove();
    }

    @Override
    public void update() {
        if (perform) {
            return;
        }
        doPerform();
        BaseLoad currentLoad = driveController.getCurrentLoad();
        DebugUtil.info("道路执行解锁 -- %s", currentLoad.getName());
        ILock lock = currentLoad.getLock();
        mKey = null;
        LockType type = lock.getType();
        if (type == LockType.Direct) {
            mKey = unlockDirect((DirectLock) lock, currentLoad);
        } else if (type == LockType.DirectorPlay) {
            mKey = unlockDirectorPlay(currentLoad);
        } else if (type == LockType.TouchBody) {
            mKey = unlockTouchBody((TouchBodyLock) lock, currentLoad);
        } else if (type == LockType.PopView) {
            mKey = unlockPopView((PopViewLock) lock, currentLoad);
        }
        if (BaseApplication.AUTO_UNLOCK && mKey != null) {
            mKey.autoUnlock(lock.getValues());
        }
    }

    private PopViewKey unlockPopView(final PopViewLock lock, final BaseLoad currentLoad) {
        DebugUtil.info("弹窗执行动画选择 -- 注册动画 和 等待");
        currentLoad.registerPlay();
        PopViewKey key = new PopViewKey();
        lock.getData().setCallback(key);
        lock.unLock(key, new LockListener<PopViewResult>() {
            @Override
            public void onLocking() {
                DebugUtil.info("弹窗执行动画选择 --  已知 发现新道路，那么播放新道路动画");
                Director.getInstance().director(BaseLoad.ON_NEW_LOAD, () -> {
                    DebugUtil.info("弹窗执行动画选择 --  新道路的动画播放完成");
                    CityApplication.getInstance().getHandler().post(() -> {
                        BaseActivity activity = BaseActivity.getTopActivity();
                        if (activity != null) {
                            DebugUtil.info("弹窗执行动画选择 --  新道路的动画出现弹窗");
                            LoadPopView loadPopView = new LoadPopView(activity);
                            loadPopView.playByData(lock.getData());
                            loadPopView.setLock((BaseLock) currentLoad.getLock());
                            activity.removeView(LoadPopView.class.getSimpleName());
                            loadPopView.addToActivity(activity);
                        }
                    });
                });
            }

            @Override
            public void onSuccess(PopViewResult value, Object param) {
                currentLoad.getLock().release();
                if (!value.isResult()) {
                    unlockFailTimes++;
                } else if (unlockFailTimes >= 0) {
                    unlockFailTimes = 0;
                    LoadMgr.getInstance().addAGoodDone();
                }
                String resultPlayKey = value.getResultPlayKey();
                DebugUtil.info("弹窗执行动画选择 --  新道路的动画弹窗选择完成 结果是 =  %s", resultPlayKey);
                Director.getInstance().director(resultPlayKey, () -> {
                    setComputeExitPathState("unlockPopView ->onSuccess");
                    reset();
                });
            }

            @Override
            public void onFail(PopViewResult value, Object param) {

            }

            @Override
            public void onTimeout() {
                Director.getInstance().director(BaseLoad.ON_LOCK_TIMEOUT, null);
            }

            @Override
            public void onMaxTimeOver() {
                BaseActivity.getTopActivity().removeView(LoadPopView.class.getSimpleName());
                Director.getInstance().director(BaseLoad.ON_LOCK_MAX_TIMEOUT, () -> {
                    setComputeExitPathState("unlockPopView->onMaxTimeOver");
                    reset();
                });
            }

            @Override
            public void delay(PopViewResult value, int delay) {

            }
        });
        return key;
    }

    private void setComputeExitPathState(String s) {
        driveController.setComputeExitPathState(s);
    }

    private TouchBodyKey unlockTouchBody(TouchBodyLock lock, final BaseLoad currentLoad) {
        currentLoad.registerPlay();
        final TouchBodyKey directKey = new TouchBodyKey();
        registerScreenClickedListener(directKey);
        lock.unLock(directKey, new LockListener<Direct>() {

            @Override
            public void onLocking() {
                Director.getInstance().director(BaseLoad.ON_NEW_LOAD, null);
            }

            @Override
            public void onSuccess(Direct target, Object param) {
                directKey.release();
                if (unlockFailTimes >= 0) {
                    unlockFailTimes = 0;
                    LoadMgr.getInstance().addAGoodDone();
                }
                Director.getInstance().director(BaseLoad.ON_UNLOCK_SUCCESS, this::release);
            }

            @Override
            public void onFail(Direct value, Object param) {
                if (currentLoad instanceof LovingHeartLoad)
                    ((LovingHeartLoad) currentLoad).registerLoadPlay();
                unlockFailTimes++;
                Director.getInstance().director(BaseLoad.ON_UNLOCK_FAIL, null);
            }

            @Override
            public void onTimeout() {
                Director.getInstance().director(BaseLoad.ON_LOCK_TIMEOUT, null);
            }

            @Override
            public void onMaxTimeOver() {
                directKey.release();
                Director.getInstance().director(BaseLoad.ON_LOCK_MAX_TIMEOUT, this::release);
            }

            @Override
            public void delay(Direct value, int delay) {

            }

            private void release() {
                registerScreenClickedListener(null);
                Director.getInstance().reset();
                setComputeExitPathState("unlockTouchBody->release");
                reset();
            }
        });
        return directKey;
    }

    public void registerScreenClickedListener(View.OnClickListener listener) {
        BaseActivity activity = BaseActivity.getTopActivity();
        GameActivity gameActivity = activity instanceof GameActivity ? (GameActivity) activity : null;
        if (gameActivity != null) {
            gameActivity.setScreenClickListener(listener);
        }
    }

    private Key unlockDirectorPlay(final BaseLoad load) {
        load.registerPlay();
        BaseApplication.getInstance().getHandler().postDelayed(() -> Director.getInstance().director(BaseLoad.ON_NEW_LOAD, () -> {
            Director.getInstance().reset();
            if (load instanceof NoEntryLoad) {//禁止通行
                exception(ICar.DriveException.NoEntry, null);
            } else {
                setComputeExitPathState("unlockDirectorPlay not NoEntryLoad");
            }
            reset();
        }), 300);
        return null;
    }

    private void exception(ICar.DriveException noEntry, Object o) {
        if (mKey != null) {
            mKey.release();
            mKey = null;
        }
        driveController.exception(noEntry, o);
    }


    private DirectKey unlockDirect(final DirectLock directLock, final BaseLoad currentLoad) {
        currentLoad.registerPlay();
        directLock.setEntranceDirect(currentLoad.getEntranceDirect(driveController.tailInfo.getEntranceoid()));
        final DirectKey directKey = new DirectKey();
        directLock.unLock(directKey, new LockListener<Direct>() {

            @Override
            public void onLocking() {
                Director.getInstance().director(BaseLoad.ON_NEW_LOAD, null);
            }

            @Override
            public void onSuccess(final Direct target, Object param) {
                final Direct userChoiceDirect = (Direct) param;
                String sound = null;
                if (userChoiceDirect == Direct.Left) {
                    sound = "tk_turn_left";
                } else if (userChoiceDirect == Direct.Right) {
                    sound = "tk_turn_right";
                } else if (userChoiceDirect == Direct.Forward) {
                    sound = "tk_go_straight";
                } else if (userChoiceDirect == Direct.Backward) {
                    sound = "tk_turn_back";
                }
                Director.getInstance().stop();
                Director.getInstance().director(BaseLoad.ON_UNLOCK_SUCCESS, sound, () -> reset());
                driveController.onUserChoiceMutilExitDirect(userChoiceDirect);
            }

            @Override
            public void onFail(Direct value, Object param) {
                final Direct userChoiceDirect = (Direct) param;
                String sound = null;
                if (userChoiceDirect == Direct.Left) {
                    sound = "mistake_left";
                } else if (userChoiceDirect == Direct.Right) {
                    sound = "mistake_right";
                } else if (userChoiceDirect == Direct.Forward) {
                    sound = "mistake_front";
                } else if (userChoiceDirect == Direct.Backward) {
                    sound = "mistake_back";
                }
                Director.getInstance().director(BaseLoad.ON_UNLOCK_FAIL, sound, currentLoad::registerPlay);
            }

            @Override
            public void onTimeout() {
                Director.getInstance().director(BaseLoad.ON_LOCK_TIMEOUT, () -> DebugUtil.debug("onTimeout==============>超时音频播放完成"));
            }

            @Override
            public void onMaxTimeOver() {
                Director.getInstance().director(BaseLoad.ON_LOCK_MAX_TIMEOUT, null);
            }

            @Override
            public void delay(Direct value, int delay) {

            }
        });
        return directKey;
    }

    public void reset() {
        perform = false;
        unlockFailTimes = 0;
    }
}