package com.everhomes.rest.uniongroup;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  
 * <li>ownerId: 所属公司id</li>
 * <li>organizationGroupId: 薪酬组/批次id</li>
 * <li>detailIds: 添加人员的detailId列表</li>
 * </ul>
 */
public class DistributionUniongroupToDetailCommand {
	private Long ownerId;
	private Long organizationGroupId;
	@ItemType(Long.class)
	private List<Long> detailIds;
	

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
    
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public Long getOrganizationGroupId() {
		return organizationGroupId;
	}
	public void setOrganizationGroupId(Long organizationGroupId) {
		this.organizationGroupId = organizationGroupId;
	}

	public List<Long> getDetailIds() {
		return detailIds;
	}

	public void setDetailIds(List<Long> detailIds) {
		this.detailIds = detailIds;
	} 
	
	
	
	
}
