package com.everhomes.rest.order;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
*  <li>	appKey	:	应用Key	</li>
*  <li>	signature	:	对所有参数进行SHA1加密后的签名	</li>
*  <li>	timestamp	:	时间戳	</li>
*  <li>	nonce	:	随机数	</li>
*  <li>	crypto	:	参数value加密算法名，有值时表示对指定的参数进行加密；无值则表示参数value不加密；	</li>
*  <li>	refundOrderNo	:	退款单号	</li>
*  <li>	onlinePayStyleNo	:	支付方式,alipay-支付宝,wechat-微信	</li>
*  <li>	orderNo	:	订单号	</li>
*  <li>	orderType	:	订单类型,wuye,dianshagn等	</li>
*  <li>	refundAmount	:	退款金额	</li>
*</ul>
*
*/

public class RefundCallbackCommand {
	private	String	appKey	;
	private	String	signature	;
	private	Long	timestamp	;
	private	Integer	nonce	;
	private	String	crypto	;
	private	String	refundOrderNo	;
	private	String	onlinePayStyleNo	;
	private	String	orderNo	;
	private	String	orderType	;
	private	BigDecimal	refundAmount	;

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Integer getNonce() {
		return nonce;
	}

	public void setNonce(Integer nonce) {
		this.nonce = nonce;
	}

	public String getCrypto() {
		return crypto;
	}

	public void setCrypto(String crypto) {
		this.crypto = crypto;
	}

	public String getRefundOrderNo() {
		return refundOrderNo;
	}

	public void setRefundOrderNo(String refundOrderNo) {
		this.refundOrderNo = refundOrderNo;
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

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
