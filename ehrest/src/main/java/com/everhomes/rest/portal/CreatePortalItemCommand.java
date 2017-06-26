// @formatter:off
package com.everhomes.rest.portal;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>itemGroupId: 门户item的id</li>
 * <li>label: 门户item名称</li>
 * <li>description: 门户item描述</li>
 * <li>itemWidth: 栏目占列比</li>
 * <li>iconUri: icon图片的uri</li>
 * <li>bgcolor: 底版颜色</li>
 * <li>status: 状态</li>
 * <li>actionType: 跳转类型</li>
 * <li>actionData: 跳转参数，无：无参数，门户：门户id(例如：{'layoutId':1})，业务应用：应用id(例如：{'moduleAppId':1})，电商：还未定义，更多/全部：类型(例如：{'type':'more'}或者{'type':'all'}</li>
 * <li>scopes: item范围</li>
 * </ul>
 */
public class CreatePortalItemCommand {

	private Long itemGroupId;

	private String label;

	private String description;

	private Integer itemWidth;

	private String bgcolor;

	private String iconUri;

	private Byte status;

	private String actionType;

	private String actionData;

	@ItemType(PortalScope.class)
	private List<PortalScope> scopes;

	public CreatePortalItemCommand() {

	}

	public CreatePortalItemCommand(Long itemGroupId, String label, String description, Integer itemWidth, String bgcolor, String iconUri, Byte status, String actionType, String actionData, List<PortalScope> scopes) {
		super();
		this.itemGroupId = itemGroupId;
		this.label = label;
		this.description = description;
		this.itemWidth = itemWidth;
		this.bgcolor = bgcolor;
		this.iconUri = iconUri;
		this.status = status;
		this.actionType = actionType;
		this.actionData = actionData;
		this.scopes = scopes;
	}

	public Long getItemGroupId() {
		return itemGroupId;
	}

	public void setItemGroupId(Long itemGroupId) {
		this.itemGroupId = itemGroupId;
	}

	public String getlabel() {
		return label;
	}

	public void setlabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getActionData() {
		return actionData;
	}

	public void setActionData(String actionData) {
		this.actionData = actionData;
	}

	public List<PortalScope> getScopes() {
		return scopes;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getItemWidth() {
		return itemWidth;
	}

	public void setItemWidth(Integer itemWidth) {
		this.itemWidth = itemWidth;
	}

	public String getBgcolor() {
		return bgcolor;
	}

	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}

	public void setScopes(List<PortalScope> scopes) {
		this.scopes = scopes;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
