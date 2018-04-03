// @formatter:off
package com.everhomes.rest.talent;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>organizationId: 管理公司id</li>
 * <li>talentCategories: 分类列表，参考{@link com.everhomes.rest.talent.TalentCategoryDTO}</li>
 * </ul>
 */
public class CreateOrUpdateTalentCategoryCommand {
	@NotNull
	private Long organizationId;
	
	@ItemType(TalentCategoryDTO.class)
	private List<TalentCategoryDTO> talentCategories;

	public CreateOrUpdateTalentCategoryCommand() {

	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public List<TalentCategoryDTO> getTalentCategories() {
		return talentCategories;
	}

	public void setTalentCategories(List<TalentCategoryDTO> talentCategories) {
		this.talentCategories = talentCategories;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
