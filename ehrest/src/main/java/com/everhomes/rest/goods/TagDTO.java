package com.everhomes.rest.goods;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id : </li>
 * <li>name : </li>
 * <li>title : 标题</li>
 * <li>tag ：商品标识 </li>
 * </ul>
 * @author miaozhou 
 * @date 2018年10月7日
 */
public class TagDTO {
	
	private Long id;
	private String name;
	private String title;
	private List<TagDTO> tagList;
	
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
