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
	private Long flowId;
    private Integer requestMonthCount;
    private Byte requestRechargeType;
    
    private String cardRequestTip;
    private String cardAgreement;
    private Integer maxIssueNum;
    private Integer maxRequestNum;
    
    private Byte cardRequestTipFlag;
	private Byte cardAgreementFlag;
	private Byte maxRequestNumFlag;
	private Byte maxIssueNumFlag;

	private Byte cardTypeTipFlag;
	private String cardTypeTip;

	public Byte getCardTypeTipFlag() {
		return cardTypeTipFlag;
	}

	public void setCardTypeTipFlag(Byte cardTypeTipFlag) {
		this.cardTypeTipFlag = cardTypeTipFlag;
	}

	public String getCardTypeTip() {
		return cardTypeTip;
	}

	public void setCardTypeTip(String cardTypeTip) {
		this.cardTypeTip = cardTypeTip;
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
	public Long getFlowId() {
		return flowId;
	}
	public void setFlowId(Long flowId) {
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
	
	public Byte getCardRequestTipFlag() {
		return cardRequestTipFlag;
	}
	public void setCardRequestTipFlag(Byte cardRequestTipFlag) {
		this.cardRequestTipFlag = cardRequestTipFlag;
	}
	public Byte getCardAgreementFlag() {
		return cardAgreementFlag;
	}
	public void setCardAgreementFlag(Byte cardAgreementFlag) {
		this.cardAgreementFlag = cardAgreementFlag;
	}
	public Byte getMaxRequestNumFlag() {
		return maxRequestNumFlag;
	}
	public void setMaxRequestNumFlag(Byte maxRequestNumFlag) {
		this.maxRequestNumFlag = maxRequestNumFlag;
	}
	public Byte getMaxIssueNumFlag() {
		return maxIssueNumFlag;
	}
	public void setMaxIssueNumFlag(Byte maxIssueNumFlag) {
		this.maxIssueNumFlag = maxIssueNumFlag;
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
