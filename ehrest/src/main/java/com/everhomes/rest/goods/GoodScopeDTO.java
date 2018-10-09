package com.everhomes.rest.goods;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>title : 标题</li>
 * <li>tag ：商品标识 </li>
 * </ul>
 * @author miaozhou 
 * @date 2018年10月7日
 */
public class GoodScopeDTO {
	
	private String title;
	private List<String> tagList;
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public List<String> getTagList() {
		return tagList;
	}
	
	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}
	
}
