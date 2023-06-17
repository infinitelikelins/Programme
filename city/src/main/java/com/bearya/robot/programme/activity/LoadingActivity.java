package com.bearya.robot.programme.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bearya.robot.base.ui.BaseActivity;
import com.bearya.robot.programme.R;

public class LoadingActivity extends BaseActivity {

    public static void start(Context context) {
        context.startActivity(new Intent(context, LoadingActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        new Handler().postDelayed(() -> {
            CityActivity.start(this);
            finish();
        }, 2000);
    }

}