package com.everhomes.rest.enterprisemoment;

import java.util.List;

/**
 * <ul>
 * <li>organizationId: 公司id</li>
 * <li>appId: 应用id</li>
 * <li>addTags: 添加的标签(id不需要传值) 参考{@link com.everhomes.rest.enterprisemoment.MomentTagDTO}</li>
 * <li>deleteTags: 删除的标签(name不需要传值) 参考{@link com.everhomes.rest.enterprisemoment.MomentTagDTO}</li>
 * <li>updateTags: 更新的标签 参考{@link com.everhomes.rest.enterprisemoment.MomentTagDTO}</li>
 * </ul>
 */
public class EditTagsCommand {
	private Long appId;
	private Long organizationId;
	private List<MomentTagDTO> addTags;
	private List<MomentTagDTO> deleteTags;
	private List<MomentTagDTO> updateTags;


	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public List<MomentTagDTO> getAddTags() {
		return addTags;
	}

	public void setAddTags(List<MomentTagDTO> addTags) {
		this.addTags = addTags;
	}

	public List<MomentTagDTO> getDeleteTags() {
		return deleteTags;
	}

	public void setDeleteTags(List<MomentTagDTO> deleteTags) {
		this.deleteTags = deleteTags;
	}

	public List<MomentTagDTO> getUpdateTags() {
		return updateTags;
	}

	public void setUpdateTags(List<MomentTagDTO> updateTags) {
		this.updateTags = updateTags;
	}
}
