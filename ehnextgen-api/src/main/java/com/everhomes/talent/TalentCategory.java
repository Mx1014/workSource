// @formatter:off
package com.everhomes.talent;

import com.everhomes.server.schema.tables.pojos.EhTalentCategories;
import com.everhomes.util.StringHelper;

public class TalentCategory extends EhTalentCategories {
	
	private static final long serialVersionUID = -1049038217375098169L;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}