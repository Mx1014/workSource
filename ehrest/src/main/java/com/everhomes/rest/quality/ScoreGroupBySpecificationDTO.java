package com.everhomes.rest.quality;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>scores: 参考 {@link com.everhomes.rest.quality.ScoreDTO}</li>
 *  <li>specificationId: 类型/规范 id</li>
 *  <li>specificationName: 类型/规范 名</li>
 *  <li>specificationWeight: 类型/规范 权重</li>
 *  <li>specificationScore: 类型/规范 分数</li>
 *  <li>specificationInspectionType: 类型/规范 类型 0: 类型, 1: 规范, 2: 规范事项</li>
 * </ul>
 */
public class ScoreGroupBySpecificationDTO {

	@ItemType(ScoreDTO.class)
	private List<ScoreDTO> scores;
	
	private Long specificationId;
	
	private String specificationName;
	
	private Double specificationWeight;
	
	private Double specificationScore;
	
	private Byte specificationInspectionType;

	public List<ScoreDTO> getScores() {
		return scores;
	}

	public void setScores(List<ScoreDTO> scores) {
		this.scores = scores;
	}
	
	public Long getSpecificationId() {
		return specificationId;
	}

	public void setSpecificationId(Long specificationId) {
		this.specificationId = specificationId;
	}

	public String getSpecificationName() {
		return specificationName;
	}

	public void setSpecificationName(String specificationName) {
		this.specificationName = specificationName;
	}

	public Double getSpecificationWeight() {
		return specificationWeight;
	}

	public void setSpecificationWeight(Double specificationWeight) {
		this.specificationWeight = specificationWeight;
	}

	public Double getSpecificationScore() {
		return specificationScore;
	}

	public void setSpecificationScore(Double specificationScore) {
		this.specificationScore = specificationScore;
	}

	public Byte getSpecificationInspectionType() {
		return specificationInspectionType;
	}

	public void setSpecificationInspectionType(Byte specificationInspectionType) {
		this.specificationInspectionType = specificationInspectionType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
