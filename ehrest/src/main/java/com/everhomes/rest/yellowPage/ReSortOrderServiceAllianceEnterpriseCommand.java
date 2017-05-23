// @formatter:off
package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>
 * <li>firstId : 第一个服务联盟企业id</li>
 * <li>secondId : 第二个服务联盟企业id</li>
 * </ul>
 *
 *  @author:dengs 2017年5月23日
 */
public class ReSortOrderServiceAllianceEnterpriseCommand {
	private Long firstId;
	private Long secondId;
	public Long getFirstId() {
		return firstId;
	}
	public void setFirstId(Long firstId) {
		this.firstId = firstId;
	}
	public Long getSecondId() {
		return secondId;
	}
	public void setSecondId(Long secondId) {
		this.secondId = secondId;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
