package com.everhomes.rest.yellowPage;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.asset.TargetDTO;
import com.everhomes.util.StringHelper;

/**
* <ul>
* <li>onlineServices : 客服列表{@link com.everhomes.rest.asset.TargetDTO}</li>
* </ul>
*  @author
*  huangmingbo 2018年6月5日
**/
public class ListOnlineServicesResponse {
	
	
	@ItemType(TargetDTO.class)
	private List<TargetDTO> onlineServices;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public List<TargetDTO> getOnlineServices() {
		return onlineServices;
	}

	public void setOnlineServices(List<TargetDTO> onlineServices) {
		this.onlineServices = onlineServices;
	}

}
