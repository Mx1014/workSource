package com.everhomes.rest.configurations.admin;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
* <ul>
* <li>id: 主键ID [必填]</li>
* <li>namespaceId: 域空间</li>
* </ul>
*/
public class ConfigurationsIdAdminCommand {
	
	@NotNull
	private Integer id;
    private Integer namespaceId;   
   
    
    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
