package com.everhomes.rest.hotTag;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 *<li>keyword:关键字</li>
 *<li>serviceType:标签服务类型 参考{@link com.everhomes.rest.hotTag.HotTagServiceType}</li>
 *</ul>
 */
public class SearchTagCommand {

	private String keyword;
	
	private String serviceType;
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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
