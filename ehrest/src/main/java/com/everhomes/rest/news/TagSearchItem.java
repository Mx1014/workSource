package com.everhomes.rest.news;

import java.util.List;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 * 查询tag标签使用的类
 * <li>parentTagId: 父标签Id</li>
 * <li>categoryId: 父标签下的所有子标签</li>
 * </ul>
 */
public class TagSearchItem {
	
	private Long parentTagId;
	private List<Long> childTagIds;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Long getParentTagId() {
		return parentTagId;
	}

	public void setParentTagId(Long parentTagId) {
		this.parentTagId = parentTagId;
	}

	public List<Long> getChildTagIds() {
		return childTagIds;
	}

	public void setChildTagIds(List<Long> childTagIds) {
		this.childTagIds = childTagIds;
	}

}
