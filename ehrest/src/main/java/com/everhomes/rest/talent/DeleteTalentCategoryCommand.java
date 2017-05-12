// @formatter:off
package com.everhomes.rest.talent;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>organizationId: 管理公司id</li>
 * <li>id: id</li>
 * </ul>
 */
public class DeleteTalentCategoryCommand {
	@NotNull
	private Long organizationId;
	@NotNull
	private Long id;

	public DeleteTalentCategoryCommand() {

	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
