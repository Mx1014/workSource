// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>packageType : 封装类型，参考 {@link com.everhomes.rest.express.ExpressPackageType}</li>
 * <li>packageTypeName : 封装类型名称</li>
 * </ul>
 *
 *  @author:dengs 2017年7月19日
 */
public class ExpressPackageTypeDTO {
	private Byte packageType;
	private String packageTypeName;

	public Byte getPackageType() {
		return packageType;
	}

	public void setPackageType(Byte packageType) {
		this.packageType = packageType;
	}
	
	public String getPackageTypeName() {
		return packageTypeName;
	}

	public void setPackageTypeName(String packageTypeName) {
		this.packageTypeName = packageTypeName;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
