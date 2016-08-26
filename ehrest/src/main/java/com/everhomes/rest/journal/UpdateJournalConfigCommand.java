package com.everhomes.rest.journal;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id</li>
 * <li>namespaceId: 域空间</li>
 * <li>description: 须知</li>
 * </ul>
 */
public class UpdateJournalConfigCommand {
	private Long id;
	private Integer namespaceId;
	private String description;
	private String posterPath;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	public String getPosterPath() {
		return posterPath;
	}
	public void setPosterPath(String posterPath) {
		this.posterPath = posterPath;
	}
}
