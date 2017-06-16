// @formatter:off
package com.everhomes.rest.talent;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>organizationId: 管理公司id</li>
 * <li>ids: ids</li>
 * </ul>
 */
public class DeleteTalentCategoryCommand {
	@NotNull
	private Long organizationId;
	@ItemType(Long.class)
	private List<Long> ids;

	public DeleteTalentCategoryCommand() {

	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public List<Long> getIds() {
		return ids;
	}

	public void setIds(List<Long> ids) {
		this.ids = ids;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
