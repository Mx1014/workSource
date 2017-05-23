// @formatter:off
package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>id : 服务联盟企业id</li>
 * <li>showOrHide : 是否在app端显示服务联盟企业, 参考 {@link com.everhomes.rest.yellowPage.ShowOrHideType}</li>
 * </ul>
 *
 *  @author:dengs 2017年5月23日
 */
public class ShowOrHideServiceAllianceEnterpriseCommand {
	private Long id;
	private Byte showOrHide;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Byte getShowOrHide() {
		return showOrHide;
	}
	public void setShowOrHide(Byte showOrHide) {
		this.showOrHide = showOrHide;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
