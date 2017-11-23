package com.everhomes.rest.module;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>targetType: 授权对象 部门机构：EhOrganizations 用户：EhUsers</li>
 * <li>targetId: 对象id</li>
 * <li>targetName: 对象名称</li>
 * <li>includeChildFlag: 是否包含子集， 参考{@link com.everhomes.rest.common.IncludeChildFlagType}</li>
 * </ul>
 */
public class AssignmentTarget {

	@NotNull
	private String targetType;

	@NotNull
	private Long targetId;

	private String targetName;

	private Byte includeChildFlag;

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public Byte getIncludeChildFlag() {
		return includeChildFlag;
	}

	public void setIncludeChildFlag(Byte includeChildFlag) {
		this.includeChildFlag = includeChildFlag;
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
