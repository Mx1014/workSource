package com.everhomes.rest.parking;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>ownerType: 归属的类型，{@link com.everhomes.rest.parking.ParkingOwnerType}</li>
 * <li>ownerId: 归属的ID，如小区ID</li>
 * <li>parkingLotId: 停车场ID</li>
 * <li>plateNumber: 车牌号</li>
 * <li>payerEnterpriseId: 付款人所在公司ID</li>
 * <li>orderToken: 临时费用订单token，来自第三方</li>
 * <li>price: 金额</li>
 * <li>clientAppName: clientAppName</li>
 * <li>paymentType: 支付方式,这里传 9， WECHAT_APPPAY(1): 微信APP支付  WECHAT_SCAN_PAY(7): 微信扫码支付(正扫) ALI_SCAN_PAY(8): 支付宝扫码支付(正扫) WECHAT_JS_PAY(9): 微信JS 支付（公众号） ALI_JS_PAY(10): 支付宝JS 支付（生活号）WECHAT_JS_ORG_PAY(21): 微信公众帐号支付</li>
 * </ul>
 */
public class CreateParkingTempOrderCommand {
	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;
	@NotNull
	private Long parkingLotId;
	@NotNull
	private String plateNumber;
	@NotNull
	private Long payerEnterpriseId;
	@NotNull
	private String orderToken;
	@NotNull
	private BigDecimal price;

	private String clientAppName;
	private Integer paymentType;
	public String getClientAppName() {
		return clientAppName;
	}

	public void setClientAppName(String clientAppName) {
		this.clientAppName = clientAppName;
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

	public Long getPayerEnterpriseId() {
		return payerEnterpriseId;
	}

	public void setPayerEnterpriseId(Long payerEnterpriseId) {
		this.payerEnterpriseId = payerEnterpriseId;
	}

	public String getOrderToken() {
		return orderToken;
	}

	public void setOrderToken(String orderToken) {
		this.orderToken = orderToken;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
