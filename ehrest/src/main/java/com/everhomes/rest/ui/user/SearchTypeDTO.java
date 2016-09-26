package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 * <li>id: id</li>
 * <li>name:类型名称</li>
 * <li>contentType: 搜索内容类型</li>
 *</ul>
 */
public class SearchTypeDTO {
	
	private Long id;
	
	private String name;
	
	private String contentType;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
