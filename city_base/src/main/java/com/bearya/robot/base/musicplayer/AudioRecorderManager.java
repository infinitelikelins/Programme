package com.bearya.robot.base.musicplayer;

import com.buihha.audiorecorder.Mp3Recorder;

import java.io.IOException;

public class AudioRecorderManager {

    private Mp3Recorder mp3Recorder;

    private static AudioRecorderManager mInstance;

    public static AudioRecorderManager getInstance() {
        if (mInstance == null) {
            mInstance = new AudioRecorderManager();
        }
        return mInstance;
    }

    private AudioRecorderManager() {
        mp3Recorder = new Mp3Recorder();
    }

    public void init(Mp3Recorder.OnRecordListener onRecordListener) {
        mp3Recorder.setOnRecordListener(onRecordListener);
    }

    public boolean isRecording() {
        return mp3Recorder.isRecording();
    }

    public void startRecord(String filePath, String fileName) {
        try {
            if (mp3Recorder.isRecording()) {
                mp3Recorder.stopRecording();
                return;
            }
            mp3Recorder.startRecording(filePath, fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        mp3Recorder.stopRecording();
    }

    public void release() {
        if (mp3Recorder.isRecording()) {
            mp3Recorder.stopRecording();
        }
        mp3Recorder = null;
        mInstance = null;
    }

}