package com.everhomes.rest.officecubicle;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *空间订单
 *<li> spaceId: 空间 id	</li> 
 * <li>rentCount: 预定工位数量</li>  
 * <li>reserverName: 预订人姓名 </li> 
 * <li>reserveContactToken:  预订人联系方式</li> 
 * <li>reserveEnterpriseId: 预订人公司Id</li> 
 *  <li>reserveEnterpriseName: 预订人公司名称</li> 
 * <li>price</li>
 * <li>rentalOrderNo:资源预约订单号</li>
 * <li>remark：备注</li>
 * <li>ownerId:项目id</li>
 * <li>ownerType：community</li>
 * <li>paymentType: 支付方式, WECHAT_APPPAY(1): 微信APP支付  WECHAT_SCAN_PAY(7): 微信扫码支付(正扫) ALI_SCAN_PAY(8): 支付宝扫码支付(正扫) WECHAT_JS_PAY(9): 微信JS 支付（公众号） ALI_JS_PAY(10): 支付宝JS 支付（生活号）WECHAT_JS_ORG_PAY(21): 微信公众帐号支付</li>
 * </ul>
 */
public class CreateOfficeCubicleOrderCommand {

	private Long spaceId;
    
    private Integer rentCount;
    private String reserverName;
    private String reserveEnterprise;
	private String reserveContactToken;
	private String remark;
	private Long rentalOrderNo;
	private Byte stationType;
	private String ownerType;
	private Long ownerId;
	private Long stationId;
	private Byte paymentType;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	  


	public Byte getPaymentType() {
		return paymentType;
	}



	public void setPaymentType(Byte paymentType) {
		this.paymentType = paymentType;
	}



	public Long getSpaceId() {
		return spaceId;
	}


	public void setSpaceId(Long spaceId) {
		this.spaceId = spaceId;
	}


	public String getReserverName() {
		return reserverName;
	}


	public void setReserverName(String reserverName) {
		this.reserverName = reserverName;
	}


	public String getReserveEnterprise() {
		return reserveEnterprise;
	}


	public void setReserveEnterprise(String reserveEnterprise) {
		this.reserveEnterprise = reserveEnterprise;
	}


	public String getReserveContactToken() {
		return reserveContactToken;
	}


	public void setReserveContactToken(String reserveContactToken) {
		this.reserveContactToken = reserveContactToken;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getRentalOrderNo() {
		return rentalOrderNo;
	}

	public void setRentalOrderNo(Long rentalOrderNo) {
		this.rentalOrderNo = rentalOrderNo;
	}



	public Integer getRentCount() {
		return rentCount;
	}



	public void setRentCount(Integer rentCount) {
		this.rentCount = rentCount;
	}



	public Byte getStationType() {
		return stationType;
	}



	public void setStationType(Byte stationType) {
		this.stationType = stationType;
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



	public Long getStationId() {
		return stationId;
	}



	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	
}
