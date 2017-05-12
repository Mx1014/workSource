// @formatter:off
package com.everhomes.talent;

import com.everhomes.server.schema.tables.pojos.EhTalentCategories;
import com.everhomes.util.StringHelper;

public class TalentCategory extends EhTalentCategories {
	
	private static final long serialVersionUID = -1049038217375098169L;
	
	public static TalentCategory other() {
		TalentCategory talentCategory = new TalentCategory();
		talentCategory.setId(otherId());
		talentCategory.setName(otherName());
		return talentCategory;
	}
	
	public static Long otherId() {
		return -1L;
	}
	
	public static String otherName() {
		return "其他";
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}