package com.bearya.robot.programme.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.bearya.robot.base.BaseApplication;
import com.bearya.robot.base.ui.BaseLauncherActivity;
import com.bearya.robot.base.ui.LauncherData;
import com.bearya.robot.programme.R;
import com.bearya.robot.programme.data.Command;
import com.bearya.sdk.bluetooth.v2.BluetoothMonitorService;
import com.bearya.sdk.bluetooth.v2.SocketConnectStatus;

public class LauncherActivity extends BaseLauncherActivity {

    private final BroadcastReceiver bluetoothBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            String intentAction = intent.getAction();
            if (TextUtils.equals(intentAction, BluetoothMonitorService.ACTION_UPDATE_STATUS)) {
                String action = intent.getStringExtra(BluetoothMonitorService.UPDATE_STATUS);
                if (action != null) {
                    Toast.makeText(context, action, Toast.LENGTH_SHORT).show();
                }
                if (SocketConnectStatus.CONNECT_BREAK.getValue().equals(action)) {
                    new Thread(() -> {
                        try {
                            Thread.sleep(6000);
                            context.sendBroadcast(new Intent(BluetoothMonitorService.ACTION_RECONNECT));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                } else if (SocketConnectStatus.CONNECT_SUCCESS.getValue().equals(action)) {
                    BaseApplication.sendAction(Command.format(Command.CITY_START));
                    BaseApplication.getInstance().saveBtMac();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BluetoothMonitorService.start(this, null);

        IntentFilter filter = new IntentFilter(BluetoothMonitorService.ACTION_UPDATE_STATUS);
        registerReceiver(bluetoothBroadcastReceiver, filter);

    }

    @Override
    protected LauncherData getLauncherData() {
        LauncherData launcherData = new LauncherData();
        launcherData.jumpToActivity = CityActivity.class;
        launcherData.bg = R.mipmap.bg_splash;
        launcherData.bgMp3 = "tts/zh/touch_screen.mp3";
        launcherData.tipMp3 = "tts/zh/launch_bg.mp3";
        return launcherData;
    }

    @Override
    protected void startHistoryRecord() {
        RecordActivity.start(this);
    }

    @Override
    protected void startBluetoothCasting() {
        DiscoveryActivity.start(this);
    }

    @Override
    protected void startWork() {
        HistoryActivity.start(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        BaseApplication.sendAction(Command.format(Command.CITY_START));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 200 && requestCode == 200) {
            if (data != null && data.getParcelableExtra("device") != null) {
                BluetoothMonitorService.start(getApplicationContext(), data.getParcelableExtra("device"));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bluetoothBroadcastReceiver);
    }

}