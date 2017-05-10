package com.everhomes.rest.quality;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>scores: 参考 {@link com.everhomes.rest.quality.ScoreGroupByTargetDTO}</li>
 *  <li>specifications: 列名称列表 参考 {@link com.everhomes.rest.quality.CountScoresSpecificationDTO}</li>
 * </ul>
 */
public class CountScoresResponse {

	@ItemType(CountScoresSpecificationDTO.class)
	private List<CountScoresSpecificationDTO> specifications;

	@ItemType(ScoreGroupByTargetDTO.class)
	private List<ScoreGroupByTargetDTO> scores;

	public List<CountScoresSpecificationDTO> getSpecifications() {
		return specifications;
	}

	public void setSpecifications(List<CountScoresSpecificationDTO> specifications) {
		this.specifications = specifications;
	}

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
