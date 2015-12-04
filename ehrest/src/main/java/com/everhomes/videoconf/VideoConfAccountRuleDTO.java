package com.everhomes.videoconf;

import java.math.BigDecimal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 主键id</li>
 *  <li>accountType: 账号类型  </li>
 *  <li>confCapacity: 会议容量</li>
 *  <li>confType: 开会方式</li>
 *  <li>minimumMonths: 起售月份</li>
 *  <li>packagePrice: 价格</li>
 * </ul>
 *
 */
public class VideoConfAccountRuleDTO {
	
	private Long id;
	
	private String accountType;
	
	private String confCapacity;
	
	private String confType;

	private Integer minimumMonths;
	
	private BigDecimal packagePrice;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
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

	public Integer getMinimumMonths() {
		return minimumMonths;
	}

	public void setMinimumMonths(Integer minimumMonths) {
		this.minimumMonths = minimumMonths;
	}

	public BigDecimal getPackagePrice() {
		return packagePrice;
	}

	public void setPackagePrice(BigDecimal packagePrice) {
		this.packagePrice = packagePrice;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
