// @formatter:off
package com.everhomes.rest.talent;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>ownerType: 所属类型，参考{@link com.everhomes.rest.talent.TalentOwnerType}</li>
 * <li>ownerId: 所属id</li>
 * <li>organizationId: 管理公司id</li>
 * <li>talentCategories: 分类列表，参考{@link com.everhomes.rest.talent.TalentCategoryDTO}</li>
 * </ul>
 */
public class CreateOrUpdateTalentCateogryCommand {

	private String ownerType;

	private Long ownerId;

	private Long organizationId;

	@ItemType(TalentCategoryDTO.class)
	private List<TalentCategoryDTO> talentCategories;

	public CreateOrUpdateTalentCateogryCommand() {

	}

	public CreateOrUpdateTalentCateogryCommand(String ownerType, Long ownerId, Long organizationId, List<TalentCategoryDTO> talentCategories) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.organizationId = organizationId;
		this.talentCategories = talentCategories;
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
