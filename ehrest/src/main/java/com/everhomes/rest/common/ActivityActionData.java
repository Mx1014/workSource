package com.everhomes.rest.common;

import java.io.Serializable;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li>publishPrivilege: 发布权限，0开放模式，1管理员发布模式，如果是1，则调用/org/checkOfficalPrivilegeByScene检查是否具有官方权限，参考{@link com.everhomes.rest.common.ActivityPublishPrivilegeFlag}</li> 
 * <li>livePrivilege: 是否有直播权限，1有0无，参考{@link com.everhomes.rest.approval.TrueOrFalseFlag}</li> 
 * <li>listStyle: 列表样式，1，2，参考{@link com.everhomes.rest.common.ActivityListStyleFlag}</li>
 * <li>categoryId: 活动入口id，用于区分不同的活动入口</li> 
 * </ul>
 */
public class ActivityActionData implements Serializable {

	private static final long serialVersionUID = -5344267251183241788L;

	private Byte publishPrivilege;

	private Byte livePrivilege;

	private Byte listStyle;

	private Long categoryId;

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
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

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

}
