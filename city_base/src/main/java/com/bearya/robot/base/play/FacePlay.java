package com.bearya.robot.base.play;

public class FacePlay {

    private final String face;
    private final FaceType faceType;

    public FacePlay(String face, FaceType faceType) {
        this.face = face;
        this.faceType = faceType;
    }

    public String getFace() {
        return face;
    }

    public FaceType getFaceType() {
        return faceType;
    }

    public int getTime() {
        return 1;
    }

}
