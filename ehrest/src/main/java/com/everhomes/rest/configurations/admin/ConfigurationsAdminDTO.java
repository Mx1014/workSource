package com.everhomes.rest.configurations.admin;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
* <ul>
* <li>dtoList: 后台返回数组 ，参考{@com.everhomes.rest.configurations.admin.ConfigurationsIdAdminDTO}</li>
* <li>nextPageAnchor: 下一页开始锚点</li>
* </ul>
*/
public class ConfigurationsAdminDTO {
	private List<ConfigurationsIdAdminDTO> dtoList;
	

    private Long nextPageAnchor;
    


	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}


	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}


	public List<ConfigurationsIdAdminDTO> getDtoList() {
		return dtoList;
	}


	public void setDtoList(List<ConfigurationsIdAdminDTO> dtoList) {
		this.dtoList = dtoList;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
