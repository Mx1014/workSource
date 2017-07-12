package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>id: 主键id</li>
 *  <li>standardId: 标准id</li>
 *  <li>groupType: 业务组类型 com.everhomes.rest.quality.QualityGroupType</li>
 *  <li>groupId: 业务组id</li>
 *  <li>positionId: 通用岗位id</li>
 *  <li>groupName: 业务组组名</li>
 *  <li>inspectorUid: 业务组核查人uid</li>
 * </ul>
 */
public class StandardGroupDTO {
	
	private Long id;
	
	private Byte groupType;
	
	private Long groupId;
	
	private String groupName;
	
	private Long standardId;
	
	private Long positionId;
	
	private Long inspectorUid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Byte getGroupType() {
		return groupType;
	}

	public void setGroupType(Byte groupType) {
		this.groupType = groupType;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getPositionId() {
		return positionId;
	}

	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Long getStandardId() {
		return standardId;
	}

	public void setStandardId(Long standardId) {
		this.standardId = standardId;
	}

	public Long getInspectorUid() {
		return inspectorUid;
	}

	public void setInspectorUid(Long inspectorUid) {
		this.inspectorUid = inspectorUid;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
