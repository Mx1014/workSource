// @formatter:off
package com.everhomes.rest.yellowPage;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>
 * serviceIds : 排好序的id列表
 * </ul>
 *
 *  @author:dengs 2017年5月23日
 */
public class UpdateServiceAllianceEnterpriseDefaultOrderCommand extends AllianceAdminCommand{
	
	private List<Long> serviceIds;

	public List<Long> getServiceIds() {
		return serviceIds;
	}

	public void setServiceIds(List<Long> serviceIds) {
		this.serviceIds = serviceIds;
	}
	
}
