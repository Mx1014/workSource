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
 * <li>targetPath: 目标路径 格式: /目录/文件夹1/文件夹2.../文件夹n</li>
 * </ul>
 */
public class MoveFileContentCommand {

	private String ownerType;

	private Long ownerId;

	private List<Long> contentIds;

	private String targetPath;


	public MoveFileContentCommand() {

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

	public String getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}
}
