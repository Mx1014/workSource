package com.everhomes.parking.ketuo;

/**
 * @author sw on 2017/10/25.
 */
public class KetuoCarInfo {
    private Integer totalNum;
    private String plateNo;
    private String entryTime;
    private Integer parkingTime;

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public Integer getParkingTime() {
        return parkingTime;
    }

    public void setParkingTime(Integer parkingTime) {
        this.parkingTime = parkingTime;
    }
}
