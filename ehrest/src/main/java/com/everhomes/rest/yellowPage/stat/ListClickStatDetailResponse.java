package com.everhomes.rest.yellowPage.stat;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
* 
* <ul>
* <li>nextPageAnchor : 下一页锚点</li>
* <li>nextPageAnchorId : 因为时间戳有可能重复，故单个锚点无法满足排序要求，这里添加多一个锚点</li>
* <li>dtos : 明细详情记录{@link com.everhomes.rest.yellowPage.stat.ClickStatDetailDTO}</li>
* </ul>
**/
public class ListClickStatDetailResponse {
	private Long nextPageAnchor;
	private Long nextPageAnchorId;
	
	@ItemType(ClickStatDetailDTO.class)
	private List<ClickStatDetailDTO> dtos;
	
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

	public List<ClickStatDetailDTO> getDtos() {
		return dtos;
	}

	public void setDtos(List<ClickStatDetailDTO> dtos) {
		this.dtos = dtos;
	}

	public Long getNextPageAnchorId() {
		return nextPageAnchorId;
	}

	public void setNextPageAnchorId(Long nextPageAnchorId) {
		this.nextPageAnchorId = nextPageAnchorId;
	}	
	
}
