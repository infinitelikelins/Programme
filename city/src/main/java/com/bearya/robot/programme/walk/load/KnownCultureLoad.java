package com.bearya.robot.programme.walk.load;

import com.bearya.robot.programme.repository.PatriotismRepository;

public class KnownCultureLoad extends KnownLoad {

    public static final int START_OID = 53400;
    public static final String NAME = "中国文化百事通";

    public KnownCultureLoad() {
        super(START_OID);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected String themeName() {
        return PatriotismRepository.THEME_CULTURE;
    }
}