// @formatter:off
package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>parkingLotId: 停车场ID</li>
 * <li>plateNumber: 车牌号</li>
 * <li>plateOwnerName: 车主名称</li>
 * <li>plateOwnerPhone: 车主手机号</li>
 * <li>payerName: 付款人名称</li>
 * <li>payerPhone: 付款人手机号</li>
 * <li>paidType: 支付方式  com.everhomes.rest.organization.VendorType  支付宝 : 10001  微信 : 10002</li>
 * <li>rechargeStatus: 充值状态  0:无效 1：未充值 2:已充值   com.everhomes.rest.parking.ParkingRechargeOrderRechargeStatus</li>
 * <li>startDate: 开始充值时间</li>
 * <li>endDate: 结束充值时间</li>
 * <li>pageAnchor: 本页开始的锚点</li>
 * <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class SearchParkingRechargeOrdersCommand {
    private String ownerType;
    private Long ownerId;
    private Long parkingLotId;
    private String plateNumber;
    private String plateOwnerName;
    private String plateOwnerPhone;
    private String payerName;
    private String payerPhone;
    private String paidType;
    private Byte rechargeStatus;
    private Long startDate;
    private Long endDate;
    private Long pageAnchor;
    private Integer pageSize;
    
    public SearchParkingRechargeOrdersCommand() {
    }

	public Long getStartDate() {
		return startDate;
	}

	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
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

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayerPhone() {
        return payerPhone;
    }

    public void setPayerPhone(String payerPhone) {
        this.payerPhone = payerPhone;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Byte getRechargeStatus() {
		return rechargeStatus;
	}

	public void setRechargeStatus(Byte rechargeStatus) {
		this.rechargeStatus = rechargeStatus;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getPaidType() {
		return paidType;
	}

	public void setPaidType(String paidType) {
		this.paidType = paidType;
	}

}
