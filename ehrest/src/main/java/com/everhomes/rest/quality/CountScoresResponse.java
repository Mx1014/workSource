package com.everhomes.rest.quality;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>scores: 参考 {@link com.everhomes.rest.quality.ScoreGroupByTargetDTO}</li>
 *  <li>nextPageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 */
public class CountScoresResponse {

	@ItemType(ScoreGroupByTargetDTO.class)
	private List<ScoreGroupByTargetDTO> scores;

	private Long nextPageAnchor;

	public List<ScoreGroupByTargetDTO> getScores() {
		return scores;
	}

	public void setScores(List<ScoreGroupByTargetDTO> scores) {
		this.scores = scores;
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
