package com.everhomes.rest.socialSecurity;

import java.math.BigDecimal;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>社保/公积金 详细信息:
 * <li>cityName: 城市名</li>
 * <li>radix: 基数</li>
 * <li>householdType: 户籍类型</li>
 * <li>items: 缴费项 参考{@link com.everhomes.rest.socialSecurity.SocialSecurityItemDTO}</li> 
 * </ul>
 */
public class SocialSecurityPaymentDetailDTO { 
	private Long cityId;
	private String cityName;
	private BigDecimal radix;
	private Byte afterPayFlag;
	private String householdType;
	@ItemType(SocialSecurityItemDTO.class)
	private List<SocialSecurityItemDTO> items;
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public BigDecimal getRadix() {
		return radix;
	}
	public void setRadix(BigDecimal radix) {
		this.radix = radix;
	}
	public Byte getAfterPayFlag() {
		return afterPayFlag;
	}
	public void setAfterPayFlag(Byte afterPayFlag) {
		this.afterPayFlag = afterPayFlag;
	}
	public String getHouseholdType() {
		return householdType;
	}
	public void setHouseholdType(String householdType) {
		this.householdType = householdType;
	}
	public List<SocialSecurityItemDTO> getItems() {
		return items;
	}
	public void setItems(List<SocialSecurityItemDTO> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}	
	
}
