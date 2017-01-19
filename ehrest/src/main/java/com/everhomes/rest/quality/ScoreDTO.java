package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 参考主键id</li>
 *  <li>specificationId: 类型/规范 id</li>
 *  <li>specificationName: 类型/规范 名</li>
 *  <li>specificationDescription: 规范细项 名</li>
 *  <li>specificationWeight: 类型/规范 权重</li>
 *  <li>specificationScore: 类型/规范 分数</li>
 *  <li>specificationInspectionType: 类型/规范 类型 0: 类型, 1: 规范, 2: 规范事项</li>
 *  <li>score: 分数</li>
 * </ul>
 */
public class ScoreDTO {

	private Long id;

	private Long specificationId;

	private String specificationName;

	private String specificationDescription;

	private Double specificationWeight;

	private Double specificationScore;

	private Byte specificationInspectionType;
	
	private Double score;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getSpecificationDescription() {
		return specificationDescription;
	}

	public void setSpecificationDescription(String specificationDescription) {
		this.specificationDescription = specificationDescription;
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
