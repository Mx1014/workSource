package com.everhomes.rest.parking;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class SetParkingRequestCardConfigCommand {
	@NotNull
    private String ownerType;
	@NotNull
    private Long ownerId;
	@NotNull
    private Long parkingLotId;
	@NotNull
	private Integer flowId;
    private Integer requestMonthCount;
    private Byte requestRechargeType;
    private String cardRequestTip;
    private String cardAgreement;
    private Integer maxIssueNum;
    private Integer maxRequestNum;
    
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
	public Integer getFlowId() {
		return flowId;
	}
	public void setFlowId(Integer flowId) {
		this.flowId = flowId;
	}
	public Integer getRequestMonthCount() {
		return requestMonthCount;
	}
	public void setRequestMonthCount(Integer requestMonthCount) {
		this.requestMonthCount = requestMonthCount;
	}
	public Byte getRequestRechargeType() {
		return requestRechargeType;
	}
	public void setRequestRechargeType(Byte requestRechargeType) {
		this.requestRechargeType = requestRechargeType;
	}
	public String getCardRequestTip() {
		return cardRequestTip;
	}
	public void setCardRequestTip(String cardRequestTip) {
		this.cardRequestTip = cardRequestTip;
	}
	public String getCardAgreement() {
		return cardAgreement;
	}
	public void setCardAgreement(String cardAgreement) {
		this.cardAgreement = cardAgreement;
	}
	public Integer getMaxIssueNum() {
		return maxIssueNum;
	}
	public void setMaxIssueNum(Integer maxIssueNum) {
		this.maxIssueNum = maxIssueNum;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public Integer getMaxRequestNum() {
		return maxRequestNum;
	}
	public void setMaxRequestNum(Integer maxRequestNum) {
		this.maxRequestNum = maxRequestNum;
	}
    
}
