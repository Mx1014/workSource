package com.everhomes.rest.rentalv2.admin;

import com.everhomes.util.StringHelper;
/**
 * <ul> 
 *  
 * <li>name: 名称</li>
 * <li>pageType: 预定展示0代表默认页面DefaultType, 1代表定制页面CustomType</li>
 * <li>iconUri: 图标uri</li> 
 * <li>status: 状态 0 关闭 2 开启</li>
 * <li>namespaceId: 域空间</li>
 * <li>payMode: 支付模式 (工作流模式) {@link com.everhomes.rest.rentalv2.admin.PayMode}</li>
 * </ul>
 */
public class CreateResourceTypeCommand {
	private String name;
	private Byte pageType;
	private String iconUri;
	private Byte status;
	private Integer namespaceId;
	private Byte payMode;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
 

	public Byte getPageType() {
		return pageType;
	}

	public void setPageType(Byte pageType) {
		this.pageType = pageType;
	}

	public String getIconUri() {
		return iconUri;
	}

	public void setIconUri(String iconUri) {
		this.iconUri = iconUri;
	}

	public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Byte getPayMode() {
		return payMode;
	}

	public void setPayMode(Byte payMode) {
		this.payMode = payMode;
	}
 

}
