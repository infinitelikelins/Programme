package com.bearya.robot.programme.entity;

public class IntroductionEntity {

    public static final String VIDEO = "video";
    public static final String AUDIO = "audio";
    public static final String PICTURE = "picture";

    private final String type;
    private final String cover;
    private final String fileDir;

    public IntroductionEntity(String type,String itemCover, String file) {
        this.type = type;
        this.cover = itemCover;
        this.fileDir = file;
    }

    public String getType() {
        return type;
    }

    public String getFileDir() {
        return fileDir;
    }

    public String getCover() {
        return cover;
    }
}