package com.everhomes.rest.quality;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>scores: 参考 {@link com.everhomes.rest.quality.ScoreDTO}</li>
 * </ul>
 */
public class ScoreGroupByTargetDTO {

	@ItemType(ScoreDTO.class)
	private List<ScoreDTO> scores;

	public List<ScoreDTO> getScores() {
		return scores;
	}

	public void setScores(List<ScoreDTO> scores) {
		this.scores = scores;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
