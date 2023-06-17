package com.bearya.robot.programme.station;


import java.util.ArrayList;
import java.util.List;

public class Lib{
    long uuid;
    public String icon;
    String name;
    List<LibItem> items;

    public Lib() {
        items = new ArrayList<>();
    }

    public int getCount() {
        return items != null ? items.size() : 0;
    }
}
