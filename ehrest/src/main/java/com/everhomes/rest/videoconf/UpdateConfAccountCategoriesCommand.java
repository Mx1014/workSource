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
public class UpdateConfAccountCategoriesCommand {
	
	private Long id;
	
	@NotNull
	private int multipleAccountThreshold;
	@NotNull
	private String confCapacity;
	@NotNull
	private String confType;
	@NotNull
	private Integer minPeriod;
	@NotNull
	private BigDecimal singleAccountPrice;
	@NotNull
	private BigDecimal multipleAccountPrice;
	
	private Byte displayFlag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public int getMultipleAccountThreshold() {
		return multipleAccountThreshold;
	}

	public void setMultipleAccountThreshold(int multipleAccountThreshold) {
		this.multipleAccountThreshold = multipleAccountThreshold;
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

	public Byte getDisplayFlag() {
		return displayFlag;
	}

	public void setDisplayFlag(Byte displayFlag) {
		this.displayFlag = displayFlag;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
