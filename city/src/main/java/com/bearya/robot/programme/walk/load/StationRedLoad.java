package com.bearya.robot.programme.walk.load;

public class StationRedLoad extends StationLoad {

    public static final int START_OID = 38100;
    public static final String NAME = "红色站点";
    public static final int STATION_INDEX = 5;

    public StationRedLoad() {
        super(START_OID);
    }

    @Override
    public int getStationIndex() {
        return STATION_INDEX;
    }

    @Override
    public String getName() {
        return NAME;
    }

}
