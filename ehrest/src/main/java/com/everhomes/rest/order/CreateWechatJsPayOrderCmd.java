package com.everhomes.rest.order;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>realm: realm</li>
 *     <li>orderType: 订单类型</li>
 *     <li>onlinePayStyleNo: 支付方式 aipay-支付宝,wechat-微信</li>
 *     <li>orderNo: 订单编号</li>
 *     <li>orderAmount: 支付金额</li>
 *     <li>userId: 微信用户openId</li>
 *     <li>subject: 标题，在微信支付页面显示</li>
 *     <li>body: 其他信息</li>
 *     <li>appKey: appKey</li>
 *     <li>timestamp: 时间戳</li>
 *     <li>randomNum: 随机数</li>
 *     <li>signature: 签名</li>
 * </ul>
 */
public class CreateWechatJsPayOrderCmd {
	private String realm;
	private String orderType;
	private String onlinePayStyleNo;
	private String orderNo;
	private String orderAmount;
	private String userId;
	private String subject;
	private String body;
	private String appKey;
	private Long timestamp;
	private Integer randomNum;
	private String signature;

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOnlinePayStyleNo() {
		return onlinePayStyleNo;
	}

	public void setOnlinePayStyleNo(String onlinePayStyleNo) {
		this.onlinePayStyleNo = onlinePayStyleNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getRandomNum() {
		return randomNum;
	}

	public void setRandomNum(Integer randomNum) {
		this.randomNum = randomNum;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
