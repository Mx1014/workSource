package com.everhomes.rest.hotTag;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 *<li>pageSize:列热门标签的数量</li>
 * <li>pageOffset:页码</li>
 *<li>serviceType:标签服务类型 参考{@link HotTagServiceType}</li>
 *</ul>
 */
public class ListAllHotTagCommand {
	
	private Integer pageSize;

	private Integer pageOffset;
	
	private String serviceType;
	
	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
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
