package com.everhomes.rest.statistics.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;


/**
 * 
 * @author Administrator
 *
 *<ul>
 *<li>nextPageAnchor:下一页页码</li>
 *<li>values:统计列表</li>
 *</ul>
 */
public class ListStatisticsByDateResponse {
	@ItemType(ListStatisticsByDateDTO.class)
	private List<ListStatisticsByDateDTO> values;
	    
	 private Long nextPageAnchor;

	public List<ListStatisticsByDateDTO> getValues() {
		return values;
	}

	public void setValues(List<ListStatisticsByDateDTO> values) {
		this.values = values;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	@Override
    public String toString() {
		return StringHelper.toJsonString(this);
	}
}
