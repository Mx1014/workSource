package com.everhomes.rest.parking.jieshun;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>orderNo : 订单号</li>
 * <li>carNo : 车牌号</li>
 * <li>startTime : 进场时间 yyyy-MM-dd HH:mm:ss</li>
 * <li>serviceTime : 停车时长</li>
 * <li>createTime : 计费时间</li>
 * <li>endTime : 应离场时间</li>
 * <li>serviceFee : 应缴费用</li>
 * <li>totalFee : 实际费用</li>
 * <li>validTimeLen : 订单有效支付时长秒数</li>
 * <li>freeMinute : （不一定有）免费分钟数（正常订单时为入场免费分钟数，超时订单时为超时分钟数）</li>
 * <li>surplusMinute :（不一定有） 剩余免费分钟数（正常订单时为入场后到现在还剩下的分钟数，超时订单时为上一次缴费后到现在还剩下的分钟数）</li>
 * <li>chargeType : （不一定有）收费类型 0：正常收费 1：超时收费 2：其他类型</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年9月10日
 */
public class TempFeeInfo {
	private String orderNo;
	private String carNo;
	private Timestamp startTime; 
	private Integer serviceTime;
	private Timestamp createTime; 
	private Timestamp endTime;
	private BigDecimal serviceFee;
	private BigDecimal totalFee;
	private Integer validTimeLen; 
	private Integer freeMinute; 
	private Integer surplusMinute; 
	private String chargeType; 
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getCarNo() {
		return carNo;
	}
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public BigDecimal getServiceFee() {
		return serviceFee;
	}
	public void setServiceFee(BigDecimal serviceFee) {
		this.serviceFee = serviceFee;
	}
	public BigDecimal getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}
	public Integer getSurplusMinute() {
		return surplusMinute;
	}
	public void setSurplusMinute(Integer surplusMinute) {
		this.surplusMinute = surplusMinute;
	}
	public String getChargeType() {
		return chargeType;
	}
	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public Integer getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(Integer serviceTime) {
		this.serviceTime = serviceTime;
	}

	public Integer getValidTimeLen() {
		return validTimeLen;
	}

	public void setValidTimeLen(Integer validTimeLen) {
		this.validTimeLen = validTimeLen;
	}

	public Integer getFreeMinute() {
		return freeMinute;
	}

	public void setFreeMinute(Integer freeMinute) {
		this.freeMinute = freeMinute;
	}
}
