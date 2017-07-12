package com.everhomes.rest.rentalv2.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * 
 * <li>id: </li>
 * <li>name: 名称</li>
 * <li>pageType: 预定展示0代表默认页面DefaultType, 1代表定制页面CustomType</li>
 * <li>iconUri: 图标uri</li>
 * <li>status: 状态 0 关闭 2 开启</li>
 * <li>namespaceId: 域空间</li>
 * <li>payMode: 支付模式 (工作流模式) {@link com.everhomes.rest.rentalv2.admin.PayMode}</li>
 * </ul>
 */
public class UpdateResourceTypeCommand {

	private java.lang.Long    id;
	private java.lang.String  name;
	private java.lang.Byte    pageType;
	private java.lang.String  iconUri;
	private java.lang.Byte    status;
	private java.lang.Integer namespaceId;
	private Byte payMode;

	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.Byte getPageType() {
		return pageType;
	}

	public void setPageType(java.lang.Byte pageType) {
		this.pageType = pageType;
	}

	public java.lang.String getIconUri() {
		return iconUri;
	}

	public void setIconUri(java.lang.String iconUri) {
		this.iconUri = iconUri;
	}

	public java.lang.Byte getStatus() {
		return status;
	}

	public void setStatus(java.lang.Byte status) {
		this.status = status;
	}

	public java.lang.Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(java.lang.Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Byte getPayMode() {
		return payMode;
	}

	public void setPayMode(Byte payMode) {
		this.payMode = payMode;
	}
	
}
