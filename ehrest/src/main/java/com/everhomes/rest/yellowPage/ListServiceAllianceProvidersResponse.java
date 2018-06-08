package com.everhomes.rest.yellowPage;
import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.RestResponseBase;
import com.everhomes.util.StringHelper;
/**
* 
* <ul>
* <li>nextPageAnchor : 下一页锚点</li>
* <li>providers : 服务商{@link com.everhomes.rest.yellowPage.ServiceAllianceProviderDTO}}</li>
* </ul>
*  @author
*  huangmingbo 2018年5月22日
**/
public class ListServiceAllianceProvidersResponse {
	
	private Long nextPageAnchor;
	
	@ItemType(ServiceAllianceProviderDTO.class)
	private List<ServiceAllianceProviderDTO> providers;

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public List<ServiceAllianceProviderDTO> getProviders() {
		return providers;
	}


	public void setProviders(List<ServiceAllianceProviderDTO> providers) {
		this.providers = providers;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
}

