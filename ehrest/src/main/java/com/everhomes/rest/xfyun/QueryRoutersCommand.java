package com.everhomes.rest.xfyun;

import com.everhomes.rest.launchpadbase.AppContext;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>context: 需要的广场等参数 {@link com.everhomes.rest.launchpadbase.AppContext}</li>
 * <li>routeTextInfo: 客户端识别出的名称</li>
 * <li>routeTag: 标识</li>
 * <li>routeType: 跳转类型  null/0-应用  1-其他 如我的申请，悦邻优选等</li>
 * </ul>
 */
public class QueryRoutersCommand {
	private AppContext context; 
	private String routeTextInfo;
	private String routeTag;
	private Byte routeType;

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

	public AppContext getContext() {
		return context;
	}

	public void setContext(AppContext context) {
		this.context = context;
	}

}
