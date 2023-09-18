package com.bearya.robot.programme.entity;

public class IntroductionEntity {
    public static final String PHOTO = "photo";
    public static final String VIDEO = "video";
    public static final String AUDIO = "audio";

    /**
     * 介绍类型 ： 视频， 音频， 图片
     */
    private final String type;

    /**
     * 列表的图片
     */
    private final String cover;

    /**
     * 具体的文件地址
     */
    private final String fileDir;

    public IntroductionEntity(String type, String itemCover, String file) {
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