// @formatter:off
package com.everhomes.talent;

import com.everhomes.server.schema.tables.pojos.EhTalentCategories;
import com.everhomes.util.StringHelper;

public class TalentCategory extends EhTalentCategories {
	
	private static final long serialVersionUID = -1049038217375098169L;
	
	private static final TalentCategory talentCategory = new TalentCategory(otherId(), otherName());
	
	public TalentCategory() {
		super();
	}

	public TalentCategory(Long id, String name) {
		super.setId(id);
		super.setName(name);
	}

	public static TalentCategory other() {
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