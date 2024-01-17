package com.bearya.robot.programme.station;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bearya.robot.base.musicplayer.AudioRecorderManager;
import com.bearya.robot.base.play.PlayData;
import com.bearya.robot.base.util.MusicUtil;
import com.bearya.robot.base.util.ResourceUtil;
import com.bearya.robot.programme.R;
import com.buihha.audiorecorder.Mp3Recorder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

public class StationSoundFragment extends BaseFragment implements MediaPlayer.OnCompletionListener {
    private static final int REQUEST_CODE_SOUND = 1001;
    private String FolderPath;
    private ObjectAnimator animator;
    private String path;
    private TextView tvTimerRecord;
    private ImageView ivPreView;
    private AppCompatImageView ivSound;

    private int longTime;
    private final Handler handler = new Handler();
    private final Runnable recordTimerRunnable = new Runnable() {
        @Override
        public void run() {
            longTime++;
            tvTimerRecord.setText(String.format("录音中\n%d S", longTime));
            handler.postDelayed(this, 1000);
        }
    };

    public static StationSoundFragment newInstance() {
        return new StationSoundFragment();
    }

    private void initFolderPath(Context context) {
        File file = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        if (file == null) {
            throw new IllegalStateException("Failed to get external storage files directory");
        } else if (file.exists()) {
            if (!file.isDirectory()) {
                throw new IllegalStateException(file.getAbsolutePath() +
                        " already exists and is not a directory");
            }
        } else {
            if (!file.mkdirs()) {
                throw new IllegalStateException("Unable to create directory: " +
                        file.getAbsolutePath());
            }
        }

        FolderPath = file.getAbsolutePath();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFolderPath(requireContext());
        AudioRecorderManager.getInstance().init(new Mp3Recorder.OnRecordListener() {
            @Override
            public void onStart() {
                handler.post(recordTimerRunnable);
                ivPreView.setImageResource(R.drawable.ic_listener_selector);
                ivPreView.setVisibility(View.GONE);
            }

            @Override
            public void onStop() {
                PlayData station = getStation();
                if (station!=null){
                    station.sound = path;
                    station.soundLongTime = longTime;
                    saveStation();
                }
                handler.removeCallbacks(recordTimerRunnable);
                longTime = 0;
                tvTimerRecord.setText("");
                ivPreView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRecording(int sampleRate, double volume) {

            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_station_config_sound;
    }

    @Override
    protected void initView(View view) {
        tvTimerRecord = view.findViewById(R.id.tvSecondRecord);
        ivPreView = view.findViewById(R.id.ivPreView);
        ivSound = view.findViewById(R.id.ivSound);
        AppCompatTextView btnSoundLib = view.findViewById(R.id.btnSoundLib);

        ivSound.setOnClickListener(view13 -> {
            view13.setSelected(!view13.isSelected());
            if (view13.isSelected()) {
                startRecord();
            } else {
                stopRecord();
            }
        });
        ivPreView.setOnClickListener(view12 -> {
            view12.setSelected(!view12.isSelected());
            if (view12.isSelected()) {
                playRecord();
            } else {
                stopPlayRecord();
            }
        });
        ivPreView.setOnLongClickListener(view1 -> {
            MusicUtil.stopMusic();
            PlayData station = getStation();
            if (station != null) {
                deleteFile(station.sound);
                station.sound = "";
                station.soundLongTime = 0;
                saveStation();
            }
            ivPreView.setVisibility(View.GONE);
            return true;
        });
        btnSoundLib.setOnClickListener(v -> {
            MusicUtil.stopMusic();
            stopRotate();
            LibActivity.start(getActivity(), REQUEST_CODE_SOUND, false);
        });
    }

    @Override
    protected void loadLastStationConfig() {
        PlayData station = getStation();
        if (station != null && !TextUtils.isEmpty(station.sound)) {
            if (station.soundLongTime > 0) {
                ivPreView.setVisibility(View.VISIBLE);
                path = station.sound;
            } else {
                LibItem item = StationsActivity.stationLib.getSoundItemByMp3(station.sound);
                if (item != null) {
                    loadSystemSoundConfig(item);
                }
            }
        }
    }

    private void loadSystemSoundConfig(final LibItem soundItem) {
        try {
            Glide.with(getActivity()).load(ResourceUtil.getMipmapId(soundItem.image)).centerCrop()
                    .bitmapTransform(new GlideCircleTransform(getActivity())).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(ivPreView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ivPreView.setVisibility(View.VISIBLE);
        path = "android_asset/" + soundItem.mp3;
    }

    private void stopRecord() {
        AudioRecorderManager.getInstance().stop();
    }

    private void startRecord() {
        if (MusicUtil.isPlaying()) {
            MusicUtil.stopMusic();
            MusicUtil.stopBgMusic();
        }
        stopPlayRecord();
        path = FolderPath + "/" + System.currentTimeMillis() + ".mp3";
        AudioRecorderManager.getInstance().startRecord(FolderPath, System.currentTimeMillis() + ".mp3");
    }

    @Override
    public void onStop() {
        super.onStop();
        stopRecord();
        stopPlayRecord();
    }

    public void onUnselected() {
        stopRecord();
        stopPlayRecord();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        ivPreView.setSelected(false);
        stopRotate();
    }

    private void startRotate() {
        if (animator != null) {
            stopRotate();
        }
        animator = ObjectAnimator.ofFloat(ivPreView, "rotation", 0, 359);
        animator.setDuration(8000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();
    }

    private void stopRotate() {
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
        ivPreView.setRotation(0);
    }

    /**
     * 播放MP3
     */
    private void playRecord() {
        if (AudioRecorderManager.getInstance().isRecording()) {
            AudioRecorderManager.getInstance().stop();
        } else {
            longTime = 0;
            tvTimerRecord.setText("");
            ivPreView.setVisibility(View.VISIBLE);
            handler.removeCallbacks(recordTimerRunnable);
        }
        try {
            MusicUtil.play(path, this);
            startRotate();
        } catch (Exception e) {
            ivPreView.setSelected(false);
            e.printStackTrace();
        }
    }

    private void stopPlayRecord() {
        MusicUtil.stopMusic();
        ivPreView.setSelected(false);
        stopRotate();
        loadLastStationConfig();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SOUND && resultCode == Activity.RESULT_OK) {
            final LibItem soundItem = data.getParcelableExtra("data");
            PlayData playData = getStation();
            if (playData != null) {
                playData.sound = soundItem.mp3;
                playData.soundLongTime = 0;
                saveStation();
                loadSystemSoundConfig(soundItem);
            }
        }
    }

    private void deleteFile(String sound) {
        try {
            if (sound.contains("storage/"))
                new File(sound).delete();
        } catch (Exception ignored) {

        }
    }
}