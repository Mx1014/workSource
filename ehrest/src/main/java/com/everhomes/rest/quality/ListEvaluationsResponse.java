package com.everhomes.rest.quality;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>performances: 参考com.everhomes.rest.quality.EvaluationDTO</li>
 *  <li>pageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 */
public class ListEvaluationsResponse {
	
	@ItemType(EvaluationDTO.class)
	private List<EvaluationDTO> performances;

	private Long nextPageAnchor;
	
	public ListEvaluationsResponse(Long nextPageAnchor, List<EvaluationDTO> performances) {
        this.nextPageAnchor = nextPageAnchor;
        this.performances = performances;
    }

	public List<EvaluationDTO> getPerformances() {
		return performances;
	}

	public void setPerformances(List<EvaluationDTO> performances) {
		this.performances = performances;
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
