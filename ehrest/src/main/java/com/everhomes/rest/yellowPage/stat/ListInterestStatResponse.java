package com.everhomes.rest.yellowPage.stat;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
* 
* <ul>
* <li>nextPageAnchor : 下一页锚点</li>
* <li>dtos : 兴趣指数记录{@link com.everhomes.rest.yellowPage.stat.InterestStatDTO}</li>
* </ul>
**/
public class ListInterestStatResponse {
	
	private Long nextPageAnchor;
	
	@ItemType(InterestStatDTO.class)
	private List<InterestStatDTO> dtos;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<InterestStatDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<InterestStatDTO> dtos) {
		this.dtos = dtos;
	}
}
