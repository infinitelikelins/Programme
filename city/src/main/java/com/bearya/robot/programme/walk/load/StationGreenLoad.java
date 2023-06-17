package com.bearya.robot.programme.walk.load;

public class StationGreenLoad extends StationLoad {

    public static final int START_OID = 40800;
    public static final String NAME = "绿色站点";
    public static final int STATION_INDEX = 3;

    public StationGreenLoad() {
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