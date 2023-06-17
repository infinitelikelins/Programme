package com.bearya.robot.programme.walk.load;

import com.bearya.robot.programme.repository.PatriotismRepository;

public class KnownScienceTechLoad extends KnownLoad {

    public static final int START_OID = 54300;
    public static final String NAME = "小小科技宣传员";

    public KnownScienceTechLoad() {
        super(START_OID);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected String themeName() {
        return PatriotismRepository.THEME_SCIENCE;
    }
}
