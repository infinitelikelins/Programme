package com.bearya.robot.base;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;

import com.bearya.actionlib.utils.RobotActionManager;
import com.bearya.actionlib.utils.SharedPreferencesUtil;
import com.bearya.robot.base.can.CanManager;
import com.bearya.robot.base.load.ILoadMgr;
import com.bearya.robot.base.play.Director;
import com.bearya.robot.base.ui.BaseActivity;
import com.bearya.robot.base.util.DebugUtil;
import com.bearya.robot.base.util.DeviceUtil;
import com.bearya.robot.base.util.MusicUtil;
import com.bearya.sdk.bluetooth.v2.BluetoothMonitorService;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mmkv.MMKV;

import java.util.Locale;

public abstract class BaseApplication extends Application {

    private static final String BT_LAST_CASTING_MAC = "bt_last_casting_mac";
    public static boolean AUTO_UNLOCK = false;

    private static BaseApplication mInstance;

    private String MAC = null;

    private final Handler handler = new Handler();
    private long lastMoveTimeStamp = 0;

    public static boolean isEnglish = false;

    public static BaseApplication getInstance() {
        return mInstance;
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            DebugUtil.debug("-- application do kill -- ");
            release();
        }
    };

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        MusicUtil.init();
        MMKV.initialize(this);

        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getInstance());
        strategy.setAppVersion(String.format(Locale.CHINA, "%s-%d", DeviceUtil.getVersionName(getApplicationContext()), DeviceUtil.getVersionCode(getApplicationContext())));
        CrashReport.initCrashReport(this, "1f0fb3c5fb", BuildConfig.DEBUG, strategy);
        CrashReport.setUserId(DeviceUtil.getRKBroadProductCode());

        registerReceiver(receiver, new IntentFilter("bearya.intent.action.KILL_APP"));

        handler.postDelayed(() -> CanManager.getInstance().startScan(), 3000);
        MAC = SharedPreferencesUtil.getInstance(this).getString(BT_LAST_CASTING_MAC);

        changeLanguage();
    }

    public Handler getHandler() {
        return handler;
    }

    /**
     * 移动一下小贝
     */
    public void moveALittle(boolean isGoAhead) {
        if (System.currentTimeMillis() - lastMoveTimeStamp > 500) {
            lastMoveTimeStamp = System.currentTimeMillis();
            if (isGoAhead) {
                RobotActionManager.goAhead(10, 10, "");
            } else {
                RobotActionManager.goBack(10, 10, "");
            }
            handler.postDelayed(RobotActionManager::stopWheel, 500);
        }
    }

    private void changeLanguage() {
        String relativePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.bearya.robot.programme/mmkv";
        MMKV mmkv = MMKV.mmkvWithID("programme-language", MMKV.MULTI_PROCESS_MODE, "PROGRAMME-LANGUAGE", relativePath);
        isEnglish = mmkv != null && mmkv.decodeBool("language-en", false);
        if (mmkv != null) {
            mmkv.close();
        }
    }

    public void release() {
        RobotActionManager.reset();
        Director.getInstance().release();
        sendBroadcast(new Intent(BluetoothMonitorService.ACTION_STOP_SERVICE));
        BaseActivity.finishAllActivity();
        MusicUtil.stopMusic();
        MusicUtil.stopBgMusic();
        CanManager.getInstance().release();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public abstract ILoadMgr getLoadMgr();

    public static String currentCommand = "city;10000";

    public static void sendAction(String command) {
        currentCommand = command;
        getInstance().sendBroadcast(new Intent(BluetoothMonitorService.ACTION_BLUETOOTH_COMMAND).putExtra(BluetoothMonitorService.BLUETOOTH_COMMAND, command));
    }

    public void updateBTMac(String mac){
        MAC = mac;
    }

    public void saveBtMac(){
        SharedPreferencesUtil.getInstance(getInstance()).put(BT_LAST_CASTING_MAC,MAC);
    }

    public String getMAC() {
        return MAC;
    }
}