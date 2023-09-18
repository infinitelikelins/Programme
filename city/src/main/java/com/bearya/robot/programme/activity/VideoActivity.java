package com.bearya.robot.programme.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bearya.robot.base.ui.BaseActivity;
import com.bearya.robot.programme.R;

public class VideoActivity extends BaseActivity {

    public static void start(Context context, String videoPath) {
        Intent starter = new Intent(context, VideoActivity.class);
        starter.putExtra("video", videoPath);
        context.startActivity(starter);
    }

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        findViewById(R.id.btnBack).setOnClickListener(view-> finish());

        String videoPath = getIntent().getStringExtra("video");

        videoView = findViewById(R.id.video);
        videoView.setMediaController(new MediaController(this));
        videoView.setOnCompletionListener(mp -> finish());
        videoView.setVideoPath(videoPath);
        videoView.requestFocus();

    }

    @Override
    protected void onStop() {
        super.onStop();
        videoView.stopPlayback();
    }
}
