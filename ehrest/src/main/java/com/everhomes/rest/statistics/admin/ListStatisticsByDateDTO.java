package com.everhomes.rest.statistics.admin;

import java.sql.Timestamp;


/**
 * 
 * @author Administrator
 * 
 * <ul>
 * <li>createTime:查询时间段内的每一天</li>
 * <li>registerConut:当天注册量</li>
 * <li>activeCount:当天激活量</li>
 * <li>regRatio:注册率</li>
 * <li>addressCount:地址量</li>
 * <li>totalRegisterCount:总注册量</li>
 * <li>addrRatio:地址率</li>
 * </ul>
 */
public class ListStatisticsByDateDTO {
	
	private Timestamp createTime;
	
	private Integer registerConut;
	
	private Integer activeCount;
	
	private double regRatio;
	
	private Integer addressCount;
	
	private Integer totalRegisterCount;
	
	private double addrRatio;

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getRegisterConut() {
		return registerConut;
	}

	public void setRegisterConut(Integer registerConut) {
		this.registerConut = registerConut;
	}

	public Integer getActiveCount() {
		return activeCount;
	}

	public void setActiveCount(Integer activeCount) {
		this.activeCount = activeCount;
	}

	public double getRegRatio() {
		return regRatio;
	}

	public void setRegRatio(double regRatio) {
		this.regRatio = regRatio;
	}

	public Integer getAddressCount() {
		return addressCount;
	}

	public void setAddressCount(Integer addressCount) {
		this.addressCount = addressCount;
	}

	public Integer getTotalRegisterCount() {
		return totalRegisterCount;
	}

	public void setTotalRegisterCount(Integer totalRegisterCount) {
		this.totalRegisterCount = totalRegisterCount;
	}

	public double getAddrRatio() {
		return addrRatio;
	}

	public void setAddrRatio(double addrRatio) {
		this.addrRatio = addrRatio;
	}
	
	

}
