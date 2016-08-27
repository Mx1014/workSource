package com.everhomes.rest.richtext;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>id : id</li>
 * 	<li>namespaceId : 域空间id</li>
 *	<li>ownerId : 类型所属的主体id</li>
 *	<li>ownerType : 类型所属的主体</li>
 *	<li>resourceType : 资源类型，参考{@link com.everhomes.rest.richtext.RichTextResourceType}</li>
 *	<li>contentType : 文本类型，参考{@link com.everhomes.rest.richtext.RichTextContentType}</li>
 *	<li>content : 文本内容</li>
 * </ul>
 */
public class RichTextDTO {

	private Long id;
	
	private Integer namespaceId;
	
	private Long ownerId;
	
	private String ownerType;
	
	private String resourceType;
	
	private String contentType;
	
	private String content;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

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

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
