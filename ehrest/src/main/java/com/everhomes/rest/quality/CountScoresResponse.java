package com.everhomes.rest.quality;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>scores: 参考 {@link com.everhomes.rest.quality.ScoreGroupBySpecificationDTO}</li>
 * </ul>
 */
public class CountScoresResponse {

	@ItemType(ScoreGroupBySpecificationDTO.class)
	private ScoreGroupBySpecificationDTO scores;

	public ScoreGroupBySpecificationDTO getScores() {
		return scores;
	}

	public void setScores(ScoreGroupBySpecificationDTO scores) {
		this.scores = scores;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
