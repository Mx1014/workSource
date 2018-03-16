// @formatter:off
package com.everhomes.parking.kexinxiaomao;

import com.everhomes.util.StringHelper;

public class KexinXiaomaoCardType {
	private String standardType;
	private String unitMoney;
	private String standardId;
	public String getStandardType() {
		return standardType;
	}
	public void setStandardType(String standardType) {
		this.standardType = standardType;
	}
	public String getUnitMoney() {
		return unitMoney;
	}
	public void setUnitMoney(String unitMoney) {
		this.unitMoney = unitMoney;
	}
	public String getStandardId() {
		return standardId;
	}
	public void setStandardId(String standardId) {
		this.standardId = standardId;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
