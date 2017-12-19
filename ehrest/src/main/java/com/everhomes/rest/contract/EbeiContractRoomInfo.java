package com.everhomes.rest.contract;

/**
 * Created by ying.xiong on 2017/11/21.
 */
public class EbeiContractRoomInfo {
    private String houseRoomCode;
    private String infoId;
    private String houseRoom;
    private double paidArea;

    public String getHouseRoom() {
        return houseRoom;
    }

    public void setHouseRoom(String houseRoom) {
        this.houseRoom = houseRoom;
    }

    public double getPaidArea() {
        return paidArea;
    }

    public void setPaidArea(double paidArea) {
        this.paidArea = paidArea;
    }

    public String getHouseRoomCode() {
        return houseRoomCode;
    }

    public void setHouseRoomCode(String houseRoomCode) {
        this.houseRoomCode = houseRoomCode;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }
}
