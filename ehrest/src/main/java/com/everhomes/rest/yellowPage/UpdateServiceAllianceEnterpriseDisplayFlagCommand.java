// @formatter:off
package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>id : 服务联盟企业id</li>
 * <li>showFlag : 是否在app端显示服务联盟企业, 参考 {@link com.everhomes.rest.yellowPage.DisplayFlagType}</li>
 * </ul>
 *
 *  @author:dengs 2017年5月23日
 */
public class UpdateServiceAllianceEnterpriseDisplayFlagCommand {
	private Long id;
	private Byte showFlag;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Byte getShowFlag() {
		return showFlag;
	}
	public void setShowFlag(Byte showFlag) {
		this.showFlag = showFlag;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
