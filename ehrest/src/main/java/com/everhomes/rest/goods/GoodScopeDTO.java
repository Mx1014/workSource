package com.everhomes.rest.goods;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>title : 标题</li>
 * <li>tagList ：标识列表 </li>
 * </ul>
 * @author miaozhou 
 * @date 2018年10月7日
 */
public class GoodScopeDTO {
	
	private String title;
	private List<TagDTO> tagList;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public List<TagDTO> getTagList() {
		return tagList;
	}

	public void setTagList(List<TagDTO> tagList) {
		this.tagList = tagList;
	}
	
	
}
