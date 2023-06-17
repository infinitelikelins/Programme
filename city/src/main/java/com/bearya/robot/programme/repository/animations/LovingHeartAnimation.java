package com.bearya.robot.programme.repository.animations;

import com.bearya.robot.base.play.LoadPlay;
import com.bearya.robot.base.walk.Direct;
import com.bearya.robot.programme.R;
import com.bearya.robot.programme.data.Command;
import com.bearya.robot.programme.walk.load.TouchBodyLoadData;

import java.util.ArrayList;
import java.util.List;

/**
 * 爱心小天使
 */
public class LovingHeartAnimation extends LoadAnimation<TouchBodyLoadData> {

    @Override
    public List<TouchBodyLoadData> generateAnimation() {
        List<TouchBodyLoadData> lovingHeartLoadPlays = new ArrayList<>();

        // 踩香蕉
        lovingHeartLoadPlays.add(getHeartLoadAnimateData(
                Command.CITY_LOVINGHEART_5,
                "tts/zh/slip_begin.mp3",
                String.valueOf(R.array.loving_heart_2_begin),
                "tts/zh/slip_guide.mp3",
                String.valueOf(R.array.loving_heart_1_end),
                "tts/zh/slip_right.mp3",
                "tts/zh/slip_wrong.mp3",
                "tts/zh/slip_delay.mp3",
                Direct.Backward));

        // 掉马路坑
        lovingHeartLoadPlays.add(getHeartLoadAnimateData(
                Command.CITY_LOVINGHEART_4,
                "tts/zh/butterfly_begin.mp3",
                String.valueOf(R.array.loving_heart2_begin),
                "tts/zh/butterfly_guide.mp3",
                String.valueOf(R.array.loving_heart2_end),
                "tts/zh/butterfly_right.mp3",
                "tts/zh/butterfly_wrong.mp3",
                "tts/zh/butterfly_delay.mp3",
                Direct.ARM));

        // 肚子疼
        lovingHeartLoadPlays.add(getHeartLoadAnimateData(
                Command.CITY_LOVINGHEART_3,
                "tts/zh/ice_cream_begin.mp3",
                String.valueOf(R.array.loving_heart3_begin),
                "tts/zh/ice_cream_guide.mp3",
                String.valueOf(R.array.loving_heart3_end),
                "tts/zh/ice_cream_right.mp3",
                "tts/zh/ice_cream_wrong.mp3",
                "tts/zh/ice_cream_delay.mp3",
                Direct.Forward));

        // 听到鞭炮响
        lovingHeartLoadPlays.add(getHeartLoadAnimateData(
                Command.CITY_LOVINGHEART_2,
                "tts/zh/firecrackers_begin.mp3",
                String.valueOf(R.array.loving_heart4_begin),
                "tts/zh/firecrackers_guide.mp3",
                String.valueOf(R.array.loving_heart4_end),
                "tts/zh/firecrackers_right.mp3",
                "tts/zh/firecrackers_wrong.mp3",
                "tts/zh/firecrackers_delay.mp3",
                Direct.EAR));

        // 撞电线杆
        lovingHeartLoadPlays.add(getHeartLoadAnimateData(
                Command.CITY_LOVINGHEART_1,
                "tts/zh/pole_begin.mp3",
                String.valueOf(R.array.loving_heart5_begin),
                "tts/zh/pole_guide.mp3",
                String.valueOf(R.array.loving_heart5_end),
                "tts/zh/pole_right.mp3",
                "tts/zh/pole_wrong.mp3",
                "tts/zh/pole_delay.mp3",
                Direct.Head));

        return lovingHeartLoadPlays;
    }

    /**
     * 爱心天使数据
     *
     * @param beginMp3  开始音频
     * @param beginFace 开始动画
     * @param endMp3    结束音频
     * @param endFace   结束动画
     * @param rightMp3  正确音频
     * @param wrongMp3  错误音频
     */
    private TouchBodyLoadData getHeartLoadAnimateData(String tag,
                                                      String beginMp3,
                                                      String beginFace,
                                                      String endMp3,
                                                      String endFace,
                                                      String rightMp3,
                                                      String wrongMp3,
                                                      String delay,
                                                      Direct direct) {

        LoadPlay newLoadPlay = getLoadPlay(beginMp3, beginFace, null);
        newLoadPlay = getLoadPlay(endMp3, endFace, newLoadPlay);
        newLoadPlay.playAction = Command.format(tag);

        LoadPlay unlockSuccessPlay = getLoadPlay(rightMp3, null, null);
        unlockSuccessPlay.playAction = Command.format(Command.CITY_LOVINGHEART_OK, rightMp3);

        LoadPlay unlockFailPlay = getLoadPlay(wrongMp3, null, null);
        unlockFailPlay.playAction = Command.format(Command.CITY_LOVINGHEART_ERROR, wrongMp3);

        LoadPlay onDelayPlay = getLoadPlay(delay, null, null);
        onDelayPlay.playAction = Command.format(Command.CITY_LOCK_MAX_TIMEOUT);

        return new TouchBodyLoadData(newLoadPlay, unlockSuccessPlay, unlockFailPlay, onDelayPlay, direct);

    }

}