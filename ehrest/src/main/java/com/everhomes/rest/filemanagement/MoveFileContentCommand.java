// @formatter:off
package com.everhomes.rest.filemanagement;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属类型,填organization</li>
 * <li>ownerId: 公司id</li>
 * <li>contentIds: id列表 要移动的文件/文件夹id</li>
 * <li>targetCatalogId: 目标目录id</li>
 * <li>targetParentId: 目标文件夹id 直接放在目录下则为空</li>
 * </ul>
 */
public class MoveFileContentCommand {

	private String ownerType;

	private Long ownerId;

	private List<Long> contentIds;

	private Long targetCatalogId;

	private Long targetParentId;

	public MoveFileContentCommand() {

	}

	public MoveFileContentCommand(String ownerType, Long ownerId, List<Long> contentIds, Long targetCatalogId, Long targetParentId) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.setContentIds(contentIds);
		this.targetCatalogId = targetCatalogId;
		this.targetParentId = targetParentId;
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
 

	public Long getTargetCatalogId() {
		return targetCatalogId;
	}

	public void setTargetCatalogId(Long targetCatalogId) {
		this.targetCatalogId = targetCatalogId;
	}

	public Long getTargetParentId() {
		return targetParentId;
	}

	public void setTargetParentId(Long targetParentId) {
		this.targetParentId = targetParentId;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
 

	public List<Long> getContentIds() {
		return contentIds;
	}

	public void setContentIds(List<Long> contentIds) {
		this.contentIds = contentIds;
	}

}
