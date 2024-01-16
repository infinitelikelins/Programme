package com.bearya.robot.programme.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bearya.robot.base.ui.BaseActivity;
import com.bearya.robot.base.util.MusicUtil;
import com.bearya.robot.programme.R;
import com.bearya.robot.programme.view.ResultView;

public class RecordActivity extends BaseActivity {

    public static void start(Context context) {
        context.startActivity(new Intent(context, RecordActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        MusicUtil.stopMusic();
        MusicUtil.stopBgMusic();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        SharedPreferences sharedPreferences = getSharedPreferences("record", Context.MODE_PRIVATE);
        int gold = sharedPreferences.getInt("gold", 0);
        int step = sharedPreferences.getInt("step", 0);
        int goodDone = sharedPreferences.getInt("goodDone", 0);
        int stationLoad = sharedPreferences.getInt("stationLoad", 0);
        int flag = sharedPreferences.getInt("flag", 0);
        int known = sharedPreferences.getInt("known", 0);

        ResultView exceptionView = new ResultView(this, new ResultView.ResultListener() {
            @Override
            public void onExit() {
                finish();
            }

            @Override
            public void onRestart() {
                finish();
            }
        }, gold, step, goodDone, stationLoad, flag, known);
        exceptionView.withShowBackBtn(view -> finish());

        addView(exceptionView, "record");
    }

}