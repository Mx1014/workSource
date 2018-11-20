package com.everhomes.rest.xfyun;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>routeTextInfo: 客户端识别出的名称</li>
 * <li>routeTag: 标识</li>
 * <li>routeType: 跳转类型 null/0-业务模块 1-其他，公共模块</li>
 * <li>routeAddr: 跳转地址</li>
 * </ul>
 */
public class XfRouterDTO {
	private String routeTextInfo;
	private String routeTag;
	private Byte routeType;
	private String routeAddr;
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getRouteTextInfo() {
		return routeTextInfo;
	}

	public void setRouteTextInfo(String routeTextInfo) {
		this.routeTextInfo = routeTextInfo;
	}

	public String getRouteTag() {
		return routeTag;
	}

	public void setRouteTag(String routeTag) {
		this.routeTag = routeTag;
	}

	public Byte getRouteType() {
		return routeType;
	}

	public void setRouteType(Byte routeType) {
		this.routeType = routeType;
	}

	public String getRouteAddr() {
		return routeAddr;
	}

	public void setRouteAddr(String routeAddr) {
		this.routeAddr = routeAddr;
	}
}
