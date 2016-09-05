package com.everhomes.rest.statistics.transaction;

import java.util.List;

import com.everhomes.discover.ItemType;

/**
 *<ul>
 *<li>orderNo:订单ID</li>
 *<li>payerUid:用户编号</li>
 *<li>status:状态 orderType=transaction 1-待支付,2-待发货,3-已发货,6-已完成,7-已关闭   orderType=refund 1-未申请退款,2-待处理,3-已拒绝,4-退款中,5-退款成功,6-已关闭</li>
 *<li>paidAmount: 支付金额</li>
 *<li>paidChannel: 支付渠道</li>
 *<li>userPhone: 电话号码</li>
 *<li>paidTime: 支付时间</li>
 *<li>orderType: 订单类型 交易transaction  退款refund</li>
 *<li>wares: 商品列表  {@link com.everhomes.rest.statistics.transaction.StatWareDTO}</li>
 *</ul>
 */
public class StatShopTransactionDTO {
	
	private String orderNo;
	
	private Long payerUid;
	
	private Byte status;
	
	private Double paidAmount;
	
	private Byte paidChannel;
	
	private String paidChannelName;
	
	private String userPhone;
	
	private Long paidTime;
	
	private String orderType;
	
	private String userName;
	
	private String statusName;
	
	
	@ItemType(StatWareDTO.class)
	private List<StatWareDTO> wares;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Long getPayerUid() {
		return payerUid;
	}

	public void setPayerUid(Long payerUid) {
		this.payerUid = payerUid;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public Byte getPaidChannel() {
		return paidChannel;
	}

	public void setPaidChannel(Byte paidChannel) {
		this.paidChannel = paidChannel;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public Long getPaidTime() {
		return paidTime;
	}

	public void setPaidTime(Long paidTime) {
		this.paidTime = paidTime;
	}

	public List<StatWareDTO> getWares() {
		return wares;
	}

	public void setWares(List<StatWareDTO> wares) {
		this.wares = wares;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getPaidChannelName() {
		return paidChannelName;
	}

	public void setPaidChannelName(String paidChannelName) {
		this.paidChannelName = paidChannelName;
	}
 
	
}
