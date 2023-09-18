package com.bearya.robot.programme.walk.car;

import com.bearya.robot.base.load.BaseLoad;
import com.bearya.robot.base.load.ILoadMgr;
import com.bearya.robot.base.util.DebugUtil;
import com.bearya.robot.base.walk.LoadEntrance;
import com.bearya.robot.programme.walk.load.CivilizedPacesetterLoad;
import com.bearya.robot.programme.walk.load.EevProLoad;
import com.bearya.robot.programme.walk.load.EndLoad;
import com.bearya.robot.programme.walk.load.FlagLoad;
import com.bearya.robot.programme.walk.load.GoldCoinLoad;
import com.bearya.robot.programme.walk.load.KnownCultureLoad;
import com.bearya.robot.programme.walk.load.KnownFestivalLoad;
import com.bearya.robot.programme.walk.load.KnownHistoryLoad;
import com.bearya.robot.programme.walk.load.KnownLoad;
import com.bearya.robot.programme.walk.load.KnownPatriotismLoad;
import com.bearya.robot.programme.walk.load.KnownScenicSpotsLoad;
import com.bearya.robot.programme.walk.load.KnownScienceTechLoad;
import com.bearya.robot.programme.walk.load.LovingHeartLoad;
import com.bearya.robot.programme.walk.load.NoEntryLoad;
import com.bearya.robot.programme.walk.load.PatriotismEndLoad;
import com.bearya.robot.programme.walk.load.SafeLoad;
import com.bearya.robot.programme.walk.load.StartLoad;
import com.bearya.robot.programme.walk.load.StationBlueLoad;
import com.bearya.robot.programme.walk.load.StationGreenLoad;
import com.bearya.robot.programme.walk.load.StationLoad;
import com.bearya.robot.programme.walk.load.StationPinkLoad;
import com.bearya.robot.programme.walk.load.StationPurpleLoad;
import com.bearya.robot.programme.walk.load.StationRedLoad;
import com.bearya.robot.programme.walk.load.StationYellowLoad;
import com.bearya.robot.programme.walk.load.StraightLoad;
import com.bearya.robot.programme.walk.load.TLoad;
import com.bearya.robot.programme.walk.load.TurnLoad;
import com.bearya.robot.programme.walk.load.XLoad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadMgr implements ILoadMgr {

    private final Map<String, BaseLoad> loads = new HashMap<>();
    private final List<LoadEntrance> loadHistory = new ArrayList<>(); // 走过的路
    private int goodDoneNumber;

    private static LoadMgr instance;
    private String tag;

    public static LoadMgr getInstance() {
        if (instance == null)
            instance = new LoadMgr();
        return instance;
    }

    private LoadMgr() {
        loads.put(StartLoad.NAME, new StartLoad());

        loads.put(PatriotismEndLoad.NAME, new PatriotismEndLoad());
        loads.put(EndLoad.NAME, new EndLoad());

        loads.put(StraightLoad.NAME, new StraightLoad());
        loads.put(TurnLoad.NAME, new TurnLoad());
        loads.put(XLoad.NAME, new XLoad());
        loads.put(TLoad.NAME, new TLoad());

        loads.put(NoEntryLoad.NAME, new NoEntryLoad());

        loads.put(GoldCoinLoad.NAME, new GoldCoinLoad());
        loads.put(FlagLoad.NAME, new FlagLoad());

        loads.put(EevProLoad.NAME, new EevProLoad());
        loads.put(CivilizedPacesetterLoad.NAME, new CivilizedPacesetterLoad());
        loads.put(SafeLoad.NAME, new SafeLoad());
        loads.put(LovingHeartLoad.NAME, new LovingHeartLoad());

        loads.put(StationBlueLoad.NAME, new StationBlueLoad());
        loads.put(StationGreenLoad.NAME, new StationGreenLoad());
        loads.put(StationPinkLoad.NAME, new StationPinkLoad());
        loads.put(StationPurpleLoad.NAME, new StationPurpleLoad());
        loads.put(StationRedLoad.NAME, new StationRedLoad());
        loads.put(StationYellowLoad.NAME, new StationYellowLoad());

        loads.put(KnownCultureLoad.NAME, new KnownCultureLoad());
        loads.put(KnownFestivalLoad.NAME, new KnownFestivalLoad());
        loads.put(KnownHistoryLoad.NAME, new KnownHistoryLoad());
        loads.put(KnownPatriotismLoad.NAME, new KnownPatriotismLoad());
        loads.put(KnownScenicSpotsLoad.NAME, new KnownScenicSpotsLoad());
        loads.put(KnownScienceTechLoad.NAME, new KnownScienceTechLoad());

    }

    public BaseLoad getLoad(int oid) {
        for (BaseLoad load : loads.values()) {
            if (load.getOidSection().in(oid)) {
                return load;
            }
        }
        return null;
    }

    public boolean inLoad(int oid) {
        return getLoad(oid) != null;
    }

    @Override
    public BaseLoad getLoad(String name) {
        return loads.containsKey(name) ? loads.get(name) : null;
    }

    public void clear() {
        goodDoneNumber = 0;
        loadHistory.clear();
        DebugUtil.info("================== 道路行走数据清理--- goodDoneNumber = %d , loadHistory = %d ", goodDoneNumber, loadHistory.size());
    }

    public void release() {
        clear();
        loads.clear();
    }

    public void addHistory(LoadEntrance newInstance) {
        loadHistory.add(newInstance);
        DebugUtil.info("================== 发现新道路,添加 == %s , 现在道路数量 == %d", newInstance.getLoad().getName(), loadHistory.size());
    }

    public void addAGoodDone() {
        goodDoneNumber++;
    }

    public int getStepNumber() {
        return loadHistory.size() - 1;
    }

    public int getGoodDoneNumber() {
        return goodDoneNumber;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void clearStationLoadPlayData() {
        for (BaseLoad value : loads.values()) {
            if (value instanceof StationLoad) {
                ((StationLoad) value).clearOuterPlayData();
            }
        }
    }

    public int getGoldNumber() {
        int number = 0;
        for (LoadEntrance loadEntrance : loadHistory) {
            if (loadEntrance.getLoad() instanceof GoldCoinLoad) {
                number++;
            }
        }
        return number;
    }

    public int getStationLoadNumber() {
        int number = 0;
        for (LoadEntrance loadEntrance : loadHistory) {
            if (loadEntrance.getLoad() instanceof StationLoad) {
                number++;
            }
        }
        return number;
    }

    public int getFlagLoadNumber() {
        int number = 0;
        for (LoadEntrance loadEntrance : loadHistory) {
            if (loadEntrance.getLoad() instanceof FlagLoad) {
                number++;
            }
        }
        return number;
    }

    public int getKnownLoadNumber() {
        int number = 0;
        for (LoadEntrance loadEntrance : loadHistory) {
            if (loadEntrance.getLoad() instanceof KnownLoad) {
                number++;
            }
        }
        return number;
    }

}