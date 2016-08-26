package com.everhomes.rest.journal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>id: id</li>
 * </ul>
 */
public class GetJournalCommand {
	private Long id;
	private Integer namespaceId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
