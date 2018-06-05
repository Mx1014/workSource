package com.everhomes.rest.yellowPage;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
	* 
	* <ul>
	* <li>id : 服务商id</li>
	* </ul>
	*  @author
	*  huangmingbo 2018年5月17日
**/
public class DeleteServiceAllianceProviderCommand {
	
    @NotNull
    private Long id;

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
}
