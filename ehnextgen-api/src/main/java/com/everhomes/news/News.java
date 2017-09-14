// @formatter:off
package com.everhomes.news;

import com.everhomes.server.schema.tables.pojos.EhNews;
import com.everhomes.util.StringHelper;

import java.util.List;

public class News extends EhNews{
	private static final long serialVersionUID = -931770711864372078L;
	
	public News() {
		super();
	}
	
	public News(Long id){
		setId(id);
	}

	private List<Long> communityIds;

	private List<Long> tagIds;

	public List<Long> getCommunityIds() {
		return communityIds;
	}

	public void setCommunityIds(List<Long> communityIds) {
		this.communityIds = communityIds;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public List<Long> getTagIds() {
		return tagIds;
	}

	public void setTagIds(List<Long> tagIds) {
		this.tagIds = tagIds;
	}
}
