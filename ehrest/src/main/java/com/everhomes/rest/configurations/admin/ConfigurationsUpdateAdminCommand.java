package com.everhomes.rest.configurations.admin;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
* <ul>
* <li>id: 主键ID [必填]</li>
* <li>namespaceId: 域空间</li>
* <li>name: 配置项名称</li>
* <li>value: 配置项值</li>
* <li>displayName: 展示名称</li>
* <li>description: 配置项描述</li>
* </ul>
*/
public class ConfigurationsUpdateAdminCommand {

	@NotNull
    private Integer id;  
	
    private Integer namespaceId;   
    
    @NotNull
    private String name;
    
    private String value;
    private String  description;    
    private String displayName;
  
    
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
