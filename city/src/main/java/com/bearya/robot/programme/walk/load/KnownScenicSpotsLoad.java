package com.bearya.robot.programme.walk.load;

import com.bearya.robot.programme.repository.PatriotismRepository;

public class KnownScenicSpotsLoad extends KnownLoad {

    public static final int START_OID = 56100;
    public static final String NAME = "名胜古迹小导游";

    public KnownScenicSpotsLoad() {
        super(START_OID);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected String themeName() {
        return PatriotismRepository.THEME_SCENIC;
    }

}