package com.bearya.robot.programme.walk.load;

public class StationPinkLoad extends StationLoad {

    public static final int START_OID = 37200;
    public static final String NAME = "粉色站点";
    public static final int STATION_INDEX = 2;

    public StationPinkLoad() {
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