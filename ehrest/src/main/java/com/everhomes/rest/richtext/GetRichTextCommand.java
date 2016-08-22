package com.everhomes.rest.richtext;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *	<li>ownerId : 类型所属的主体id</li>
 *	<li>ownerType : 类型所属的主体</li>
 *	<li>resourceType : 资源类型，参考{@link com.everhomes.rest.richtext.RichTextResourceType}</li>
 * </ul>
 */
public class GetRichTextCommand {

	private Long ownerId;
	
	private String ownerType;
	
	private String resourceType;
	
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

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
