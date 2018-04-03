// @formatter:off
package com.everhomes.rest.business;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>result: 结果</li>
 * <li>body: 数据</li>
 * </ul>
 */
public class SearchShopsResponse {
	Boolean result;	
	@ItemType(SearchShopsPageFinder.class)
	SearchShopsPageFinder body;
	
	public Boolean getResult() {
		return result;
	}
	
	public void setResult(Boolean result) {
		this.result = result;
	}
	
	public SearchShopsPageFinder getBody() {
		return body;
	}

	public void setBody(SearchShopsPageFinder body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
