package com.everhomes.rest.news.open;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.common.IdNameDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 参数
 * <li>id:父id</li>
 * <li>name: 名称</li>
 * <li>childs: 子节点list {@link com.everhomes.rest.news.open.IdNameInfoDTO}</li> 
 * </ul>
 */
public class TagDTO {
	
	private Long id;
	private String name;
	@ItemType(IdNameDTO.class)
	List<IdNameDTO> childs;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

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

	public List<IdNameDTO> getChilds() {
		return childs;
	}

	public void setChilds(List<IdNameDTO> childs) {
		this.childs = childs;
	}
	
}
