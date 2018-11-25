package com.everhomes.rest.parking.handler.haikangweishi;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>preBillUuid: 账单id</li>
 * <li>plateNo: 车牌号</li>
 * <li>enterTime:进场时间 格式：yyyy-MM-dd HH:mm:ss</li>
 * <li>parkingTime: 停车时长(分钟)</li>
 * <li>type:  0-首次收费 1-超时收费</li>
 * <li>cost: 应付金额（元）</li>
 * <li>totalCost: 总收费金额（元）</li>
 * <li>paidCost: 已支付金额（元）</li>
 * <li>reocrdUuid: 过车记录UUID</li>
 * <li>delayTime: 缴费后允许延时出场时间(分钟)</li>
 * <li>cardNo: 卡号</li>
 * <li>vehicleType: 车辆类型 </li>
 * </ul>
 */
public class TempFeeInfo {
	private String preBillUuid; 
	private String plateNo; 
	private String parkUuid;
	private String parkName;
	private String enterTime; 
	private Integer parkingTime; 
	private Integer type; 
	private String cost; 
	private String totalCost; 
	private String paidCost; 
	private String reocrdUuid; 
	private Integer delayTime;
	private String cardNo; 
	private Integer vehicleType; 
	
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public String getPreBillUuid() {
		return preBillUuid;
	}

	public void setPreBillUuid(String preBillUuid) {
		this.preBillUuid = preBillUuid;
	}

	public String getPlateNo() {
		return plateNo;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

	public String getParkUuid() {
		return parkUuid;
	}

	public void setParkUuid(String parkUuid) {
		this.parkUuid = parkUuid;
	}

	public String getParkName() {
		return parkName;
	}

	public void setParkName(String parkName) {
		this.parkName = parkName;
	}

	public String getEnterTime() {
		return enterTime;
	}

	public void setEnterTime(String enterTime) {
		this.enterTime = enterTime;
	}

	public Integer getParkingTime() {
		return parkingTime;
	}

	public void setParkingTime(Integer parkingTime) {
		this.parkingTime = parkingTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}

	public String getPaidCost() {
		return paidCost;
	}

	public void setPaidCost(String paidCost) {
		this.paidCost = paidCost;
	}

	public String getReocrdUuid() {
		return reocrdUuid;
	}

	public void setReocrdUuid(String reocrdUuid) {
		this.reocrdUuid = reocrdUuid;
	}

	public Integer getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(Integer delayTime) {
		this.delayTime = delayTime;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Integer getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(Integer vehicleType) {
		this.vehicleType = vehicleType;
	}
    
}
