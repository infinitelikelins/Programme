package com.bearya.robot.base.can;

import android.os.Bundle;
import android.os.Parcelable;

import java.util.ArrayList;

public class Messager {

    public static final String ACTION_TALKYPEN_FROM_CLIENT = "action_talkypen_from_client";
    public static final String ACTION_TALKYPEN_FROM_SERVICE = "action_talkypen_from_service";
    public static final String VALUE = "value";
    public static final String EMOTION = "emotion";
    public static final String TTS = "tts";
    public static final String AUDIO = "audio";
    public static final String ACTION = "action";
    public static final String ACTION_OPEN = "open";
    public static final String ACTION_UPGRADE = "upgrade";
    public static final String ACTION_UPGRADE_SUCCESS = "success";
    public static final String ACTION_UPGRADE_FAIL = "fail";
    public static final String ACTION_OTA = "ota";

    public static final int SERVICE_INITED = 3;//回答有注册触摸身体部分
    public static final int SERVICE_STARTED = 1;//发送服务已启动
    public static final int ASK_REGISTER_TOUCH_BODY = 2;//询问是否有App注册触摸身体部分
    public static final int OBSTACLE = 4;//避障
    public static final int TOUCH_HEADER = 5;//执行摸头
    public static final int WAKE_UP = 7;//执行摸头
    public static final int ASK_SERVICE_INIT = 6;//


    public static final int GAME_SHOPPING_TRAILING = 101;//寻迹
    public static final int GAME_SHOPPING_SCAN_GOODS = 102;//扫描物品
    public static final int GAME_SHOPPING_SCAN_MONEY = 103;//扫描零钱
    public static final int GAME_SHOPING_SCAN_SHOP_CARD = 105;//扫描商品卡
    public static final int GAME_SHOPING_RESTART = 106;//新一轮地垫开始
    public static final int GAME_SHOPING_STOP_ALL_ACTION = 107;

    public static final int GAME_ENGINEER_OID = 108;//小小工程师/编程总动员

    public static final int TEST = 100000001;
    public static final int TEST2 = 100000002;
    public static final int TEST_CODING_MASTER_PERFORM = 100000003;
    public static final int TEST_CODING_MASTER_LOCATE = 100000004;

    public static final int DATA_DICE = 104;//摇骰子
    public static final String OID_FROM = "oid_from";

    public static final int CONTROL_BAR = 201;
    public static final int SHOW_EMOTION = 202;
    public static final int CONTROL_SPEECH_CMD_ID = 203;
    public static final int CONTROL_LIGHT = 204;

//    public static final int CONTROL_DANCE = 205;

    public static final int OPEN_SETTING = 400;
    private static final String KEY_MSG_ID = "msg_id";
    public static final int APP_RESUME = 2001;
    public static final int APP_PAUSE = 2002;

    public static final int WAKE_UP_PLAY = 3000;//播放
    public static final int WAKE_UP_STOP = 3001;//停止
    public static final int WAKE_UP_PAUSE = 3002;//暂停
    public static final int WAKE_UP_PREVIOUS = 3003;//上一首
    public static final int WAKE_UP_NEXT = 3004;//下一首


    public static final int PLAY_TTS = 5555;
    public static final int PLAY_ACTION = 5556;
    public static final int PLAY_AUDIO = 5557;
    public static final int PLAY_BLEND = 5558;
    public static final int PLAY_SPEECHID = 5559;
    public static final int SERVICE_CONTROL_ASR = 5560;//由服务端处理语音识别结果
    public static final int PLAY_SOUND_EFFECT = 5561;//由服务端处理语音识别结果
    public static final int PLAY_TTS_EMOTION = 5562;
    public static final int STOP_PLAY_TTS = 5563;
    public static final int PLAY_EMOTION = 5564;
    public static final int PLAY_LAUNCH = 5567;

    public static final int SEND_TTS_TO_SERVER = 5565;

    public static final int PLAY_ACTION_END = 5566;//动作结束

    public static final int SAVE_LEARN_RECORD = 8000;//保存学习记录

    public static final int BIND_PARENT = 8001;//绑定操作
    public static final int BIND_PARENT_RESULT = 8002;
    public static final int UNBIND_PARENT = 8003;
    public static final int UPDATE_SOFT = 8004;//软件更新
    public static final int RESOURCE_NOT_FOUND = 8005;//资源没有找到

    public static final int OPEN_GAME = 9001;//开启各类游戏
    public static final int SEND_EVENT_START = 91000;//发送事件
    public static final int SEND_EVENT_END = 91001;//发送事件
    public static final int SEND_EVENT_START_END = 91002;//发送事件

    public static final int SCREEN_CLICKED = 92003;//唤醒

    public static final int LITTLE_ENGINEER_TRAVEL_INIT = 92006;//初始化
    public static final int LITTLE_ENGINEER_TRAVEL = 92007;//行走
    public static final int LITTLE_ENGINEER_TRAVEL_COMPLETE = 92008;
    public static final int LITTLE_ENGINEER_GO_NEAR_BLOCK = 92009;//走到离机器人最近的16X16格子中
    public static final int LITTLE_ENGINEER_TRAVEL_ARRIVE = 920010;//到达
    public static final int GAME_PAUSE = 920011;
    public static final int GAME_RUNNING = 920012;

    public static final int STOP_LIVE_STREAM = 100004;//停止直播推送

    public static final int ASK_ROBOT_ON_MAP = 100005;//询问小贝是否放置在地垫上
    public static final int ASK_ROBOT_ON_MAP_RESP = 100006;//回答小贝是否放置在地垫上
    public static final int NOTIFY_SCAN_BIND_RESULT = 100007;//通知扫描绑定结果
    public static final int LAUNCH_TRANSLATE = 100008;//通知扫描绑定结果
    public static final int RESTART_CAN = 100009;//重启Can
    public static final int FINISH_SELF = 100010;//重启Can
    public static final int DATA_OID = 100011;//oid
    public static final int PLAY_TTS_END = 100012;
    public static final int STOP_TTS = 100013;
    public static final int UPDATE_ROBOT_INFO = 100014;//更新机器人信息
    public static final int SAVE_LOG = 100015;//更新机器人信息
    public static final int STOP_ALL = 100016;//更新机器人信息
    public static final int KEEP_SCREEN_ON = 100017;//屏幕常亮
    public static final int KEEP_SCREEN_OFF = 100018;//屏幕开启屏保
    public static final int CLOSE_CAN = 100019;//关闭Can
    public static final int OPEN_CAN = 100025;//关闭Can
    public static final int COMMAND = 100022;//命令词

    public static final int TOUCH_BODY = 100050;//触摸身体
    public static final int START_TRAVEL = 100051;//寻迹到达

    public static final int UP_LEFT_HAND = 100052;//抬左手
    public static final int UP_RIGHT_HAND = 100053;//抬右手
    public static final int ACTION_RESET = 100054;//重置

    public static final int ACTION_FORWARD  =  100055;//前进
    public static final int ACTION_BACKWARD = 100056;//后退
    public static final int ACTION_LEFT     =  100057;//左转
    public static final int ACTION_RIGHT    = 100058;//右转

    public static final int START_RECORD    = 100059;//开始录音
    public static final int STOP_RECORD     = 100060;//停止录音
    public static final int RECORD_COMPLETE = 100061;//录音完成



    public static final int HARDWARE_EXCEPTION = 100023;//硬件异常
    public static final int UPDATE_DEBUG_SWITCH = 100024;//更新开发者开关



    public static final int ROBOT_LIGHT = 200017;
    public static final int ROBOT_BODY_ACTION = 200018;
    public static final int ROBOT_AGORA_SERVICE = 300018;//重启声网服务

    public static final String LIGHT_POSITION = "light_position";
    public static final String LIGHT_MODE = "light_mode";
    public static final String LIGHT_FREQUENCY = "light_frequency";
    public static final String LIGHT_COLOR = "light_color";
    public static final String LIGHT_RESET = "light_reset";
    public static final String TIME = "time";
    public static final String TIMES = "times";
    public static final String COMMAND_CONTENT = "command_content";

    //颜色
    public static final int COLOR_RED = 0;
    public static final int COLOR_YELLOW = 1;
    public static final int COLOR_GREEN = 2;
    public static final int COLOR_CYAN = 3;
    public static final int COLOR_BLUE = 4;
    public static final int COLOR_PURPLE = 5;
    public static final int COLOR_WHITE = 6;

    //位置
    public static final int POSITION_USB = 1<<1;
    public static final int POSITION_LEFT_EAR = 1<<2;
    public static final int POSITION_RIGHT_EAR = 1<<3;
    public static final int POSITION_LEFT_HAND = 1<<4;
    public static final int POSITION_RIGHT_HAND = 1<<5;
    public static final int POSITION_CHEST = 1<<6;
    public static final int POSITION_BACK = 1<<7;

    //点亮模式，上位机的灯光没有频闪，为了统一，只提供这两种模式
    public static final int MODE_NORMAL= 1;
    public static final int MODE_BREATH = 2;

    //频率
    public static final int FREQUENCY_FAST = 10;
    public static final int FREQUENCY_MIDDLE = 20;
    public static final int FREQUENCY_SLOW = 30;

    private int msgId;
    private Bundle bundle;

    public Messager(int msgId, Bundle bundle) {
        this.msgId = msgId;
        if(bundle==null){
            bundle = new Bundle();
        }
        this.bundle = bundle;
        this.bundle.putInt(KEY_MSG_ID,msgId);
    }

    /**
     * 接收端使用
     * @param bundle
     */
    public Messager(Bundle bundle) throws Exception {
        msgId = bundle.getInt(KEY_MSG_ID);
        this.bundle = bundle;
    }

    /**
     * 根据ID创建
     * 发端使用
     * @param msgId
     */
    public Messager(int msgId) {
        this(msgId,new Bundle());
    }

    /**
     *
     * @param msgId
     * @param keyValuePair key1,value1,key2,value2.....
     */
    public Messager(int msgId, Object... keyValuePair){
        this(msgId);
        bundle.putAll(arrayToBundle(keyValuePair));
    }

    public Bundle getBundle() {
        return bundle;
    }

    public int getMsgId() {
        return msgId;
    }

    /**
     *将key-value成对写入到Bundle中
     * @param data key,value,key,value
     * @return
     */
    public static Bundle arrayToBundle(Object... data){
        Bundle bundle = new Bundle();
        for(int i=0;i<data.length/2;i++){
            String key = String.valueOf(data[2*i]);
            Object value = data[2*i+1];
            if(value instanceof Integer){
                bundle.putInt(key,(Integer)value);
            }else if(value instanceof Float){
                bundle.putFloat(key,(Float)value);
            }else if(value instanceof Double){
                bundle.putDouble(key,(Double) value);
            }else if(value instanceof String){
                bundle.putString(key,(String)value);
            }else if(value instanceof Boolean){
                bundle.putBoolean(key,(Boolean)value);
            }else if(value instanceof Long){
                bundle.putLong(key,(Long)value);
            }else if(value instanceof ArrayList){
                bundle.putParcelableArrayList(key,(ArrayList)value);
            }else if(value  instanceof Parcelable){
                bundle.putParcelable(key,(Parcelable)value);
            }
        }
        return bundle;
    }
}
