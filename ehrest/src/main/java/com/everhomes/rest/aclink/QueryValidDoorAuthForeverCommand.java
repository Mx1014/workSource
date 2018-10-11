// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.openapi.FamilyAddressDTO;
import com.everhomes.rest.openapi.OrganizationAddressDTO;
import com.everhomes.util.StringHelper;

public class QueryValidDoorAuthForeverCommand{
	private Long userId;
	
	private List<Long> orgIds;
	
	@ItemType(FamilyAddressDTO.class)
    private List<FamilyAddressDTO> familyAddresses;
	
    @ItemType(OrganizationAddressDTO.class)
    private List<OrganizationAddressDTO> organizationAddresses;
    
    private String driver;
	private Long doorId;
	private Byte rightRemote;
	private Byte rightVisitor;
	
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
	public Long getDoorId() {
		return doorId;
	}
	public void setDoorId(Long doorId) {
		this.doorId = doorId;
	}
	public Byte getRightRemote() {
		return rightRemote;
	}
	public void setRightRemote(Byte rightRemote) {
		this.rightRemote = rightRemote;
	}
	public Byte getRightVisitor() {
		return rightVisitor;
	}
	public void setRightVisitor(Byte rightVisitor) {
		this.rightVisitor = rightVisitor;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
