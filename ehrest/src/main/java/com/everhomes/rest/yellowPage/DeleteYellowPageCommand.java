package com.everhomes.rest.yellowPage;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class DeleteYellowPageCommand { 
	@NotNull
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
