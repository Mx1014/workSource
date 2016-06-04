package com.everhomes.rest.hotTag;

import com.everhomes.util.StringHelper;

/**
 *<ul>
 *<li>name:标签名</li>
 *<li>serviceType:标签服务类型 参考{@link com.everhomes.rest.hotTag.HotTagServiceType}</li>
 *</ul>
 */
public class SetHotTagCommand {
	
	private String name;
	
	private String serviceType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
