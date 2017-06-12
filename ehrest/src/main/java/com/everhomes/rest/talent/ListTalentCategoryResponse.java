// @formatter:off
package com.everhomes.rest.talent;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>talentCategories: 分类列表，参考{@link com.everhomes.rest.talent.TalentCategoryDTO}</li>
 * </ul>
 */
public class ListTalentCategoryResponse {

	@ItemType(TalentCategoryDTO.class)
	private List<TalentCategoryDTO> talentCategories;

	public ListTalentCategoryResponse() {

	}

	public ListTalentCategoryResponse(List<TalentCategoryDTO> talentCategories) {
		super();
		this.talentCategories = talentCategories;
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
