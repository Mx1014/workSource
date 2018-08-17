package com.everhomes.rest.equipment;

import com.everhomes.discover.ItemType;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 *  <li>ownerId: 设备备件所属组织等的id</li>
 *  <li>ownerType: 设备备件所属组织类型，如enterprise</li>
 *  <li>targetId: 设备备件所属管理处id</li>
 *  <li>targetType: 设备备件所属管理处类型</li>
 *  <li>targetIdString: 项目列表id   get方式</li>
 *  <li>keyword: 查询关键字</li>
 *  <li>pageAnchor: 锚点</li>
 *  <li>pageSize: 页面大小</li>
 *  <li>namespaceId: namespaceId</li>
 * </ul>
 */
public class SearchEquipmentAccessoriesCommand {
	@NotNull
	private Long ownerId;
	
	@NotNull
	private String ownerType;

	private Long targetId;
	@ItemType(Long.class)
	private List<Long> targetIds;
	
	private String targetType;

	private String targetIdString;
	
	private String keyword;
	
	private Long pageAnchor;
	
	private Integer pageSize;

	private Integer namespaceId;

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public List<Long> getTargetIds() {
		return targetIds;
	}

	public void setTargetIds(List<Long> targetIds) {
		this.targetIds = targetIds;
	}

	public String getTargetIdString() {
		return targetIdString;
	}

	public void setTargetIdString(String targetIdString) {
		this.targetIdString = targetIdString;
	}
}
