package com.everhomes.rest.quality;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>scores: 参考 {@link com.everhomes.rest.quality.ScoreGroupBySpecificationDTO}</li>
 * </ul>
 */
public class CountScoresResponse {

	@ItemType(ScoreGroupBySpecificationDTO.class)
	private List<ScoreGroupBySpecificationDTO> scores;

	public List<ScoreGroupBySpecificationDTO> getScores() {
		return scores;
	}

	public void setScores(List<ScoreGroupBySpecificationDTO> scores) {
		this.scores = scores;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
