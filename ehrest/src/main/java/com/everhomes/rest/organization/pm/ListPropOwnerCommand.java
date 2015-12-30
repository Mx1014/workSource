// @formatter:off
package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区id</li>
 * <li>contactType: 业主类型</li>
 * <li>contactToken: 业主标示</li>
 * <li>address: 地址</li>
 * <li>pageOffset: 页码</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListPropOwnerCommand {
    private Long communityId;
    private Byte contactType;
    private String contactToken;
    private String address;
    
    private Integer pageOffset;
    private Integer pageSize;
    
    public ListPropOwnerCommand() {
    }

  
    public Long getCommunityId() {
		return communityId;
	}


	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	

	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
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

	public Integer getPageOffset() {
		return pageOffset;
	}


	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
	}


	public Integer getPageSize() {
		return pageSize;
	}


	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
