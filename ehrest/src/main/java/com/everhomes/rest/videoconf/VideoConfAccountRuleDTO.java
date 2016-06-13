package com.everhomes.rest.videoconf;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 主键id</li>
 *  <li>multipleAccountThreshold: 多账号起售数量  </li>
 *  <li>confCapacity: 会议容量</li>
 *  <li>confType: 开会方式</li>
 *  <li>minPeriod: 起售月份</li>
 *  <li>singleAccountPrice: 普通价格</li>
 *  <li>multipleAccountPrice: 多账号购买价格</li>
 *  <li>displayFlag: 线上是否可购买</li>
 * </ul>
 *
 */
public class VideoConfAccountRuleDTO {
	
	private Long id;
	
	private int multipleAccountThreshold;
	
	private String confCapacity;
	
	private String confType;
	
	private Integer minPeriod;
	
	private BigDecimal singleAccountPrice;
	
	private BigDecimal multipleAccountPrice;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public int getMultipleAccountThreshold() {
		return multipleAccountThreshold;
	}


	public void setMultipleAccountThreshold(int multipleAccountThreshold) {
		this.multipleAccountThreshold = multipleAccountThreshold;
	}


	public String getConfCapacity() {
		return confCapacity;
	}


	public void setConfCapacity(String confCapacity) {
		this.confCapacity = confCapacity;
	}


	public String getConfType() {
		return confType;
	}


	public void setConfType(String confType) {
		this.confType = confType;
	}


	public Integer getMinPeriod() {
		return minPeriod;
	}


	public void setMinPeriod(Integer minPeriod) {
		this.minPeriod = minPeriod;
	}


	public BigDecimal getSingleAccountPrice() {
		return singleAccountPrice;
	}


	public void setSingleAccountPrice(BigDecimal singleAccountPrice) {
		this.singleAccountPrice = singleAccountPrice;
	}


	public BigDecimal getMultipleAccountPrice() {
		return multipleAccountPrice;
	}


	public void setMultipleAccountPrice(BigDecimal multipleAccountPrice) {
		this.multipleAccountPrice = multipleAccountPrice;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
