package com.everhomes.rest.aclink;


/**
 * <ul>
 * <li>floor: 楼层</li>
 * <li>floorName: 名称</li>
 * </ul>
 */
public class FloorDTO {

    private String floor;
    private String floorName;

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }
}
