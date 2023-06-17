package com.bearya.robot.programme.entity;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class PatriotismContent {

    // name
    private final String contentName;

    // cover
    private final String contentCover;

    // Pair <sound , face>
    private final List<Pair<String, String>> content;

    public PatriotismContent(String contentName, int contentSize) {
        this.contentName = contentName;
        content = new ArrayList<>();
        contentCover = "file:///android_asset/patriotism/" + contentName + "/cover.webp";
        for (int index = 1; index <= contentSize; index++) {
            String path = "patriotism/" + contentName + "/" + index;
            String face = "file:///android_asset/" + path + ".webp";
            String sound = path + ".mp3";
            content.add(new Pair<>(sound, face));
        }
    }

    public String getContentName() {
        return contentName;
    }

    public String getContentCover() {
        return contentCover;
    }

    public List<Pair<String, String>> getContent() {
        return content;
    }

}