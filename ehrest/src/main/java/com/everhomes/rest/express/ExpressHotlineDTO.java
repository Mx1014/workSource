// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>id : 热线id</li>
 * <li>serviceName : 服务热线名称</li>
 * <li>hotline : 服务热线电话号码</li>
 * </ul>
 *
 *  @author:dengs 2017年7月19日
 */
public class ExpressHotlineDTO {
	private Long id;
	private String serviceName;
	private String hotline;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getHotline() {
		return hotline;
	}

	public void setHotline(String hotline) {
		this.hotline = hotline;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
