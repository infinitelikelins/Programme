package com.bearya.robot.programme.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.bearya.robot.base.play.PlayData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class WalkHistory implements Parcelable {

    private long id;
    private String playData;

    public WalkHistory(long id, List<PlayData> playDataArrayList) {
        this.id = id;
        this.playData = new Gson().toJson(playDataArrayList);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlayData() {
        return playData;
    }

    public void setPlayData(String playData) {
        this.playData = playData;
    }

    public List<PlayData> getPlayDataArrayList() {
        return new Gson().fromJson(playData, new TypeToken<List<PlayData>>() {}.getType());
    }

    protected WalkHistory(Parcel in) {
        id = in.readLong();
        playData = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(playData);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WalkHistory> CREATOR = new Creator<WalkHistory>() {
        @Override
        public WalkHistory createFromParcel(Parcel in) {
            return new WalkHistory(in);
        }

        @Override
        public WalkHistory[] newArray(int size) {
            return new WalkHistory[size];
        }
    };

}