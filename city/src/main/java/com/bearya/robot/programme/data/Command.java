package com.bearya.robot.programme.data;

import java.util.Locale;

/**
 * 跳跳镇投屏业务码定义
 */
public final class Command {
    private Command() {
    }

    private static final String CITY_BASE = "city;";
    // 启动
    public static final String CITY_START = "10000";
    // 选择
    public static final String CITY_THEME = "10001";
    // 移动
    public static final String CITY_MOVE = "10002";
    // 结果;金币;步数;解决问题;创想模块;红旗模块;知识模块;
    public static final String CITY_RESULT_NORMAL = "10003;%d;%d;%d;%d;%d;%d";

    // 超时提示 首次
    public static final String CITY_LOCK_TIMEOUT = "10006";
    // 超时提示 最大时间
    public static final String CITY_LOCK_MAX_TIMEOUT = "10007";

    // T字路口
    public static final String CITY_ROAD_T = "10013";
    // 十字路口
    public static final String CITY_ROAD_X = "10014";
    // 禁止通行
    public static final String CITY_ROAD_NO = "10015";
    // 路口选择  正确
    public static final String CITY_ROAD_SELECT_OK = "10016;%s";
    // 路口选择  错误
    public static final String CITY_ROAD_SELECT_ERROR = "10017;%s";

    // 答对  恭喜你，答对啦
    public static final String CITY_ANSER_OK_1 = "10021";
    // 答对  欧耶，回答正确
    public static final String CITY_ANSER_OK_2 = "10022";
    // 答对  不错哦，答对啦
    public static final String CITY_ANSER_OK_3 = "10023";
    // 答错  这样不太好吧，我们来看看怎么做更合适吧
    public static final String CITY_ANSER_ERROR_1 = "10024";
    // 答错  很遗憾，没答对，我们来看看正确的做法吧
    public static final String CITY_ANSER_ERROR_2 = "10025";

    // 文明小标兵
    // 下雨小朋友没有带雨伞
    public static final String CITY_CIVILIZED_1 = "10031";
    // 路上吐痰
    public static final String CITY_CIVILIZED_2 = "10032";
    // 人行天桥
    public static final String CITY_CIVILIZED_3 = "10033";
    // 大肚子阿姨掉东西
    public static final String CITY_CIVILIZED_4 = "10034";
    // 小贝帮忙捡东西
    public static final String CITY_CIVILIZED_5 = "10035";
    // 环卫工人打扫卫生
    public static final String CITY_CIVILIZED_6 = "10036";
    // 小朋友帮助小贝捡东西
    public static final String CITY_CIVILIZED_7 = "10037";
    // 小贝撞到路人
    public static final String CITY_CIVILIZED_8 = "10038";
    // 遇见邻居爷爷
    public static final String CITY_CIVILIZED_9 = "10039";

    // 环保小达人
    // 路上吃三明治
    public static final String CITY_PROTECTION_1 = "10041";
    // 通过草地
    public static final String CITY_PROTECTION_2 = "10042";
    // 水龙头在滴水
    public static final String CITY_PROTECTION_3 = "10043";
    // 树苗快枯死
    public static final String CITY_PROTECTION_4 = "10044";
    // 丢电池
    public static final String CITY_PROTECTION_5 = "10045";
    // 小贝流鼻涕
    public static final String CITY_PROTECTION_6 = "10046";
    // 路边看到牛奶盒
    public static final String CITY_PROTECTION_7 = "10047";
    // 路边采花
    public static final String CITY_PROTECTION_8 = "10048";

    // 安全小卫士
    // 车辆着火
    public static final String CITY_SAFEGUARD_0 = "10050";
    // 老爷爷摔倒
    public static final String CITY_SAFEGUARD_1 = "10051";
    // 小盆友迷路
    public static final String CITY_SAFEGUARD_2 = "10052";
    // 雷雨天户外避雨
    public static final String CITY_SAFEGUARD_3 = "10053";
    // 火灾逃生
    public static final String CITY_SAFEGUARD_4 = "10054";
    // 不和陌生人说话
    public static final String CITY_SAFEGUARD_5 = "10055";
    // 地震逃生
    public static final String CITY_SAFEGUARD_6 = "10056";
    // 陌生人诱骗小贝
    public static final String CITY_SAFEGUARD_7 = "10057";
    // 不和陌生人走
    public static final String CITY_SAFEGUARD_8 = "10058";
    // 红绿灯
    public static final String CITY_SAFEGUARD_9 = "10059";

    // 爱心小天使
    // 撞电线杆
    public static final String CITY_LOVINGHEART_1 = "10061";
    // 听到鞭炮响
    public static final String CITY_LOVINGHEART_2 = "10062";
    // 肚子疼
    public static final String CITY_LOVINGHEART_3 = "10063";
    // 掉马路坑
    public static final String CITY_LOVINGHEART_4 = "10064";
    // 踩香蕉
    public static final String CITY_LOVINGHEART_5 = "10065";
    // 爱心;正确答案
    public static final String CITY_LOVINGHEART_OK = "10067;%s";
    // 爱心;错误答案
    public static final String CITY_LOVINGHEART_ERROR = "10068;%s";

    // 金币;金币数量
    public static final String CITY_GOLD = "10069;%d";

    // 游戏成功到达终点
    // 回家吃饭
    public static final String CITY_SUCCESS_HOME_1 = "10071";
    // 回家听妈妈讲故事
    public static final String CITY_SUCCESS_HOME_2 = "10072";
    // 回家睡觉
    public static final String CITY_SUCCESS_HOME_3 = "10073";
    // 图书馆添加了很多新的书
    public static final String CITY_SUCCESS_LIB_1 = "10074";
    // 图书馆和好朋友一起看书
    public static final String CITY_SUCCESS_LIB_2 = "10075";
    // 图书馆看到了一本神奇的书
    public static final String CITY_SUCCESS_LIB_3 = "10076";
    // 动物园小猴子很调皮
    public static final String CITY_SUCCESS_ZOO_1 = "10077";
    // 动物园骑乘大象
    public static final String CITY_SUCCESS_ZOO_2 = "10078";
    // 动物园和小鸟孔雀拍照
    public static final String CITY_SUCCESS_ZOO_3 = "10079";
    // 公园过山车
    public static final String CITY_SUCCESS_PARK_1 = "10080";
    // 公园旋转木马
    public static final String CITY_SUCCESS_PARK_2 = "10081";
    // 公园摩天轮看跳跳镇
    public static final String CITY_SUCCESS_PARK_3 = "10082";
    // 幼儿园玩玩具
    public static final String CITY_SUCCESS_SCHOOL_1 = "10083";
    // 幼儿园和朋友唱歌跳舞
    public static final String CITY_SUCCESS_SCHOOL_2 = "10084";
    // 学校老师很温柔
    public static final String CITY_SUCCESS_SCHOOL_3 = "10085";
    // 爱国教育主题到达天安门
    public static final String CITY_SUCCESS_PATRIOTISM = "10086";

    // 道路异常  没有路了
    public static final String CITY_EXCEPT_NOENTRY = "10091";
    // 道路异常  走出道路
    public static final String CITY_EXCEPT_OUTOFLOAD = "10092";
    // 道路异常  小贝走进避障区
    public static final String CITY_EXCEPT_DRIVEINOBSTACLE = "10093";
    // 道路异常  用户放置进避障区
    public static final String CITY_EXCEPT_PUTINOBSTACLE = "10094";
    // 道路异常  道路拼接错误
    public static final String CITY_EXCEPT_LOADCONNECT = "10095";
    // 道路异常  读头坏了一个
    public static final String CITY_EXCEPT_HARDWARE = "10096";
    // 道路异常  将小贝放置在两张地垫上了
    public static final String CITY_EXCEPT_INTOWLOAD = "10097";

    // 小贝要出发了发出的提示音;出发目的地的提示音乐
    public static final String CITY_GO = "10098;%s";
    // 红旗;红旗提示音乐名称
    public static final String CITY_FLAG = "10100;%s";
    // 知识;知识内容名称
    public static final String CITY_KNOWN = "10101;%s";

    public static String format(String format, Object... args) {
        String command = CITY_BASE + format;
        return args == null || args.length == 0 ? command : String.format(Locale.CHINA, command, args);
    }

}