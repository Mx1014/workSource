// @formatter:off
package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>communityId: 小区id</li>
 * <li>dateStr: 账单月份</li>
 * <li>address: 地址</li>
 * <li>pageOffset: 页码</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListPropBillCommand {
    private Long communityId;
    private String dateStr;
    private String address;
    private Integer pageOffset;
    private Integer pageSize;
    
    public ListPropBillCommand() {
    }

  
    public Long getCommunityId() {
		return communityId;
	}


	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}


	public String getDateStr() {
		return dateStr;
	}


	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
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
