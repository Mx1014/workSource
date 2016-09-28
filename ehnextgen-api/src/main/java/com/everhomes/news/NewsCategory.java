package com.everhomes.news;

import com.everhomes.server.schema.tables.pojos.EhNewsCategories;
import com.everhomes.util.StringHelper;

public class NewsCategory extends EhNewsCategories {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1796117121187200850L;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
