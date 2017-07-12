package com.everhomes.rest.quality;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>scores: 参考 {@link com.everhomes.rest.quality.ScoreDTO}</li>
 *  <li>targetName: 项目名</li>
 *  <li>targetId: 项目id</li>
 * </ul>
 */
public class ScoreGroupByTargetDTO {

	@ItemType(ScoreDTO.class)
	private List<ScoreDTO> scores;

	private Long targetId;

	private String targetName;
	
	public List<ScoreDTO> getScores() {
		return scores;
	}

	public void setScores(List<ScoreDTO> scores) {
		this.scores = scores;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
