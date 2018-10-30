//@formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.family.FamilyDTO;
import com.everhomes.rest.openapi.FamilyAddressDTO;
import com.everhomes.rest.openapi.OrganizationAddressDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userId:用户id</li>
 * <li>orgIds:组织架构节点id列表</li>
 * <li>familyAddresses:家庭地址列表</li>
 * <li>organizationAddresses:公司地址列表</li>
 * <li>driver:门禁驱动类型,null则过滤掉园区班车</li>
 * </ul>
 */
public class ListAuthsByLevelandLocationCommand {
	private Long userId;
	
	private List<Long> orgIds;
	
	@ItemType(FamilyAddressDTO.class)
    private List<FamilyAddressDTO> familyAddresses;
	
    @ItemType(OrganizationAddressDTO.class)
    private List<OrganizationAddressDTO> organizationAddresses;
    
    private String driver;
    
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<Long> getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(List<Long> orgIds) {
		this.orgIds = orgIds;
	}

	public List<FamilyAddressDTO> getFamilyAddresses() {
		return familyAddresses;
	}

	public void setFamilyAddresses(List<FamilyAddressDTO> familyAddresses) {
		this.familyAddresses = familyAddresses;
	}

	public List<OrganizationAddressDTO> getOrganizationAddresses() {
		return organizationAddresses;
	}

	public void setOrganizationAddresses(List<OrganizationAddressDTO> organizationAddresses) {
		this.organizationAddresses = organizationAddresses;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
