package com.everhomes.rest.hotTag;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 *<li>pageSize:列热门标签的数量</li>
 *<li>serviceType:标签服务类型 参考{@link com.everhomes.rest.hotTag.HotTagServiceType}</li>
 *</ul>
 */
public class ListHotTagCommand {
	
	private Integer pageSize;
	
	private String serviceType;
	
	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
