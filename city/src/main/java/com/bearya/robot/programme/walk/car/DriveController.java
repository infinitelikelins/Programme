package com.bearya.robot.programme.walk.car;

import android.graphics.Point;
import android.view.View;

import com.bearya.actionlib.utils.RobotActionManager;
import com.bearya.robot.base.BaseApplication;
import com.bearya.robot.base.load.BaseLoad;
import com.bearya.robot.base.play.AnimationsContainer;
import com.bearya.robot.base.play.Director;
import com.bearya.robot.base.protocol.DriveResult;
import com.bearya.robot.base.protocol.ILock;
import com.bearya.robot.base.protocol.Key;
import com.bearya.robot.base.protocol.LockListener;
import com.bearya.robot.base.protocol.LockType;
import com.bearya.robot.base.ui.BaseActivity;
import com.bearya.robot.base.util.DebugUtil;
import com.bearya.robot.base.util.MusicUtil;
import com.bearya.robot.base.walk.Direct;
import com.bearya.robot.base.walk.InObstacleReason;
import com.bearya.robot.base.walk.LoadEntrance;
import com.bearya.robot.base.walk.RobotInLoadMethod;
import com.bearya.robot.programme.CityApplication;
import com.bearya.robot.programme.activity.GameActivity;
import com.bearya.robot.programme.data.Command;
import com.bearya.robot.programme.view.LoadPopView;
import com.bearya.robot.programme.walk.car.travel.BaseTravelState;
import com.bearya.robot.programme.walk.car.travel.IState;
import com.bearya.robot.programme.walk.car.travel.TWheelController;
import com.bearya.robot.programme.walk.car.travel.TravelPath;
import com.bearya.robot.programme.walk.car.travel.WheelControllerListener;
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

public class DriveController {

    private static final int MAX_TOW_OID_EMPTY_TIMES = 3;
    private static final int MAX_ONE_OID_EMPTY_TIMES = 6;
    private final Engine engine;
    private int headOid;
    private int tailOid;

    private BaseLoad preLoad; // 前一块路
    private LoadEntrance currentLoadEntrance = new LoadEntrance(); // 当前路况:包括小贝从哪进来从哪出去
    private ICar.DriveListener mListener;
    private TWheelController wheelController;
    private Runnable delayStopRunnable;
    private boolean isMoving;
    private Key mKey = null;
    private int oidEmptyTimes = 0;
    private final StayInPlaceWatcher stayInPlaceWatcher = new StayInPlaceWatcher();

    public void towOidEmpty() {
        if (mState == recognitionLoadState) {
            if (oidEmptyTimes < MAX_TOW_OID_EMPTY_TIMES) {
                BaseApplication.getInstance().moveALittle(oidEmptyTimes % 2 == 0);
                oidEmptyTimes++;
                return;
            } else {
                oidEmptyTimes = 0;
                setOutOfLoadState();
            }
        }
        if (System.currentTimeMillis() - lastOneOidEmptyTimeStamp > 5000) {
            oidEmptyTimes = 0;
        }
        lastOneOidEmptyTimeStamp = System.currentTimeMillis();
        if (mState == travelState || mState == exitLoadState) {
            if (oidEmptyTimes < MAX_TOW_OID_EMPTY_TIMES) {
                oidEmptyTimes++;
            } else {
                oidEmptyTimes = 0;
                setOutOfLoadState();
            }
        }
    }

    private long lastOneOidEmptyTimeStamp;

    public void onOidEmpty() {
        if (mState == recognitionLoadState) {
            BaseApplication.getInstance().moveALittle(oidEmptyTimes % 2 == 0);
        }
        if (System.currentTimeMillis() - lastOneOidEmptyTimeStamp > 5000) {
            oidEmptyTimes = 0;
        }
        lastOneOidEmptyTimeStamp = System.currentTimeMillis();
        if (mState == travelState || mState == exitLoadState) {
            if (oidEmptyTimes < MAX_ONE_OID_EMPTY_TIMES) {
                oidEmptyTimes++;
            } else {
                oidEmptyTimes = 0;
                setOutOfLoadState();
            }
        }
    }

    public void oidFull() {
        oidEmptyTimes = 0;
        lastOneOidEmptyTimeStamp = 0;
    }

    private IState mState;
    private IState recognitionLoadState;
    private IState newLoadState;
    private IState computeExitPathState;
    private IState unLockingState;
    private TravelState travelState;
    private IState arriveTargetState;
    private IState exitLoadState;
    private IState inObstacleState;
    private IState outOfLoadState;

    public DriveController() {
        engine = new Engine() {
            @Override
            public void update() {
                if (mState != null) {
                    mState.recognitionLoad();//识别道路
                }
                if (mState != null) {
                    mState.newLoad();//两个读头进入地垫
                }
                if (mState != null) {
                    mState.computeExitPath();//规划出口路径
                }
                if (mState != null) {
                    mState.unLocking();//解锁中
                }
                if (mState != null) {
                    mState.travel();//行走
                }
                if (mState != null) {
                    mState.arriveTarget();//行程完成
                }
                if (mState != null) {
                    mState.exitLoad();//走出当前地垫
                }
                if (mState != null) {
                    mState.inObstacle();//进入避障区
                }
                if (mState != null) {
                    mState.outOfLoad();//走出地垫
                }

            }
        };
        initState();
    }

    private void reset() {
        if (recognitionLoadState != null) {
            recognitionLoadState.reset();
        }
        if (newLoadState != null) {
            newLoadState.reset();
        }
        if (computeExitPathState != null) {
            computeExitPathState.reset();
        }
        if (unLockingState != null) {
            unLockingState.reset();
        }
        if (travelState != null) {
            travelState.reset();
        }
        if (arriveTargetState != null) {
            arriveTargetState.reset();
        }
        if (exitLoadState != null) {
            exitLoadState.reset();
        }
        if (inObstacleState != null) {
            inObstacleState.reset();
        }
        if (outOfLoadState != null) {
            outOfLoadState.reset();
        }

        if (mKey != null) {
            mKey.release();
        }
    }

    public void setState(IState state) {
        DebugUtil.debug("设置为%s状态", state == null ? "空" : state.getClass().getSimpleName());
        if (state != null) {
            state.reset();
        }
        if (mState != null) {
            mState.reset();
        }
        this.mState = state;
    }

    public void setListener(ICar.DriveListener listener) {
        this.mListener = listener;
    }

    /**
     * 用户选择好了出口
     */
    public void onUserChoiceExitDirect(Direct direct) {
        currentLoadEntrance.setUserChoiceDirect(direct);
        setComputeExitPathState("onUserChoiceExitDirect");
    }

    public void setDrive(boolean drive) {
        if (drive) {
            setRecognitionLoadState();
            if (!engine.isRunning()) engine.start();
        } else {
            stopMove();
            engine.stop();
        }
    }

    public void setHeadOid(int oid) {
        this.headOid = oid;
    }

    public void setTailOid(int oid) {
        this.tailOid = oid;
    }

    public void release() {
        setDrive(false);
        mListener = null;
        mState = null;
        recognitionLoadState = null;
        newLoadState = null;
        computeExitPathState = null;
        unLockingState = null;
        travelState = null;
        arriveTargetState = null;
        exitLoadState = null;
        inObstacleState = null;
        outOfLoadState = null;
        if (currentLoadEntrance != null && getCurrentLoad() != null) {
            getCurrentLoad().release();
        }
        LoadMgr.getInstance().clear();
        Director.getInstance().reset();
        currentLoadEntrance = null;
        if (wheelController != null) {
            wheelController.release();
        }
        MusicUtil.stopMusic();
        stopBgMusic();
    }

    private void initState() {
        recognitionLoadState = new RecognitionLoadState();
        newLoadState = new NewLoadState();
        computeExitPathState = new ComputeExitPathState();
        unLockingState = new UnLockingState();
        travelState = new TravelState();
        arriveTargetState = new ArriveTargetState();
        exitLoadState = new ExitLoadState();
        inObstacleState = new InObstacleState();
        outOfLoadState = new OutOfLoadState();
    }

    private void setRecognitionLoadState() {
        reset();
        setState(recognitionLoadState);
    }

    private void setNewLoadState() {
        Director.getInstance().reset();
        setState(newLoadState);
    }

    private void setComputeExitPathState(String caller) {
        if (!isMoving) {
            moving();
        }
        setState(computeExitPathState);
    }

    private void setUnLockingState() {
        setState(unLockingState);
    }

    private void setTravelState(TravelPath travelPath) {
        travelState.setTravelPath(travelPath);
        setState(travelState);
    }

    private void setArriveTargetState() {
        setState(arriveTargetState);
    }

    private void setExitLoadState() {
        setState(exitLoadState);
    }

    private void setInObstacleState(InObstacleReason reason) {
        ((InObstacleState) inObstacleState).setInObstacleReason(reason);
        setState(inObstacleState);
    }

    private void setOutOfLoadState() {
        setState(outOfLoadState);
    }

    class RecognitionLoadState extends BaseTravelState {

        /**
         * 设备不在同一地垫上
         */
        private void onRobotInTowLoad() {
            if (mListener != null) {
                DebugUtil.debug("设备不在同一地垫上================>");
                exception(ICar.DriveException.PutRobotInTowLoad, null);
            }
        }

        /**
         * 设备在同一地垫上
         */
        private void onRobotInOneLoad(BaseLoad load) {
            DebugUtil.debug("前后读头在同一地垫上");
            if (load.getLock() != null) {
                //延迟停止是为了能够彻底走出当前地垫
                BaseApplication.getInstance().getHandler().postDelayed(RobotActionManager::stopWheel, 500);
            }
            load.updateRobotInLoadDirect(headOid, tailOid);
            DebugUtil.debug("放置在%s地垫上,朝向出口为%s", load.getName(), load.getRobotInLoadDirect().getFaceDirect().name());
            currentLoadEntrance.setLoad(load);
            setNewLoadState();
        }


        @Override
        public void recognitionLoad() {
            RobotInLoadInfo robotInLoadInfo = getRobotInLoadInfo();
            if (robotInLoadInfo != null) {
                switch (robotInLoadInfo.state) {
                    case InTowLoad:
                        if (getRobotInLoadMethod() == RobotInLoadMethod.USER_PUT) {
                            onRobotInTowLoad();//两读头分布在两张地垫上
                        }
                        break;
                    case InOneLoad:
                        onRobotInOneLoad(robotInLoadInfo.load);//在同一张地垫上
                        break;
                    case InObstacle:
                        setInObstacleState(getRobotInLoadMethod() == RobotInLoadMethod.USER_PUT ? InObstacleReason.UserPutInObstacle : InObstacleReason.DriveInObstacle);
                        break;
                    case OutOfLoad:
                        setOutOfLoadState();
                        break;
                }
            }
        }

    }

    /**
     * 移动一点点,主要试着解决
     */
    private void moveALittle() {
        RobotActionManager.goAhead(30, 30, "");
        delayStopRunnable = () -> {
            delayStopRunnable = null;
            RobotActionManager.reset();
        };
        BaseApplication.getInstance().getHandler().postDelayed(delayStopRunnable, 500);
    }

    private RobotInLoadInfo getRobotInLoadInfo() {
        if (headOid == 0 && tailOid == 0) {
            return null;
        }
        BaseLoad headLoad = LoadMgr.getInstance().getLoad(headOid);
        BaseLoad tailLoad = LoadMgr.getInstance().getLoad(tailOid);
        if (headLoad == null || tailLoad == null) {
            if (headOid == 0 && delayStopRunnable == null) {
                DebugUtil.debug("无法找到对应道路,移动看一下");
                moveALittle();
            }
            return null;
        }
        Point headPoint = headLoad.converToPoint(headOid);
        Point tailPoint = tailLoad.converToPoint(tailOid);
        if (headLoad != tailLoad) {//前后读头在两张不同的地垫上
//            DebugUtil.debug("Bug2 在不同地垫上--前读头=%d,对应地垫=%s 后读头=%d,对应地垫=%s", headOid, headLoad == null ? "null" : headLoad.getName(), tailOid, tailLoad == null ? "null" : tailLoad.getName());
            return new RobotInLoadInfo(headLoad, RobotInLoadState.InTowLoad, null);
        } else {
            int distance = headLoad.distince(headOid, tailOid);
//            DebugUtil.debug("前后读头距离%d",distance);
            if (distance > 6 && distance < 10) {//前后读头在同一张地垫上
                boolean headInObstacle = headLoad.isInObstacle(headPoint);
                boolean tailInObstacle = headLoad.isInObstacle(tailPoint);
                if (headInObstacle) {
                    return new RobotInLoadInfo(headLoad, RobotInLoadState.InObstacle, headOid);//前读头进入避障区
                }
                if (tailInObstacle) {
                    return new RobotInLoadInfo(tailLoad, RobotInLoadState.InObstacle, tailOid);//后读头进入避障区
                }
                return new RobotInLoadInfo(headLoad, RobotInLoadState.InOneLoad, headLoad);
            } else {//前后读头在两张相同的地垫上
                DebugUtil.debug("Bug2 在相同地垫上--前读头=%d,后读头=%d,对应地垫=%s,读头距离=%d", headOid, tailOid, headLoad.getName(), distance);
                return new RobotInLoadInfo(headLoad, RobotInLoadState.InTowLoad, tailLoad);
            }
        }
    }

    enum RobotInLoadState {
        InObstacle,
        InTowLoad,
        InOneLoad,
        OutOfLoad,
    }

    static class RobotInLoadInfo {
        BaseLoad load;
        RobotInLoadState state;
        Object info;

        public RobotInLoadInfo(BaseLoad load, RobotInLoadState state, Object param) {
            this.load = load;
            this.state = state;
            this.info = param;
        }
    }

    /**
     * 进入新地垫
     */
    class NewLoadState extends BaseTravelState {

        @Override
        public void newLoad() {
            BaseLoad currentLoad = currentLoadEntrance.getLoad();
            if (currentLoad != null) {
                Direct entranceDirect = currentLoad.getRobotInLoadDirect().getEntranceDirect();
                int entranceOid = currentLoad.getEntrance(entranceDirect);
                if (entranceOid > 0) {
                    DebugUtil.debug("进入%s地垫,oid=%d,entranceOid=%d", currentLoad.getName(), tailOid, entranceOid);
                    currentLoadEntrance.setEntranceOid(entranceOid);
                    if (currentLoad.hasLock()) {
                        stopMove();
                        setUnLockingState();
                    } else {
                        setComputeExitPathState("newLoad not lock");
                    }
                }
            }
        }
    }

    /**
     * 返回小贝是如何上来
     *
     * @return RobotInLoadMethod
     */
    private RobotInLoadMethod getRobotInLoadMethod() {
        return preLoad == null ? RobotInLoadMethod.USER_PUT : RobotInLoadMethod.DRIVE;
    }

    /**
     * 计算出口路径状态
     */
    class ComputeExitPathState extends BaseTravelState {
        @Override
        public void computeExitPath() {
            if (isPerform()) {
                return;
            }
            DebugUtil.debug("哪一张地垫 ==========> %s", preLoad != null ? preLoad.getName() : "");
            int[] oids = currentLoadEntrance.computeExitPath(getRobotInLoadMethod());
            onSureOidPath(oids);
            doPerform();
        }

        /**
         * 至此已经确定了从入口到出口的OID经过点
         */
        public void onSureOidPath(int[] oids) {
            if (oids != null) {
                if (oids.length == 0) {
                    setOutOfLoadState();
                    return;
                }
                currentLoadEntrance.setExitOid(oids[oids.length - 1]);
                LoadMgr.getInstance().addHistory(currentLoadEntrance.newInstance());
                TravelPath travelPath = new TravelPath(oids);
                setTravelState(travelPath);
            } else {
                setOutOfLoadState();
            }
        }
    }

    /**
     * 等待解锁状态
     */
    class UnLockingState extends BaseTravelState {

        private int unlockFailTimes;

        @Override
        public void unLocking() {
            if (isPerform()) {
                return;
            }
            doPerform();
            BaseLoad currentLoad = currentLoadEntrance.getLoad();
            DebugUtil.debug("%s-执行解锁", currentLoad.getName());
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
            currentLoad.registerPlay();
            if (wheelController != null) {
                wheelController.stop();
            }
            PopViewKey key = new PopViewKey();
            lock.getData().setCallback(key);
            lock.unLock(key, new LockListener<PopViewResult>() {
                @Override
                public void onLocking() {
                    Director.getInstance().director(BaseLoad.ON_NEW_LOAD, () -> {
                        CityApplication.getInstance().getHandler().post(() -> {
                            BaseActivity activity = BaseActivity.getTopActivity();
                            if (activity != null) {
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
                    Director.getInstance().director(value.getResultPlayKey(), () -> {
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

        private TouchBodyKey unlockTouchBody(TouchBodyLock lock, final BaseLoad currentLoad) {
            currentLoad.registerPlay();
            if (wheelController != null) {
                wheelController.stop();
            }
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

        private Key unlockDirectorPlay(final BaseLoad load) {
            load.registerPlay();
            if (wheelController != null) {
                wheelController.stop();
            }
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

        private DirectKey unlockDirect(final DirectLock directLock, final BaseLoad currentLoad) {
            currentLoad.registerPlay();
            if (wheelController != null) {
                wheelController.stop();
            }

            directLock.setEntranceDirect(currentLoad.getEntranceDirect(currentLoadEntrance.getEntranceOid()));
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
                    onUserChoiceExitDirect(userChoiceDirect);

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

        @Override
        public void reset() {
            super.reset();
            unlockFailTimes = 0;
        }

        public void registerScreenClickedListener(View.OnClickListener listener) {
            BaseActivity activity = BaseActivity.getTopActivity();
            GameActivity gameActivity = activity instanceof GameActivity ? (GameActivity) activity : null;
            if (gameActivity != null) {
                gameActivity.setScreenClickListener(listener);
            }
        }
    }

    /**
     * oid路径走完
     */
    public void onOidPathComplete() {
        DebugUtil.debug("达到终点目标");
        setArriveTargetState();
    }

    /**
     * 寻迹状态
     */
    class TravelState extends BaseTravelState {
        private TravelPath travelPath;

        @Override
        public void travel() {
            RobotInLoadInfo robotInLoadInfo = getRobotInLoadInfo();
            if (robotInLoadInfo != null && robotInLoadInfo.state == RobotInLoadState.InObstacle) {
                setInObstacleState(InObstacleReason.DriveInObstacle);
                return;
            }
            if (wheelController == null) {
                wheelController = new TWheelController(DriveController.this, new WheelControllerListener() {
                    @Override
                    public int getHeaderOid() {
                        return headOid;
                    }

                    @Override
                    public int getTailerOid() {
                        return tailOid;
                    }

                    @Override
                    public void onCompleteTravelPath() {
                        onOidPathComplete();
                    }
                });
                wheelController.setTravelPath(travelPath);
                DebugUtil.debug("寻迹中wheelController ====> %s", wheelController != null ? "wheelController初始化完毕" : "wheelController初始化失败");
            }
            wheelController.drive();
            stayInPlaceWatcher.setLocate(headOid, tailOid);

        }

        public void setTravelPath(TravelPath travelPath) {
            this.travelPath = travelPath;
        }
    }

    public int getAngle(int target) {
        return getCurrentLoad().getAngle(headOid, tailOid, target);
    }

    public int getAngle(Point target) {
        return getCurrentLoad().getAngle(headOid, tailOid, target);
    }

    /**
     * 到达目标点状态
     */
    class ArriveTargetState extends BaseTravelState {

        private Point facePoint;

        @Override
        public void arriveTarget() {
            if (wheelController != null) {
                wheelController.stop();
                wheelController.release();
                wheelController = null;
            }
            if (facePoint == null) {
                facePoint = getFacePoint();
            }
            adjustAngle();

        }

        /**
         * 获取出口点垂直距离dis远的OID
         */
        private Point getFacePoint() {
            int exitOid = currentLoadEntrance.getExitOid();
            Direct direct = currentLoadEntrance.getLoad().getEntranceDirect(exitOid);
            Point facePoint = null;
            Point p = currentLoadEntrance.getLoad().converToPoint(exitOid);
            int dis = 50;
            if (direct == Direct.Forward) {
                facePoint = new Point(p.x, p.y - dis);
            } else if (direct == Direct.Backward) {
                facePoint = new Point(p.x, p.y + dis);
            } else if (direct == Direct.Left) {
                facePoint = new Point(p.x - dis, p.y);
            } else if (direct == Direct.Right) {
                facePoint = new Point(p.x + dis, p.y);
            }
            return facePoint;
        }

        /**
         * 调整角度值接近0度
         */
        private void adjustAngle() {
            int angle = getAngle(facePoint);
            DebugUtil.debug("facePoint = (%d,%d),adjustAngle=%d", facePoint.x, facePoint.y, angle);
            if (angleNearZero(angle)) {
                adjustComplete();
            } else {
                int speed = 10;
                if (angle < 180) {
                    RobotActionManager.turnLeft(speed, speed, "编程地垫游戏");
                } else {
                    RobotActionManager.turnRight(speed, speed, "编程地垫游戏");
                }
            }
        }

        /**
         * 角度调整完毕
         */
        private void adjustComplete() {
            facePoint = null;
            final DriveResult result = currentLoadEntrance.getLoad().onResult(getRobotInLoadMethod());
            if (result == DriveResult.EndLoad || result == DriveResult.PatriotismEndLoad) {
                pause();
                currentLoadEntrance.getLoad().registerPlay();
                Director.getInstance().director(BaseLoad.ON_END_LOAD, () -> {
                    if (mListener != null) {
                        mListener.onDriveResult(result);
                    }
                });
            } else {
                setExitLoadState();
            }
        }

        /**
         * 是否在误差范围内
         */
        protected boolean angleNearZero(int angle) {
            int allowErrorAngle = 8;
            return (angle >= 0 && angle <= allowErrorAngle) || (angle <= 360 && angle >= (360 - allowErrorAngle));
        }

    }

    /**
     * 走出当前地垫状态
     */
    class ExitLoadState extends BaseTravelState {
        private int inTownLoadTimes = 0;

        @Override
        public void exitLoad() {
            RobotInLoadInfo robotInLoadInfo = getRobotInLoadInfo();
            if (robotInLoadInfo == null) {
                return;
            }
            if (robotInLoadInfo.state == RobotInLoadState.InObstacle) {
                RobotActionManager.stopWheel();
                setInObstacleState(InObstacleReason.LoadConnectException);
                return;
            }
            int dis = getCurrentLoad().distince(headOid, tailOid);
            if ((dis < 7 || dis > 10) || (LoadMgr.getInstance().getLoad(headOid) != LoadMgr.getInstance().getLoad(tailOid))) {
                inTownLoadTimes++;
                if (inTownLoadTimes == 1) {
                    RobotActionManager.goAhead(30, 30, "走直线");
                    if (preLoad != null) {
                        preLoad.recordEntranceInfo(true, currentLoadEntrance.getExitOid());
                    }
                }
            } else {
                if (inTownLoadTimes > 5) {
                    if (preLoad != null) {
                        preLoad.recordEntranceInfo(false, currentLoadEntrance.getExitOid());
                    }
                    setRecognitionLoadState();
                } else {
                    RobotActionManager.goAhead(30, 30, "走直线");
                    preLoad = currentLoadEntrance.getLoad();
                }
            }
            stayInPlaceWatcher.setLocate(headOid, tailOid);
        }

        @Override
        public void reset() {
            super.reset();
            inTownLoadTimes = 0;
        }
    }


    /**
     * 身处避障区状态
     */
    class InObstacleState extends BaseTravelState {
        private InObstacleReason reason;

        @Override
        public void inObstacle() {
            if (!isPerform() && mListener != null) {
                DebugUtil.debug("身处避障区状态===================>");
                exception(ICar.DriveException.InObstacle, reason);
                preLoad = null;
            }
            doPerform();
        }

        public void setInObstacleReason(InObstacleReason reason) {
            this.reason = reason;
        }
    }

    /**
     * 走出地垫状态
     */
    class OutOfLoadState extends BaseTravelState {
        @Override
        public void outOfLoad() {
            stopMove();
            if (!isPerform() && mListener != null) {
                DebugUtil.debug("走出了地垫=======================>");
                exception(ICar.DriveException.OutOfLoad, null);
                preLoad = null;
            }
            doPerform();
        }
    }

    public BaseLoad getCurrentLoad() {
        return currentLoadEntrance.getLoad();
    }

    private void moving() {
        DebugUtil.debug("moving");
        isMoving = true;
        BaseApplication.sendAction(Command.format(Command.CITY_MOVE));

        Director.getInstance().playMovingEmotion();
        MusicUtil.playTravelBgMusic();
        RobotActionManager.handShake(80);
    }

    private void stopMove() {
        DebugUtil.error("stopMove");
        isMoving = false;
        stopBgMusic();
        RobotActionManager.reset();
    }

    private void stopBgMusic() {
        MusicUtil.stopBgMusic();
    }

    private void exception(ICar.DriveException exception, Object param) {
        BaseLoad load = getCurrentLoad();
        if (load != null) {
            if (load.getLock() != null) {
                load.getLock().release();
            }
        }
        if (mKey != null) {
            mKey.release();
            mKey = null;
        }
        CityApplication.getInstance().getHandler().post(() -> {
            if (AnimationsContainer.getInstance() != null) {
                if (AnimationsContainer.getInstance().getProgressDialogAnim() != null) {
                    AnimationsContainer.getInstance().getProgressDialogAnim().release();
                }
            }
        });

        Director.getInstance().reset();
        headOid = 0;
        tailOid = 0;
        pause();
        if (mListener != null) {
            mListener.onException(exception, param);
        }
    }

    private void pause() {
        BaseLoad load = getCurrentLoad();
        if (load != null) {
            load.release();
        }
        if (wheelController != null) {
            wheelController.release();
            wheelController = null;
        }
        setDrive(false);
        setState(null);
        stopMove();
    }

    private static class StayInPlaceWatcher {
        public static final long WATCHER_DURING = 2000;
        int lastHeadOidLocate;
        int lastTailOidLocate;
        long lastWatcherTimestamp;

        public void setLocate(int hOid, int tOid) {
            if (System.currentTimeMillis() - lastWatcherTimestamp > WATCHER_DURING) {
                if (isStayInPlace(hOid, tOid)) {
                    RobotActionManager.resetSpeed();
                    RobotActionManager.goAhead(30, 30, "");
                    DebugUtil.debug("补发前进指令");
                }
                lastWatcherTimestamp = System.currentTimeMillis();
            }
        }

        private boolean isStayInPlace(int hOid, int tOid) {
            if (hOid == 0 || tOid == 0) {
                return false;
            }
            boolean isStayInPlace = false;
            if ((hOid == lastHeadOidLocate && tOid == lastTailOidLocate)) {
                isStayInPlace = true;
            }
            if (!isStayInPlace && hOid == lastHeadOidLocate) {
                BaseLoad tLoad = LoadMgr.getInstance().getLoad(hOid);
                int d = tLoad.distince(hOid, lastHeadOidLocate);
                if (d <= 1) {
                    isStayInPlace = true;
                }
            }
            if (!isStayInPlace && tOid == lastTailOidLocate) {
                BaseLoad tLoad = LoadMgr.getInstance().getLoad(tOid);
                int d = tLoad.distince(tOid, lastTailOidLocate);
                if (d <= 1) {
                    isStayInPlace = true;
                }
            }
            lastHeadOidLocate = hOid;
            lastTailOidLocate = tOid;
            return isStayInPlace;
        }
    }

}
