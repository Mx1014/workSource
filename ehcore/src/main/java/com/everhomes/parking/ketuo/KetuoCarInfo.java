package com.everhomes.parking.ketuo;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author sw on 2017/10/25.
 */
public class KetuoCarInfo {
    private Long id;
    private Integer totalNum;
    private String plateNo;
    private String entryTime;
    private Integer parkingTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getlEntryTime() throws Exception{
        if(entryTime==null)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        long ts;
        try {
            ts = sdf.parse(entryTime).getTime();
        } catch (ParseException e) {
            throw  e;
        }
        return ts;
    }
}
