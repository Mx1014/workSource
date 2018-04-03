package com.everhomes.rest.hotTag;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>namespaceId: 域空间Id，不传则取当前域空间的</li>
 *     <li>moduleType: 模块类型  参考 {@link com.everhomes.rest.forum.ForumModuleType}</li>
 *     <li>categoryId: categoryId</li>
 *     <li>serviceType: 标签服务类型 参考{@link com.everhomes.rest.hotTag.HotTagServiceType}</li>
 *     <li>names: 标签名列表</li>
 * </ul>
 */
public class ResetHotTagCommand {

	private Integer namespaceId;

	private Byte moduleType;

	private Long categoryId;

	private String serviceType;

	@ItemType(String.class)
	private List<String> names;

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

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Byte getModuleType() {
		return moduleType;
	}

	public void setModuleType(Byte moduleType) {
		this.moduleType = moduleType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
