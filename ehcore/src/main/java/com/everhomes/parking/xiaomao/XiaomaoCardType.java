// @formatter:off
package com.everhomes.parking.xiaomao;

import com.everhomes.util.StringHelper;

public class XiaomaoCardType {
	
	private String standardType;//月卡类型
	private String unitMoney; //收费标准，金额 
	private String standardId; //月卡id

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
