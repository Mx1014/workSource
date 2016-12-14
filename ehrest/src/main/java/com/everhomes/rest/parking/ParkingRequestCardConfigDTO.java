package com.everhomes.rest.parking;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>parkingLotId: 停车场ID</li>
 * <li>requestMonthCount: 申请月卡，智能模式 开通月卡时 所需缴费月数</li>
 * <li>requestRechargeType: 申请月卡，智能模式 开通月卡时 缴费类型， 1: 整月，2:实际天数   com.everhomes.rest.parking.ParkingLotRechargeType</li>
 * <li>cardRequestTip: app月卡申请页面提示文案</li>
 * <li>cardAgreement: 停车月卡协议</li>
 * <li>maxIssueNum: 可发放的月卡总数</li>
 * <li>flowId: 工作流模式id</li>
 * </ul>
 */
public class ParkingRequestCardConfigDTO {
	
    private String ownerType;
    private Long ownerId;
    private Long parkingLotId;
	private Integer requestMonthCount;
    private Byte requestRechargeType;
    
    private Byte cardRequestTipFlag;
	private Byte cardAgreementFlag;
	private Byte maxRequestNumFlag;
	private Byte maxIssueNumFlag;
    private String cardRequestTip;
    private String cardAgreement;
    private Integer maxRequestNum;
    private Integer maxIssueNum;
    private Long flowId;
    
    private String cardAgreementUrl;
    
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
	public Long getFlowId() {
		return flowId;
	}
	public void setFlowId(Long flowId) {
		this.flowId = flowId;
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
	public Integer getMaxRequestNum() {
		return maxRequestNum;
	}
	public void setMaxRequestNum(Integer maxRequestNum) {
		this.maxRequestNum = maxRequestNum;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public String getCardAgreementUrl() {
		return cardAgreementUrl;
	}
	public void setCardAgreementUrl(String cardAgreementUrl) {
		this.cardAgreementUrl = cardAgreementUrl;
	}
}
