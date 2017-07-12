package com.everhomes.parking.bosigao;

/**
 * Created by Administrator on 2017/4/10.
 */
public class BosigaoCarLockInfo {
    private Integer ID;
    private String RecordID;
    private String PlateNumber;
    private Integer Status;

    private String LockDate;
    private String ParkingID;
    private String SystemID;
    private String VillageID;
    private String ParkingName;
    private String EntranceDate;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getRecordID() {
        return RecordID;
    }

    public void setRecordID(String recordID) {
        RecordID = recordID;
    }

    public String getPlateNumber() {
        return PlateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        PlateNumber = plateNumber;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public String getLockDate() {
        return LockDate;
    }

    public void setLockDate(String lockDate) {
        LockDate = lockDate;
    }

    public String getParkingID() {
        return ParkingID;
    }

    public void setParkingID(String parkingID) {
        ParkingID = parkingID;
    }

    public String getSystemID() {
        return SystemID;
    }

    public void setSystemID(String systemID) {
        SystemID = systemID;
    }

    public String getVillageID() {
        return VillageID;
    }

    public void setVillageID(String villageID) {
        VillageID = villageID;
    }

    public String getParkingName() {
        return ParkingName;
    }

    public void setParkingName(String parkingName) {
        ParkingName = parkingName;
    }

    public String getEntranceDate() {
        return EntranceDate;
    }

    public void setEntranceDate(String entranceDate) {
        EntranceDate = entranceDate;
    }
}
