package com.everhomes.yellowPage.faq;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>count : 正在处理的个数</li>
 * </ul>
 * @author huangmingbo 
 * @date 2018年10月23日
 */
public class GetPendingServiceCountsResponse {
	private Integer count;
	
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	
}
