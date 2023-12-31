package com.bearya.robot.programme.walk.load;

public class StationYellowLoad extends StationLoad {

    public static final int START_OID = 39000;
    public static final String NAME = "黄色站点";
    public static final int STATION_INDEX = 6;

    public StationYellowLoad() {
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