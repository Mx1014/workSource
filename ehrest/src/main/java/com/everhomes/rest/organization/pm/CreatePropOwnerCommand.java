// @formatter:off
package com.everhomes.rest.organization.pm;



import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>organizationId: 机构id</li>
 * <li>communityId: 小区id</li>
 * <li>contactName：业主名称</li>
 * <li>contactType：业主标识类型：0-手机，1-邮箱</li>
 * <li>contactToken：业主标识</li>
 * <li>addressIds：门牌号列表</li>
 * </ul>
 */
public class CreatePropOwnerCommand {

	private Long     organizationId;
	private Long     communityId;
	private String   contactName;
	private Byte     contactType;
	private String   contactToken;
	@ItemType(Long.class)
	private List<Long> addressIds;
	
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	public Long getCommunityId() {
		return communityId;
	}
	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public Byte getContactType() {
		return contactType;
	}
	public void setContactType(Byte contactType) {
		this.contactType = contactType;
	}
	public String getContactToken() {
		return contactToken;
	}
	public void setContactToken(String contactToken) {
		this.contactToken = contactToken;
	}
	
	public List<Long> getAddressIds() {
		return addressIds;
	}
	public void setAddressIds(List<Long> addressIds) {
		this.addressIds = addressIds;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
