package com.everhomes.rest.quality;

import com.everhomes.util.StringHelper;

/**
 * 
 * standardId: 标准的主键id
 *
 */
public class DeleteQualityStandardCommand {
	
	private Long standardId;

	public Long getStandardId() {
		return standardId;
	}

	public void setStandardId(Long standardId) {
		this.standardId = standardId;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
