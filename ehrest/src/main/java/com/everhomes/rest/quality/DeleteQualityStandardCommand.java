package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li> standardId: 标准的主键id </li>
 * <li> targetId :项目id </li>
 * </li> namespaceId :namespaceId </li>
 * </ul>
 */
public class DeleteQualityStandardCommand {
	
	private Long standardId;

	private  Long targetId;

	private Integer namespaceId;

	public Long getStandardId() {
		return standardId;
	}

	public void setStandardId(Long standardId) {
		this.standardId = standardId;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
