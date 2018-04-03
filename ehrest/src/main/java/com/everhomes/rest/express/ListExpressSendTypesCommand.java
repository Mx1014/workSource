// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>参数，如果expressCompanyId 为空，查询所有sendTypes
 * <li>ownerType: 所属者类型，参考{@link com.everhomes.rest.express.ExpressOwnerType}</li>
 * <li>ownerId: 所属者id</li>
 * <li>expressCompanyId : 公司id</li>
 * </ul>
 *
 *  @author:dengs 2017年7月19日
 */
public class ListExpressSendTypesCommand {
	private String ownerType;
	private Long ownerId;
	private Long expressCompanyId;
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public Long getExpressCompanyId() {
		return expressCompanyId;
	}
	public void setExpressCompanyId(Long expressCompanyId) {
		this.expressCompanyId = expressCompanyId;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
