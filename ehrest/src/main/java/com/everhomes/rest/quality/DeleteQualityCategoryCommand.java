package com.everhomes.rest.quality;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * 
 * categoryId:category表的主键id
 *
 */
public class DeleteQualityCategoryCommand {
	
	@NotNull
	private Long categoryId;
	
	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
