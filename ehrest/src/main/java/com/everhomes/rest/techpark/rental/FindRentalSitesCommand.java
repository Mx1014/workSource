package com.everhomes.rest.techpark.rental;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * <ul>
 * 查询场所
 * <li>ownerType：所有者类型：“community”</li>
 * <li>ownerId：所有者id</li>
 * <li>resourceTypeId: 广场图标id</li>
 * <li>keyword: 关键字</li>
 * <li>anchor: 锚点</li>
 * <li>pageSize: 每页个数</li>
 * <li>status: 状态码，List<Byte></li>
 * </ul>
 */
public class FindRentalSitesCommand {
	private String ownerType;
	private Long ownerId; 
	private Long resourceTypeId;
	private String keyword;
	private Long anchor;
	private Integer pageSize;
	@ItemType(Byte.class)
	private List<Byte> status;
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    } 
	  
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	} 
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public List<Byte> getStatus() {
		return status;
	}
	public void setStatus(List<Byte> status) {
		this.status = status;
	}
	public Long getResourceTypeId() {
		return resourceTypeId;
	}
	public void setResourceTypeId(Long resourceTypeId) {
		this.resourceTypeId = resourceTypeId;
	}

	public Long getAnchor() {
		return anchor;
	}

	public void setAnchor(Long anchor) {
		this.anchor = anchor;
	} 
}
