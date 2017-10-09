package com.everhomes.rest.hotTag;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 *<li>id:标签ID</li>
 *<li>name:标签名</li>
 *<li>namespaceId: 域空间Id</li>
 *</ul>
 */
public class TagDTO {
	
	private Long id;

	private String name;

	private Integer namespaceId;

	private Long categoryId;
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

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

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
