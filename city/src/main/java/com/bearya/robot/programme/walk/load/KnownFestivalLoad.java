package com.bearya.robot.programme.walk.load;

import com.bearya.robot.programme.repository.PatriotismRepository;

public class KnownFestivalLoad extends KnownLoad {

    public static final int START_OID = 55200;
    public static final String NAME = "中国节日宣传员";

    public KnownFestivalLoad() {
        super(START_OID);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected String themeName() {
        return PatriotismRepository.THEME_FESTIVAL;
    }
}