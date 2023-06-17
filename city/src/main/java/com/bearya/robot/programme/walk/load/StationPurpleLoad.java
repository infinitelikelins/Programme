package com.bearya.robot.programme.walk.load;

public class StationPurpleLoad extends StationLoad {

    public static final int START_OID = 41700;
    public static final String NAME = "紫色站点";
    public static final int STATION_INDEX = 4;

    public StationPurpleLoad() {
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