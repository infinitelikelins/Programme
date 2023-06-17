package com.bearya.robot.programme.repository.animations;

import android.text.TextUtils;

import com.bearya.robot.base.play.FacePlay;
import com.bearya.robot.base.play.FaceType;
import com.bearya.robot.base.play.LoadPlay;
import com.bearya.robot.base.play.PlayData;
import com.bearya.robot.programme.data.Command;
import com.bearya.robot.programme.walk.load.PopViewLoadData;
import com.bearya.robot.programme.walk.load.lock.PopViewData;
import com.bearya.robot.programme.walk.load.lock.PopViewItemData;
import com.bearya.robot.programme.walk.load.lock.PopViewResult;

import java.util.List;

public abstract class LoadAnimation<T> {

    public static final String TTS_RIGHT_1 = "tts/zh/right1.mp3";
    public static final String TTS_RIGHT_2 = "tts/zh/right2.mp3";
    public static final String TTS_RIGHT_3 = "tts/zh/right3.mp3";
    public static final String TTS_WRONG_1 = "tts/zh/wrong1.mp3";
    public static final String TTS_WRONG_2 = "tts/zh/wrong2.mp3";

    public static final String[] TTS_RIGHTS = {TTS_RIGHT_1, TTS_RIGHT_2, TTS_RIGHT_3};
    public static final String[] TTS_WRONGS = {TTS_WRONG_1, TTS_WRONG_2};

    public abstract List<T> generateAnimation();

    /**
     * 组装LoadPopTypeData数据
     *
     * @param leftViewData  弹窗左边的数据
     * @param rightViewData 弹窗右边的数据
     * @param quiz1         问题起始音频路劲名称
     * @param beginMp3      起始音频路劲名称
     * @param endMp3        结束音频路劲名称
     * @param beginFace     起始动画资源id
     * @param endFace       结束动画资源id
     * @param timeMp3       30秒超时音频资源路劲
     * @param timeOverMp3   50秒超时音频资源路劲
     */
    protected PopViewLoadData getLoadPopTypeData(String tag, PopViewItemData leftViewData, PopViewItemData rightViewData, String quiz1,
                                                 String beginMp3, String endMp3, String beginFace, String endFace, String timeMp3, String timeOverMp3) {
        PopViewData popViewData = null;
        if (leftViewData != null && rightViewData != null) {
            popViewData = new PopViewData(quiz1, leftViewData, rightViewData);
        }

        LoadPlay newLoadPlay = getLoadPlay(beginMp3, beginFace, null);
        if (TextUtils.isEmpty(tag)) {
            tag = Command.CITY_CIVILIZED_1;
        }
        newLoadPlay.playAction = Command.format(tag);

        LoadPlay unlockSuccessRightPlay = getLoadPlay(endMp3, endFace, null);

        LoadPlay unlockSuccessWrongPlay = getLoadPlay(endMp3, endFace, null);

        LoadPlay timePlay = getLoadPlay(timeMp3, null, null);
        timePlay.playAction = Command.format(Command.CITY_LOCK_TIMEOUT);

        LoadPlay maxTimeOverPlay = getLoadPlay(timeOverMp3, null, null);
        maxTimeOverPlay = getLoadPlay(endMp3, endFace, maxTimeOverPlay);
        maxTimeOverPlay.playAction = Command.format(Command.CITY_LOCK_MAX_TIMEOUT);

        return new PopViewLoadData(newLoadPlay, popViewData, unlockSuccessRightPlay, unlockSuccessWrongPlay, timePlay, maxTimeOverPlay);
    }

    /**
     * 组装LoadPlay
     *
     * @param sound     音频资源名
     * @param face      动画资源Id
     * @param mLoadPlay 是否组装到上一个数据源里 （为NULL时组装新的数据源)
     */
    protected LoadPlay getLoadPlay(String sound, String face, LoadPlay mLoadPlay) {
        LoadPlay loadPlay = mLoadPlay;
        if (loadPlay == null) {
            loadPlay = new LoadPlay();
        }
        PlayData playData = new PlayData(sound);
        if (!TextUtils.isEmpty(face)) {
            playData.facePlay = new FacePlay(face, FaceType.Arrays);
        }
        loadPlay.addLoad(playData);
        return loadPlay;
    }

    protected PopViewItemData getPopViewItemData(int entrap_wrong, String animatorMp3, int do_error_anim, String selectMp3, PopViewResult result) {
        return new PopViewItemData(entrap_wrong, animatorMp3, do_error_anim, selectMp3, result);
    }

}
