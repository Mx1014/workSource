package com.everhomes.rest.pmtask;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>score: 分数</li>
 * <li>count: 人数</li>
 * </ul>
 */
public class EvaluateScoreDTO {
	private Integer score;
	private Integer count;
	
	public EvaluateScoreDTO(Integer score, Integer count){
		this.score = score;
		this.count = count;
	}
	
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
