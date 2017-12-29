package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * 
 * standardId: 标准的主键id
 * targetId :项目id
 *
 */
public class DeleteQualityStandardCommand {
	
	private Long standardId;

	private  Long targetId;

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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
