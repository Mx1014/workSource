package com.everhomes.rest.configurations.admin;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
* <ul>
* <li>dtos: 后台返回数组 ，参考{@com.everhomes.rest.configurations.admin.ConfigurationsIdAdminDTO}</li>
* <li>nextPageAnchor: 下一页开始锚点</li>
* </ul>
*/
public class ConfigurationsAdminDTO {
	private List<ConfigurationsIdAdminDTO> dtos;
	

    private Long nextPageAnchor;
    


	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}


	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}


	public List<ConfigurationsIdAdminDTO> getDtos() {
		return dtos;
	}


	public void setDtos(List<ConfigurationsIdAdminDTO> dtos) {
		this.dtos = dtos;
	}


	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
