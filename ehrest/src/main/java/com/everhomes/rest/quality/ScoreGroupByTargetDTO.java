package com.everhomes.rest.quality;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

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

	//fix order
	private  Integer OrderId;

	//fix order
	private  Double totalScore;

	//产品确认得分一致的情况按照建筑面积排序
	private  Double buildArea;
	
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

	public Integer getOrderId() {
		return OrderId;
	}

	public void setOrderId(Integer orderId) {
		OrderId = orderId;
	}

	public Double getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Double totalScore) {
		this.totalScore = totalScore;
	}

	public Double getBuildArea() {
		return buildArea;
	}

	public void setBuildArea(Double buildArea) {
		this.buildArea = buildArea;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
