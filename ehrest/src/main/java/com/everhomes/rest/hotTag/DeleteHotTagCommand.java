package com.everhomes.rest.hotTag;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 *<li>id:标签ID</li>
 *</ul>
 */
public class DeleteHotTagCommand {
	
	private Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
