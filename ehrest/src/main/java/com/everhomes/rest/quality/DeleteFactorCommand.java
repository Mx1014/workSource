package com.everhomes.rest.quality;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * 
 * id:业务组-权重 主键id
 *
 */
public class DeleteFactorCommand {
	
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
