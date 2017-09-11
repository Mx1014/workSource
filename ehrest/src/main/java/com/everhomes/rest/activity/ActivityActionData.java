package com.everhomes.rest.activity;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 *     <li>categoryId: 活动入口</li>
 *     <li>publishPrivilege: 活动发布模式，参考 {@link com.everhomes.rest.common.ActivityPublishPrivilegeFlag}</li>
 *     <li>livePrivilege: 是否支持直播，参考{@link VideoSupportType}</li>
 *     <li>listStyle: 活动列表样式，参考{@link com.everhomes.rest.common.ActivityListStyleFlag}</li>
 *     <li>scope: 活动可见范围，参考{@link com.everhomes.rest.ui.user.ActivityLocationScope}</li>
 *     <li>style: 主题分类项的样式，参考{@link com.everhomes.rest.widget.AssociactionCategoryStyle}</li>
 *     <li>title: 名称</li>
 * </ul>
 */
public class ActivityActionData {

	private Long categoryId;

	private Byte publishPrivilege;

	private Byte livePrivilege;

	private Byte listStyle;

	private Byte scope;

	private Byte style;

	private String title;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Byte getPublishPrivilege() {
		return publishPrivilege;
	}

	public void setPublishPrivilege(Byte publishPrivilege) {
		this.publishPrivilege = publishPrivilege;
	}

	public Byte getLivePrivilege() {
		return livePrivilege;
	}

	public void setLivePrivilege(Byte livePrivilege) {
		this.livePrivilege = livePrivilege;
	}

	public Byte getListStyle() {
		return listStyle;
	}

	public void setListStyle(Byte listStyle) {
		this.listStyle = listStyle;
	}

	public Byte getScope() {
		return scope;
	}

	public void setScope(Byte scope) {
		this.scope = scope;
	}

	public Byte getStyle() {
		return style;
	}

	public void setStyle(Byte style) {
		this.style = style;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
