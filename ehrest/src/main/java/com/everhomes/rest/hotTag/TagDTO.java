package com.everhomes.rest.hotTag;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 标签ID</li>
 *     <li>name: 标签名</li>
 *     <li>namespaceId: 域空间Id</li>
 *     <li>moduleType: 模块类型  参考 {@link com.everhomes.rest.forum.ForumModuleType}</li>
 *     <li>categoryId: categoryId</li>
 *     <li>serviceType: 标签服务类型 参考{@link com.everhomes.rest.hotTag.HotTagServiceType}</li>
 * </ul>
 */
public class TagDTO {

	private Long id;

	private String name;

	private Integer namespaceId;

	private Byte moduleType;

	private Long categoryId;

	private String serviceType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
