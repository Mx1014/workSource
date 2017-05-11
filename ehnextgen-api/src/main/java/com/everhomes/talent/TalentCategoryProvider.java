// @formatter:off
package com.everhomes.talent;

import java.util.List;

public interface TalentCategoryProvider {

	void createTalentCategory(TalentCategory talentCategory);

	void updateTalentCategory(TalentCategory talentCategory);

	TalentCategory findTalentCategoryById(Long id);

	List<TalentCategory> listTalentCategory();

}