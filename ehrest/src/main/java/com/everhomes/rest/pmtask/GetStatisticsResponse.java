package com.everhomes.rest.pmtask;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerId: 园区id</li>
 * <li>ownerName: 园区名称</li>
 * <li>avgScore: 评价均分</li>
 * <li>evaluateCount: 评价人数</li>
 * </ul>
 */
public class GetStatisticsResponse {
	private Long ownerId;
	private String ownerName;
	@ItemType(CategoryTaskStatisticsDTO.class)
	private List<CategoryTaskStatisticsDTO> CategoryTaskStatistics;
	private Float avgScore;
	private Integer evaluateCount;
	@ItemType(EvaluateScoreDTO.class)
	private List<EvaluateScoreDTO> evaluates;
	public Long getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public List<CategoryTaskStatisticsDTO> getCategoryTaskStatistics() {
		return CategoryTaskStatistics;
	}
	public void setCategoryTaskStatistics(
			List<CategoryTaskStatisticsDTO> categoryTaskStatistics) {
		CategoryTaskStatistics = categoryTaskStatistics;
	}
	public Float getAvgScore() {
		return avgScore;
	}
	public void setAvgScore(Float avgScore) {
		this.avgScore = avgScore;
	}
	public Integer getEvaluateCount() {
		return evaluateCount;
	}
	public void setEvaluateCount(Integer evaluateCount) {
		this.evaluateCount = evaluateCount;
	}
	public List<EvaluateScoreDTO> getEvaluates() {
		return evaluates;
	}
	public void setEvaluates(List<EvaluateScoreDTO> evaluates) {
		this.evaluates = evaluates;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
