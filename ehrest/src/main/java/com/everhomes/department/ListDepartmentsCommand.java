// @formatter:off
package com.everhomes.department;

import com.everhomes.util.StringHelper;

/**
 * <ul>

 * <li>parentId：所在的区域id</li>
 * <li>name：名称</li>
 * <li>pageOffset: 页码</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListDepartmentsCommand {
	
	private Long    parentId;
	private String  name;
	private Integer pageOffset;
	private Integer pageSize;
	
	public ListDepartmentsCommand() {
    }

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
