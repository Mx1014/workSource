package com.everhomes.rest.me_menu;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 菜单id</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>name: 菜单名称</li>
 *     <li>actionPath: actionPath</li>
 *     <li>actionData: actionData</li>
 *     <li>iconUri: iconUri</li>
 *     <li>status: status</li>
 * </ul>
 */
public class MeWebMenuDTO {

	private Long id;
	private Integer namespaceId;
	private String name;
	private String actionPath;
	private String actionData;
	private String iconUri;
	private Byte status;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getActionPath() {
		return actionPath;
	}

	public void setActionPath(String actionPath) {
		this.actionPath = actionPath;
	}

	public String getActionData() {
		return actionData;
	}

	public void setActionData(String actionData) {
		this.actionData = actionData;
	}

	public String getIconUri() {
		return iconUri;
	}

	public void setIconUri(String iconUri) {
		this.iconUri = iconUri;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String toString() {
		return StringHelper.toJsonString(this);
	}
}