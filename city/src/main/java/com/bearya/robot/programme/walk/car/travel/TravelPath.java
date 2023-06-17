package com.bearya.robot.programme.walk.car.travel;

import java.util.LinkedList;
import java.util.List;

public class TravelPath {

    private final List<Travel> travelList = new LinkedList<>();

    public TravelPath(int[] oids) {
        for (int oid : oids) {
            addTravel(new TravelCrossOver(oid));
        }
    }

    public void reset(){
        travelList.clear();
    }

    public void addTravel(Travel travel){
        travelList.add(travel);
    }

    public Travel nextTravel(){
        return travelList.remove(0);
    }

    public boolean isEmpty(){
        return travelList.isEmpty();
    }

}
