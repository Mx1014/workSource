package com.everhomes.rest.configurations.admin;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
* <ul>
* <li>request: List<ConfigurationsIdAdminDTO> ，参考{@com.everhomes.rest.configurations.admin.ConfigurationsIdAdminDTO}</li>
* <li>pageSize: 页数据量</li>
* <li>pageAnchor: 开始页码</li>
* </ul>
*/
public class ConfigurationsAdminDTO {
	private List<ConfigurationsIdAdminDTO> request;
	
	private String  pageSize;    
    private String pageOffset;
    
    
	public List<ConfigurationsIdAdminDTO> getRequest() {
		return request;
	}


	public void setRequest(List<ConfigurationsIdAdminDTO> request) {
		this.request = request;
	}


	public String getPageSize() {
		return pageSize;
	}


	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getPageOffset() {
		return pageOffset;
	}


	public void setPageOffset(String pageOffset) {
		this.pageOffset = pageOffset;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
