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
 * 文明标兵
 */
public class CivilizationAnimation extends LoadAnimation<PopViewLoadData> {

    @Override
    public List<PopViewLoadData> generateAnimation() {
        List<PopViewLoadData> data = new ArrayList<>();

        // 遇见邻居爷爷
        PopViewItemData meetGrandpaLeftViewData = getPopViewItemData(R.mipmap.meet_grandpa_wrong, "tts/zh/c_p1_quiz2.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG));
        PopViewItemData meetGrandpaRightViewData = getPopViewItemData(R.mipmap.meet_grandpa_right, "tts/zh/c_p1_quiz3.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS), new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        data.add(getLoadPopTypeData(Command.CITY_CIVILIZED_9, meetGrandpaLeftViewData, meetGrandpaRightViewData, "tts/zh/c_p1_quiz1.mp3",
                "tts/zh/c_p1_begin.mp3", "tts/zh/c_p1_end.mp3", String.valueOf(R.array.meet_grandpa_begin),
                String.valueOf(R.array.meet_grandpa_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));

        // 小盆友帮小贝捡东西
        PopViewItemData pickUpLeftViewData = getPopViewItemData(R.mipmap.pick_up_right, "tts/zh/c_p2_quiz2.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS), new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        PopViewItemData pickUpRightViewData = getPopViewItemData(R.mipmap.pick_up_wrong, "tts/zh/c_p2_quiz3.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG));
        data.add(getLoadPopTypeData(Command.CITY_CIVILIZED_7, pickUpLeftViewData, pickUpRightViewData, "tts/zh/c_p2_quiz1.mp3",
                "tts/zh/c_p2_begin.mp3", "tts/zh/c_p2_end.mp3", String.valueOf(R.array.pick_up_begin),
                String.valueOf(R.array.pick_up_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));

        // 小贝撞到路人
        PopViewItemData hitTheLeftViewData = getPopViewItemData(R.mipmap.hit_the_wrong, "tts/zh/c_p3_quiz2.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG));
        PopViewItemData hitTheRightViewData = getPopViewItemData(R.mipmap.hit_the_right, "tts/zh/c_p3_quiz3.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS), new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        data.add(getLoadPopTypeData(Command.CITY_CIVILIZED_8, hitTheLeftViewData, hitTheRightViewData, "tts/zh/c_p3_quiz1.mp3",
                "tts/zh/c_p3_begin.mp3", "tts/zh/c_p3_end.mp3", String.valueOf(R.array.hit_the_begin),
                String.valueOf(R.array.hit_the_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));

        // 小贝帮忙捡东西
        PopViewItemData lcLeftViewData2 = getPopViewItemData(R.mipmap.l_civilization2_wrong, "tts/zh/c_s1_quiz2.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG));
        PopViewItemData lcRightViewData2 = getPopViewItemData(R.mipmap.l_civilization2_right, "tts/zh/c_s1_quiz3.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS), new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        data.add(getLoadPopTypeData(Command.CITY_CIVILIZED_5, lcLeftViewData2, lcRightViewData2, "tts/zh/c_s1_quiz1.mp3",
                "tts/zh/c_s1_begin.mp3", "tts/zh/c_s1_end.mp3", String.valueOf(R.array.l_civilization2_begin),
                String.valueOf(R.array.l_civilization2_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));

        // 环卫工人打扫
        PopViewItemData lcLeftViewData = getPopViewItemData(R.mipmap.l_civilization1_wrong, "tts/zh/c_s2_quiz2.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG));
        PopViewItemData lcRightViewData = getPopViewItemData(R.mipmap.l_civilization1_right, "tts/zh/c_s2_quiz3.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS), new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        data.add(getLoadPopTypeData(Command.CITY_CIVILIZED_6, lcLeftViewData, lcRightViewData, "tts/zh/c_s2_quiz1.mp3",
                "tts/zh/c_s2_begin.mp3", "tts/zh/c_s2_end.mp3", String.valueOf(R.array.l_civilization1_begin),
                String.valueOf(R.array.l_civilization1_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));

        // 大肚子阿姨掉东西
        PopViewItemData lcLeftViewData3 = getPopViewItemData(R.mipmap.l_civilization3_right, "tts/zh/c_s3_quiz2.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS), new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        PopViewItemData lcRightViewData3 = getPopViewItemData(R.mipmap.l_civilization3_wrong, "tts/zh/c_s3_quiz3.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG));
        data.add(getLoadPopTypeData(Command.CITY_CIVILIZED_4, lcLeftViewData3, lcRightViewData3, "tts/zh/c_s3_quiz1.mp3",
                "tts/zh/c_s3_begin.mp3", "tts/zh/c_s3_end.mp3", String.valueOf(R.array.l_civilization3_begin),
                String.valueOf(R.array.l_civilization3_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));

        // 人行天桥
        PopViewItemData bridgeLeftViewData = getPopViewItemData(R.mipmap.pacesetter_right, "tts/zh/c_m1_quiz2.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS), new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        PopViewItemData bridgeRightViewData = getPopViewItemData(R.mipmap.pacesetter_left, "tts/zh/c_m1_quiz3.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG));
        data.add(getLoadPopTypeData(Command.CITY_CIVILIZED_3, bridgeLeftViewData, bridgeRightViewData, "tts/zh/c_m1_quiz1.mp3",
                "tts/zh/c_m1_begin.mp3", "tts/zh/c_m1_end.mp3", String.valueOf(R.array.pacesetter_start),
                String.valueOf(R.array.pacesetter_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));

        // 路上吐痰
        PopViewItemData spittingLeftViewData = getPopViewItemData(R.mipmap.spitting_right, "tts/zh/c_m2_quiz2.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS), new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        PopViewItemData spittingRightViewData = getPopViewItemData(R.mipmap.spitting_wrong, "tts/zh/c_m2_quiz3.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG));
        data.add(getLoadPopTypeData(Command.CITY_CIVILIZED_2, spittingLeftViewData, spittingRightViewData, "tts/zh/c_m2_quiz1.mp3",
                "tts/zh/c_m2_begin.mp3", "tts/zh/c_m2_end.mp3", String.valueOf(R.array.spitting_begin),
                String.valueOf(R.array.spitting_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));

        // 下雨小盆友没伞
        PopViewItemData rainsLeftViewData = getPopViewItemData(R.mipmap.rains_wrong, "tts/zh/c_m3_quiz2.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG) );
        PopViewItemData rainsRightViewData = getPopViewItemData(R.mipmap.rains_right, "tts/zh/c_m3_quiz3.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS),new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        data.add(getLoadPopTypeData(Command.CITY_CIVILIZED_1, rainsLeftViewData, rainsRightViewData, "tts/zh/c_m3_quiz1.mp3",
                "tts/zh/c_m3_begin.mp3", "tts/zh/c_m3_end.mp3", String.valueOf(R.array.rains_begin),
                String.valueOf(R.array.rains_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));

        return data;
    }

}