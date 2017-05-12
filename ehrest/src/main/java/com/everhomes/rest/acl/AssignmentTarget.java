package com.everhomes.rest.acl;


import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>targetType: 授权对象 部门机构：EhOrganizations 用户：EhUsers</li>
 * <li>targetId: 对象id</li>
 * <li>includeChildFlag: 是否包含子集， 参考{@link com.everhomes.rest.common.IsIncludeChildFlag}</li>
 * </ul>
 */
public class AssignmentTarget {

	@NotNull
	private String targetType;

	@NotNull
	private Long targetId;

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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    
}
