package com.everhomes.test.demo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.everhomes.test.core.base.BaseLoginAuthTestCase;
import com.everhomes.test.core.search.SearchConstant;
import com.everhomes.test.core.search.SearchProvider;

public class SearchTest extends BaseLoginAuthTestCase{
	@Autowired
	private SearchProvider searchProvider;
	
	@Test
	public void testClearType(){
		searchProvider.clearType(SearchConstant.NEWS);
	}
}
