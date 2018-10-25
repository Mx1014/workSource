package com.everhomes.rest.general.order;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>callBackType : 回调类型 0-企业支付成功 1-发票 {@link com.everhomes.rest.general.order.OrderCallBackType}</li>
 * <li>businessType : 业务类型标识 如OrderType.PRINT_ORDER_CODE 填{@link com.everhomes.rest.order.OrderType}</li>
 * <li>callBackInfo : 回调信息{@link com.everhomes.rest.general.order.OrderCallBackInfo}</li>
 * <li>signature : signature</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年10月6日
 */
public class OrderCallBackCommand {
	private Byte callBackType; 
	private String businessType;
	private OrderCallBackInfo callBackInfo;
	private String signature;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public Byte getCallBackType() {
		return callBackType;
	}

	public void setCallBackType(Byte callBackType) {
		this.callBackType = callBackType;
	}

	public OrderCallBackInfo getCallBackInfo() {
		return callBackInfo;
	}

	public void setCallBackInfo(OrderCallBackInfo callBackInfo) {
		this.callBackInfo = callBackInfo;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

}
