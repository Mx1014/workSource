package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 参考主键id</li>
 *  <li>specificationId: 类型id</li>
 *  <li>specificationName: 类型名</li>
 *  <li>specificationWeight: 类型权重</li>
 *  <li>specificationScore: 类型分数</li>
 *  <li>targetName: 项目名</li>
 *  <li>targetId: 项目id</li>
 *  <li>specificationWeight: 类型权重</li>
 *  <li>score: 分数</li>
 * </ul>
 */
public class ScoreDTO {

	private Long id;
	
	private Long specificationId;
	
	private String specificationName;
	
	private Double specificationWeight;
	
	private Double specificationScore;
	
	private Long targetId;
	
	private String targetName;
	
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
