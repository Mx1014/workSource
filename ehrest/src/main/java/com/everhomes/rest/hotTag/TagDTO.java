package com.everhomes.rest.hotTag;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 *<li>id:标签ID</li>
 *<li>name:标签名</li>
 *</ul>
 */
public class TagDTO {
	
	private Long id;

	private String name;
	 
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


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
