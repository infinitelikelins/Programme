package com.bearya.robot.programme.walk.load;

public class StationBlueLoad extends StationLoad {

    public static final int START_OID = 39900;
    public static final String NAME = "蓝色站点";
    public static final int STATION_INDEX = 1;

    public StationBlueLoad() {
        super(START_OID);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getStationIndex() {
        return STATION_INDEX;
    }

}