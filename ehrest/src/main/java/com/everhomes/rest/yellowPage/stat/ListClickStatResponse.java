package com.everhomes.rest.yellowPage.stat;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
* 
* <ul>
* <li>nextPageAnchor : 下一页锚点</li>
* <li>dtos : 点击明细记录{@link com.everhomes.rest.yellowPage.stat.ClickStatDTO}</li>
* </ul>
**/
public class ListClickStatResponse {
	
	private Long nextPageAnchor;
	
	@ItemType(ClickStatDTO.class)
	private List<ClickStatDTO> dtos;
	
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

	public List<ClickStatDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<ClickStatDTO> dtos) {
		this.dtos = dtos;
	}

	
}
