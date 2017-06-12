// @formatter:off
package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>id : 服务联盟企业id</li>
 * <li>displayFlag : 是否在app端显示服务联盟企业, 参考 {@link com.everhomes.rest.yellowPage.DisplayFlagType}</li>
 * </ul>
 *
 *  @author:dengs 2017年5月23日
 */
public class UpdateServiceAllianceEnterpriseDisplayFlagCommand {
	private Long id;
	private Byte displayFlag;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
