package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 主键id</li>
 *  <li>groupId: 业务组id</li>
 *  <li>groupName: 业务组名称</li>
 *  <li>score: 绩效得分</li>
 * </ul>
 */
public class EvaluationDTO {
	
	private Long id;
	
	private Long groupId;
	
	private String groupName;
	
	private Double score;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
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
