package com.bearya.robot.programme.repository;

import android.os.Environment;

import com.bearya.robot.programme.entity.PatriotismContent;
import com.tencent.mmkv.MMKV;

import java.util.HashMap;
import java.util.Map;

public class PatriotismRepository {

    public static final String THEME_CULTURE = "culture";
    public static final String THEME_FESTIVAL = "festival";
    public static final String THEME_SCIENCE = "science";
    public static final String THEME_PATRIOTISM = "patriotism";
    public static final String THEME_SCENIC = "scenic";
    public static final String THEME_HISTORY = "history";

    private final MMKV mmkv;
    private final Map<String, Boolean> contentMap;

    private static PatriotismRepository repository = null;

    private PatriotismRepository() {
        String relativePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.bearya.robot.programme/mmkv";
        mmkv = MMKV.mmkvWithID("patriotism-config", MMKV.MULTI_PROCESS_MODE, "patriotism-CONFIG", relativePath);
        contentMap = new HashMap<>();
    }

    public static PatriotismRepository getInstance() {
        if (repository == null) {
            repository = new PatriotismRepository();
        }
        return repository;
    }

    public void init() {
        for (String theme : themes) {
            for (PatriotismContent patriotismContent : getPatriotismContents(theme)) {
                String contentName = patriotismContent.getContentName();
                if (mmkv != null && !mmkv.contains(contentName)) {
                    put(contentName);
                } else {
                    contentMap.put(contentName, mmkv != null && mmkv.decodeBool(contentName, false));
                }
            }
        }
    }

    public void release() {
        if (mmkv != null) mmkv.close();
        contentMap.clear();
    }

    public void put(String content) {
        if (mmkv != null) {
            mmkv.encode(content, true);
            contentMap.put(content, true);
        }
    }

    public boolean get(String contentName) {
        return contentMap.containsKey(contentName) && contentMap.get(contentName);
    }

    public void remove(String contentName) {
        if (mmkv != null) {
            mmkv.encode(contentName, false);
            contentMap.put(contentName, false);
        }
    }

    public void clearAll() {
        if (mmkv != null) {
            for (String theme : themes) {
                for (PatriotismContent patriotismContent : getPatriotismContents(theme)) {
                    String contentName = patriotismContent.getContentName();
                    mmkv.encode(contentName, false);
                    contentMap.put(contentName, false);
                }
            }
        }
    }

    public final String[] themes = new String[]{
            THEME_CULTURE, THEME_FESTIVAL, THEME_HISTORY,
            THEME_PATRIOTISM, THEME_SCENIC, THEME_SCIENCE
    };

    public PatriotismContent[] getPatriotismContents(String theme) {
        switch (theme) {
            case THEME_CULTURE:
                return cultureContents;
            case THEME_FESTIVAL:
                return festivalContents;
            case THEME_SCIENCE:
                return scienceContents;
            case THEME_HISTORY:
                return historyContents;
            case THEME_PATRIOTISM:
                return patriotismContents;
            case THEME_SCENIC:
                return scenicContents;
            default:
                return new PatriotismContent[]{};
        }
    }

    /**
     * 祖国母亲我爱您
     */
    private final PatriotismContent[] patriotismContents = new PatriotismContent[]{
            new PatriotismContent("national_flag", 6), // 国旗
            new PatriotismContent("national_anthem", 3) // 国歌
    };

    /**
     * 小小党史宣传员
     */
    private final PatriotismContent[] historyContents = new PatriotismContent[]{
            new PatriotismContent("red_boat", 3), // 红船
            new PatriotismContent("long_march", 4) // 长征
    };

    /**
     * 名胜古迹小导游
     */
    private final PatriotismContent[] scenicContents = new PatriotismContent[]{
            new PatriotismContent("the_imperial_palace", 4), // 故宫
            new PatriotismContent("the_great_wall", 1) // 长城
    };

    /**
     * 小小科技宣传员
     */
    private final PatriotismContent[] scienceContents = new PatriotismContent[]{
            new PatriotismContent("chang5", 2), // 嫦娥五号
            new PatriotismContent("eye_of_heaven", 1), // 天眼
            new PatriotismContent("nine_arithmetic", 1) // 九章算术
    };

    /**
     * 中国文化百事通
     */
    private final PatriotismContent[] cultureContents = new PatriotismContent[]{
            new PatriotismContent("porcelain", 2), // 瓷器
            new PatriotismContent("painting", 2), // 国画
            new PatriotismContent("calligraphy", 2),// 书法
            new PatriotismContent("martialArt", 2) // 武术
    };

    /**
     * 中国节日宣传员
     */
    private final PatriotismContent[] festivalContents = new PatriotismContent[]{
            new PatriotismContent("spring_festival", 3), // 春节
            new PatriotismContent("qingming_festival", 3) // 清明节
    };

}