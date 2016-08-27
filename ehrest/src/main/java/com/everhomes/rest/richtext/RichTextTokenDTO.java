package com.everhomes.rest.richtext;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *	<li>ownerId : 类型所属的主体id</li>
 *	<li>ownerType : 类型所属的主体</li>
 *	<li>resourceType : 资源类型，参考{@link com.everhomes.rest.richtext.RichTextResourceType}</li>
 *	<li>namespaceId : 域空间id</li>
 * </ul>
 */
public class RichTextTokenDTO {
	
	private Long ownerId;
	
	private String ownerType;
	
	private String resourceType;
	
	private Integer namespaceId;

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
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
