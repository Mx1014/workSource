package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * Created by jm.ding on 2018/9/27.
 */
public class ExcelPropertyInfo {

	private String[] propertyNames;
	private String[] titleName;
	private int[] titleSize;

	public String[] getPropertyNames() {
		return propertyNames;
	}

	public void setPropertyNames(String[] propertyNames) {
		this.propertyNames = propertyNames;
	}

	public String[] getTitleName() {
		return titleName;
	}

	public void setTitleName(String[] titleName) {
		this.titleName = titleName;
	}

	public int[] getTitleSize() {
		return titleSize;
	}

	public void setTitleSize(int[] titleSize) {
		this.titleSize = titleSize;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
