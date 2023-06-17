package com.bearya.robot.programme.walk.load;

import com.bearya.robot.programme.repository.PatriotismRepository;

public class KnownHistoryLoad extends KnownLoad {

    public static final int START_OID = 52500;
    public static final String NAME = "小小党史宣传员";

    public KnownHistoryLoad() {
        super(START_OID);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected String themeName() {
        return PatriotismRepository.THEME_HISTORY;
    }
}