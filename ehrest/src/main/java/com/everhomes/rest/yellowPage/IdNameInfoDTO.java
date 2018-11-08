package com.everhomes.rest.yellowPage;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>parentId: parentId</li>
 * <li>id: id</li>
 * <li>name: 名称</li>
 * </ul>
 */
public class IdNameInfoDTO {
	
	private Long parentId;
	private Long id;
	private String name;
	private String content;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}

