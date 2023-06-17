package com.bearya.robot.programme.repository.animations;

import com.bearya.robot.base.load.BaseLoad;
import com.bearya.robot.base.util.CodeUtils;
import com.bearya.robot.programme.R;
import com.bearya.robot.programme.data.Command;
import com.bearya.robot.programme.walk.load.PopViewLoadData;
import com.bearya.robot.programme.walk.load.lock.PopViewItemData;
import com.bearya.robot.programme.walk.load.lock.PopViewResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 安全卫士
 */
public class SafeAnimation extends LoadAnimation<PopViewLoadData> {

    @Override
    public List<PopViewLoadData> generateAnimation() {
        List<PopViewLoadData> data = new ArrayList<>();

        // 红绿灯
        PopViewItemData cLeftViewData = getPopViewItemData(R.mipmap.red_light, "tts/zh/s_p1_quiz2.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG));
        PopViewItemData cRightViewData = getPopViewItemData(R.mipmap.grren_light, "tts/zh/s_p1_quiz3.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS), new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        data.add(getLoadPopTypeData(Command.CITY_SAFEGUARD_9, cLeftViewData, cRightViewData, "tts/zh/s_p1_quiz1.mp3",
                "tts/zh/s_p1_begin.mp3", "tts/zh/s_p1_end.mp3", String.valueOf(R.array.crossroads_begin),
                String.valueOf(R.array.crossroads_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));

        // 不和陌生人走
        PopViewItemData strangerWalkedLeftViewData = getPopViewItemData(R.mipmap.stranger_walked_wrong, "tts/zh/s_p3_quiz2.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG));
        PopViewItemData strangerWalkedRightViewData = getPopViewItemData(R.mipmap.stranger_walked_right, "tts/zh/s_p3_quiz3.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS), new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        data.add(getLoadPopTypeData(Command.CITY_SAFEGUARD_8, strangerWalkedLeftViewData, strangerWalkedRightViewData, "tts/zh/s_p3_quiz1.mp3",
                "tts/zh/s_p3_begin.mp3", "tts/zh/s_p3_end.mp3", String.valueOf(R.array.stranger_walked_begin),
                String.valueOf(R.array.stranger_walked_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));

        // 陌生人诱骗小贝
        PopViewItemData entrapLeftViewData = getPopViewItemData(R.mipmap.entrap_wrong, "tts/zh/s_p2_quiz2.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG));
        PopViewItemData entrapRightViewData = getPopViewItemData(R.mipmap.entrap_right, "tts/zh/s_p2_quiz3.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS), new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        data.add(getLoadPopTypeData(Command.CITY_SAFEGUARD_7, entrapLeftViewData, entrapRightViewData, "tts/zh/s_p2_quiz1.mp3",
                "tts/zh/s_p2_begin.mp3", "tts/zh/s_p2_end.mp3", String.valueOf(R.array.entrap_begin),
                String.valueOf(R.array.entrap_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));

        // 地震逃生
        PopViewItemData earthquakeLeftViewData = getPopViewItemData(R.mipmap.choose_escape, "tts/zh/s_s2_quiz2.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS), new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        PopViewItemData earthquakeRightViewData = getPopViewItemData(R.mipmap.choose_hiding, "tts/zh/s_s2_quiz3.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG));
        data.add(getLoadPopTypeData(Command.CITY_SAFEGUARD_6, earthquakeLeftViewData, earthquakeRightViewData, "tts/zh/s_s2_quiz1.mp3",
                "tts/zh/s_s2_begin.mp3", "tts/zh/s_s2_end.mp3", String.valueOf(R.array.earthquake_start),
                String.valueOf(R.array.earthquake_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));

        // 不和陌生人说话
        PopViewItemData strangerTalkLeftViewData = getPopViewItemData(R.mipmap.stranger_talk_wrong, "tts/zh/s_s3_quiz2.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG));
        PopViewItemData strangerTalkRightViewData = getPopViewItemData(R.mipmap.stranger_talk_right, "tts/zh/s_s3_quiz3.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS), new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        data.add(getLoadPopTypeData(Command.CITY_SAFEGUARD_5, strangerTalkLeftViewData, strangerTalkRightViewData, "tts/zh/s_s3_quiz1.mp3",
                "tts/zh/s_s3_begin.mp3", "tts/zh/s_s3_end.mp3", String.valueOf(R.array.stranger_talk_begin),
                String.valueOf(R.array.stranger_talk_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));

        // 火灾逃生
        PopViewItemData fireEscapeLeftViewData = getPopViewItemData(R.mipmap.fire_escape_right, "tts/zh/s_s1_quiz2.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS), new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        PopViewItemData fireEscapeRightViewData = getPopViewItemData(R.mipmap.fire_escape_wrong, "tts/zh/s_s1_quiz3.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG));
        data.add(getLoadPopTypeData(Command.CITY_SAFEGUARD_4, fireEscapeLeftViewData, fireEscapeRightViewData, "tts/zh/s_s1_quiz1.mp3",
                "tts/zh/s_s1_begin.mp3", "tts/zh/s_s1_end.mp3", String.valueOf(R.array.fire_escape_begin),
                String.valueOf(R.array.fire_escape_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));
        // 雷雨天户外避雨
        PopViewItemData shelterLeftViewData = getPopViewItemData(R.mipmap.shelter_wrong, "tts/zh/s_s4_quiz2.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG));
        PopViewItemData shelterRightViewData = getPopViewItemData(R.mipmap.shelter_right, "tts/zh/s_s4_quiz3.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS), new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        data.add(getLoadPopTypeData(Command.CITY_SAFEGUARD_3, shelterLeftViewData, shelterRightViewData, "tts/zh/s_s4_quiz1.mp3",
                "tts/zh/s_s4_begin.mp3", "tts/zh/s_s4_end.mp3", String.valueOf(R.array.shelter_begin),
                String.valueOf(R.array.shelter_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));

        // 小盆友迷路 lost
        PopViewItemData lostLeftViewData = getPopViewItemData(R.mipmap.choose_110, "tts/zh/s_m3_quiz2.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS), new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        PopViewItemData lostRightViewData = getPopViewItemData(R.mipmap.choose_120, "tts/zh/s_m3_quiz3.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG));
        data.add(getLoadPopTypeData(Command.CITY_SAFEGUARD_2, lostLeftViewData, lostRightViewData, "tts/zh/s_m3_quiz1.mp3",
                "tts/zh/s_m3_begin.mp3", "tts/zh/s_m3_end.mp3", String.valueOf(R.array.lost_begin),
                String.valueOf(R.array.lost_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));

        // 老爷爷摔倒
        PopViewItemData mLeftViewData = getPopViewItemData(R.mipmap.choose_119, "tts/zh/s_m2_quiz2.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG));
        PopViewItemData mRightViewData = getPopViewItemData(R.mipmap.choose_120, "tts/zh/s_m2_quiz3.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS), new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        data.add(getLoadPopTypeData(Command.CITY_SAFEGUARD_1, mLeftViewData, mRightViewData, "tts/zh/s_m2_quiz1.mp3",
                "tts/zh/s_m2_begin.mp3", "tts/zh/s_m2_end.mp3", String.valueOf(R.array.guards_start),
                String.valueOf(R.array.guards_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));

        // 车辆着火
        PopViewItemData fLeftViewData = getPopViewItemData(R.mipmap.choose_110, "tts/zh/s_m1_quiz2.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG));
        PopViewItemData fRightViewData = getPopViewItemData(R.mipmap.choose_119, "tts/zh/s_m1_quiz3.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS), new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        data.add(getLoadPopTypeData(Command.CITY_SAFEGUARD_0, fLeftViewData, fRightViewData, "tts/zh/s_m1_quiz1.mp3",
                "tts/zh/s_m1_begin.mp3", "tts/zh/s_m1_end.mp3", String.valueOf(R.array.fire_begin),
                String.valueOf(R.array.fire_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));

        return data;
    }

}