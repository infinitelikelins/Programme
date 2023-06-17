package com.bearya.robot.programme.walk.load;

import com.bearya.robot.programme.repository.PatriotismRepository;

public class KnownPatriotismLoad extends KnownLoad {

    public static final int START_OID = 51600;
    public static final String NAME = "祖国母亲我爱你";

    public KnownPatriotismLoad() {
        super(START_OID);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected String themeName() {
        return PatriotismRepository.THEME_PATRIOTISM;
    }
}