package com.everhomes.rest.quality;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>scores: 参考 {@link ScoreGroupByTargetDTO}</li>
 * </ul>
 */
public class CountScoresResponse {

	@ItemType(ScoreGroupByTargetDTO.class)
	private List<ScoreGroupByTargetDTO> scores;

	public List<ScoreGroupByTargetDTO> getScores() {
		return scores;
	}

	public void setScores(List<ScoreGroupByTargetDTO> scores) {
		this.scores = scores;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
