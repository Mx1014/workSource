package com.everhomes.rest.asset.zhuzong;

import java.math.BigDecimal;

public class CostDetailDTO {
	
	private String houseName;
	private String feename;
	private BigDecimal amount;
	private BigDecimal ws_amount;
	private BigDecimal ss_amount;
	private String beginTime;
	private String endTime;
	private String feeid;
	
	public String getHouseName() {
		return houseName;
	}
	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}
	public String getFeename() {
		return feename;
	}
	public void setFeename(String feename) {
		this.feename = feename;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getWs_amount() {
		return ws_amount;
	}
	public void setWs_amount(BigDecimal ws_amount) {
		this.ws_amount = ws_amount;
	}
	public BigDecimal getSs_amount() {
		return ss_amount;
	}
	public void setSs_amount(BigDecimal ss_amount) {
		this.ss_amount = ss_amount;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getFeeid() {
		return feeid;
	}
	public void setFeeid(String feeid) {
		this.feeid = feeid;
	}

}
