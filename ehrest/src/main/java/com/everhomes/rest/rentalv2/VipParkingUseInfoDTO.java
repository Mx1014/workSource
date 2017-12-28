package com.everhomes.rest.rentalv2;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>parkingLotId: 停车场id</li>
 * <li>parkingLotName: 停车场名称</li>
 * <li>plateNumber: 车牌</li>
 * <li>plateOwnerName: 车主姓名</li>
 * <li>plateOwnerPhone: 车主手机号</li>
 * <li>priceStr: 价格字符串</li>
 * <li>spaceNo: 车位编号</li>
 * <li>spaceAddress: 车位地址</li>
 * </ul>
 */
public class VipParkingUseInfoDTO {
    private Long parkingLotId;
    private String parkingLotName;
    private String plateNumber;
    private String plateOwnerName;
    private String plateOwnerPhone;
    private String priceStr;
    private String spaceNo;
    private String spaceAddress;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Long getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public String getParkingLotName() {
        return parkingLotName;
    }

    public void setParkingLotName(String parkingLotName) {
        this.parkingLotName = parkingLotName;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getPlateOwnerName() {
        return plateOwnerName;
    }

    public void setPlateOwnerName(String plateOwnerName) {
        this.plateOwnerName = plateOwnerName;
    }

    public String getPlateOwnerPhone() {
        return plateOwnerPhone;
    }

    public void setPlateOwnerPhone(String plateOwnerPhone) {
        this.plateOwnerPhone = plateOwnerPhone;
    }

    public String getPriceStr() {
        return priceStr;
    }

    public void setPriceStr(String priceStr) {
        this.priceStr = priceStr;
    }

    public String getSpaceNo() {
        return spaceNo;
    }

    public void setSpaceNo(String spaceNo) {
        this.spaceNo = spaceNo;
    }

    public String getSpaceAddress() {
        return spaceAddress;
    }

    public void setSpaceAddress(String spaceAddress) {
        this.spaceAddress = spaceAddress;
    }
}
