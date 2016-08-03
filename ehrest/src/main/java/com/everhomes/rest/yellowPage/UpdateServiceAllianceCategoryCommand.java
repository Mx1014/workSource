package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *  <li>categoryId: 类型id</li>
 *  <li>name: 类型名称</li>
 * </ul>
 */
public class UpdateServiceAllianceCategoryCommand {
	
	private Long categoryId;
	
	private String name;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
