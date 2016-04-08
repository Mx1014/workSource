package com.everhomes.rest.quality;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>qaStandards: 标准列表 com.everhomes.rest.quality.QualityStandardsDTO</li>
 *  <li>pageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 */
public class ListQualityStandardsResponse {
	
	@ItemType(QualityStandardsDTO.class)
	private List<QualityStandardsDTO> qaStandards;
	
	private Long nextPageAnchor;
	
	public ListQualityStandardsResponse(Long nextPageAnchor, List<QualityStandardsDTO> qaStandards) {
        this.nextPageAnchor = nextPageAnchor;
        this.qaStandards = qaStandards;
    }

	public List<QualityStandardsDTO> getQaStandards() {
		return qaStandards;
	}

	public void setQaStandards(List<QualityStandardsDTO> qaStandards) {
		this.qaStandards = qaStandards;
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
