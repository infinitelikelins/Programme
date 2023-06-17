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
 * 环保达人
 */
public class ProtectionAnimation extends LoadAnimation<PopViewLoadData> {

    @Override
    public List<PopViewLoadData> generateAnimation() {
        List<PopViewLoadData> data = new ArrayList<>();

        // 路边采花
        PopViewItemData fLeftViewData = getPopViewItemData(R.mipmap.flowers_right, "tts/zh/e_p1_quiz2.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS), new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        PopViewItemData fRightViewData = getPopViewItemData(R.mipmap.flowers_wrong, "tts/zh/e_p1_quiz3.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG));
        data.add(getLoadPopTypeData(Command.CITY_PROTECTION_8, fLeftViewData, fRightViewData, "tts/zh/e_p1_quiz1.mp3",
                "tts/zh/e_p1_begin.mp3", "tts/zh/e_p1_end.mp3", String.valueOf(R.array.flowers_begin),
                String.valueOf(R.array.flowers_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));

        // 路边看到牛奶盒
        PopViewItemData mcLeftViewData = getPopViewItemData(R.mipmap.milk_carton_wrong, "tts/zh/e_p2_quiz2.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG));
        PopViewItemData mcRightViewData = getPopViewItemData(R.mipmap.milk_carton_right, "tts/zh/e_p2_quiz3.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS), new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        data.add(getLoadPopTypeData(Command.CITY_PROTECTION_7, mcLeftViewData, mcRightViewData, "tts/zh/e_p2_quiz1.mp3",
                "tts/zh/e_p2_begin.mp3", "tts/zh/e_p2_end.mp3", String.valueOf(R.array.milk_carton_begin),
                String.valueOf(R.array.milk_carton_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));

        // 小贝流鼻涕
        PopViewItemData nrLeftViewData = getPopViewItemData(R.mipmap.nose_running_right, "tts/zh/e_p3_quiz2.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS), new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        PopViewItemData nrRightViewData = getPopViewItemData(R.mipmap.nose_running_wrong, "tts/zh/e_p3_quiz3.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG));
        data.add(getLoadPopTypeData(Command.CITY_PROTECTION_6, nrLeftViewData, nrRightViewData, "tts/zh/e_p3_quiz1.mp3",
                "tts/zh/e_p3_begin.mp3", "tts/zh/e_p3_end.mp3", String.valueOf(R.array.nose_running_begin),
                String.valueOf(R.array.nose_running_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));

        // 水龙头滴水
        PopViewItemData cLeftViewData = getPopViewItemData(R.mipmap.e_protection_choose1, "tts/zh/e_m1_quiz2.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS), new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        PopViewItemData cRightViewData = getPopViewItemData(R.mipmap.e_protection_choose2, "tts/zh/e_m1_quiz3.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG));
        data.add(getLoadPopTypeData(Command.CITY_PROTECTION_3, cLeftViewData, cRightViewData, "tts/zh/e_m1_quiz1.mp3",
                "tts/zh/e_m1_begin.mp3", "tts/zh/e_m1_end.mp3", String.valueOf(R.array.e_protection_start),
                String.valueOf(R.array.e_protection_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));

        // 丢电池
        PopViewItemData strangerWalkedLeftViewData = getPopViewItemData(R.mipmap.lost_battery_right, "tts/zh/e_s1_quiz2.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS), new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        PopViewItemData strangerWalkedRightViewData = getPopViewItemData(R.mipmap.lost_battery_wrong, "tts/zh/e_s1_quiz3.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG));
        data.add(getLoadPopTypeData(Command.CITY_PROTECTION_5, strangerWalkedLeftViewData, strangerWalkedRightViewData, "tts/zh/e_s1_quiz1.mp3",
                "tts/zh/e_s1_begin.mp3", "tts/zh/e_s1_end.mp3", String.valueOf(R.array.lost_battery_begin),
                String.valueOf(R.array.lost_battery_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));

        // 树苗快枯死
        PopViewItemData entrapLeftViewData = getPopViewItemData(R.mipmap.seedlings_wrong, "tts/zh/e_s2_quiz2.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG));
        PopViewItemData entrapRightViewData = getPopViewItemData(R.mipmap.seedlings_right, "tts/zh/e_s2_quiz3.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS), new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        data.add(getLoadPopTypeData(Command.CITY_PROTECTION_4, entrapLeftViewData, entrapRightViewData, "tts/zh/e_s2_quiz1.mp3",
                "tts/zh/e_s2_begin.mp3", "tts/zh/e_s2_end.mp3", String.valueOf(R.array.seedlings_begin),
                String.valueOf(R.array.seedlings_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));

        // 买冰淇淋 过草坪？
        PopViewItemData grassLeftViewData = getPopViewItemData(R.mipmap.grass_right, "tts/zh/e_m3_quiz2.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS), new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        PopViewItemData grassRightViewData = getPopViewItemData(R.mipmap.grass_wrong, "tts/zh/e_m3_quiz3.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG));
        data.add(getLoadPopTypeData(Command.CITY_PROTECTION_2, grassLeftViewData, grassRightViewData, "tts/zh/e_m3_quiz1.mp3",
                "tts/zh/e_m3_begin.mp3", "tts/zh/e_m3_end.mp3", String.valueOf(R.array.grass_begin),
                String.valueOf(R.array.grass_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));

        // 路上吃三明治
        PopViewItemData sandwichLeftViewData = getPopViewItemData(R.mipmap.sandwich_wrong, "tts/zh/e_s3_quiz2.mp3", R.drawable.do_error_anim, CodeUtils.oneOf(TTS_WRONGS), new PopViewResult(false, BaseLoad.ON_UNLOCK_SUCCESS_WRONG));
        PopViewItemData sandwichRightViewData = getPopViewItemData(R.mipmap.sandwich_right, "tts/zh/e_s3_quiz3.mp3", R.drawable.ic_light, CodeUtils.oneOf(TTS_RIGHTS), new PopViewResult(true, BaseLoad.ON_UNLOCK_SUCCESS_RIGHT));
        data.add(getLoadPopTypeData(Command.CITY_PROTECTION_1, sandwichLeftViewData, sandwichRightViewData, "tts/zh/e_s3_quiz1.mp3",
                "tts/zh/e_s3_begin.mp3", "tts/zh/e_s3_end.mp3", String.valueOf(R.array.sandwich_begin),
                String.valueOf(R.array.sandwich_end), "tts/zh/delay_30s.mp3", "tts/zh/delay_50s.mp3"));

        return data;
    }

}
