package com.bearya.robot.programme.walk.load;

import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Pair;

import com.bearya.robot.base.load.BaseLoad;
import com.bearya.robot.base.play.Director;
import com.bearya.robot.base.play.FacePlay;
import com.bearya.robot.base.play.FaceType;
import com.bearya.robot.base.play.LoadPlay;
import com.bearya.robot.base.play.PlayData;
import com.bearya.robot.base.protocol.DriveResult;
import com.bearya.robot.base.util.CodeUtils;
import com.bearya.robot.base.walk.Direct;
import com.bearya.robot.base.walk.RobotInLoadMethod;
import com.bearya.robot.programme.R;
import com.bearya.robot.programme.data.Command;
import com.bearya.robot.programme.data.Tag;
import com.bearya.robot.programme.walk.car.LoadMgr;

public class EndLoad extends BaseLoad {

    public static final String NAME = "终点";
    public static final int START_OID = 1000;

    public EndLoad() {
        super(START_OID);
        supportEntranceOid = new int[]{leftEntranceOid, topEntranceOid, rightEntranceOid, bottomEntranceOid};
        addLoadRect(new Rect(BORDER, 0, COLUMN - BORDER, ROW));
        addLoadRect(new Rect(0, BORDER, COLUMN, ROW - BORDER));
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int[] getExitOid(int entranceOid, Direct direct, RobotInLoadMethod method) {
        if (entranceOid == topEntranceOid) {
            return new int[]{getCenterOid(), getCenterOid() + ROW * 8};
        } else if (entranceOid == leftEntranceOid) {
            return new int[]{getCenterOid(), getCenterOid() + 8};
        } else if (entranceOid == rightEntranceOid) {
            return new int[]{getCenterOid(), getCenterOid() - 8};
        } else if (entranceOid == bottomEntranceOid) {
            return new int[]{getCenterOid(), getCenterOid() - ROW * 8};
        }
        return new int[]{getCenterOid()};
    }

    @Override
    public DriveResult onResult(RobotInLoadMethod method) {
        return DriveResult.EndLoad;
    }

    @Override
    public void registerPlay() {
        LoadPlay endLoadPlay = new LoadPlay();
        String tag = LoadMgr.getInstance().getTag();
        if (!TextUtils.isEmpty(tag)) {
            Pair<Integer, String> pair = null;
            switch (tag) {
                case Tag.TAG_WORLD:
                    endLoadPlay.addLoad(new PlayData("music/zh/emotion_1.mp3"));
                    break;
                case Tag.TAG_HOME:
                    pair = CodeUtils.oneOf(new Pair<>(R.array.game_success_home_1, "tts/zh/e_home1.mp3"),
                            new Pair<>(R.array.game_success_home_2, "tts/zh/e_home2.mp3"),
                            new Pair<>(R.array.game_success_home_3, "tts/zh/e_home3.mp3"));
                    break;
                case Tag.TAG_LIBRARY:
                    pair = CodeUtils.oneOf(new Pair<>(R.array.game_success_library_1, "tts/zh/e_library1.mp3"),
                            new Pair<>(R.array.game_success_library_2, "tts/zh/e_library2.mp3"),
                            new Pair<>(R.array.game_success_library_3, "tts/zh/e_library3.mp3"));
                    break;
                case Tag.TAG_ZOO:
                    pair = CodeUtils.oneOf(new Pair<>(R.array.game_success_zoo_1, "tts/zh/e_zoo1.mp3"),
                            new Pair<>(R.array.game_success_zoo_2, "tts/zh/e_zoo2.mp3"),
                            new Pair<>(R.array.game_success_zoo_3, "tts/zh/e_zoo3.mp3"));
                    break;
                case Tag.TAG_PARK:
                    pair = CodeUtils.oneOf(new Pair<>(R.array.game_success_park_1, "tts/zh/e_amusement1.mp3"),
                            new Pair<>(R.array.game_success_park_2, "tts/zh/e_amusement2.mp3"),
                            new Pair<>(R.array.game_success_park_3, "tts/zh/e_amusement3.mp3"));
                    break;
                case Tag.TAG_SCHOOL:
                    pair = CodeUtils.oneOf(new Pair<>(R.array.game_success_school_1, "tts/zh/e_kindergarten1.mp3"),
                            new Pair<>(R.array.game_success_school_2, "tts/zh/e_kindergarten2.mp3"),
                            new Pair<>(R.array.game_success_school_3, "tts/zh/e_kindergarten3.mp3"));
                    break;
                case Tag.TAG_PATRIOTISM:
                    endLoadPlay.addLoad(new PlayData("tts/zh/to_patriotism_end.mp3", new FacePlay(String.valueOf(R.array.patriotism_end), FaceType.Arrays)));
                    endLoadPlay.playAction = Command.format(Command.CITY_SUCCESS_PATRIOTISM);
                    break;
                default:
                    endLoadPlay.addLoad(new PlayData("theme/music/" + LoadMgr.getInstance().getTag() + "_end.mp3"));
                    break;
            }
            if (pair != null) {
                endLoadPlay.addLoad(new PlayData(pair.second, new FacePlay(String.valueOf(pair.first), FaceType.Arrays)));
                endLoadPlay.playAction = Command.format(getCmd(pair.first));
            }
        }
        Director.getInstance().register(ON_END_LOAD, endLoadPlay);
    }

    /**
     * 获取终点投屏业务码
     *
     * @param typeID 终点类型
     * @return 投屏业务码
     */
    private String getCmd(int typeID) {
        String action = "";
        if (typeID == R.array.game_success_home_1) {
            action = Command.CITY_SUCCESS_HOME_1;
        } else if (typeID == R.array.game_success_home_2) {
            action = Command.CITY_SUCCESS_HOME_2;
        } else if (typeID == R.array.game_success_home_3) {
            action = Command.CITY_SUCCESS_HOME_3;
        } else if (typeID == R.array.game_success_library_1) {
            action = Command.CITY_SUCCESS_LIB_1;
        } else if (typeID == R.array.game_success_library_2) {
            action = Command.CITY_SUCCESS_LIB_2;
        } else if (typeID == R.array.game_success_library_3) {
            action = Command.CITY_SUCCESS_LIB_3;
        } else if (typeID == R.array.game_success_zoo_1) {
            action = Command.CITY_SUCCESS_ZOO_1;
        } else if (typeID == R.array.game_success_zoo_2) {
            action = Command.CITY_SUCCESS_ZOO_2;
        } else if (typeID == R.array.game_success_zoo_3) {
            action = Command.CITY_SUCCESS_ZOO_3;
        } else if (typeID == R.array.game_success_park_1) {
            action = Command.CITY_SUCCESS_PARK_1;
        } else if (typeID == R.array.game_success_park_2) {
            action = Command.CITY_SUCCESS_PARK_2;
        } else if (typeID == R.array.game_success_park_3) {
            action = Command.CITY_SUCCESS_PARK_3;
        } else if (typeID == R.array.game_success_school_1) {
            action = Command.CITY_SUCCESS_SCHOOL_1;
        } else if (typeID == R.array.game_success_school_2) {
            action = Command.CITY_SUCCESS_SCHOOL_2;
        } else if (typeID == R.array.game_success_school_3) {
            action = Command.CITY_SUCCESS_SCHOOL_3;
        }
        return action;
    }

}