package com.everhomes.rest.hotTag;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 *<ul>
 *<li>names:标签名列表</li>
 *<li>serviceType:标签服务类型 参考{@link HotTagServiceType}</li>
 *<li>namespaceId: 域空间Id</li>
 *</ul>
 */
public class resetHotTagCommand {

	@ItemType(String.class)
	private List<String> names;

	private String serviceType;

	private Integer namespaceId;

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
