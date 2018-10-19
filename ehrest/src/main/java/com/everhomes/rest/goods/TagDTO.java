package com.everhomes.rest.goods;

import java.util.List;

import com.everhomes.util.StringHelper;

public class TagDTO {
	
	private String id;
	private String name;
	private String serveApplyName;
	private List<TagDTO> tags;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
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
	
	public List<TagDTO> getTags() {
		return tags;
	}
	public void setTags(List<TagDTO> tags) {
		this.tags = tags;
	}

	public String getServeApplyName() {
		return serveApplyName;
	}

	public void setServeApplyName(String serveApplyName) {
		this.serveApplyName = serveApplyName;
	}
}
