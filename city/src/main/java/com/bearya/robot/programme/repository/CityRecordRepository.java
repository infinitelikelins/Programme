package com.bearya.robot.programme.repository;

import android.os.Environment;

import com.bearya.actionlib.utils.SharedPreferencesUtil;
import com.bearya.robot.base.BaseApplication;
import com.bearya.robot.base.play.PlayData;
import com.bearya.robot.programme.data.Tag;
import com.bearya.robot.programme.entity.WalkHistory;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.Arrays;

public class CityRecordRepository {

    private static CityRecordRepository cityRecordRepository;

    public static CityRecordRepository getInstance() {
        if (cityRecordRepository == null)
            cityRecordRepository = new CityRecordRepository();
        return cityRecordRepository;
    }

    private final MMKV mmkv;
    private final ArrayList<PlayData> playDataList;

    private CityRecordRepository() {
        String relativePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.bearya.robot.programme/mmkv";
        mmkv = MMKV.mmkvWithID("play-data-config", MMKV.MULTI_PROCESS_MODE, "PLAY-DATA-CONFIG", relativePath);
        playDataList = new ArrayList<>();
    }

    public long save() {
        SharedPreferencesUtil instance = SharedPreferencesUtil.getInstance(BaseApplication.getInstance().getApplicationContext());
        int id = instance.getInt(Tag.COUNT, 0);
        WalkHistory history = new WalkHistory(id + 1, playDataList);
        mmkv.encode(String.valueOf(history.getId()), history);
        instance.put(Tag.COUNT, history.getId());
        playDataList.clear();
        return history.getId();
    }

    public boolean put(PlayData data) {
        return !data.isEmpty() && playDataList.add(new PlayData((data)));
    }

    public WalkHistory getItem(String idKey) {
        return mmkv.decodeParcelable(idKey, WalkHistory.class);
    }

    public String[] allKey() {
        String[] strings = mmkv.allKeys();
        if (strings != null && strings.length > 0) {
            Arrays.sort(strings, (o1, o2) -> Integer.compare(Integer.parseInt(o1), Integer.parseInt(o2)));
        }
        return strings;
    }

    public void remove(String key) {
        if (mmkv.contains(key)) {
            mmkv.remove(key);
        }
    }

    public void clearSavePlayDataMemory(){
        playDataList.clear();
    }

    public void release() {
        if (mmkv != null) mmkv.close();
        playDataList.clear();
    }

}