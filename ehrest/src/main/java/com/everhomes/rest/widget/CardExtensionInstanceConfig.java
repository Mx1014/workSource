package com.everhomes.rest.widget;

import com.everhomes.util.StringHelper;

import java.io.Serializable;


/**
 * <ul>
 *     <li>appOriginId: appOriginId</li>
 * </ul>
 */
public class CardExtensionInstanceConfig implements Serializable {


	private static final long serialVersionUID = 4262461800817550321L;
	private Long appOriginId;

	public Long getAppOriginId() {
		return appOriginId;
	}

	public void setAppOriginId(Long appOriginId) {
		this.appOriginId = appOriginId;
	}


	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
