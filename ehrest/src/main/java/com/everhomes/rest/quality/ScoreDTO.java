package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 参考主键id</li>
 *  <li>targetName: 项目名</li>
 *  <li>targetId: 项目id</li>
 *  <li>score: 分数</li>
 * </ul>
 */
public class ScoreDTO {

	private Long id;
	
	private Long targetId;
	
	private String targetName;
	
	private Double score;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
