package com.everhomes.rest.common;

import com.everhomes.util.StringHelper;

import java.io.Serializable;

public class PrintOrderActionData implements Serializable{

	private static final long serialVersionUID = -7134989050392679006L;
	private Long moduleId;
	private String url;
	private Long appId;
	private Byte clientHandlerType;
	private Long communityId;


    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getModuleId() {
		return moduleId;
	}

	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
	}

	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Byte getClientHandlerType() {
		return clientHandlerType;
	}

	public void setClientHandlerType(Byte clientHandlerType) {
		this.clientHandlerType = clientHandlerType;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
