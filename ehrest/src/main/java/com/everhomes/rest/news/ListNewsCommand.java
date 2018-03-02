// @formatter:off

package com.everhomes.rest.news;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 参数
 * <li>ownerType: 所属类型，参考{@link com.everhomes.rest.news.NewsOwnerType}</li>
 * <li>ownerId: 所属ID</li>
 * <li>categoryId: 新闻类型ID</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页的数量</li>
 * <li>keyword: 搜索关键字</li>
 * <li>tagIds: 需要筛选的标签id</li>
 * <li>status: 状态，参考 {@link NewsStatus}</li>
 * <li>currentPMId: 当前管理公司ID</li>
 * <li>currentProjectId: 当前选中项目Id，如果是全部则不传</li>
 * </ul>
 */
public class ListNewsCommand {
	@NotNull
	private String ownerType;
	@NotNull
	private Long ownerId;
	private Long categoryId;
	private Long pageAnchor;
	private Integer pageSize;
	private String keyword;
	@ItemType(Long.class)
	private List<Long> tagIds;	
	private Byte status;
	private Byte checkPrivilegeFlag = TrueOrFalseFlag.FALSE.getCode();
	private Long currentPMId;
	private Long currentProjectId;

	public Long getCurrentPMId() {
		return currentPMId;
	}

	public void setCurrentPMId(Long currentPMId) {
		this.currentPMId = currentPMId;
	}

	public Long getCurrentProjectId() {
		return currentProjectId;
	}

	public void setCurrentProjectId(Long currentProjectId) {
		this.currentProjectId = currentProjectId;
	}


	public Byte getCheckPrivilegeFlag() {
		return checkPrivilegeFlag;
	}

	public void setCheckPrivilegeFlag(Byte checkPrivilegeFlag) {
		this.checkPrivilegeFlag = checkPrivilegeFlag;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
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

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List<Long> getTagIds() {
		return tagIds;
	}

	public void setTagIds(List<Long> tagIds) {
		this.tagIds = tagIds;
	}
 
}