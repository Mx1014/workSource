package com.everhomes.rest.configurations.admin;

import com.everhomes.util.StringHelper;

/**
* <ul>
* <li>namespaceId: 域空间</li>
* <li>name: 配置项名称</li>
* <li>value: 配置项值</li>
* <li>pageSize: 页数据量</li>
* <li>pageAnchor: 本页开始的锚点</li>
* </ul>
*/
public class ConfigurationsAdminCommand {

    private Integer namespaceId;   
    private String name;
    private String value;
    private Integer  pageSize;    
    private Long pageAnchor;
    
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
    
	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}


	public Long getPageAnchor() {
		return pageAnchor;
	}

	public void setPageAnchor(Long pageAnchor) {
		this.pageAnchor = pageAnchor;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
