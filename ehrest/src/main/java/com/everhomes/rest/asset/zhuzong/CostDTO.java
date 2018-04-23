package com.everhomes.rest.asset.zhuzong;

import java.math.BigDecimal;

public class CostDTO {
	
	private String housename;
	private String feename;
	private BigDecimal amount;
	private BigDecimal ws_amount;
	private BigDecimal ss_amount;
	private String begintime;
	private String endtime;
	private String feeid;
	
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
	public String getBegintime() {
		return begintime;
	}
	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getFeeid() {
		return feeid;
	}
	public void setFeeid(String feeid) {
		this.feeid = feeid;
	}
	public String getHousename() {
		return housename;
	}
	public void setHousename(String housename) {
		this.housename = housename;
	}
	
}
